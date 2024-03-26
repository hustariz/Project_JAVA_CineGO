/*
 * UserViewController.java                                8 févr. 2016
 * CESI RILA 2015/2017
 */
package cineGOv02.client.controller;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import cineGOv02.client.model.ClientModel;
import cineGOv02.client.view.ReservationView;
import cineGOv02.client.view.UserView;
import cineGOv02.common.entity.Film;
import cineGOv02.common.entity.Reservation;
import cineGOv02.common.entity.Seance;
import cineGOv02.common.entity.User;
import cineGOv02.common.graphics.Controller;
import cineGOv02.common.graphics.MainApp;
import cineGOv02.common.graphics.ResaCellRenderer;
import cineGOv02.common.util.PasswordUtil;

/**
 * Controlleur de la vue d'affichage des infos utilistaeurs
 * @author Remi
 *
 */
public class UserViewController implements MouseListener, ActionListener{

    /** Fenêtre principale */
    private MainApp frame;
    /** Fenètre de connexion */
    private JDialog dialog;
    /** Fenêtre d'affichage des infos de l'utilisateur */
    private UserView userView;
    /** Modèle de données */
    private ClientModel model;
    /** Controlleur de la vue précédente */
    private Controller controller;
    /** Liste de réservation de l'utilisateur */
    private ArrayList<Reservation> resa;
    /** Message d'info concernant la non concordance des mots de passes */
    public static final String MSG_WRONG_PASSWD = "Les mots de passe ne corresponde pas.";
    /** Message concernant la non-saisie du mot de passe */
    public static final String MSG_PASSWD_EMPTY = "Veuillez saisir votre mot de passe et le confimer.";
    /** Message */
    public static final String MSG_MODIF_OK = "Compte modifié avec succès !";
    
    /**
     * Constructeur du controlleur de la vue utilisateur
     * @param frame
     * @param dialog
     * @param userView
     * @param factory
     * @param controller 
     * @param homeController
     */
    public UserViewController(MainApp frame, JDialog dialog, UserView userView, ClientModel model,
            Controller controller) {
        this.model = model;
        this.frame = frame;
        this.userView = userView;
        this.dialog = dialog;
        this.controller = controller;
        loadInfos();
    }

