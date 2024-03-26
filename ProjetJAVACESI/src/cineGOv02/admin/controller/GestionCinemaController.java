package cineGOv02.admin.controller;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;

import cineGOv02.admin.model.AdminModel;
import cineGOv02.admin.view.AjouterFilmView;
import cineGOv02.admin.view.GestionCinemaView;
import cineGOv02.admin.view.LauncherAdminView;
import cineGOv02.admin.view.PlanningView;
import cineGOv02.admin.view.RoomCreatorView;
import cineGOv02.common.entity.Cinema;
import cineGOv02.common.entity.Film;
import cineGOv02.common.entity.Salle;
import cineGOv02.common.graphics.FilmCellRenderer;
import cineGOv02.common.graphics.MainApp;
import cineGOv02.common.util.TMDBImport;

/**
 * Controller de la vue de gestion des informations des cinémas.
 * Javadoc Done
 * @author hustariz
 */
public class GestionCinemaController implements ActionListener, MouseListener, KeyListener {

    /** Fenêtre principale */
    private MainApp frame;

    /** Vue de gestion d'un Cinéma */
    private GestionCinemaView gestionCinemaView;

    /** Modèle de données */
    private AdminModel model;
    /** Salle */
    private Salle salle;

    /** Cinéma qu'on va pouvoir éditer. */
    private Cinema cinema;
    /** Message */
    private static final String MSG_CINEMA_EXIST = "Un cinéma de ce nom éxiste déjà, seuls ses tarifs ont été changé.";
    /** Message */
    private static final String MSG_SALLE_EXIST = "Une salle de ce nom éxiste déjà, seuls ses attributs ont été changé.";
    /** Message */
    private static final String MSG_CHAMP_VIDE = "Vous n'avez pas renseigné tous les champs.";
    /** Message */
    private static final String MSG_ERROR_FK_CONSTRAINT ="ERROR: Cannot delete or update a parent row: a foreign key constraint fails.";
    /** Message */
    private static final String MSG_ERROR_WRONGTARIF = "Veuillez respecter le format de tarifs : NN.NN (€)!";
    /** Message */
    private static final String MSG_SUCCES_SAUVEGARDE = "Votre cinéma a bien été enregistrée dans notre base de donnée!";
    /** Message */
    private static final String MSG_SUCCES_SALLE_ADDED = "Votre salle a bien été enregistrée dans notre base de donnée!";
    /** Message */
    private static final String MSG_SUCCES_SALLE_DELETED = "Votre salle a bien été supprimée de notre base de donnée!";
    /** Nombre de rangées */
    private int nbRangees;
    /** Nombre de sièges */
    private int nbSieges;
    /** Numéro de salle */
    private int numeroDeSalle;
    /** Nombre de place disponibles */
    private int placesDisponibles;
    private String deleteSalleComboboxResult;
    private Object blocker = new Object();


