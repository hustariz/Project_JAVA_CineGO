/*
 * Admin.java                                21 janv. 2016
 * CESI RILA 2015/2017
 */
package cineGOv02.admin;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager.LookAndFeelInfo;
import cineGOv02.admin.controller.LauncherAdminController;
import cineGOv02.admin.model.AdminModel;
import cineGOv02.admin.view.LauncherAdminView;
import cineGOv02.common.graphics.MainApp;

/**
 * Classe e lancement de l'application Admin
 * @author Hugo
 *
 */
public class Admin {

    /** Object bloquant l'execution d'instruction avant la fin d'un thread */
    private static Object blocker = new Object();
    
    /** Modèle de données*/
    private static AdminModel model;
    
    /**
     * Lancement de l'application admin
     * @param args
     */
    public static void main(String[] args) {
        JPanel load = new JPanel();
        JLabel circle = new JLabel(new ImageIcon("images/ajax-loader-admin.gif"));
        load.setLayout(new BorderLayout());
        load.add(circle,BorderLayout.CENTER);
        MainApp frame = new MainApp();
        frame.setMinimumSize(new Dimension(265,250));
        frame.setContentPane(load);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setTitle("Admin CineGO");

        Thread t = new Thread() {
            public void run() { 
                synchronized (blocker) {
                   model = new AdminModel();
                }    
            }
        };
        t.start();  

        for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                try {
                    UIManager.setLookAndFeel(info.getClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                        | UnsupportedLookAndFeelException e) {
                    e.printStackTrace();
                }
                break;
            }
        }   
        
        frame.setTitle("CinAdmin");
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        LauncherAdminView launcherView = new LauncherAdminView();
        synchronized (blocker) {
            LauncherAdminController controller = new LauncherAdminController(frame,launcherView, model);
            frame.setContentPane(launcherView);
            frame.pack();
            frame.setLocationRelativeTo(null);
        }
    }
}