    /**
     * Chargement des infos de l'utilisateur
     */
    private void loadInfos() {
        User user = frame.getUser();
        if(user != null){
            userView.getLblNom().setText(user.getNom());
            userView.getLblPrenom().setText(user.getPrenom());
            userView.getLblMail().setText(user.getMail());
            userView.getLblAvoir().setText(user.getAvoir() + "€");
            resa = model.getAllResa(user);
            UIManager.put("List.opaque", "false");
            UIManager.put("List.cellRenderer.opaque","false");
            UIManager.put("List.opaque", false);
            UIManager.put("List.cellRenderer.opaque",false);
            DefaultListModel<Reservation> model = new DefaultListModel<Reservation>();
            for (Reservation reservation : resa) {
                model.addElement(reservation);
            }
            userView.getListResa().setCellRenderer(new ResaCellRenderer());
            userView.getListResa().setFixedCellHeight(90);
            userView.getListResa().setFixedCellWidth(300);
            userView.getListResa().setModel(model);

            userView.getListResa().addMouseListener(new MouseListener() {
                @Override
                public void mouseReleased(MouseEvent arg0) {}
                @Override
                public void mousePressed(MouseEvent event) {
                    JList list = (JList)event.getSource();
                    int index = list.locationToIndex(event.getPoint()); 

                    Calendar cal = Calendar.getInstance();

                    Date debut = ((Reservation)list.getModel().getElementAt(index)).getSeance().getDebut();
                    cal.setTime(debut);
                    cal.add(Calendar.HOUR, -24);
                    Date today = new Date();
                    if(today.getTime() < cal.getTimeInMillis()){
                        list.setCursor(new Cursor(Cursor.HAND_CURSOR));
                        JPopupMenu menu = new JPopupMenu();
                        JMenuItem modif = new JMenuItem("Modifier");
                        JMenuItem annul = new JMenuItem("Annuler");
                        modif.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                Reservation resa = (Reservation)list.getModel().getElementAt(index);
                                deleteResa(resa);
                                double avoir = ((Reservation)list.getModel().getElementAt(index)).getPrix();
                                frame.getUser().setAvoir(avoir);
                                userView.getLblAvoir().setText(avoir + "€");
                                Film film = ((Reservation)list.getModel().getElementAt(index)).getSeance().getFilm();
                                frame.setFilm(film);
                                ReservationView movieView = new ReservationView();
                                ReservationController controller = new ReservationController(frame, movieView, UserViewController.this.model);
                                movieView.setController(controller);
                                frame.setContentPane(movieView);
                                frame.pack();
                                frame.setLocationRelativeTo(null);     
                                dialog.dispose();
                            }
                        });
                        annul.addActionListener(new ActionListener() {    
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                Reservation resa = (Reservation)list.getModel().getElementAt(index);
                                double avoir = resa.getPrix();
                                frame.getUser().setAvoir(avoir);
                                UserViewController.this.model.updateEntity(frame.getUser());
                                userView.getLblAvoir().setText(avoir + "€");
                                deleteResa(resa);
                                ((DefaultListModel)list.getModel()).remove(index);    
                                JOptionPane.showMessageDialog(userView, "La séance a bien été annulée et votre compte crédité d'un avoir "
                                        + "d'un montant de " + avoir + "€", "Annulation", JOptionPane.INFORMATION_MESSAGE);
                            }
                        });
                        menu.add(modif);
                        menu.add(annul);
                        menu.show(list, event.getX(), event.getY());

                    }  
                }
                @Override
                public void mouseClicked(MouseEvent arg0) {}
                @Override
                public void mouseEntered(MouseEvent arg0) {}
                @Override
                public void mouseExited(MouseEvent arg0) {}
            });
        }else{
            dialog.dispose();
        }  
    }

    /**
     * Méthode de suppression d'une réservation
     * @param resa
     */
    private void deleteResa(Reservation resa) {
        Thread t = new Thread(){
            public void run(){
                Seance seance = resa.getSeance();
                int nbPlace = seance.getNbPlacesLibres() + resa.getNbPlace();
                seance.setNbPlacesLibres(nbPlace);
                String listeSiegeResa = resa.getListeSiege();
                String placeSeance = getPlaceFromString(listeSiegeResa);
                seance.setListeSiege(seance.getListeSiege().replace(placeSeance, ""));
                model.updateEntity(seance);
                model.deleteEntity(resa);
            }
        };t.start();
    }

    /**
     * Méthode de récupération des places réservé par l'utilisateur
     * @param listeSiegeResa
     * @return
     */
    private static String getPlaceFromString(String listeSiegeResa) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < listeSiegeResa.length(); i++) {
            char c = listeSiegeResa.charAt(i);
            if(c == ','){
                builder.append(",");
            }
            if(c == '.'){
                builder.append(":");
            }
            if (Character.isDigit(c)) {
                builder.append(c);
            }
        }
        return builder.toString();
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        if(event.getSource() == userView.getBtnDconnexion()){
            frame.setUser(null);
            dialog.dispose();
            controller.loadUser(null);
        }else if(event.getSource() == userView.getBtnModifier()){
            modifInfos();    
        }
    }

    /**
     * Méthode de création d'un panel de modification des infos de l'utilisateur
     */
    private void modifInfos() {
        JPanel modif = new JPanel(new BorderLayout(5, 5));
        modif.setPreferredSize(new Dimension(220,160));
        JPanel label = new JPanel(new GridLayout(5, 1, 2, 2));
        label.add(new JLabel("Nom", SwingConstants.LEFT));
        label.add(new JLabel("Prenom", SwingConstants.LEFT));
        label.add(new JLabel("Mail", SwingConstants.LEFT));
        label.add(new JLabel("Mot de passe", SwingConstants.LEFT));
        label.add(new JLabel("Confirmation", SwingConstants.LEFT));
        modif.add(label, BorderLayout.WEST);
        JPanel controls = new JPanel(new GridLayout(5, 1, 2, 2));
        JTextField nom  = new JTextField(frame.getUser().getNom()); 
        JTextField prenom  = new JTextField(frame.getUser().getPrenom()); 
        JTextField mail  = new JTextField(frame.getUser().getMail()); 
        JPasswordField mdp = new JPasswordField();
        JPasswordField mdpConfirm = new JPasswordField();
        controls.add(nom);
        controls.add(prenom);
        controls.add(mail);
        controls.add(mdp);
        controls.add(mdpConfirm);
        modif.add(controls, BorderLayout.CENTER);
        int rep = JOptionPane.showConfirmDialog(userView, modif, "Saisissez vos informations", JOptionPane.OK_CANCEL_OPTION);
        String passwd = new String(mdp.getPassword());
        String passwd2 = new String(mdpConfirm.getPassword());
        if(rep == JOptionPane.CANCEL_OPTION || rep == JOptionPane.CLOSED_OPTION){

        }else{
            if(!passwd.equals(passwd2)){
                JOptionPane.showMessageDialog(userView, MSG_WRONG_PASSWD);
                modifInfos();
            }else if(passwd.equals(passwd2)) { 
                if(!passwd.equals("") && !passwd2.equals("")){
                    String hash =  PasswordUtil.hashpw(passwd, PasswordUtil.gensalt(12));
                    frame.getUser().setMotDePasse(hash);
                }
                frame.getUser().setNom(nom.getText());
                frame.getUser().setPrenom(prenom.getText());
                frame.getUser().setMail(mail.getText());
                model.updateEntity(frame.getUser());
                JOptionPane.showMessageDialog(userView, MSG_MODIF_OK);
                loadInfos();
                controller.loadUser(frame.getUser());   
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent arg0) {}
    @Override
    public void mouseEntered(MouseEvent arg0) {}
    @Override
    public void mouseExited(MouseEvent arg0) {}
    @Override
    public void mousePressed(MouseEvent arg0) {}
    @Override
    public void mouseReleased(MouseEvent arg0) {}
}