    /**
     * Constructeur du controlleur de la vue de gestion d'un cinéma
     * @param frame
     * @param gestionCinemaView 
     * @param factory 
     * @param cinema 
     */
    public GestionCinemaController(MainApp frame, GestionCinemaView gestionCinemaView, AdminModel model) {
        this.frame = frame;
        this.gestionCinemaView = gestionCinemaView;
        this.model = model;
        this.cinema = frame.getCinema();
        gestionCinemaView.getLblCinemaNom().setText(cinema.getNom());
        gestionCinemaView.getTxtFieldNomCine().setText(cinema.getNom());
        gestionCinemaView.getTxtFieldTarifNormal().setText(Double.toString(cinema.getTarifNormal()));
        gestionCinemaView.getTxtFieldTarifEtu().setText(Double.toString(cinema.getReductionEtudiant()));
        gestionCinemaView.getTxtFieldTarif3D().setText(Double.toString(cinema.getTarif3D()));
        setCmbSalle();
    }


    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        //Bouton EditPlanning
        if(event.getSource() == gestionCinemaView.getBtnEditPlanning()){
            PlanningView planningView = new PlanningView();
            PlanningController planningController = new PlanningController(frame, planningView, model);
            planningView.setController(planningController);
            frame.setContentPane(planningView);
            frame.pack();
            frame.setLocationRelativeTo(null); 
        }
        //Bouton sauvegarde info
        else if(event.getSource() == gestionCinemaView.getBtnSaveInfo()){
            if(!gestionCinemaView.getTxtFieldNomCine().getText().equals("")
                    && !gestionCinemaView.getTxtFieldTarifNormal().getText().equals("")
                    && !gestionCinemaView.getTxtFieldTarifEtu().getText().equals("")
                    && !gestionCinemaView.getTxtFieldTarif3D().getText().equals("")){
                if(!model.cinemaExist(gestionCinemaView.getTxtFieldNomCine().getText())){
                    //Si le nom de cinema est différent, on sauvegarde un nouveau.
                    //Voir algo tarifs ajoutCinemaController.
                    if(!(gestionCinemaView.getTxtFieldTarif3D().getText().contains("."))
                            || !(gestionCinemaView.getTxtFieldTarifEtu().getText().contains("."))
                            || !(gestionCinemaView.getTxtFieldTarifNormal().getText().contains("."))){
                        JOptionPane.showMessageDialog(gestionCinemaView, MSG_ERROR_WRONGTARIF,"Error", JOptionPane.ERROR_MESSAGE);
                    }else{
                        cinema.setNom(gestionCinemaView.getTxtFieldNomCine().getText());
                        cinema.setTarifNormal(Double.parseDouble(gestionCinemaView.getTxtFieldTarifNormal().getText()));
                        cinema.setTarif3D(Double.parseDouble(gestionCinemaView.getTxtFieldTarif3D().getText()));
                        cinema.setReductionEtudiant(Double.parseDouble(gestionCinemaView.getTxtFieldTarifEtu().getText()));
                        model.saveEntity(cinema);
                        frame.setCinema(cinema);
                        setCmbSalle();
                        JOptionPane.showMessageDialog(gestionCinemaView, MSG_SUCCES_SAUVEGARDE,"Success", JOptionPane.INFORMATION_MESSAGE);
                        gestionCinemaView.getLblCinemaNom().setText(cinema.getNom());
                    }
                }else{
                    //Sinon on Update les tarifs du cinéma actuel.
                    JOptionPane.showMessageDialog(gestionCinemaView, MSG_CINEMA_EXIST,"Information", JOptionPane.INFORMATION_MESSAGE);
                    cinema.setTarifNormal(Double.parseDouble(gestionCinemaView.getTxtFieldTarifNormal().getText()));
                    cinema.setTarif3D(Double.parseDouble(gestionCinemaView.getTxtFieldTarif3D().getText()));
                    cinema.setReductionEtudiant(Double.parseDouble(gestionCinemaView.getTxtFieldTarifEtu().getText()));
                    model.updateEntity(cinema);
                    frame.setCinema(cinema);
                }

            }else{
                JOptionPane.showMessageDialog(gestionCinemaView, MSG_CHAMP_VIDE,"Error", JOptionPane.ERROR_MESSAGE);
            }       
        }
        //Bouton ajouter salle.
        else if(event.getSource() == gestionCinemaView.getBtnAddSalle()){

            if(!gestionCinemaView.getTxtFieldAddSalle().getText().equals("")
                    && !gestionCinemaView.getTextFieldNbSiege().getText().equals("")
                    && !gestionCinemaView.getTextFieldNbRangee().getText().equals("")){
                Salle salle = model.getSalle(Integer.parseInt(gestionCinemaView.getTxtFieldAddSalle().getText()),cinema);
                //Create salle
                if(salle == null){
                    numeroDeSalle = Integer.parseInt(gestionCinemaView.getTxtFieldAddSalle().getText());
                    nbSieges = Integer.parseInt(gestionCinemaView.getTextFieldNbSiege().getText());
                    nbRangees = Integer.parseInt(gestionCinemaView.getTextFieldNbRangee().getText());
                    placesDisponibles = nbSieges * nbRangees;
                    Salle newSalle = new Salle(numeroDeSalle, nbRangees, nbSieges, placesDisponibles, cinema, null);
                    model.saveEntity(newSalle);
                    setCmbSalle();
                    JOptionPane.showMessageDialog(gestionCinemaView, MSG_SUCCES_SALLE_ADDED,"Information", JOptionPane.INFORMATION_MESSAGE);
                    gestionCinemaView.getTxtFieldAddSalle().setText("");
                    gestionCinemaView.getTextFieldNbRangee().setText("");
                    gestionCinemaView.getTextFieldNbSiege().setText("");
                    //Update salle
                }else{
                    JOptionPane.showMessageDialog(gestionCinemaView, MSG_SALLE_EXIST,"Information", JOptionPane.INFORMATION_MESSAGE);
                    nbSieges = Integer.parseInt(gestionCinemaView.getTextFieldNbSiege().getText());
                    nbRangees = Integer.parseInt(gestionCinemaView.getTextFieldNbRangee().getText());
                    placesDisponibles = nbSieges * nbRangees;
                    salle.setNbSieges(nbSieges);
                    salle.setNbRangees(nbRangees);
                    model.updateEntity(salle);

                }
            }
            else{
                JOptionPane.showMessageDialog(gestionCinemaView, MSG_CHAMP_VIDE,"Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        //Bouton ajouter salle.
        else if(event.getSource() == gestionCinemaView.getBtnAddSalle()){

            if(!gestionCinemaView.getTxtFieldAddSalle().getText().equals("")
                    && !gestionCinemaView.getTextFieldNbSiege().getText().equals("")
                    && !gestionCinemaView.getTextFieldNbRangee().getText().equals("")){
                Salle salle = model.getSalle(Integer.parseInt(gestionCinemaView.getTxtFieldAddSalle().getText()),cinema);
                //Create salle
                if(salle == null){
                    numeroDeSalle = Integer.parseInt(gestionCinemaView.getTxtFieldAddSalle().getText());
                    nbSieges = Integer.parseInt(gestionCinemaView.getTextFieldNbSiege().getText());
                    nbRangees = Integer.parseInt(gestionCinemaView.getTextFieldNbRangee().getText());
                    placesDisponibles = nbSieges * nbRangees;


                    Salle newSalle = new Salle(numeroDeSalle, nbRangees, nbSieges, placesDisponibles, cinema, null);
                    model.saveEntity(newSalle);
                    setCmbSalle();
                    //Update salle
                }else{
                    JOptionPane.showMessageDialog(gestionCinemaView, MSG_SALLE_EXIST,"Information", JOptionPane.INFORMATION_MESSAGE);
                    nbSieges = Integer.parseInt(gestionCinemaView.getTextFieldNbSiege().getText());
                    nbRangees = Integer.parseInt(gestionCinemaView.getTextFieldNbRangee().getText());
                    placesDisponibles = nbSieges * nbRangees;
                    salle.setNbSieges(nbSieges);
                    salle.setNbRangees(nbRangees);
                    model.updateEntity(salle);

                }
            }
            else{
                JOptionPane.showMessageDialog(gestionCinemaView, MSG_CHAMP_VIDE,"Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        //Bouton Supprimer salle
        else if(event.getSource() == gestionCinemaView.getBtnSupprSalle()){
            Salle todelete = (Salle) gestionCinemaView.getComboBoxSupprSalle().getSelectedItem();
            gestionCinemaView.getComboBoxSupprSalle().removeItem(todelete);
            try{
                model.deleteEntity(todelete);
                JOptionPane.showMessageDialog(gestionCinemaView, MSG_SUCCES_SALLE_DELETED,"Success", JOptionPane.INFORMATION_MESSAGE);
            }catch (ConstraintViolationException e){
                JOptionPane.showMessageDialog(gestionCinemaView, MSG_ERROR_FK_CONSTRAINT,"Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        //Bouton Ajouter Film
        else if(event.getSource() == gestionCinemaView.getBtnAjouterFilm()){
            AjouterFilmView ajouterFilmView = new AjouterFilmView();
            AjouterFilmController ajouterFilmController = new AjouterFilmController(frame, ajouterFilmView, model, cinema);
            ajouterFilmView.setController(ajouterFilmController);
            frame.setContentPane(ajouterFilmView);
            frame.pack();
            frame.setLocationRelativeTo(null);
        }
        // Bouton éditer plan
        else if(event.getSource() == gestionCinemaView.getBtnEditPlan()){
            if(gestionCinemaView.getCmbPlanSalle().getModel().getSize() > 0){
                frame.setSalle((Salle) gestionCinemaView.getCmbPlanSalle().getSelectedItem());
                RoomCreatorView roomView = new RoomCreatorView();
                RoomCreatorController controller = new RoomCreatorController(frame, roomView, model);
                roomView.setController(controller);
                frame.setContentPane(roomView);
                frame.pack();
            }
        }else if(event.getSource() == gestionCinemaView.getBtnDownloadPlan()){
            if(gestionCinemaView.getCmbPlanSalle().getModel().getSize() > 0){
                String planXML = ((Salle) gestionCinemaView.getCmbPlanSalle().getSelectedItem()).getPlanDeSalle();
                if(planXML != null){
                    try {
                        File file = new File("Salle_"+((Salle)gestionCinemaView.getCmbPlanSalle().getSelectedItem()).getNumeroDeSalle() + ".xml");
                        FileWriter fileWriter = new FileWriter(file);
                        fileWriter.write(planXML);
                        fileWriter.flush();
                        fileWriter.close();
                        JOptionPane.showMessageDialog(frame, "Le plan de la salle a été enregistré dans votre répertoire d'installation de l'aaplication.", "Sauvegarde de plan", JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(frame, "Erreur d'écriture du fiechier. Le programme a renvoyé l'erreur suivant : " +  e.getMessage(), "Erreur d'écriture", JOptionPane.ERROR_MESSAGE);
                    }
                }else{
                    JOptionPane.showMessageDialog(frame, "Aucun plan n\'a été enregistré pour cette salle.", "Plan inexistant", JOptionPane.ERROR_MESSAGE);
                }
            }
        }else if(event.getSource() == gestionCinemaView.getBtnImporter()){
            JPanel load = new JPanel();
            JLabel circle = new JLabel(new ImageIcon("images/ajax-loader-admin.gif"));
            load.setLayout(new BorderLayout());
            load.add(circle,BorderLayout.CENTER);

            JDialog dialog = new JDialog(frame, "Chargement des films", true);
            dialog.setModalityType(JDialog.ModalityType.MODELESS);
            dialog.setContentPane(load);
            dialog.setResizable(true);
            dialog.setMinimumSize(new Dimension(265,250));
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
            Thread t = new Thread(){
                public void run(){
                    TMDBImport importer = new TMDBImport();
                    ArrayList<Film> listeFilm = importer.ImportFromTMDB();
                    if(listeFilm.size() != 0){
                        JList list = new JList<>();
                        DefaultListModel<Film> model = new DefaultListModel<Film>();
                        for (Film film : listeFilm) {
                            model.addElement(film);
                        }
                        dialog.setMinimumSize(new Dimension(400,500));
                        list.setCellRenderer(new FilmCellRenderer());
                        list.setFixedCellHeight(90);
                        list.setFixedCellWidth(300);
                        list.setModel(model);
                        load.removeAll();
                        JScrollPane scroll = new JScrollPane(list);
                        load.add(scroll,BorderLayout.CENTER);
                        load.revalidate();
                        load.repaint();
                    }else{
                        JLabel label = new JLabel("Pas de nouveaux film à l'affiche.");
                        load.removeAll();
                        load.add(label,BorderLayout.CENTER);
                        load.revalidate();
                        load.repaint();
                    }
                }
            };t.start();
        }
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    @Override
    public void mousePressed(MouseEvent event) {
        if(event.getSource() == gestionCinemaView.getLblSlectionnerUnAutre()){
            LauncherAdminView launcherView = new LauncherAdminView();
            LauncherAdminController controller = new LauncherAdminController(frame,launcherView, model);
            frame.setContentPane(launcherView);
            frame.pack();
        }
    }

    /**
     * Rempli la combobox de salles
     */
    private void setCmbSalle() {
        ArrayList<Salle> salle = model.getAllSalles(cinema);
        gestionCinemaView.getComboBoxSupprSalle().removeAllItems();
        gestionCinemaView.getCmbPlanSalle().removeAllItems();
        for (int i = 0; i < salle.size(); i++){
            gestionCinemaView.getComboBoxSupprSalle().addItem(salle.get(i));
            gestionCinemaView.getCmbPlanSalle().addItem(salle.get(i));
        }
    }

    /* (non-Javadoc)
     * Permet de rentrer uniquement des entiers et de les supprimer.  
     */
    @Override
    public void keyTyped(KeyEvent event) {
        char c;
        if(event.getSource() == gestionCinemaView.getTxtFieldAddSalle()){
            c = event.getKeyChar();
            if(!(Character.isDigit(c) || c==KeyEvent.VK_DELETE) || gestionCinemaView.getTxtFieldAddSalle().getText().length()  >= 3 ){
                Toolkit.getDefaultToolkit().beep();
                event.consume();
            }
        }else if(event.getSource() == gestionCinemaView.getTextFieldNbRangee()){
            c = event.getKeyChar();
            if(!(Character.isDigit(c) || c==KeyEvent.VK_DELETE) || gestionCinemaView.getTextFieldNbRangee().getText().length() >= 2 ){
                Toolkit.getDefaultToolkit().beep();
                event.consume();
            }
        }else if(event.getSource() == gestionCinemaView.getTextFieldNbSiege()){
            c = event.getKeyChar();
            if(!(Character.isDigit(c) || c==KeyEvent.VK_DELETE) || gestionCinemaView.getTextFieldNbSiege().getText().length()  >= 2 ){
                Toolkit.getDefaultToolkit().beep();
                event.consume();
            }
        }// Permet de rentrer uniquement des nombres décimaux et de les supprimer.
        else if (event.getSource() == gestionCinemaView.getTxtFieldTarifNormal()) {
            c = event.getKeyChar();
            if(!(Character.isDigit(c) || c==KeyEvent.VK_DELETE|| c==KeyEvent.VK_PERIOD) || gestionCinemaView.getTxtFieldTarifNormal().getText().length() >= 5){
                Toolkit.getDefaultToolkit().beep();
                event.consume();
            }else if(c == KeyEvent.VK_PERIOD && ((JTextField)event.getSource()).getText().contains(".")){
                event.consume();
            }
        }else if(event.getSource() == gestionCinemaView.getTxtFieldTarifEtu()){
            c = event.getKeyChar();
            if(!(Character.isDigit(c) || c==KeyEvent.VK_DELETE|| c==KeyEvent.VK_PERIOD) || gestionCinemaView.getTxtFieldTarifEtu().getText().length() >= 5){
                Toolkit.getDefaultToolkit().beep();
                event.consume();
            }else if(c == KeyEvent.VK_PERIOD && ((JTextField)event.getSource()).getText().contains(".")){
                event.consume();
            }
        }else if(event.getSource() == gestionCinemaView.getTxtFieldTarif3D()){
            c = event.getKeyChar();
            if(!(Character.isDigit(c) || c==KeyEvent.VK_DELETE|| c==KeyEvent.VK_PERIOD) || gestionCinemaView.getTxtFieldTarif3D().getText().length() >= 5){
                Toolkit.getDefaultToolkit().beep();
                event.consume();
            }else if(c == KeyEvent.VK_PERIOD && ((JTextField)event.getSource()).getText().contains(".")){
                event.consume();
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent arg0) {}
    @Override
    public void keyReleased(KeyEvent arg0) {}
    @Override
    public void mouseReleased(MouseEvent arg0) {}
    @Override
    public void mouseClicked(MouseEvent arg0) {}
    @Override
    public void mouseEntered(MouseEvent arg0) {}
    @Override
    public void mouseExited(MouseEvent arg0) {}
}

