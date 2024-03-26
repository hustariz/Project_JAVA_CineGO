/*
 * PlanningController.java                                6 janv. 2016
 * CESI RILA 2015/2017
 */
package cineGOv02.admin.controller;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import cineGOv02.admin.model.AdminModel;
import cineGOv02.admin.view.GestionCinemaView;
import cineGOv02.admin.view.PlanningView;
import cineGOv02.common.entity.Cinema;
import cineGOv02.common.entity.Film;
import cineGOv02.common.entity.Salle;
import cineGOv02.common.entity.Seance;
import cineGOv02.common.graphics.Creneau;
import cineGOv02.common.graphics.MainApp;

/**
 * Gestion d'un plnning des seance pour un cinema
 * @author Remi
 */
public class PlanningController implements MouseListener, ActionListener{

    /** Modèle de données */
    private AdminModel model;

    /** Vue du planning */
    private PlanningView view;

    /** Cinéma dont on veut éditer le planning */
    private Cinema cinema;

    /** Liste de salles de ce cinéma */
    private ArrayList<Salle> salles = new ArrayList<Salle>();

    /** Liste de tous les films */
    private ArrayList<Film> films;

    /** Liste des séance proggrammées pour une salle */
    private ArrayList<Seance> seances;

    /** Liste des dates des jour de la semaine affichée */
    private ArrayList<Date> thisWeek = new ArrayList<Date>();

    /** Correspondance entre les nom des mois et les indices des mois */
    private ArrayList<String> month = new ArrayList<String>(Arrays.asList("JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"));

    /** Caledrier utilisé pour charger les semaines */
    private Calendar calendar;

    /** Date d'aujourd'hui */
    private Date thisDate = new Date(); 

    private MainApp frame;


    /**
     * Controlleur de la vue associé au planning des salles
     * @param frame 
     * @param planningView 
     * @param factory 
     * @param view
     * @param cinema cinema dont la vue est le planning
     */
    public PlanningController(MainApp frame, PlanningView planningView, AdminModel model) { 
        calendar = Calendar.getInstance(new Locale("FR"));
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        planningView.getLblCinema().setText(frame.getCinema().getNom());
        this.view = planningView;
        this.frame = frame;
        this.model = model;
        this.cinema = frame.getCinema();
        // Thread de chargement des infos BD
        Thread t = new Thread() {
            public void run() { 
                // Chargement des salles pour ce cinéma
                salles = model.getAllSalles(cinema);
                for (int i = 0; i < salles.size(); i++) {
                    view.getCmbbSalle().addItem(salles.get(i));
                }
                // Chargement des films présent en BD
                loadFilm(); 
                // Chargement des dates pour cette semaine
                loadWeek(); 

            }
        };t.setName("planning");
        t.start();     
    }


