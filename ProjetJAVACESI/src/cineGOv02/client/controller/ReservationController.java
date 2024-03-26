/*
 * ReservationController.java                                31 janv. 2016
 * CESI RILA 2015/2017
 */
package cineGOv02.client.controller;

import java.awt.Color;
import java.awt.Image;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;

import cineGOv02.client.model.ClientModel;
import cineGOv02.client.view.HomeView;
import cineGOv02.client.view.LoginUserView;
import cineGOv02.client.view.MovieDetailsView;
import cineGOv02.client.view.ReservationView;
import cineGOv02.client.view.SeatChoiceView;
import cineGOv02.client.view.UserView;
import cineGOv02.common.entity.Film;
import cineGOv02.common.entity.Reservation;
import cineGOv02.common.entity.Seance;
import cineGOv02.common.entity.User;
import cineGOv02.common.graphics.ComboType;
import cineGOv02.common.graphics.Controller;
import cineGOv02.common.graphics.MainApp;
import cineGOv02.common.util.PlanParser;
import cineGOv02.common.util.SendAMail;

/**
 * Controlleur de la vue de réservation 
 * @author Remi
 *
 */
public class ReservationController implements Controller, ActionListener, MouseListener{
    /** Fenêtre principale */
    private MainApp frame;
    /** Vue de réservation */
    private ReservationView reservationView;
    /** Modèle de données */
    private ClientModel model;
    /** Film choisis */
    private Film film;
    /** Liste des seances pour ce film */
    private List<Seance> seances;
    /** Liste des seance pour ce film ayant lieu aujourd'hui */
    private ArrayList<ArrayList<Seance>> seances_by_day; 
    /** Liste des seance en fonction du type (vo, vf, 3d...) */
    private ArrayList<Seance> seances_by_type;
    /** Liste des jours de la semaine en français */
    private static final String[] JOURS = {"Dimanche","Lundi","Mardi","Mercredi","Jeudi",
            "Vendredi","Samedi"};
    /** Liste des mois de l'année en Français */
    private static final String[] MOIS = {"Janvier","Février","Mars","Avril","Mai","Juin"
            ,"Juillet","Aout","Septembre","Octobre","Novembre","Décembre"};
    /** Nombre maximum de place réservable en une fois */
    public static final int MAX_RESA = 10;
    /** Séance sélectionné (film, heure, type) */
    private Seance selectedSeance;
    /** Séance sélectionné pour unecertaine salle (film, heure, type, salle) */
    private Seance selectedSeanceSalle;
    /** Prix total de la réservation */
    private double prixTotal;
    /** Plan de la salle dont on réserve une place */
    private String planDeSalle;
    /** Parser permettant de récupérer les places disponibles à partir de la séance*/
    private PlanParser planParser;
    
    private double avoir = 0;

    /**
     * Constructeur du contrôleur de réservation d'une séance
     * @param frame
     * @param reservationView
     * @param factory
     */
    public ReservationController(MainApp frame, ReservationView reservationView, ClientModel model){
        this.frame = frame;
        this.reservationView = reservationView;
        this.model = model;
        this.film = frame.getFilm();
        loadInfo();
        loadSeance();
        if(seances.size()>0){
            sortByDay();
            dispatchbyType();
        }
    }

    /**
     * Chargement des infos pour la Vue réservation (affiche du film, utilisateur)
     */
    public void loadInfo(){
        setImageSize(reservationView.getLblAffiche(), film.getImage());
        this.reservationView.getLblTitre().setText(film.getTitre() + "( durée : " 
                + (int)film.getDuree()/60 + "h" + film.getDuree()%60 + ")");
        loadUser(frame.getUser());
    }

    /**
     * Chargement des séances pour ce film dans ce cinéma
     */
    private void loadSeance() {
        Calendar calendar = Calendar.getInstance();
        Timestamp debut = new Timestamp(calendar.getTime().getTime());
        seances = model.getSeances(frame.getCinema(),frame.getFilm(),debut);  
    }

