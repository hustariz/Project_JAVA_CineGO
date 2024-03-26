/*
 * LoginController.java                                10 déc. 2015
 * CESI RILA 2015/2017
 */
package cineGOv02.client.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import cineGOv02.client.model.ClientModel;
import cineGOv02.client.view.CreateUserView;
import cineGOv02.client.view.LoginUserView;
import cineGOv02.client.view.RecoverPasswdView;
import cineGOv02.common.entity.User;
import cineGOv02.common.graphics.Controller;
import cineGOv02.common.graphics.MainApp;
import cineGOv02.common.util.PasswordUtil;

/**
 * Controleur de la vue LoginView
 * @author Remi
 */
public class LoginController implements MouseListener, ActionListener{
    /** Fenêtre générale */
    private MainApp frame;
    /** Fenêtre de connexion */
    private JDialog dialog;
    /** Vue dont cette classe est le controller */
    private LoginUserView loginView;
    /** Modele dont cette classe est le contrôleur */
    private ClientModel model;
    /** Controller de la vue précédente */
    private Controller controller;
    /** Message affiché quand les infos de login sont incorrectes */
    public final String MSG_LOGIN_EMPTY = "Veuillez saisir votre login et mot de passe.";


    /**
     * Contructeur du controlleur de la vue de connexion utilisateur
     * @param frame 
     * @param dialog
     * @param loginView 
     * @param factory 
     * @param parent 
     */
    public LoginController(MainApp frame, JDialog dialog, LoginUserView loginView, ClientModel model, 
            Controller controller) {
        this.dialog = dialog;
        this.model = model;
        this.loginView = loginView;
        this.controller = controller;
        this.frame = frame;
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseClicked(MouseEvent event) {
        if(event.getSource() == loginView.getLblMdp()){
            RecoverPasswdView passwdView = new RecoverPasswdView();
            RecoverPasswdController controller = new RecoverPasswdController(dialog,loginView,passwdView, model);
            passwdView.setController(controller);
            dialog.setContentPane(passwdView);
            dialog.pack();
            dialog.setLocationRelativeTo(null);
        }else if(event.getSource() == loginView.getLblCreate()){
            CreateUserView createView = new CreateUserView();
            CreateUserController createController = new CreateUserController(frame,dialog, createView, model);
            createView.setController(createController);
            dialog.setContentPane(createView);
            dialog.pack();
            dialog.setLocationRelativeTo(null);
        }
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        if(event.getSource() == loginView.getBtnEnvoyer()){
            if(!loginView.getMail().getText().equals("") && !loginView.getMdp().getText().equals("")){
                String mail = loginView.getMail().getText();
                String mdp = loginView.getMdp().getText();
                boolean userExist = model.userExist(mail);
                if(userExist){
                    String hash = model.getHash(mail);
                    if(PasswordUtil.checkpw(mdp, hash)){
                        User user = model.getUser(mail,hash);
                        frame.setUser(user);
                        controller.loadUser(user);
                        dialog.dispose();
                    }else{
                        JOptionPane.showMessageDialog(loginView, "Combinaison identifiant / mot de passe incorecte.");
                    }
                }else{
                    JOptionPane.showMessageDialog(dialog, "Aucun compte n'est enregistré avec cette Mail/Identifiant");
                }
            }else{
                JOptionPane.showMessageDialog(loginView, "Veuillez renseigner votre login / mot de passe...");
            }
        }else if(event.getSource() == loginView.getBtnRetour()){
            dialog.dispose();
        }
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {}
    @Override
    public void mouseExited(MouseEvent arg0) {}
    @Override
    public void mousePressed(MouseEvent arg0) {}
    @Override
    public void mouseReleased(MouseEvent arg0) {}
}