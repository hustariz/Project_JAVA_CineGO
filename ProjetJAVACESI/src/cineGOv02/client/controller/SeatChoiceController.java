/*
 * SeatChoiceController.java                                6 mars 2016
 * CESI RILA 2015/2017
 */
package cineGOv02.client.controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import cineGOv02.client.model.ClientModel;
import cineGOv02.client.view.HomeView;
import cineGOv02.client.view.ReservationView;
import cineGOv02.client.view.SeatChoiceView;
import cineGOv02.common.graphics.MainApp;
import cineGOv02.common.graphics.SalleIcon;
import cineGOv02.common.graphics.SiegeIcon;
import cineGOv02.common.graphics.WallIcon;
import cineGOv02.common.util.SendAMail;
import jdk.nashorn.internal.scripts.JO;

/**
 * Controlleur de la vue de choix de la place sur un plan de salle
 * @author Remi
 *
 */
public class SeatChoiceController implements ActionListener, MouseListener {
    /** Fenêtre principale */
    private MainApp frame;
    /** Vue de sélection de la place */
    private SeatChoiceView seatView;
    /** Modèle de données */
    private ClientModel model;
    /** Plan de la salle */
    private String plan;
    /** Tableau représentant les différents élkéments de la Salle */
    private JPanel[][] panelIcon;
    /** Dimension du tableau */
    private static int X;
    /** Dimension du tableau */
    private static int Y;
    /** Taille Max des icones */
    private static int size = 44;
    /** Images */
    private ImageIcon selectedIconAllee, selectedIconSiege, selectedIconeSiegeSelect, selectedIconSiegeOccupe, 
    selectedIconPorte,selectedIconHand, selectedIconEcran,selectedIconMur, 
    iconeSiege, iconeSiegeOccupe,iconeSiegeHand, iconeAllee, iconeEcran, iconePorte,
    iconeMur, iconeSiegeSelect = null;
    /** Buffer d'image */
    private BufferedImage bufferImage;
    /** Jpanel d'affichage du plan */
    private JPanel grille; 
    /** Nombre de places à choisir */
    private int nbPlace;
    /** Liste des sièges selectionnées */
    private ArrayList<SiegeIcon> placeSelect = new ArrayList<SiegeIcon>();

    /**
     * Controlleur de la vue de sélection des places
     * @param frame
     * @param seatView2
     * @param factory
     */
    public SeatChoiceController(MainApp frame, SeatChoiceView seatView, ClientModel model) {
        super();
        this.frame = frame;
        this.seatView = seatView;
        this.model = model;
        plan = frame.getReservation().getSeance().getSalle().getPlanDeSalle();
        try {
            bufferImage = ImageIO.read(new File("images/siege_min.jpg"));
            iconeSiege = new ImageIcon(bufferImage);
            bufferImage = ImageIO.read(new File("images/siegeOccupe_min.jpg"));
            iconeSiegeOccupe = new ImageIcon(bufferImage);
            bufferImage = ImageIO.read(new File("images/siegeSelect_min.jpg"));
            iconeSiegeSelect = new ImageIcon(bufferImage);
            bufferImage = ImageIO.read(new File("images/siegeHand_min.jpg"));
            iconeSiegeHand = new ImageIcon(bufferImage);
            bufferImage = ImageIO.read(new File("images/allee_min.jpg"));
            iconeAllee = new ImageIcon(bufferImage);
            bufferImage = ImageIO.read(new File("images/ecran_min.jpg"));
            iconeEcran = new ImageIcon(bufferImage);
            bufferImage = ImageIO.read(new File("images/porte_min.jpg"));
            iconePorte = new ImageIcon(bufferImage);
            bufferImage = ImageIO.read(new File("images/wall_min.jpg"));
            iconeMur = new ImageIcon(bufferImage);
        } catch (IOException e) {
            System.out.println(e);
        }
        loadMap();
        if(frame.getSeance().getListeSiege().length() !=0){
            loadOccupedSeat();
        }
        addRollerZoom();
        nbPlace = frame.getReservation().getNbPlace();
        this.seatView.getLblNbPlace().setText(nbPlace + "");
    }

