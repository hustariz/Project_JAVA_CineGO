package cineGOv02.client.controller;

import cineGOv02.client.model.ClientModel;
import cineGOv02.client.view.HomeView;
import cineGOv02.client.view.LoginUserView;
import cineGOv02.client.view.MovieDetailsView;
import cineGOv02.client.view.UserView;
import cineGOv02.common.entity.Cinema;
import cineGOv02.common.entity.Film;
import cineGOv02.common.entity.User;
import cineGOv02.common.graphics.Affiche;
import cineGOv02.common.graphics.Controller;
import cineGOv02.common.graphics.Genre;
import cineGOv02.common.graphics.MainApp;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.hibernate.Session;
import org.hibernate.SessionFactory;


/**
 * Classe du controlleur de la vue de la page d'accueil
 * @author hustariz
 */
public class HomeController implements ActionListener, MouseListener, DocumentListener, Controller{

    /** Liste de film de la BD */
    private ArrayList<Film> listeFilm;
    /** Liste de film à l'affiche aujourd'hui */
    private ArrayList<Film> listeFilmsDuJour;
    /** Liste de film filtrée */
    private ArrayList<Film> listeFilmFiltree;
    /** Modèle de données */
    private ClientModel model;
    /**  Variables affichéees dans textField_1(compteur) de la View.*/
    private int compteurPage = 0;
    /** Instance de la View */
    private HomeView homeView;
    /** Mainframe de l'application */
    private MainApp frame;
    /** Cinema sélectionné au chargement de l'appli */
    private Cinema cinema;
    private Calendar calendar;
    private Timestamp debut, fin;
    private ArrayList<ArrayList<Film>> listeFilmDispatch;

    /**
     * Constructeur du controller
     * @param frame 
     * @param homeView
     * @param factory 
     * @param homeModel
     */
    public HomeController(MainApp frame, HomeView homeView, ClientModel model){
        this.frame = frame;
        this.homeView = homeView;
        this.model = model;
        this.cinema = frame.getCinema();
        getAllFilm(cinema);
        listeFilmDispatch = new ArrayList<ArrayList<Film>>();
        dispatchFilm(listeFilm);
        // On initialise la liste des films
        initListFilm();
        homeView.getCompteur().setText("1/" + listeFilmDispatch.size());
        if(frame.getUser() != null){
            homeView.getLblCompte().setText("Bienvenue " + frame.getUser().getPrenom());
            homeView.getLblCompte().setVisible(true);
            homeView.getBtnCompte().setVisible(true);
            homeView.getBtnDeconnexion().setVisible(true);
        }else{
            homeView.getLblCompte().setText("");
            homeView.getLblCompte().setVisible(false);
            homeView.getBtnCompte().setVisible(false);
            homeView.getBtnDeconnexion().setVisible(false);
        }
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        if(event.getSource() == homeView.getBtnBackbutton()){
            previousPage();
        }else if(event.getSource() == homeView.getBtnNextbutton() 
                || event.getSource() == homeView.getTm()){
            nextPage();
        }else if(event.getSource() == homeView.getBtnDeconnexion()){
            frame.setUser(null);
            loadUser(null);
        }else if(event.getSource() == homeView.getBtnConnexion()){
            LoginUserView loginView = new LoginUserView();
            JDialog dialog = new JDialog(frame,"Connection",true);
            LoginController controller = new LoginController(frame, dialog, loginView, model, this);
            loginView.setController(controller);
            dialog.setContentPane(loginView);
            dialog.pack();
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
            dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }else if(event.getSource() == homeView.getBtnCompte()){
            UserView userView = new UserView();
            JDialog dialog = new JDialog(frame,"Mon compte",true);
            UserViewController controller = new UserViewController(frame,dialog,userView,model,this);
            userView.setController(controller);
            dialog.setContentPane(userView);
            dialog.pack();
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
            dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }else if(event.getSource() == homeView.getRdbtnFilmDuJour()){
            if(homeView.getRdbtnFilmDuJour().isSelected()==true){
                calendar = Calendar.getInstance();
                debut = new Timestamp(calendar.getTime().getTime());
                calendar.add(Calendar.DATE, 1);
                calendar.set(Calendar.HOUR_OF_DAY, 00);
                calendar.set(Calendar.MINUTE, 59);
                fin = new Timestamp(calendar.getTime().getTime());
                listeFilmsDuJour = model.getFilmJour(cinema,debut,fin);
                dispatchFilm(listeFilmsDuJour);
                initListFilm();
            }else{
                calendar = Calendar.getInstance();
                debut = new Timestamp(calendar.getTime().getTime());
                listeFilm = model.getFilms(cinema, debut);
                dispatchFilm(listeFilm);
                initListFilm();

            }
        }
    }

