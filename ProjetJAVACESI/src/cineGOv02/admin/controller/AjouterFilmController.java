/*
 * AjouterFilmController.java                                19 févr. 2016
 * IUT Info1 2013-2014 Groupe 3
 */
package cineGOv02.admin.controller;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import cineGOv02.admin.model.AdminModel;
import cineGOv02.admin.view.AjouterFilmView;
import cineGOv02.admin.view.GestionCinemaView;
import cineGOv02.common.entity.Cinema;
import cineGOv02.common.entity.Film;
import cineGOv02.common.graphics.MainApp;


/**
 * Controlleur de la vue d'ajout de film
 * @author hustariz
 *
 */
public class AjouterFilmController implements ActionListener, KeyListener{

    /** Fenêtre principale */
    private MainApp frame;

    /** Vue d'ajout d'un film */
    private AjouterFilmView ajouterFilmView;

    /** Modèle de données */
    private AdminModel model;

    /** Cinéma qu'on va pouvoir éditer. */
    private Cinema cinema;
    /** Couleur du film dans le planning en hexa */
    private int hexColor;
    /** Film créé */
    private Film film;
    /** Titre du film */
    private String titre;
    /** Synospis */
    private String overview;
    /** Genre du film*/
    private String genre;
    /** Date de sortie */
    private static Date releaseDate;
    /** Durée */
    private static int runTime = 0;
    /** Note */
    private static double rate;
    /**Réalisateur */
    private static String realisateur;
    /** Casting */
    private static String casting = null;
    /** Date de sortie du film*/
    private static java.sql.Date dateSortie = null;
    /** Poster au format Blob*/
    private static Color color;
    /** Couleur du panel du film dans le planning */
    private static String couleur;
    /** Message */
    private static final String MSG_ERROR_IMAGEFORMAT_EXCEPTION ="Veuillez renseigner le chemin vers une image VALIDE pour votre film!";
    /** Message */
    private static final String MSG_ERROR_NOHTMLCOLOR = "Veuillez générer une couleur pour le planning!";
    /** Message */
    private static final String MSG_ERROR_NOTITLE = "Veuillez renseigner un titre pour votre film!";
    /** Message */
    private static final String MSG_ERROR_NOOVERVIEW = "Veuillez renseigner un synopsys pour votre film!";
    /** Message */
    private static final String MSG_ERROR_NOCASTING = "Veuillez renseigner un casting pour votre film!";
    /** Message */
    private static final String MSG_ERROR_NOGENRE = "Veuillez renseigner un genre pour votre film!";
    /** Message */
    private static final String MSG_ERROR_NOREALISATEUR = "Veuillez renseigner un realisateur pour votre film!";
    /** Message */
    private static final String MSG_ERROR_NODATESORTIE = "Veuillez renseigner une date de sortie pour votre film!";
    /** Message */
    private static final String MSG_ERROR_NORUNTIME = "Veuillez renseigner une durée pour votre film!";
    /** Message */
    private static final String MSG_ERROR_NORATING = "Veuillez renseigner une note pour votre film!";
    /** Message */
    private static final String MSG_ERROR_WRONGRATING = "Veuillez respecter le format de rating : N.N (/10)!";
    /** Message */
    private static final String MSG_SUCCES_SAUVEGARDE = "Votre film a bien été enregistré dans notre base de donnée!";

    /** Date d'ajout du film */
    private static java.sql.Date dateAjout;

    /** Poster au format Blob*/
    private static Blob blob = null;

    /** URL de l'image en local */
    URL imageURL;
    /** Buffer d'image */
    BufferedImage originalImage;
    /** Buffer de data */
    ByteArrayOutputStream baos=new ByteArrayOutputStream();
    /** Tableau de byte */
    byte[] imageInByte;

