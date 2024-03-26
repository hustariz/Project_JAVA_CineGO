/*
 * LauncherController.java                                19 janv. 2016
 * CESI RILA 2015/2017
 */
package cineGOv02.client.controller;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import cineGOv02.client.model.ClientModel;
import cineGOv02.client.view.HomeView;
import cineGOv02.client.view.LauncherView;
import cineGOv02.common.entity.Cinema;
import cineGOv02.common.graphics.MainApp;

/**
 * controlleur de la vue du launcher de l'application Client
 * @author Remi
 *
 */
public class LauncherController implements ActionListener{
    /** Application générale */
    private MainApp frame;
    /** Vue de lancement de l'application client */
    private LauncherView launcherView;
    /** Modèle de données */
    private ClientModel model;
    /** Liste des cinémas */
    private ArrayList<Cinema> cinema;
    
    /**
     * constructeur de la vue de l'application client
     * @param frame
     * @param launcherView
     * @param factory
     */
    public LauncherController(MainApp frame, LauncherView launcherView, ClientModel model) {
        super();
        this.frame = frame;
        this.launcherView = launcherView;
        this.model = model;
        this.launcherView.setController(this);
        cinema = model.getAllCinema();
        for(int i = 0 ; i < cinema.size() ; i++){
            this.launcherView.getCmbCinema().addItem(cinema.get(i).getNom());
        }
        setImageSize(launcherView.getLblLogoCesi(), "images/LogoCesiAlternance.png");
    }
    
    
    /**
     * Affiche les images sur le composant pris en argument,
     * tout en le redimenssionnant à la taille du composant.
     * @param imagePath 
     * @param imageLabel
     */ 

	public void setImageSize(JLabel imageLabel,String imagePath){
		ImageIcon icon = new ImageIcon(imagePath);
		Image img = icon.getImage();
		Image newImg = img.getScaledInstance(imageLabel.getWidth(), imageLabel.getHeight(), Image.SCALE_SMOOTH);
		ImageIcon newImc = new ImageIcon(newImg);
		imageLabel.setIcon(newImc);
	}

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        if(event.getSource() == launcherView.getBtnValider()){
            Cinema cinema = this.cinema.get(launcherView.getCmbCinema().getSelectedIndex());
            frame.setCinema(cinema);
            HomeView homeView = new HomeView();
            HomeController homeController = new HomeController(frame, homeView, model);
            homeView.setController(homeController);
            frame.setContentPane(homeView);
            frame.pack();
            frame.setLocationRelativeTo(null);
        }
    }
}