    @Override
    public void mouseClicked(MouseEvent event) {
    }
    @Override
    public void mouseEntered(MouseEvent event) {}
    @Override
    public void mouseExited(MouseEvent event) {}
    @Override
    public void mousePressed(MouseEvent event) {
        if(event.getSource() instanceof Creneau){
            // Ajout de séances lors du clic gauche
            if(SwingUtilities.isLeftMouseButton(event) && ((Creneau)event.getSource()).getSeance() == null){
                programmerSeance((Creneau)event.getSource());
                // Suppression de séance lors du cli droit
            }else if(SwingUtilities.isRightMouseButton(event) && ((Creneau)event.getSource()).getSeance() != null){
                try{
                deprogrammerSeance((Creneau)event.getSource());
                }catch(org.hibernate.exception.ConstraintViolationException e){
                    JOptionPane.showMessageDialog(frame, "Impossible de déprogrammer une seance car des client ont déjà réserver leur places", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /**
     * Déprogrammation d'une seance en supprimant la séance de la BD, de la liste des séances
     * et rafraichissement de la vue
     * @param creneau horaires concerné par la séance
     */
    private void deprogrammerSeance(Creneau creneau) {
        // On parcours la liste des seances jusqu'a en trouver celle qui est placée sur ce créneau
        for(int i = 0 ; i < seances.size() ; i++){
            if(seances.get(i).getDebut() == creneau.getSeance().getDebut()
                    && seances.get(i).getFin() == creneau.getSeance().getFin()){
                // On la supprime de la BD
                model.deleteEntity(seances.get(i));
                // On la supprime de la liste
                seances.remove(i);
            }
        }
        // On rafraichit la vue
        view.resetPanel();
        refreshSeances();
    }

    /**
     * Programmation d'une séance en fonction du film et de la salle sélectionné 
     * sur l'interface ainsi que du créneau horaire choisi lors du clic
     * @param creneau qui a été selectionné 
     */
    private void programmerSeance(Creneau creneau) {
        // On sette la date du calendrier au jour dont le créneau fait parti.
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getCreneauDate(creneau));
        // On sette l'heure à l'heure du créneau
        calendar.set(Calendar.HOUR_OF_DAY, creneau.getHeure());
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        // On génère l'heure de début de la séance
        Timestamp debut = new Timestamp(calendar.getTime().getTime());
        // On calcule la durée
        int nbHeure = 0;
        if(creneau.getHeure() != 23){
            // Si le créneau choisi n'est pas le dernier on calcule la durée (en creneau) du film
            int dureeFilm = films.get(view.getCmbbFilm().getSelectedIndex()).getDuree();
            nbHeure = dureeFilm / 60;
            nbHeure = dureeFilm % 60 > 0 ? nbHeure+1 : nbHeure;
        }else{
            // Sinon on fixe la durée à un créneau (le dernier de la journée)
            nbHeure = 1;
        }
        // Vérif si il y a asses de créneaux vide après celui-ci pour placer la séance
        int nbCreneauVide = 0;
        boolean finJournee = false;
        for(int i = creneau.getHeure()-11 ; i < creneau.getJour().length 
                && creneau.getJour()[i].getSeance() == null; i++){
            nbCreneauVide ++;
            // Si le dernier creneau vide est le dernier de la journée
            finJournee = i == creneau.getJour().length -1;
        }
        // Si il y a assez de créneau vide après celui sélectionné
        if(nbCreneauVide >= nbHeure || finJournee){
            Timestamp fin;
            //Si ce n'est pas le dernier creneau de la journée qui a été selectionné 
            if(creneau.getHeure() != 23){
                // On determine l'heur de fin de la seance
                calendar.add(Calendar.HOUR, nbHeure);
                fin =  new Timestamp(calendar.getTime().getTime());
                // Sinon peu nous importe l'heure de fin 
            }else{
                calendar.add(Calendar.HOUR, nbHeure);
                calendar.add(Calendar.MINUTE, 59);
                fin =  new Timestamp(calendar.getTime().getTime());
            }
            Film film = films.get(view.getCmbbFilm().getSelectedIndex());
            Salle salle = salles.get(view.getCmbbSalle().getSelectedIndex());
            boolean vf3d = view.getChckbx3DVF().isSelected();
            boolean vost3d = view.getChckbx3DVOST().isSelected();
            boolean vf = view.getChckbxVF().isSelected();
            boolean vost = view.getChckbxVOST().isSelected();
            boolean vo = view.getChckbxVO().isSelected();
            boolean placement = salle.getPlanDeSalle() != null;
            Seance seance = new Seance(salle,film,debut,fin,vf3d,vost3d,vf,vost,vo, "", salle.getPlacesDisponibles(),placement);
            // On enregistre la séance en BD
            model.saveEntity(seance);
            // On l'ajoute à la liste
            seances.add(seance);
            // On rafraichi l'interface
            view.resetPanel();
            refreshSeances();
        }
    }

    /**
     * On récupère la date du créneau en fonction du jour auquel il appartient 
     * et donc de la liste de créneau dans lequel il est
     * @param creneau creneau concerné
     * @return La date du jour dont ce créneau fait parti
     */
    private Date getCreneauDate(Creneau creneau) {
        if(creneau.getJour() == view.gethLundi()){
            return thisWeek.get(0);
        }else if(creneau.getJour() == view.gethMardi()){
            return thisWeek.get(1);
        }else if(creneau.getJour() == view.gethMercredi()){
            return thisWeek.get(2);
        }else if(creneau.getJour() == view.gethJeudi()){
            return thisWeek.get(3);
        }else if(creneau.getJour() == view.gethVendredi()){
            return thisWeek.get(4);
        }else if(creneau.getJour() == view.gethSamedi()){
            return thisWeek.get(5);
        }else if(creneau.getJour() == view.gethDimanche()){
            return thisWeek.get(6);
        }
        return null;
    }

    /**
     * On rafraichi l'affichage des séances
     */
    private void refreshSeances() {
        for (Seance seance : seances) {
            afficherSeance(seance);
        }   
    }

    @Override
    public void mouseReleased(MouseEvent event) {}

    @Override
    public void actionPerformed(ActionEvent event) {
        if(event.getSource() == view.getBtnNext()){
            // Si on passe à la semaine suivante
            seances = new ArrayList<>();
            view.resetPanel();
            // On charge les seances de la semaine suivante
            loadSeance(nextWeek());
        }else if(event.getSource() == view.getBtnPrevious()){
            // Idem pour la semaine précédente
            view.resetPanel();
            seances = new ArrayList<>();
            loadSeance(previousWeek());
        }else if(event.getSource() == view.getCmbbSalle()){
            // Si on selectionne une autre salle
            seances = new ArrayList<>();
            view.resetPanel();
            // On determine l'intervalle de date dans lesquelles 
            // il faut aller chercher les seances en BD
            // Creation d'un objet calendrier
            Calendar calendar = Calendar.getInstance();
            calendar.setFirstDayOfWeek(Calendar.MONDAY);
            calendar.setTime(this.calendar.getTime());
            // On se place sur le premier jour de cette semaine
            calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
            // A l'heure 0 et minute 0
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            // On sauve cette date
            Date start = calendar.getTime();
            // On avance de 6 jour pour arriver à la fin de semaine
            calendar.add(Calendar.DATE, 6);
            // 0 l'heur 23, minute 59
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            // On sauvegarde cette date
            Date end = calendar.getTime();
            // On charge les eance de cet intervalle de temps
            if(view.getCmbbSalle().getModel().getSize()>0){
                loadSeance(new Date[]{start,end});
            }
        }else if(event.getSource() == view.getBtnRetour()){
            GestionCinemaView view = new GestionCinemaView();
            GestionCinemaController controller = new GestionCinemaController(frame, view, model);
            view.setController(controller);
            frame.setContentPane(view);
            frame.pack();
        }
    }

    /**
     * Va chercher les salles pour ce cinéma et les ajoutes dans la vue
     * (combobox de selection des salles) 
     * @param cinema cinema dont on veut récupérer les salles
     */
    private void loadFilm() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {    
                films = model.getAllFilm();
                for(Film film : films){
                    view.getCmbbFilm().addItem(film.getTitre());
                }
            }
        });
    }   

    /**
     * Methode de chargement de seance depuis la BD
     * @param intervalle intervalle de temps pour lequel on veut cxharger les seances
     */
    private void loadSeance(Date[] intervalle){ 
        // Récupération et conversion des date de debut et de fin
        Timestamp debut = new Timestamp(intervalle[0].getTime());
        Timestamp fin = new Timestamp(intervalle[1].getTime());
        // Récupération de l'id de la salle depuis l'interface
        int idSalle = salles.get(view.getCmbbSalle().getSelectedIndex()).getId();
        if(debut != null && fin != null && idSalle > -1){
            seances = model.getSeance(idSalle,debut,fin);
            // On veut récupérer toutes les seance dont l'id de la salle 
            // est celui en argument et dont la date de debut est postérieure 
            // à celle en argument et la date de fin antérieure à celle passée en argument
        }
        // Si il existe des seances dans cet intervalle
        if(seances.size() > 0){
            for (Seance seance : seances) {
                afficherSeance(seance);
            }
        }
    }
    /**
     * Place les seances sur le planning à l'intérieur des Creneaux
     * @param seance à placer sur le planning
     */
    private void afficherSeance(Seance seance) { 
        // on récupère le jour et l'heure de debut de la seance
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(seance.getDebut());
        int jour = calendar.get(Calendar.DAY_OF_WEEK);
        int debut = calendar.get(Calendar.HOUR_OF_DAY);
        int fin = 0;
        int duree = 0;
        // Si l'heure de fin n'est pas spécifiée on fixe la durée à 1
        // Car cela signifi que la seance est programmée pour le dernier créneau de la journée
        if(seance.getFin() == null){
            duree = 1;
        }else{
            // Sinon on récupère l'heure de fin
            calendar.setTime(seance.getFin());
            // On refait une vérification au cas ou la sence aurait 
            // une heure de fin non nulle malgrès son positionnement en fin de journée
            fin = calendar.get(Calendar.HOUR_OF_DAY) == 0 
                    || calendar.get(Calendar.HOUR_OF_DAY) < 11 
                    ? 24 : calendar.get(Calendar.HOUR_OF_DAY);
            duree = fin - debut;
        }
        // Selon le jour ou à lieu la senace on modifie les chéneaux appripriés 
        switch(jour){
        case Calendar.MONDAY:
            seancePlacement(view.gethLundi(), seance, debut, duree);
            break;
        case Calendar.TUESDAY :
            seancePlacement(view.gethMardi(), seance, debut, duree);
            break;
        case Calendar.WEDNESDAY :
            seancePlacement(view.gethMercredi(), seance, debut, duree);
            break;
        case Calendar.THURSDAY :
            seancePlacement(view.gethJeudi(), seance, debut, duree);
            break;
        case Calendar.FRIDAY :
            seancePlacement(view.gethVendredi(), seance, debut, duree);
            break;
        case Calendar.SATURDAY :
            seancePlacement(view.gethSamedi(), seance, debut, duree);
            break;
        case Calendar.SUNDAY :
            seancePlacement(view.gethDimanche(), seance, debut, duree);
            break;     
        }
    }
    /**
     * Place les informations sur les creneaux concerné par la seance
     * @param jour Liste des creneaux pour ce jour
     * @param duree Duree du film (en nombre de creneaux)
     * @param seance Seance à placer 
     * @param debut Numero de creneau de debut de la senace
     */
    private void seancePlacement(Creneau[] jour, Seance seance, int debut, int duree) {
        for(int i = debut; i < debut + duree ; i++){
            // On parcours la liste des creneaux en se plaçant au debut de la seance
            // Et on intère autant de fois que de creneaux necessaire
            String htmlCodeColor = seance.getFilm().getHtlmColor();
            Color couleur = Color.decode((htmlCodeColor == null ? "FFFFFF":htmlCodeColor));
            jour[i-11].setBackground(couleur);
            jour[i-11].setForeground(Color.WHITE);
            if(i == debut){
                jour[i-11].setFilm(seance.getFilm().getTitre());
            }else if(i == debut + 1){
                if(seance.isVF()){
                    jour[i-11].setFilm("VF");
                }else if(seance.isVF3D()){
                    jour[i-11].setFilm("3D VF");
                }else if(seance.isVOST3D()){
                    jour[i-11].setFilm("3D VOST");
                }else if(seance.isVO()){
                    jour[i-11].setFilm("VO");
                }else if(seance.isVOST()){
                    jour[i-11].setFilm("VOST");
                }
            }
            jour[i-11].setLayout(new FlowLayout(FlowLayout.LEFT));
            jour[i-11].setSeance(seance);
            jour[i-11].revalidate();  
        }
    }

    /**
     * Chargement des dates de cette semaine
     */
    private void loadWeek() {
        int number = calendar.get(Calendar.WEEK_OF_YEAR);
        PlanningController.this.view.getLblSemaine().setText("Semaine n° " + number);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        Date start = calendar.getTime();
        calendar.add(Calendar.DATE, 6);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        Date end = calendar.getTime();
        generateDates(start,end);
        loadSeance(new Date[]{start,end});   
    }

    /**
     * Chargement des dates de la semaine précédente
     * @return la date de début et de fin de semaine
     */
    private Date[] previousWeek() {
        calendar.add(Calendar.DATE, -13);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        Date start = calendar.getTime();
        calendar.add(Calendar.DATE, +6);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        Date end = calendar.getTime();
        generateDates(start,end);
        return new Date[]{start,end};
    }

    /**
     * Methode de chargement des dates de la semaine suivante
     * @return la date de début et de fin de semaine
     */
    private Date[] nextWeek() {
        calendar.add(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        Date start = calendar.getTime();
        calendar.add(Calendar.DATE, 6);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        Date end = calendar.getTime();
        generateDates(start,end);  
        return new Date[]{start,end};
    }

    /**
     * G"n"ration des libéllées de date de l'interface
     * @param end Date de début de la semaine
     * @param start Date de fin de la semaine
     */
    private void generateDates(Date start, Date end) {
        Calendar calTemp = Calendar.getInstance();
        thisWeek = new ArrayList<>();
        calTemp.setTime(start);
        String date;
        int i = 0;

        while (calTemp.getTime().getTime() <= end.getTime()){
            Date result = calTemp.getTime();
            String jour = calTemp.get(Calendar.DAY_OF_MONTH) + " ";
            String mois = month.get(calTemp.get(Calendar.MONTH));
            thisWeek.add(result);
            calTemp.add(Calendar.DATE, 1);
            view.getLblDate().get(i).setText(jour+mois);
            i++;         
        } 
        int number = calTemp.get(Calendar.WEEK_OF_YEAR);
        int year = calTemp.get(Calendar.YEAR);
        PlanningController.this.view.getLblSemaine().setText(year + " Semaine " + number);
    }

    /**
     * Génère des couleurs aux tons pastel
     * @return color
     */
    public static Color generatePastelColor() {
        Random random = new Random();
        // Will produce only bright / light colours:
        float red = (float) (random.nextFloat() / 2 + 0.5);
        float green = (float) (random.nextFloat() / 2 + 0.5);
        float blue = (float) (random.nextFloat() / 2 + 0.5);
        Color color = new Color(red, green, blue);
        return color;
    }
}