    /**
     * Trie des séance par jour
     */
    private void sortByDay() {
        seances_by_day = new ArrayList<ArrayList<Seance>>();
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        int day = 0, month = 0, cpt = 0;
        for (Seance seance : seances) {
            // Chargement du jour et du mois
            cal.setTimeInMillis(seances.get(cpt).getDebut().getTime());
            int day_temp = cal.get(Calendar.DAY_OF_MONTH);
            int month_temp = cal.get(Calendar.MONTH);
            Date debut_temp = seance.getDebut();
            if(month != month_temp || day != day_temp){
                month = month_temp;
                day = day_temp;
                seances_by_day.add(new ArrayList<Seance>());
                String jour =  JOURS[((cal.get(Calendar.DAY_OF_WEEK))-1)];
                String numero = cal.get(Calendar.DAY_OF_MONTH) + "";
                String mois = MOIS[cal.get(Calendar.MONTH)];
                reservationView.getCmbJour().addItem(jour + " " + numero + " " + mois);
            }
            seances_by_day.get(seances_by_day.size()-1).add(seances.get(cpt));
            cpt++;
        } 
    }

    /**
     * Trie des séances par type (vo,vf,3d...)
     */
    private void dispatchbyType() {
        int index = reservationView.getCmbJour().getSelectedIndex();
        reservationView.getCmb3DVF().removeAllItems();
        reservationView.getCmb3DVOST().removeAllItems();
        reservationView.getCmbVF().removeAllItems();
        reservationView.getCmdVOST().removeAllItems();
        reservationView.getCmbVO().removeAllItems();
        Date debut = new Date();
        for (Seance seance : seances_by_day.get(index)) {
            Date date_temp = seance.getDebut();
            if(date_temp.getTime() != debut.getTime()){
                debut = date_temp;
                if(seance.isVF3D()){
                    reservationView.getCmb3DVF().addItem(seance);
                    reservationView.getCmb3DVF().setSelectedIndex(-1);
                }
                if(seance.isVOST3D()){
                    reservationView.getCmb3DVOST().addItem(seance);
                    reservationView.getCmb3DVOST().setSelectedIndex(-1);
                }
                if(seance.isVF()){
                    reservationView.getCmbVF().addItem(seance);
                    reservationView.getCmbVF().setSelectedIndex(-1);
                }
                if(seance.isVOST()){
                    reservationView.getCmdVOST().addItem(seance);
                    reservationView.getCmdVOST().setSelectedIndex(-1);
                }
                if(seance.isVO()){
                    reservationView.getCmbVO().addItem(seance);
                    reservationView.getCmbVO().setSelectedIndex(-1);
                }
            }
        }
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    @Override
    public void mousePressed(MouseEvent event) {
        if(event.getSource() == reservationView.getLblRetour()){
            MovieDetailsView detailsView = new MovieDetailsView();
            MovieDetailsController controller = new MovieDetailsController(frame, detailsView, model);
            detailsView.setController(controller);
            controller.loadUser(frame.getUser());
            frame.setContentPane(detailsView);
            frame.pack();
            frame.setLocationRelativeTo(null);
        }
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        if(event.getSource() == reservationView.getBtnConnexion()){
            LoginUserView loginView = new LoginUserView();
            JDialog dialog = new JDialog(frame,"Connection",true);
            LoginController controller = new LoginController(frame, dialog, loginView, model, this);
            loginView.setController(controller);
            dialog.setContentPane(loginView);
            dialog.pack();
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
            dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }else if(event.getSource() == reservationView.getBtnDeconnexion()){
            frame.setUser(null);
            loadUser(null);
        }else if(event.getSource() == reservationView.getCmbJour()){
            // On supprime le listener pour éviter de propager des évènements en 
            // chaine lors de l'ajout des séances dans les différentes combobox
            for (ComboType combo : reservationView.getType()) {
                combo.removeActionListener(this);
            }
            reservationView.getBtnPayer().setEnabled(false);
            reservationView.getBtnPayer().setForeground(Color.GRAY);
            reservationView.getBtnChoix().setEnabled(false);
            reservationView.getBtnChoix().setForeground(Color.GRAY);
            dispatchbyType();
            for (ComboType combo : reservationView.getType()) {
                combo.addActionListener(this);
            }
            // Selection du type de séance 
        }else if(event.getSource() instanceof ComboType){
            for (ComboType combo : reservationView.getType()) {  
                if(combo != event.getSource()){
                    combo.removeActionListener(this);
                    combo.setSelectedIndex(-1);
                    combo.addActionListener(this);
                }
            }
            if(((ComboType)event.getSource()).getSelectedIndex() != -1){
                selectedSeance = (Seance) ((JComboBox)event.getSource()).getSelectedItem();
                reservationView.getCmbSalle().removeActionListener(this);
                loadRoom(selectedSeance);
                reservationView.getCmbSalle().setSelectedIndex(-1);
                reservationView.getCmbSalle().addActionListener(this);
            }
        }else if(event.getSource() == reservationView.getCmbSalle()){
            reservationView.getCmbNbPlace().removeActionListener(this);
            selectedSeanceSalle = setSeanceAndSeat(selectedSeance);
            reservationView.getCmbNbPlace().addActionListener(this);
            planDeSalle = selectedSeanceSalle.getSalle().getPlanDeSalle();
            if(selectedSeanceSalle.isPlacement()){
                reservationView.getBtnChoix().setEnabled(true);
                reservationView.getBtnChoix().setForeground(SystemColor.textHighlight);
            }
            reservationView.getBtnPayer().setEnabled(true);
            reservationView.getBtnPayer().setForeground(Color.WHITE);
        }else if(event.getSource() == reservationView.getCmbNbPlace()){
            calculPrix((JComboBox)event.getSource());   
        }else if(event.getSource() == reservationView.getBtnPayer()){
            recapPaiement(creerResa());
            frame.getUser().setAvoir(avoir);
            model.updateEntity(frame.getUser());
            reservationView.getCmbJour().setSelectedIndex(0);
            reservationView.getType().forEach(cmb -> cmb.removeActionListener(this));
            reservationView.getType().forEach(cmb -> cmb.setSelectedIndex(-1));
            reservationView.getType().forEach(cmb -> cmb.addActionListener(this));
            reservationView.getCmbSalle().removeActionListener(this);
            reservationView.getCmbSalle().removeAllItems();
            reservationView.getCmbSalle().addActionListener(this);
            reservationView.getLblPlacesLibres().setText("");
            reservationView.getCmbNbPlace().removeActionListener(this);
            reservationView.getCmbNbPlace().removeAllItems();
            reservationView.getCmbNbPlace().addActionListener(this);
            HomeView home = new HomeView();
            HomeController controller = new HomeController(frame, home, model);
            home.setController(controller);
            frame.setContentPane(home);
        }else if(event.getSource() == reservationView.getBtnCompte()){
            UserView userView = new UserView();
            JDialog dialog = new JDialog(frame,"Mon compte",true);
            UserViewController controller = new UserViewController(frame,dialog,userView,model,this);
            userView.setController(controller);
            dialog.setContentPane(userView);
            dialog.pack();
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
            dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }else if(event.getSource() == reservationView.getBtnChoix()){
            frame.setSeance(selectedSeanceSalle);
            frame.setReservation(creerResa());
            SeatChoiceView seatView = new SeatChoiceView();
            SeatChoiceController controller = new SeatChoiceController(frame, seatView, model);
            seatView.setController(controller);
            frame.setContentPane(seatView);
            frame.pack();
            frame.setLocationRelativeTo(null);
        }
    }

    /**
     * Chargement des alle diffusant comportant une séances selon les critères selectionné à l'écran.
     * @param selection Seance sélectionné 
     */
    private void loadRoom(Seance selection) {
        reservationView.getCmbSalle().removeAllItems();
        for (Seance seance : seances) {
            if(seance.getNbPlacesLibres() > 0 ){
                boolean present = false;
                for (int i = 0; i < reservationView.getCmbSalle().getModel().getSize(); i++) {
                    if((int)reservationView.getCmbSalle().getModel().getElementAt(i) == seance.getSalle().getNumeroDeSalle()){
                        present = true;
                    }
                }
                if(!present){
                    reservationView.getCmbSalle().addItem(seance.getSalle().getNumeroDeSalle());
                }
            }
        }
    }

    /**
     * Récupère la séance pour une salle donnée.
     * @param source
     */
    private Seance setSeanceAndSeat(Seance selectedSeance) {
        Timestamp debut = selectedSeance.getDebut();
        Seance seance = model.getTheSeance(frame.getCinema(),frame.getFilm(),
                debut,selectedSeance.isVF(),selectedSeance.isVO(),
                selectedSeance.isVOST(),selectedSeance.isVF3D(),
                selectedSeance.isVOST3D(),(int) reservationView.getCmbSalle().getSelectedItem());
        int maxBySalle = seance.getNbPlacesLibres();
        reservationView.getLblPlacesLibres().setText(maxBySalle + " places libres");
        reservationView.getCmbNbPlace().removeAllItems();
        for(int i = 1 ; i <= MAX_RESA && i <= maxBySalle ; i++){
            reservationView.getCmbNbPlace().addItem(i);
        }
        return seance;
    }

    /* (non-Javadoc)
     * @see cineGOv02.controller.Controller#loadUser(cineGOv02.model.entity.User)
     */
    @Override
    public void loadUser(User user) {
        if(user != null){
            reservationView.getLblNoCompte().setVisible(false);
            reservationView.getBtnConnexion().setVisible(false);
            reservationView.getLblUser().setVisible(true);
            reservationView.getLblUser().setText(user.getPrenom());
            reservationView.getLblCreate().setVisible(false);
            reservationView.getLblCompte().setText("Bienvenue " + user.getPrenom());
            reservationView.getLblCompte().setVisible(true);
            reservationView.getBtnCompte().setVisible(true);
            reservationView.getBtnDeconnexion().setVisible(true);
        }else{
            reservationView.getLblNoCompte().setVisible(true);
            reservationView.getBtnConnexion().setVisible(true);
            reservationView.getLblCreate().setVisible(true);
            reservationView.getLblUser().setVisible(false);
            reservationView.getLblCompte().setText("");
            reservationView.getLblCompte().setVisible(false);
            reservationView.getBtnCompte().setVisible(false);
            reservationView.getBtnDeconnexion().setVisible(false);
        }
    }

    /**
     * Création d'une réservation
     * @return
     */
    public Reservation creerResa(){
        String film = frame.getFilm().getTitre();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String heure = sdf.format(selectedSeanceSalle.getDebut());
        int numSalle = (int) reservationView.getCmbSalle().getSelectedItem();
        int nbPlace = reservationView.getCmbNbPlace().getSelectedIndex()+1;
        selectedSeanceSalle.setNbPlacesLibres(selectedSeanceSalle.getNbPlacesLibres() - nbPlace);
        java.sql.Timestamp dateResa = new java.sql.Timestamp((new java.util.Date()).getTime());
        return new Reservation(prixTotal, frame.getUser(), null, selectedSeanceSalle, nbPlace, dateResa);
    }

    /**
     * Méthode de calcul du prix et d'envoi du mail de récapitulatif 
     */
    private void recapPaiement(Reservation resa) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String heure = sdf.format(selectedSeanceSalle.getDebut());
        int numSalle = selectedSeanceSalle.getSalle().getNumeroDeSalle();
        int nbPlace = reservationView.getCmbNbPlace().getSelectedIndex()+1;
        String placeOccupees = selectedSeanceSalle.getListeSiege();
        String places = setSeat(placeOccupees,nbPlace);
        resa.setListeSiege(resa.getListeSiege() + places);
        selectedSeanceSalle.setListeSiege(selectedSeanceSalle.getListeSiege() + places);
        Thread update = new Thread(){
            public void run(){
                model.saveEntity(resa);
                model.updateEntity(selectedSeanceSalle);
            }
        };update.start();

        String body = "<strong>Bonjour " + (frame.getUser() != null ? frame.getUser().getPrenom() : "")   + "</strong>, <br /><br />"
                + "<strong>Voici un récapitulatif de votre commande :</strong><br/><br/>"
                + "<ul><li>Tire du film : <strong>" + film + "</strong></li>"               
                + "<li>Heure de la séance : <strong> " + heure + "</strong></li>"
                + "<li>Nombre de place : <strong>" + nbPlace + "</strong></li>"
                + "<li>Salle : <strong>" + numSalle + "</strong></li>"
                + "<li>Place(s) : <strong>" + places + "</strong></li>"
                + "<li>Prix : <strong>" + prixTotal + "</strong></li></ul><br /><br />"
                + "<span style=\"font-style:italic;\">"
                + "Votre reservation est modifiable et ce jusqu'à 24h avant le début de la séance.</span><br /><br />"
                + "<strong>Toute l'équipe de CineGO vous souhaite un bon film !</strong><br /><br />"
                + "Amicalement,<br />"
                + "L'équipe de CinéGO";
        if(frame.getUser() != null){
            String subject = "Confirmation de votre commande CineGO.";
            JOptionPane.showMessageDialog(reservationView, "Votre paiment a été accepté un mail récapitulatif vous a étét envoyé", "Information paiement", JOptionPane.INFORMATION_MESSAGE);
            Thread sendMail = new Thread(){
                public void run(){
                    try {
                        SendAMail.send("remi.plantade.pro@gmail.com", frame.getUser().getMail(), body, subject);
                    } catch (MessagingException e) {
                        JOptionPane.showMessageDialog(frame, "Erreur lors de l'envoie du mail, vérifiez vos paramètre de connexion", "Echec", JOptionPane.ERROR_MESSAGE);
                    }
                }
            };sendMail.start();
        }else{
            JTextPane pane = new JTextPane();
            pane.setBackground(new Color(220,219,31));
            pane.setContentType("text/html");
            pane.setText(body);
            JOptionPane.showMessageDialog(reservationView,pane ,"Information paiement", JOptionPane.INFORMATION_MESSAGE); 
        }
    }