    /**
     * constructeur du controlleur
     * @param frame
     * @param ajouterFilmView
     * @param factory
     * @param cinema
     */
    public AjouterFilmController(MainApp frame, AjouterFilmView ajouterFilmView, AdminModel model, Cinema cinema) {
        this.frame = frame;
        this.ajouterFilmView = ajouterFilmView;
        this.model = model;
        this.cinema = cinema;
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        if(event.getSource() == ajouterFilmView.getBtnAnnuler()){
            GestionCinemaView gestionCinemaView = new GestionCinemaView();
            GestionCinemaController gestionCinemaController = new GestionCinemaController(frame, gestionCinemaView, model);
            gestionCinemaView.setController(gestionCinemaController);
            frame.setContentPane(gestionCinemaView);
            frame.pack();
            frame.setLocationRelativeTo(null);
            couleur = null;
        }
        else if(event.getSource() == ajouterFilmView.getBtnSauvegarder()){
            /** on sauvegarde tous les champs et on gère les exception éventuellements 
             * Film film = new Film(idMovie,titre, runTime, genre, realisateur, date, today, casting,  overview, rate, blob, couleurs);
               filmList.add(film);*/

            // Obtient la date
            try {
                releaseDate = ajouterFilmView.getDateChooser().getDate();
                dateSortie = new java.sql.Date(releaseDate.getTime());
            } catch (NullPointerException e) {
                JOptionPane.showMessageDialog(ajouterFilmView, MSG_ERROR_NODATESORTIE, "Error", JOptionPane.ERROR_MESSAGE);
            }



            //Obtention de l'image à partir de l'URL et convertion en type java.SQL.blob
            try {
                imageURL = new URL("file:///" + ajouterFilmView.getTxtFieldUrlImage().getText());
                originalImage = ImageIO.read(imageURL);
                ImageIO.write(originalImage, "jpg", baos );
                baos.flush();
                imageInByte = baos.toByteArray();
                blob = new javax.sql.rowset.serial.SerialBlob(imageInByte);
                //Persist - in this case to a file
                FileOutputStream fos = new FileOutputStream("images/myImage.jpg");
                baos.writeTo(fos);
                fos.close();
            } catch (IOException | SQLException | java.lang.IllegalArgumentException e) {
                JOptionPane.showMessageDialog(ajouterFilmView, MSG_ERROR_IMAGEFORMAT_EXCEPTION,"Error", JOptionPane.ERROR_MESSAGE);
            }

            titre = ajouterFilmView.getTxtFieldTitre().getText();
            overview = ajouterFilmView.getTxtAreaSyno().getText();
            casting = ajouterFilmView.getTxtFieldCasting().getText();
            genre = ajouterFilmView.getTxtFieldGenre().getText();
            realisateur = ajouterFilmView.getTxtFieldRealisateur().getText();

            if ((ajouterFilmView.getTxtFieldDuree().getText()).equals("")){
                ajouterFilmView.getTxtFieldDuree().setText("0");
            }
            runTime = Integer.parseInt(ajouterFilmView.getTxtFieldDuree().getText());

            if ((ajouterFilmView.getTxtFieldNote().getText()).equals("")){
                ajouterFilmView.getTxtFieldNote().setText("0");
            }
            rate = Double.parseDouble(ajouterFilmView.getTxtFieldNote().getText());
            dateAjout = new java.sql.Date(Calendar.getInstance().getTime().getTime());

            if (couleur == null || hexColor == 0){
                JOptionPane.showMessageDialog(ajouterFilmView, MSG_ERROR_NOHTMLCOLOR,"Error", JOptionPane.ERROR_MESSAGE);
            }else if(titre.equals("")){
                JOptionPane.showMessageDialog(ajouterFilmView, MSG_ERROR_NOTITLE,"Error", JOptionPane.ERROR_MESSAGE);
            }else if(overview.equals("")){
                JOptionPane.showMessageDialog(ajouterFilmView, MSG_ERROR_NOOVERVIEW,"Error", JOptionPane.ERROR_MESSAGE);
            }else if(casting.equals("")){
                JOptionPane.showMessageDialog(ajouterFilmView, MSG_ERROR_NOCASTING,"Error", JOptionPane.ERROR_MESSAGE);
            }else if(genre.equals("")){
                JOptionPane.showMessageDialog(ajouterFilmView, MSG_ERROR_NOGENRE,"Error", JOptionPane.ERROR_MESSAGE);
            }else if(realisateur.equals("")){
                JOptionPane.showMessageDialog(ajouterFilmView, MSG_ERROR_NOREALISATEUR,"Error", JOptionPane.ERROR_MESSAGE);
            }else if(runTime == 0){
                JOptionPane.showMessageDialog(ajouterFilmView, MSG_ERROR_NORUNTIME,"Error", JOptionPane.ERROR_MESSAGE);
            }else if(rate == 0){
                JOptionPane.showMessageDialog(ajouterFilmView, MSG_ERROR_NORATING,"Error", JOptionPane.ERROR_MESSAGE);
            }else if(!(ajouterFilmView.getTxtFieldNote().getText().contains("."))){
                JOptionPane.showMessageDialog(ajouterFilmView, MSG_ERROR_WRONGRATING,"Error", JOptionPane.ERROR_MESSAGE);
            }else if(dateSortie == null){
                JOptionPane.showMessageDialog(ajouterFilmView, MSG_ERROR_NODATESORTIE, "Error", JOptionPane.ERROR_MESSAGE);
            }else if(ajouterFilmView.getTxtFieldUrlImage().getText().equals("") || blob == null){
                
            }else{
                film = new Film(0, titre, runTime, genre, realisateur, dateSortie, dateAjout, casting,  overview, rate, blob, couleur);
                model.saveEntity(film);

                JOptionPane.showMessageDialog(ajouterFilmView, MSG_SUCCES_SAUVEGARDE,"Success", JOptionPane.INFORMATION_MESSAGE);
                GestionCinemaView view = new GestionCinemaView();
                GestionCinemaController controller = new GestionCinemaController(frame, view, model);
                view.setController(controller);
                frame.setContentPane(view);
                frame.pack();
                frame.setLocationRelativeTo(null);
            }
            

        }
        else if (event.getSource() == ajouterFilmView.getBtnPalette()){
            /** Algorithme de génération aléatoire de couleur donc on créer une variable qu'on va remplir aléatoirement avec 6 chiffres ou lettres mélangées.
             * On affichera après le résultat dans un jlabel à côté du champ, que l'admin puisse voir à quoi correspon cette couleur 
             * Ajouter une icone stylé*/
            Random rand = new Random();
            hexColor = rand.nextInt(0xFFFFFF);
            color = generatePastelColor();
            ajouterFilmView.getPnlColor().setBackground(color);
            couleur = Integer.toString(color.getRGB());
        }
        else if(event.getSource() == ajouterFilmView.getBtnParcourir()){
            /** Fonction qui ouvre une fenêtre "Parcourir dans l'ordinateur".
             * Ajouter une icone stylé */
            JFileChooser browser = new JFileChooser();
            browser.showOpenDialog(null);
            File file = browser.getSelectedFile();
            String filename = file.getAbsolutePath();
            ajouterFilmView.getTxtFieldUrlImage().setText(filename);
        }
    }

