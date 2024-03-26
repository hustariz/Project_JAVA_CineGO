/*
 * LauncherView.java                                19 janv. 2016
 * CESI RILA 2015/2017
 */
package cineGOv02.client.view;

import javax.swing.JPanel;
import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import java.awt.SystemColor;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.SwingConstants;
import javax.swing.LayoutStyle.ComponentPlacement;

import cineGOv02.client.controller.LauncherController;
import cineGOv02.common.graphics.Affiche;

import javax.swing.JComboBox;
import javax.swing.JButton;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.border.BevelBorder;

/**
 * Vue de lancement de l'application client
 * @author Remi
 */
public class LauncherView extends JPanel {
    private JComboBox<String> cmbCinema;
    private JButton btnValider;
    private JLabel lblLogoCesi;
    
    /**
     * Vues de lancement de l'appli
     */
    public LauncherView() {
        setPreferredSize(new Dimension(265, 300));
        setSize(new Dimension(265, 300));
        setMinimumSize(new Dimension(265, 300));
        
        JLabel lblCinego = new JLabel("Cinego");
        lblCinego.setHorizontalTextPosition(SwingConstants.CENTER);
        lblCinego.setHorizontalAlignment(SwingConstants.CENTER);
        lblCinego.setFont(new Font("SansSerif", Font.BOLD, 30));
        lblCinego.setForeground(SystemColor.textHighlight);
        
        JLabel lblSelectionnezLeCinma = new JLabel("Selectionnez le cinéma à afficher");
        lblSelectionnezLeCinma.setForeground(Color.WHITE);
        lblSelectionnezLeCinma.setHorizontalTextPosition(SwingConstants.CENTER);
        lblSelectionnezLeCinma.setHorizontalAlignment(SwingConstants.CENTER);
        
        cmbCinema = new JComboBox<String>();
        
        btnValider = new JButton("Valider");
        
        lblLogoCesi = new JLabel("");
        lblLogoCesi.setBorder(new BevelBorder(BevelBorder.LOWERED, SystemColor.desktop, SystemColor.desktop, SystemColor.desktop, SystemColor.textHighlight));
        lblLogoCesi.setSize(180, 100);
        GroupLayout groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(
        	groupLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(groupLayout.createSequentialGroup()
        			.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
        				.addGroup(groupLayout.createSequentialGroup()
        					.addGap(82)
        					.addComponent(lblCinego))
        				.addGroup(groupLayout.createSequentialGroup()
        					.addGap(99)
        					.addComponent(btnValider))
        				.addGroup(groupLayout.createSequentialGroup()
        					.addGap(60)
        					.addComponent(cmbCinema, GroupLayout.PREFERRED_SIZE, 145, GroupLayout.PREFERRED_SIZE))
        				.addGroup(groupLayout.createSequentialGroup()
        					.addGap(42)
        					.addComponent(lblSelectionnezLeCinma))
        				.addGroup(groupLayout.createSequentialGroup()
        					.addGap(40)
        					.addComponent(lblLogoCesi, GroupLayout.PREFERRED_SIZE, 184, GroupLayout.PREFERRED_SIZE)))
        			.addContainerGap(41, Short.MAX_VALUE))
        );
        groupLayout.setVerticalGroup(
        	groupLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(groupLayout.createSequentialGroup()
        			.addGap(19)
        			.addComponent(lblCinego)
        			.addGap(12)
        			.addComponent(lblSelectionnezLeCinma)
        			.addGap(6)
        			.addComponent(cmbCinema, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        			.addGap(18)
        			.addComponent(btnValider)
        			.addGap(18)
        			.addComponent(lblLogoCesi, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
        			.addContainerGap(51, Short.MAX_VALUE))
        );
        setLayout(groupLayout);
    }

    
    /**
	 * @return le lblLogoCesi
	 */
	public JLabel getLblLogoCesi() {
		return lblLogoCesi;
	}


	/**
	 * @param lblLogoCesi le lblLogoCesi to set
	 */
	public void setLblLogoCesi(JLabel lblLogoCesi) {
		this.lblLogoCesi = lblLogoCesi;
	}


	/**
     * @return le cmbCinema
     */
    public JComboBox<String> getCmbCinema() {
        return cmbCinema;
    }
    /**
     * @param cmbCinema le cmbCinema to set
     */
    public void setCmbCinema(JComboBox<String> cmbCinema) {
        this.cmbCinema = cmbCinema;
    }
    /**
     * @return le btnValider
     */
    public JButton getBtnValider() {
        return btnValider;
    }
    /**
     * @param btnValider le btnValider to set
     */
    public void setBtnValider(JButton btnValider) {
        this.btnValider = btnValider;
    }
    /**
     * Lie le controlleur aux widgets 
     * @param controller
     */
    public void setController(LauncherController controller){
        cmbCinema.addActionListener(controller);
        btnValider.addActionListener(controller);
        
        
    }
    @Override
    public void paintComponent(Graphics g){
        Graphics2D g2d = (Graphics2D)g;         
        GradientPaint gp = new GradientPaint(0, 0, Color.black, 0, this.getHeight(), Color.decode("#3b3b3b"), true);                
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, this.getWidth(), this.getHeight());                
    }   
}