    /**
     * Chargement des éléments de la salle depuis le plan de salle au format XML
     */
    private void loadMap() {
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(plan));
            Document doc = db.parse(is);
            Element root = doc.getDocumentElement();
            X = Integer.parseInt(root.getAttribute("colonnes"));
            Y = Integer.parseInt(root.getAttribute("lignes"));
            grille = seatView.getPlan();
            size = 730 / X;
            grille.removeAll();
            grille.setLayout(new GridLayout(X, Y));
            panelIcon = new JPanel[X][Y];
            NodeList nodes = doc.getElementsByTagName("element");
            for (int i = 0; i < nodes.getLength(); i++) {
                Element element = (Element) nodes.item(i);
                int x = Integer.parseInt(element.getAttribute("X"));
                int y = Integer.parseInt(element.getAttribute("Y"));
                String type = element.getAttribute("type");
                JPanel panel = new JPanel();
                panel.setLayout(new BorderLayout());
                panel.setMinimumSize(new Dimension(size, size));
                panel.setPreferredSize(new Dimension(size, size));
                panel.setMaximumSize(new Dimension(size, size));
                SalleIcon icon = null;
                switch(type){
                case "ecran":
                    icon = new WallIcon();
                    ((WallIcon)icon).setEcran(true);
                    break;
                case "porte":
                    icon = new WallIcon();
                    ((WallIcon) icon).setPorte(true);
                    break;
                case "normal":
                    icon = new SiegeIcon();
                    ((SiegeIcon) icon).setNormal(true);
                    ((SiegeIcon) icon).setHand(false);
                    ((SiegeIcon) icon).setAllee(false);
                    ((SiegeIcon) icon).setSet(true);
                    icon.addMouseListener(this);
                    break;
                case "hand":
                    icon = new SiegeIcon();
                    ((SiegeIcon) icon).setNormal(false);
                    ((SiegeIcon) icon).setHand(true);
                    ((SiegeIcon) icon).setAllee(false);
                    ((SiegeIcon) icon).setSet(true);
                    icon.addMouseListener(this);
                    break;
                case "allee":
                    icon = new SiegeIcon();
                    ((SiegeIcon) icon).setNormal(false);
                    ((SiegeIcon) icon).setHand(false);
                    ((SiegeIcon) icon).setAllee(true);
                    ((SiegeIcon) icon).setSet(true);
                    icon.addMouseListener(this);
                    break;
                }
                panel.setOpaque(false);
                panel.setBorder(null);
                panel.add(icon);
                icon.setAnX(x);
                icon.setAnY(y);
                panelIcon[x][y] = panel;
            }

