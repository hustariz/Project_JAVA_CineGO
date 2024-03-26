/*
 * Run.java                                3 janv. 2016
 * CESI RILA 2015/2017
 */
package cineGOv02.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import cineGOv02.client.controller.LauncherController;
import cineGOv02.client.model.ClientModel;
import cineGOv02.client.view.LauncherView;
import cineGOv02.common.graphics.MainApp;

/**
 * Classe de lancement de l'application client
 * @author Remi
 */
public class Client {
    
    private static Object blocker = new Object();
    
    private static ClientModel model;
    
    /**
     * Méthode de lancement de l'application client
     * @param args
     */
    public static void main(String[] args) {
        Loader load = new Loader();
        JLabel circle = new JLabel(new ImageIcon("images/ajax-loader.gif"));
        load.setLayout(new BorderLayout());
        load.add(circle,BorderLayout.CENTER);
        MainApp frame = new MainApp();
        frame.setMinimumSize(new Dimension(265,250));
        frame.setContentPane(load);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setTitle("Client CineGO");

        Thread t = new Thread() {
            public void run() { 
                synchronized (blocker) {
                    model = new ClientModel();
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
        frame.setTitle("Cinego");
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        LauncherView launcherView = new LauncherView();
        synchronized (blocker) {
            LauncherController controller = new LauncherController(frame,launcherView, model);
            frame.setContentPane(launcherView);
            frame.pack();
            frame.setLocationRelativeTo(null);
        }
    }
    /**
     * Méthode de dessin d'un dégradé de noir
     * @author Remi
     */
    static class Loader extends JPanel{
        @Override
        public void paintComponent(Graphics g){
            Graphics2D g2d = (Graphics2D)g;         
            GradientPaint gp = new GradientPaint(0, 0, Color.black, 0, this.getHeight(), Color.decode("#3b3b3b"), true);                
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, this.getWidth(), this.getHeight());                
        }   
    } 
}