    /**
     * Choisis des places au hasard parmis celles de libre
     * @param placeOccupees 
     * @param nbPlace 
     * @return 
     */
    private String setSeat(String placeOccupees, int nbPlace) {
        String placesString = "";
        if(planDeSalle != null){
            planParser =  planParser == null ? new PlanParser(planDeSalle) : planParser;
            ArrayList<String> places = planParser.getFreePlace(placeOccupees, nbPlace);
            for (String string : places) {
                placesString += string;
            }
        }
        return placesString;
    }

    /**
     * Méthode de calcul du prix de la réservation
     * @param source
     */
    private void calculPrix(JComboBox source) {
        if(selectedSeanceSalle.isVF() || selectedSeanceSalle.isVO() || selectedSeanceSalle.isVOST()){
            prixTotal = frame.getCinema().getTarifNormal();
        }else if(selectedSeanceSalle.isVF3D() || selectedSeanceSalle.isVOST3D()){
            prixTotal = frame.getCinema().getTarif3D();
        }
        if(frame.getUser() != null && frame.getUser().isEtudiant()){
            prixTotal = prixTotal - (prixTotal * frame.getCinema().getReductionEtudiant());
        }
        prixTotal = prixTotal * (source.getSelectedIndex()+1);
        NumberFormat nf = NumberFormat.getInstance(Locale.ENGLISH);
        nf.setMaximumFractionDigits(2);
        prixTotal = Double.valueOf(nf.format(prixTotal));
        if(frame.getUser() != null){
            double avoir = frame.getUser().getAvoir();
            if(avoir > 0){
                reservationView.getLblReduction().setText(" après application d'un avoir de " + avoir +"€");
            }
            prixTotal = prixTotal - avoir;
            if(prixTotal <= 0){
                this.avoir = Math.abs(prixTotal);
                prixTotal = 0;
            }
        }
        reservationView.getLblPrix().setText(prixTotal + "");
    }

    /**
     * Redimensionne l'image de l'affiche du film
     * @param affiche
     * @param blob
     */
    public static void setImageSize(JLabel affiche, Blob blob){
        BufferedImage buff;
        try {
            buff = ImageIO.read(blob.getBinaryStream());
            ImageIcon icon = new ImageIcon(buff);
            Image img = icon.getImage();
            Image newImg = img.getScaledInstance(300, 400, Image.SCALE_SMOOTH);
            ImageIcon newImc = new ImageIcon(newImg);
            affiche.setIcon(null);
            affiche.setIcon(newImc);
            affiche.revalidate();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void mouseClicked(MouseEvent arg0) {}
    @Override
    public void mouseEntered(MouseEvent arg0) {}
    @Override
    public void mouseExited(MouseEvent arg0) {}
    @Override
    public void mouseReleased(MouseEvent arg0) {}
}