    /**
     * Récupère tous les films
     * @param cinema 
     */
    private void getAllFilm(Cinema cinema) {
        calendar = Calendar.getInstance();
        debut = new Timestamp(calendar.getTime().getTime());
        listeFilm = model.getFilms(cinema,debut);
    }

    /**
     * Réparti les films sur les différentes pages
     * @param liste 
     */
    private void dispatchFilm(ArrayList<Film> liste) {
        listeFilmDispatch = new ArrayList<ArrayList<Film>>();
        compteurPage = 0;
        for(int i =0 ; i < liste.size() ; i++){
            // Au début et tous les 6 films on ajoute une page dans le tableeau
            if(i % 6 == 0){
                listeFilmDispatch.add(new ArrayList<Film>());
            }
            listeFilmDispatch.get(listeFilmDispatch.size()-1).add(liste.get(i));   
        }
    }

    /**
     * Initialise les pages avec les films
     */
    public void initListFilm(){
        if(listeFilmDispatch.size() >0){
            for(int i = 0; i < 6 ; i++){
                Film film =null;
                if(i < listeFilmDispatch.get(compteurPage).size()){
                    film = listeFilmDispatch.get(compteurPage).get(i);
                }
                if(film != null){
                    this.homeView.getListPanel().get(i).setSize(200, 270);
                    this.homeView.setImageSize(homeView.getListPanel().get(i), film.getImage());
                    this.homeView.getListPanel().get(i).setFilm(film);  
                    this.homeView.getListPanel().get(i).revalidate();
                }else{
                    homeView.getListPanel().get(i).setFilm(null);
                    homeView.getListPanel().get(i).setOpaque(false);
                    homeView.getListPanel().get(i).setIcon(null);
                    homeView.getListPanel().get(i).revalidate();
                }
            }
            homeView.getCompteur().setText( (compteurPage+1) + "/" + listeFilmDispatch.size());
        }
    }

