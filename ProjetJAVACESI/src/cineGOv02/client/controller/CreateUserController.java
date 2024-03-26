/*
 * CreateUserController.java                                30 janv. 2016
 * CESI RILA 2015/2017
 */
package cineGOv02.client.controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.mail.MessagingException;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import cineGOv02.client.model.ClientModel;
import cineGOv02.client.view.CreateUserView;
import cineGOv02.common.entity.User;
import cineGOv02.common.util.PasswordUtil;
import cineGOv02.common.util.SendAMail;

/**
 * Controlleur de la vue de création de compte utilisateur
 * @author Remi
 *
 */
public class CreateUserController implements ActionListener {
    /** Fenetre d'affichage des info utilisateurs */
    private JDialog dialog;
    /** Vue de création d'un compte client */
    private CreateUserView createView;
    /** Modèle de données */
    private ClientModel model;
    /** Fenêtre principale */
    private JFrame frame;
    /** 
     * Message dinformation concernant la sasies incomplète ou incorrecte d'infos
     * lors de la création d'un nouvel utilisateur
     */
    /** Message d'info concernant la non concordance des mots de passes */
    public static final String MSG_WRONG_PASSWD = "Les mots de passe ne corresponde pas.";
    /** Message concernant la non-saisie du mot de passe */
    public static final String MSG_PASSWD_EMPTY = "Veuillez saisir votre mot de passe et le confimer.";
    /** Message de confifrmation de création du compte */
    public static final String MSG_CREATE_OK = "Compte créé avec succès !";
    /** Message de confirmation de remplissage des champs */
    public static final String MSG_FIELD_EMPTY = "Tous les champs n'ont pas été remplis.";
    /** Message d'avertissement d'existance d'un utilisateurs avec cette mail */
    public static final String MSG_USER_EXIST = "Un utilisateur a déjà enregistré cette mail";

    /**
     * Constructeur de la vue de création d'un utilisateur
     * @param frame
     * @param dialog 
     * @param createView
     * @param factory
     */
    public CreateUserController(JFrame frame, JDialog dialog, CreateUserView createView, ClientModel model){
        this.dialog = dialog;
        this.createView = createView;
        this.model = model;
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        if(event.getSource() == createView.getBtnEnvoyer()){
            if(createView.getTxtNom().getText() != null 
                    && createView.getTxtPrenom().getText() != null  
                    && createView.getTxtMail().getText() != null){
                if(!model.userExist(createView.getTxtMail().getText())){
                    // On saisit le password
                    String password = getPassword();
                    if(password != null){
                        String hash =  PasswordUtil.hashpw(password, PasswordUtil.gensalt(12));
                        User user = new User("", createView.getTxtNom().getText(), 
                                createView.getTxtPrenom().getText(), createView.getTxtMail().getText()
                                , false, hash, createView.getChckbxEtudiant().isSelected(),0.0);
                        model.saveEntity(user);
                        String body = "Bonjour " + user.getPrenom() + "<br /><br />Vous recevez ce message car vous vous êtes enregistré"
                                + "\n sur l'application CineGO !<br /><br />"
                                + "Amicalement, l'équipe de CineGo.";
                        // On envoit le mail
                        String subject = "Confirmation inscription";
                        Thread sendMail = new Thread(){
                            public void run(){
                                try {
                                    SendAMail.send("remi.plantade.pro@gmail.com", createView.getTxtMail().getText(), body, subject);
                                } catch (MessagingException e) {
                                    JOptionPane.showMessageDialog(frame, "Erreur lors de l'envoie du mail, vérifiez vos paramètre de connexion", "Echec", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        };sendMail.start();
                        JOptionPane.showMessageDialog(createView, MSG_CREATE_OK);
                        dialog.dispose();
                    }
                }else{
                    JOptionPane.showMessageDialog(createView, MSG_USER_EXIST);
                }
            }else{
                JOptionPane.showMessageDialog(createView, MSG_FIELD_EMPTY);
            }
        }else if(event.getSource() == createView.getBtnRetour()){
            dialog.dispose();
        }
    }

    /**
     * Affiche une boite de dialogue pour la saisie d'un nouveau mot de passe
     * @return le password ou null si la saisie est annulée
     */
    private String getPassword() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        JPanel label = new JPanel(new GridLayout(0, 1, 2, 2));
        label.add(new JLabel("Password", SwingConstants.RIGHT));
        label.add(new JLabel("Re-Password", SwingConstants.RIGHT));
        panel.add(label, BorderLayout.WEST);

        JPanel controls = new JPanel(new GridLayout(0, 1, 2, 2));
        JPasswordField password = new JPasswordField();
        controls.add(password);
        JPasswordField passwordConfirm = new JPasswordField();
        controls.add(passwordConfirm);
        panel.add(controls, BorderLayout.CENTER);

        int rep = JOptionPane.showConfirmDialog(createView, panel, "Tappez votre mot de passe", JOptionPane.OK_CANCEL_OPTION);
        String passwd = new String(password.getPassword());
        String passwd2 = new String(passwordConfirm.getPassword());
        if(rep == JOptionPane.CANCEL_OPTION || rep == JOptionPane.CLOSED_OPTION){
            return null;  
        }else{
            if (passwd.equals("") || passwd2.equals("")){
                JOptionPane.showMessageDialog(createView, MSG_PASSWD_EMPTY);
                return getPassword();
            }else if(!passwd.equals(passwd2)){
                JOptionPane.showMessageDialog(createView, MSG_WRONG_PASSWD);
                return getPassword();
            }else if(passwd.equals(passwd2)) {
                return new String(password.getPassword());  
            }else{
                return null;          
            }
        }
    }
    /**
     * @author Remi
     *
     */
    class BlackPanel extends JPanel{

        /**
         * Créé un panel noir
         * @param layout
         */
        public BlackPanel(LayoutManager layout) {
            super(layout);
        }

        @Override
        public void paintComponent(Graphics g){
            Graphics2D g2d = (Graphics2D)g;         
            GradientPaint gp = new GradientPaint(0, 0, Color.black, 0, this.getHeight(), Color.decode("#3b3b3b"), true);                
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, this.getWidth(), this.getHeight());                
        }  
    }
}
