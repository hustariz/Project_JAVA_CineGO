/*
 * RecoverPasswordController.java                                13 déc. 2015
 * CESI RILA 2015/2017
 */
package cineGOv02.client.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.mail.MessagingException;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import cineGOv02.client.model.ClientModel;
import cineGOv02.client.view.LoginUserView;
import cineGOv02.client.view.RecoverPasswdView;
import cineGOv02.common.entity.User;
import cineGOv02.common.util.EmailVerifier;
import cineGOv02.common.util.PasswordUtil;
import cineGOv02.common.util.SendAMail;

/**
 * Controlleur associé à la vue RecoverPasswordView.
 * Permet d'agir en fonction des évènements déclenchés par la vue et d'apeller 
 * les fonctions du modèle correspondantes.
 * @author Remi
 *
 */
public class RecoverPasswdController implements ActionListener{
    /** Vue dont cette classe est le controller */
    private RecoverPasswdView passwdView;
    /** Fenêtre de récupération du mot de passe */
    private JDialog dialog;
    /** Modèle de données */
    private ClientModel model;
    /** Vue associée à ce controlleur */
    private LoginUserView loginView;

    /**
     * Constructeur du controller permettant de définir la vue et le modèle associé.
     * @param dialog 
     * @param loginView 
     * @param passwdView 
     * @param factory 
     */
    public RecoverPasswdController(JDialog dialog, LoginUserView loginView, RecoverPasswdView passwdView, ClientModel model) {
        this.dialog = dialog;
        this.model = model;
        this.passwdView = passwdView;
        this.loginView = loginView;
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        // Si l'utilisateur clique sur le bouton "Envoyer"
        if(event.getSource() == passwdView.getBtnEnvoyer()){
            JFormattedTextField mailField = (JFormattedTextField) passwdView.getTxtMail();
            EmailVerifier verifier = new EmailVerifier();
            String mail = mailField.getText();
            // On désactive les widget de saisie le temps de la vérification
            passwdView.getBtnEnvoyer().setEnabled(false);
            passwdView.getTxtMail().setEditable(false);
          
            // On lance un nouveau thread pour éviter de freezer l'interface
            Thread t = new Thread() {
                public void run() {
                    User user = model.getUser(mail);
                    if(verifier.verify(mailField)){
                        if(user != null){
                            // Si le mail est valide syntaxiquement
                            // On génrère un nouveau mot de passe
                            String password = PasswordUtil.genPassword();
                            // On génère le hash correspondant
                            String hash = PasswordUtil.hashpw(password, PasswordUtil.gensalt(12));
                            // On met à jour le mot de passe en BD en y stockant le hash
                            model.executeUpdate(hash,mail);
                            // On cré le texte pour le corp du mail
                            String body = "Bonjour " + user.getPrenom() + "<br /><br />Vous recevez ce message car vous avez demandé la réinitialisation"
                                    + " de votre mot de passe.<br /><br />Le voici : <strong>" + password + "</strong><br /><br />"
                                    + "Amicalement, l'équipe de CineGo.";
                            // On envoit le mail
                            Thread sendMail = new Thread(){
                                public void run(){
                                    try {
                                        SendAMail.send("remi.plantade.pro@gmail.com", mail, body,"Réinitialisation de votre mot de passe CineGO");
                                    } catch (MessagingException e) {
                                        JOptionPane.showMessageDialog(dialog, "Erreur lors de l'envoie du mail, vérifiez vos paramètre de connexion", "Echec", JOptionPane.ERROR_MESSAGE);
                                    }
                                }
                            };sendMail.start();
                            
                            // On réouvre la fenètre de connexion
                            dialog.setContentPane(loginView);
                            dialog.pack();
                            dialog.setLocationRelativeTo(null);
                            JOptionPane.showMessageDialog(dialog,
                                    "Votre mot de passe a bien été réinitialisé \net envoyé sur votre adresse mail",
                                    "Réinitialisation",
                                    JOptionPane.INFORMATION_MESSAGE);
                            passwdView.getBtnEnvoyer().setEnabled(true);
                            passwdView.getTxtMail().setEditable(true);
                        }else{
                            // Si le mail ne correspond à aucun compte connu
                            JOptionPane.showMessageDialog(dialog,
                                    "Aucun compte enregistré avec cette adresse email.",
                                    "Erreur", 
                                    JOptionPane.ERROR_MESSAGE);
                            passwdView.getBtnEnvoyer().setEnabled(true);
                            passwdView.getTxtMail().setEditable(true);
                        }
                    }
                }
            }; t.start();
        }else if(event.getSource() == passwdView.getBtnRetour()){
            // Si l'utilisateur clique sur le bouton "Retour"
            dialog.setContentPane(loginView);
            dialog.pack();
            dialog.setLocationRelativeTo(null);
        }  
    }
}