    @Override
    public void keyPressed(KeyEvent arg0) {}
    @Override
    public void keyReleased(KeyEvent arg0) {}

    @Override
    public void keyTyped(KeyEvent event) {
        if(event.getSource() == ajouterFilmView.getTxtFieldDuree()){
            char c = event.getKeyChar();
            if(!(Character.isDigit(c) || c==KeyEvent.VK_DELETE) || ajouterFilmView.getTxtFieldDuree().getText().length() >= 3){
                Toolkit.getDefaultToolkit().beep();
                event.consume();
            }
            // Permet de rentrer uniquement des nombres décimaux et de les supprimer.
        } else if(event.getSource() == ajouterFilmView.getTxtFieldNote()) {
            char c = event.getKeyChar();
            if(!(Character.isDigit(c) || c==KeyEvent.VK_DELETE || c==KeyEvent.VK_PERIOD) || ajouterFilmView.getTxtFieldNote().getText().length() >= 3){
                Toolkit.getDefaultToolkit().beep();
                event.consume();
            }else if(c == KeyEvent.VK_PERIOD && ((JTextField)event.getSource()).getText().contains(".")){
                event.consume();
            }
        }
    }
    /**
     * Générateur de couleurs pastels
     * @return color
     */
    public Color generatePastelColor() {
        Random random = new Random();
        // Will produce only bright / light colours:
        float red = (float) (random.nextFloat() / 2 + 0.5);
        float green = (float) (random.nextFloat() / 2 + 0.5);
        float blue = (float) (random.nextFloat() / 2 + 0.5);
        Color color = new Color(red, green, blue);
        return color;
    }
}