    @Override
    public void mouseClicked(MouseEvent event) {}
    @Override
    public void mouseReleased(MouseEvent arg0) {}

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseEntered(MouseEvent event) {
        if(event.getSource() instanceof Genre
                && !((Genre)event.getSource()).isActif()){
            ((Genre)event.getSource()).setBackground(Color.decode("#0067ce"));
            ((Genre)event.getSource()).setForeground(Color.WHITE);
        }
    }
    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseExited(MouseEvent event) {
        if(event.getSource() instanceof Genre 
                && !((Genre)event.getSource()).isActif()){
            ((JLabel)event.getSource()).setBackground(Color.WHITE);
            ((JLabel)event.getSource()).setForeground(Color.BLACK);
        }
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    @Override
    public void mousePressed(MouseEvent event) {
        if(event.getSource() == homeView.getAffiche1()){
            afficherDetails(homeView.getAffiche1().getFilm());
        }else if(event.getSource() == homeView.getAffiche2()){
            afficherDetails(homeView.getAffiche2().getFilm());
        }else if(event.getSource() == homeView.getAffiche3()){
            afficherDetails(homeView.getAffiche3().getFilm());
        }else if(event.getSource() == homeView.getAffiche4()){
            afficherDetails(homeView.getAffiche4().getFilm());
        }else if(event.getSource() == homeView.getAffiche5()){
            afficherDetails(homeView.getAffiche5().getFilm());
        }else if(event.getSource() == homeView.getAffiche6()){
            afficherDetails(homeView.getAffiche6().getFilm());
        }else if(event.getSource() instanceof Genre){
            if(!((Genre)event.getSource()).isActif()){
                ((Genre)event.getSource()).setBackground(Color.decode("#0067ce"));
                ((Genre)event.getSource()).setForeground(Color.WHITE);
                ((Genre)event.getSource()).setActif(true);
            }else{
                ((Genre)event.getSource()).setBackground(Color.WHITE);
                ((Genre)event.getSource()).setForeground(Color.BLACK);
                ((Genre)event.getSource()).setActif(false);
            }
            filterByGenre();
        }
    }

    /**
     * Filtre les films par genre
     * @param event 
     */
    private void filterByGenre() {
        listeFilmFiltree = new ArrayList<Film>();
        boolean zeroFiltre = true;
        for (int i = 0; i < listeFilm.size(); i++) {
            for (int j = 0; j < homeView.getFiltres().size(); j++) {
                if(listeFilm.get(i).getGenre().equals(homeView.getFiltres().get(j).getGenre())
                        && homeView.getFiltres().get(j).isActif()){
                    listeFilmFiltree.add(listeFilm.get(i));
                }
                if(zeroFiltre){
                    zeroFiltre = !homeView.getFiltres().get(j).isActif();
                }
            }
        }
        if(zeroFiltre){
            dispatchFilm(listeFilm);
        }else{
            dispatchFilm(listeFilmFiltree);
        }
        initListFilm();
    }
    /**
     * Filtre les film via leur nom
     * @param text
     * @param remove 
     */
    private void searchFromString(String text) {
        // Si aucun caractère n'est saisi on cahrge la liste en entier
        if(text.length() == 0){
            filterByGenre();
        }else{
            // Si on vient d'ajouter ou de supprimer un caractère dans le champ 
            // on met à jour la liste des films results
            for(Film film : listeFilm){
                // On vérifie que la taille du texte saisie soit bien inférieure 
                // ou égale à la taille du titre afin de les comparer
                String token = text.length() > film.getTitre().length() ? text.substring(0,film.getTitre().length()) : text;
                if(film.getTitre().substring(0, token.length()).toLowerCase().equals(token)){
                    if(!listeFilmFiltree.contains(film)){
                        listeFilmFiltree.add(film);
                    }
                }else{
                    if(listeFilmFiltree.contains(film)){
                        listeFilmFiltree.remove(film);
                    }
                }
            }
            dispatchFilm(listeFilmFiltree);
            initListFilm();
        }    
    }

    /**
     * Affiche les détails d'un film
     * @param film
     */
    private void afficherDetails(Film film) {
        if(film != null){
            MovieDetailsView detailsView = new MovieDetailsView();
            frame.setFilm(film);
            MovieDetailsController controller = new MovieDetailsController(frame, detailsView, model);
            detailsView.setController(controller);
            controller.loadUser(frame.getUser());
            frame.setContentPane(detailsView);
            frame.pack();
            frame.setLocationRelativeTo(null);
        }
    }

    /**
     * Affiche les films de la page suivante
     */
    private void nextPage() {
        compteurPage = compteurPage < listeFilmDispatch.size()-1 ? compteurPage+1 : 0;
        initListFilm();
        homeView.getCompteur().setText( (compteurPage+1)  + "/" + listeFilmDispatch.size());
        homeView.getTm().stop(); 
    }

    /**
     * Affiche les films de la page précédente
     */
    private void previousPage() {
        compteurPage = compteurPage > 0 ? compteurPage-1 : listeFilmDispatch.size()-1;
        initListFilm();
        homeView.getCompteur().setText( (compteurPage+1) + "/" + listeFilmDispatch.size());
        homeView.getTm().stop();  
    }

    /**
     * Redimensionne l'image de l'affiche du film
     * @param affiche
     * @param blob
     */
    public void setImageSize(Affiche affiche, Blob blob){
        BufferedImage buff;
        try {
            buff = ImageIO.read(blob.getBinaryStream());
            ImageIcon icon = new ImageIcon(buff);
            Image img = icon.getImage();
            Image newImg = img.getScaledInstance(affiche.getWidth(), affiche.getHeight(), Image.SCALE_SMOOTH);
            ImageIcon newImc = new ImageIcon(newImg);
            affiche.setIcon(null);
            affiche.setIcon(newImc);
            affiche.revalidate();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void changedUpdate(DocumentEvent e) {}

    /* (non-Javadoc)
     * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
     */
    @Override
    public void insertUpdate(DocumentEvent d) {
        Document doc = d.getDocument();

        try {
            searchFromString(doc.getText(0, doc.getLength()).toLowerCase());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    /* (non-Javadoc)
     * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
     */
    @Override
    public void removeUpdate(DocumentEvent d) {
        Document doc = d.getDocument();
        try {
            searchFromString(doc.getText(0, doc.getLength()).toLowerCase());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadUser(User user){
        if(user != null){
            homeView.getLblCompte().setText("Bienvenue " + user.getPrenom());
            homeView.getLblCompte().setVisible(true);
            homeView.getBtnCompte().setVisible(true);
            homeView.getBtnDeconnexion().setVisible(true);
            homeView.getBtnConnexion().setVisible(false);
        }else{
            homeView.getLblCompte().setText("");
            homeView.getLblCompte().setVisible(false);
            homeView.getBtnCompte().setVisible(false);
            homeView.getBtnDeconnexion().setVisible(false);
            homeView.getBtnConnexion().setVisible(true);
        }
    }
}