            for(int x = 0 ; x < X ; x++){
                for(int y = 0 ; y < Y ; y++){
                    // Placement des icones du mur
                    if(x == 0 || y == 0 || x == X-1 || y == Y-1){
                        if(panelIcon[x][y] == null){
                            JPanel panel = new JPanel();
                            panel.setLayout(new BorderLayout());
                            WallIcon wall = new WallIcon();
                            selectedIconMur = new ImageIcon(iconeMur.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH));
                            wall.setIcon(selectedIconMur);
                            panel.add(wall, BorderLayout.CENTER);
                            panelIcon[x][y] = panel;
                        }else if(((WallIcon)panelIcon[x][y].getComponent(0)).isEcran()){
                            selectedIconEcran = new ImageIcon(iconeEcran.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH));
                            ((WallIcon)panelIcon[x][y].getComponent(0)).setIcon(selectedIconEcran);
                        }else if(((WallIcon)panelIcon[x][y].getComponent(0)).isPorte()){
                            selectedIconPorte = new ImageIcon(iconePorte.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH));
                            ((WallIcon)panelIcon[x][y].getComponent(0)).setIcon(selectedIconPorte);
                        }
                        // Placement des icones de sièges
                    }else{
                        if(!(panelIcon[x][y] == null)){
                            if(((SiegeIcon)panelIcon[x][y].getComponent(0)).isNormal()){
                                selectedIconSiege = new ImageIcon(iconeSiege.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH));
                                ((SiegeIcon)panelIcon[x][y].getComponent(0)).setIcon(selectedIconSiege); 
                            }else if(((SiegeIcon)panelIcon[x][y].getComponent(0)).isHand()){
                                selectedIconHand = new ImageIcon(iconeSiegeHand.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH));
                                ((SiegeIcon)panelIcon[x][y].getComponent(0)).setIcon(selectedIconHand); 
                            }else if(((SiegeIcon)panelIcon[x][y].getComponent(0)).isAllee()){
                                selectedIconAllee = new ImageIcon(iconeAllee.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH));
                                ((SiegeIcon)panelIcon[x][y].getComponent(0)).setIcon(selectedIconAllee); 
                            }
                        }else{
                            panelIcon[x][y] = new JPanel();
                            //panelIcon[x][y].add(new SiegeIcon());
                        }
                    }
                    panelIcon[x][y].revalidate();
                    grille.add(panelIcon[x][y]);
                    panelIcon[x][y].setBorder(BorderFactory.createLineBorder(Color.BLACK, 0));
                }
            }
            grille.revalidate();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }   
    }

    /**
     * Chargement des places occupées pour cette séance
     */
    private void loadOccupedSeat() {
        String[] occuppees = frame.getSeance().getListeSiege().split(":");
        for (String place : occuppees) {
            int x = Integer.parseInt(place.substring(0,place.indexOf(",")));
            int y = Integer.parseInt(place.substring(place.indexOf(",")+1,place.length()));
            ((SiegeIcon)panelIcon[x][y].getComponent(0)).setOccupe(true);
            selectedIconeSiegeSelect =  new ImageIcon(iconeSiegeOccupe.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH));
            ((SiegeIcon)panelIcon[x][y].getComponent(0)).setIcon(selectedIconeSiegeSelect);
        }
    }

    /**
     * Ajout d'un zoom via la molette de la souris
     */
    private void addRollerZoom() {
        this.frame.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent event) {
                if(panelIcon.length > 0){
                    if(event.getWheelRotation() < 0 && size < 60 ){
                        size++;
                    }else if(size > 730 / X){
                        size--;
                    }
                    for(int x = 0 ; x < X ; x++){
                        for(int y = 0 ; y < Y ; y++){  
                            panelIcon[x][y].setMinimumSize(new Dimension(size, size));
                            panelIcon[x][y].setPreferredSize(new Dimension(size, size));
                            panelIcon[x][y].setMaximumSize(new Dimension(size, size));
                            if(panelIcon[x][y].getComponentCount() > 0 && panelIcon[x][y].getComponent(0) instanceof SiegeIcon && ((SalleIcon)panelIcon[x][y].getComponent(0)).isSet()){
                                if(((SiegeIcon)panelIcon[x][y].getComponent(0)).isNormal()){
                                    selectedIconSiege = new ImageIcon(iconeSiege.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH));
                                    ((SiegeIcon)panelIcon[x][y].getComponent(0)).setIcon(selectedIconSiege);
                                }else if(((SiegeIcon)panelIcon[x][y].getComponent(0)).isHand()){
                                    selectedIconHand = new ImageIcon(iconeSiegeHand.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH));
                                    ((SiegeIcon)panelIcon[x][y].getComponent(0)).setIcon(selectedIconHand);
                                }else if (((SiegeIcon)panelIcon[x][y].getComponent(0)).isAllee()){
                                    selectedIconAllee = new ImageIcon(iconeAllee.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH));
                                    ((SiegeIcon)panelIcon[x][y].getComponent(0)).setIcon(selectedIconAllee);
                                }
                                if (((SiegeIcon)panelIcon[x][y].getComponent(0)).isOccupe()){
                                    selectedIconSiegeOccupe = new ImageIcon(iconeSiegeOccupe.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH));
                                    ((SiegeIcon)panelIcon[x][y].getComponent(0)).setIcon(selectedIconSiegeOccupe);
                                }
                                if (((SiegeIcon)panelIcon[x][y].getComponent(0)).isSelected()){
                                    selectedIconeSiegeSelect = new ImageIcon(iconeSiegeSelect.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH));
                                    ((SiegeIcon)panelIcon[x][y].getComponent(0)).setIcon(selectedIconeSiegeSelect);
                                }
                                ((SiegeIcon)panelIcon[x][y].getComponent(0)).revalidate();
                            }else if(panelIcon[x][y].getComponent(0) instanceof WallIcon){
                                if(((WallIcon) panelIcon[x][y].getComponent(0)).isEcran()){
                                    selectedIconEcran = new ImageIcon(iconeEcran.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH));
                                    ((WallIcon)panelIcon[x][y].getComponent(0)).setIcon(selectedIconEcran);
                                }else if(((WallIcon) panelIcon[x][y].getComponent(0)).isPorte()){
                                    selectedIconPorte = new ImageIcon(iconePorte.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH));
                                    ((WallIcon)panelIcon[x][y].getComponent(0)).setIcon(selectedIconPorte);
                                }else{
                                    selectedIconMur = new ImageIcon(iconeMur.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH));
                                    ((WallIcon)panelIcon[x][y].getComponent(0)).setIcon(selectedIconMur);
                                }
                            }
                            panelIcon[x][y].getComponent(0).revalidate();
                        }
                    }
                }
            }
        });
    }

    @Override
    public void mouseClicked(MouseEvent arg0) {}
    @Override
    public void mouseEntered(MouseEvent arg0) {}
    @Override
    public void mouseExited(MouseEvent arg0) {}
    @Override
    public void mouseReleased(MouseEvent arg0) {}

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    @Override
    public void mousePressed(MouseEvent arg0) {
        if(arg0.getSource() instanceof SiegeIcon){
            if(SwingUtilities.isLeftMouseButton(arg0)){
                if(!((SiegeIcon)arg0.getSource()).isOccupe() 
                        && !((SiegeIcon)arg0.getSource()).isSelected() 
                        && (((SiegeIcon)arg0.getSource()).isNormal() || ((SiegeIcon)arg0.getSource()).isHand())
                        && nbPlace > 0){
                    selectedIconeSiegeSelect = new ImageIcon(iconeSiegeSelect.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH));
                    ((SiegeIcon)arg0.getSource()).setIcon(selectedIconeSiegeSelect);
                    ((SiegeIcon)arg0.getSource()).setSelected(true);
                    placeSelect.add((SiegeIcon)arg0.getSource());
                    nbPlace--;
                    if(nbPlace == 0){
                        seatView.getBtnConfirmer().setEnabled(true);
                        seatView.getBtnConfirmer().setForeground(SystemColor.textHighlight);
                    }
                }
            }else if (SwingUtilities.isRightMouseButton(arg0)){
                if(!((SiegeIcon)arg0.getSource()).isOccupe() && ((SiegeIcon)arg0.getSource()).isSelected()){
                    if(((SiegeIcon)arg0.getSource()).isHand()){
                        selectedIconHand = new ImageIcon(iconeSiegeHand.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH));
                        ((SiegeIcon)arg0.getSource()).setIcon(selectedIconHand);
                    }else if(((SiegeIcon)arg0.getSource()).isNormal()){
                        selectedIconSiege = new ImageIcon(iconeSiege.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH));
                        ((SiegeIcon)arg0.getSource()).setIcon(selectedIconSiege);
                    }
                    placeSelect.remove((SiegeIcon)arg0.getSource());
                    nbPlace++;
                    ((SiegeIcon)arg0.getSource()).setSelected(false);
                    seatView.getBtnConfirmer().setEnabled(false);
                    seatView.getBtnConfirmer().setForeground(Color.GRAY);
                }
            }
            seatView.getLblNbPlace().setText(nbPlace + "");
        }
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) { 
        if(event.getSource() == seatView.getBtnAnnuler()){
            frame.setReservation(null);
            frame.setSeance(null);
            ReservationView resa = new ReservationView();
            ReservationController controller = new ReservationController(frame, resa, model);
            resa.setController(controller);
            frame.setContentPane(resa);
            frame.pack();
        }else if(event.getSource() == seatView.getBtnPlacer()){
            int nbPlaceHand = seatView.getTxtNbPlaceHand().getText().equals("") ? 0 : Integer.parseInt(seatView.getTxtNbPlaceHand().getText());
            placementAuto(nbPlaceHand);
        }else if(event.getSource() == seatView.getBtnConfirmer()){
            String placesSeance = "";
            String placesResa = "";
            for(int i =0 ; i < placeSelect.size();i++){
                placesSeance += placeSelect.get(i).getAnX() + "," + placeSelect.get(i).getAnY() +":";
                placesResa += i+1 + "- Rang : " + placeSelect.get(i).getAnX() + ", place n° " + placeSelect.get(i).getAnY()  + ".<br >";
            }
            frame.getSeance().setListeSiege(frame.getSeance().getListeSiege() + placesSeance);
            frame.getReservation().setListeSiege(placesResa);
            model.saveEntity(frame.getReservation());
            model.updateEntity(frame.getSeance());
            String body = "<strong>Bonjour " + (frame.getUser() != null ? frame.getUser().getPrenom() : "")   + "</strong>, <br /><br />"
                    + "<strong>Voici un récapitulatif de votre commande :</strong><br/><br/>"
                    + "<ul><li>Tire du film : <strong>" + frame.getFilm() + "</strong></li>"               
                    + "<li>Heure de la séance : <strong> " + frame.getSeance().getDebut() + "</strong></li>"
                    + "<li>Nombre de place : <strong>" + nbPlace + "</strong></li>"
                    + "<li>Salle : <strong>" + frame.getSeance().getSalle().getNumeroDeSalle() + "</strong></li>"
                    + "<li>Place(s) : <strong>" + placesResa + "</strong></li>"
                    + "<li>Prix : <strong>" + frame.getReservation().getPrix() + "</strong></li></ul><br /><br />"
                    + "<span style=\"font-style:italic;\">"
                    + "Votre reservation est modifiable et ce jusqu'à 24h avant le début de la séance.</span><br /><br />"
                    + "<strong>Toute l'équipe de CineGO vous souhaite un bon film !</strong><br /><br />"
                    + "Amicalement,<br />"
                    + "L'équipe de CinéGO";
            if(frame.getUser() != null){
                String subject = "Confirmation de votre commande CineGO.";
                int input = JOptionPane.showConfirmDialog(seatView, "Votre paiment a été accepté un mail récapitulatif vous a étét envoyé", "Information paiement",JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE);
                if(input == JOptionPane.OK_OPTION){
                    HomeView home = new HomeView();
                    HomeController controller = new HomeController(frame, home, model);
                    home.setController(controller);
                    frame.setContentPane(home);
                }
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
                int input = JOptionPane.showConfirmDialog(seatView,pane ,"Information paiement",JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE); 
                if(input == JOptionPane.OK_OPTION){
                    HomeView home = new HomeView();
                    HomeController controller = new HomeController(frame, home, model);
                    home.setController(controller);
                    frame.setContentPane(home);
                }
            }
        }
    }

    /**
     * Algorithme de placement automatique
     * @param nbPlaceHand
     * @param plcmt
     */
    private void placementAuto(int nbPlaceHand) {
        // Reset des position existantes
        for (int i = 0; i < placeSelect.size(); i++) {
            int x = placeSelect.get(i).getAnX();
            int y = placeSelect.get(i).getAnY();
            ((SiegeIcon)panelIcon[x][y].getComponent(0)).setSelected(false);
            if(((SiegeIcon)panelIcon[x][y].getComponent(0)).isNormal()){
                selectedIconSiege = new ImageIcon(iconeSiegeSelect.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH));
                ((SiegeIcon)panelIcon[x][y].getComponent(0)).setIcon(selectedIconSiege);
            }else if(((SiegeIcon)panelIcon[x][y].getComponent(0)).isHand()){
                selectedIconHand = new ImageIcon(iconeSiegeSelect.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH));
                ((SiegeIcon)panelIcon[x][y].getComponent(0)).setIcon(selectedIconHand);
            }
            ((SiegeIcon)panelIcon[x][y].getComponent(0)).repaint();
            panelIcon[x][y].repaint();
        }
        placeSelect = new ArrayList<SiegeIcon>();
        nbPlace = frame.getReservation().getNbPlace();
        int totalPlace = nbPlace;
        nbPlace = nbPlace - nbPlaceHand > 0 ?  nbPlace - nbPlaceHand : 0;

        // On place les handicapées
        for (int i = 0; i < panelIcon.length-1; i++) {
            for (int j = 0; j < panelIcon[i].length-1; j++) {
                if(panelIcon[i][j].getComponentCount() > 0 
                        && !(((SalleIcon)panelIcon[i][j].getComponent(0)) instanceof WallIcon)){
                    if(((SiegeIcon)panelIcon[i][j].getComponent(0)).isHand() 
                            && !((SiegeIcon)panelIcon[i][j].getComponent(0)).isOccupe()
                            && nbPlaceHand > 0){
                        placeSelect.add((SiegeIcon) panelIcon[i][j].getComponent(0));
                        nbPlaceHand--;
                    }
                }
            }
        }
        if(nbPlaceHand > 0){
            JOptionPane.showMessageDialog(frame, "Il n'y a pas assez de places handicapées, disponibles des places normales seront selectionnées à la place.", "Echec", JOptionPane.ERROR_MESSAGE);
            nbPlace = nbPlace + nbPlaceHand;

        }
        boolean ok = false;
        ArrayList<ArrayList<SiegeIcon>> precedents = new ArrayList<ArrayList<SiegeIcon>>();
        // PArcours dans un sens
        for (int i = panelIcon.length-1; i >= 1 ; i--) {
            ArrayList<SiegeIcon> ligne = new ArrayList<SiegeIcon>();
            precedents.add(ligne);
            for (int j = 0; j < panelIcon[i].length-1; j++) {
                if(panelIcon[i][j].getComponentCount() > 0  &&
                        !(((SalleIcon)panelIcon[i][j].getComponent(0)) instanceof WallIcon)){
                    if(!((SiegeIcon)panelIcon[i][j].getComponent(0)).isAllee() 
                            && !((SiegeIcon)panelIcon[i][j].getComponent(0)).isHand()){
                        // Pour l'initialisation
                        if(!((SiegeIcon)panelIcon[i][j].getComponent(0)).isOccupe()){
                            ligne.add(((SiegeIcon)panelIcon[i][j].getComponent(0)));
                            System.out.println(" first loop add :" + i + " " + j);
                        }
                    }else if(((SiegeIcon)panelIcon[i][j].getComponent(0)).isAllee()){
                        break;
                    }
                } 
            }
            if(ligne.size() >= nbPlace){
                ok = true;
                break;
            }
        }
        // Parcours dans l'autre sens
        for (int i = panelIcon.length-1; i >= 1 ; i--) {
            ArrayList<SiegeIcon> ligne = new ArrayList<SiegeIcon>();
            precedents.add(ligne);
            for (int j = panelIcon[i].length-1; j >= 1 ; j--) {
                if(panelIcon[i][j].getComponentCount() > 0  &&
                        !(((SalleIcon)panelIcon[i][j].getComponent(0)) instanceof WallIcon)){
                    if(!((SiegeIcon)panelIcon[i][j].getComponent(0)).isAllee() 
                            && !((SiegeIcon)panelIcon[i][j].getComponent(0)).isHand()){
                        // Pour l'initialisation
                        if(!((SiegeIcon)panelIcon[i][j].getComponent(0)).isOccupe()){
                            ligne.add(((SiegeIcon)panelIcon[i][j].getComponent(0)));
                        }
                    }else if(((SiegeIcon)panelIcon[i][j].getComponent(0)).isAllee()){
                        break;
                    }
                } 
            }
            if(ligne.size() >= nbPlace){
                ok = true;
                break;
            }
        }

        if(!ok){
            boolean complete = false;
            do{
                int max = 0;
                int indiceMax = 0;
                for (int i = 0; i < precedents.size(); i++) {
                    if(max < precedents.get(i).size()){
                        max = precedents.get(i).size();
                        indiceMax = i;
                    }
                }
                for (int i = 0; i < precedents.get(indiceMax).size() && placeSelect.size() < totalPlace; i++) {
                    if(!placeSelect.contains(precedents.get(indiceMax).get(i))){
                        placeSelect.add(precedents.get(indiceMax).get(i));
                        nbPlace--;
                    }
                }
                precedents.remove(indiceMax);
                if(placeSelect.size() >= totalPlace){
                    complete = true;
                }

            }while(!complete);
        }else{
            for(int i = 0 ; i < precedents.get(precedents.size()-1).size() && placeSelect.size() < totalPlace ; i++){
                if(!placeSelect.contains(precedents.get(precedents.size()-1).get(i))){
                placeSelect.add(precedents.get(precedents.size()-1).get(i));
                nbPlace--;
                }
            }
        }

        for (int i = 0; i < placeSelect.size(); i++) {
            int x = placeSelect.get(i).getAnX();
            int y = placeSelect.get(i).getAnY();
            System.out.println("x :" + x  + " y :" + y);
            placeSelect.get(i).setSelected(true);
            selectedIconeSiegeSelect = new ImageIcon(iconeSiegeSelect.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH));
            placeSelect.get(i).setIcon(selectedIconeSiegeSelect);
            placeSelect.get(i).repaint();
            panelIcon[x][y].repaint();
        }
        this.seatView.getLblNbPlace().setText(nbPlace + "");
        seatView.getBtnConfirmer().setEnabled(true);
        seatView.getBtnConfirmer().setForeground(SystemColor.textHighlight);
    }  
}

