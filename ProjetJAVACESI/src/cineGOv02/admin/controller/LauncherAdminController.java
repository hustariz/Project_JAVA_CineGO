package cineGOv02.admin.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import cineGOv02.admin.model.AdminModel;
import cineGOv02.admin.view.AjouterCinemaView;
import cineGOv02.admin.view.GestionCinemaView;
import cineGOv02.admin.view.LauncherAdminView;
import cineGOv02.common.entity.Cinema;
import cineGOv02.common.graphics.MainApp;

/**
 * Initialise la frame, la view associée au controller, la factory.
 */
public class LauncherAdminController implements ActionListener{
    /** Fenêtre générale */
    private MainApp frame;
    /** Vue de lancement de l'application admin */
    private LauncherAdminView launcherAdminView;
    /** Modèle de données */
    private AdminModel model;
    /** Liste des cinéma */
    private ArrayList<Cinema> cinema;

    /**
     * Constructeur de la vue de lancement de l'application admin
     * @param frame
     * @param launcherAdminView 
     * @param launcherView
     * @param factory
     */
    public LauncherAdminController(MainApp frame, LauncherAdminView launcherAdminView, AdminModel model) {
        super();
        this.frame = frame;
        this.launcherAdminView = launcherAdminView;
        this.model = model;
        this.launcherAdminView.setController(this);
        cinema = model.getAllCinema();
        for(int i = 0 ; i < cinema.size() ; i++){
            this.launcherAdminView.getCmbCinema().addItem(cinema.get(i));
        }
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        //Bouton Valider
        if(event.getSource() == launcherAdminView.getBtnValider()){
            if(launcherAdminView.getCmbCinema().getSelectedIndex() != -1){
                Cinema cinema = (Cinema) launcherAdminView.getCmbCinema().getSelectedItem();
                GestionCinemaView gestionCinemaView = new GestionCinemaView();
                frame.setCinema(cinema);
                GestionCinemaController gestionCinemaController = new GestionCinemaController(frame, gestionCinemaView, model);
                gestionCinemaView.setController(gestionCinemaController);
                frame.setContentPane(gestionCinemaView);
                frame.pack();
                frame.setLocationRelativeTo(null);
            }

        }
        //Bouton Ajouter
        else if(event.getSource() == launcherAdminView.getBtnAjouter()){
            AjouterCinemaView ajoutCinemaView = new AjouterCinemaView();
            AjouterCinemaController ajoutCinemaController = new AjouterCinemaController(frame, ajoutCinemaView, model);
            ajoutCinemaView.setController(ajoutCinemaController);
            frame.setContentPane(ajoutCinemaView);
            frame.pack();
            frame.setLocationRelativeTo(null);
        }
    }
}
