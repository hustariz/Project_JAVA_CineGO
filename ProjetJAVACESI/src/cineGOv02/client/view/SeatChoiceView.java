/*
 * RoomCreatorView.java                                10 févr. 2016
 * CESI RILA 2015/2017
 */
package cineGOv02.client.view;

import javax.swing.JPanel;
import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.GroupLayout.Alignment;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.LayoutStyle.ComponentPlacement;

import cineGOv02.client.controller.SeatChoiceController;

import javax.swing.JScrollPane;
import java.awt.Dimension;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.Color;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;

/**
 * Vue de choix de place sur un plan de salle
 * @author Remi
 *
 */
public class SeatChoiceView extends JPanel implements ActionListener {
    private JPanel plan;
    private JLabel lblSiegeNormal;
    private BufferedImage bufferImage;
    private ImageIcon iconeSiege;
    private ImageIcon iconeSiegeHand;
    private ImageIcon iconeSiegeOccupe;
    private JLabel lblSiegeHand;
    private JLabel lblInfos;
    private JScrollPane scrollPane;
    private JButton btnPlacer;
    private JLabel lblSiegeOccupe;
    private JTextArea txtrSelectionnezSurLe;
    private JLabel lblNewLabel;
    private JTextField txtNbPlaceHand;
    private JLabel lblPlacementAutomatique;
    private JLabel lblLgende;
    private JLabel lblNewLabel_1;
    private JLabel lblSigeHandicap;
    private JLabel lblSigeOccup;
    private JCheckBox chckbxPlcmtAuto;
    private JLabel lblNbPlace;
    private JLabel lblNewLabel_2;
    private JButton btnAnnuler;
    private JButton btnConfirmer;
    private JPanel panel_1;
    
    
    public SeatChoiceView() {
        setBackground(Color.BLACK);
        try {
            bufferImage = ImageIO.read(new File("images/siege.png"));
            iconeSiege = new ImageIcon(bufferImage);
            bufferImage = ImageIO.read(new File("images/siegeHand.png"));
            iconeSiegeHand = new ImageIcon(bufferImage);
            bufferImage = ImageIO.read(new File("images/siegeOccupe.png"));
            iconeSiegeOccupe = new ImageIcon(bufferImage);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JPanel panel = new JPanel();
        panel.setBackground(Color.BLACK);
        panel_1 = new JPanel();
        panel_1.setBackground(SystemColor.controlShadow);
        GroupLayout groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(groupLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(panel, GroupLayout.PREFERRED_SIZE, 256, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 750, Short.MAX_VALUE)
                        .addContainerGap())
                );
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
                                .addComponent(panel_1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 708, Short.MAX_VALUE)
                                .addComponent(panel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 708, Short.MAX_VALUE))
                        .addContainerGap())
                );
        
        JLabel lblEditeurDeSalle = new JLabel("CineGO");
        lblEditeurDeSalle.setFont(new Font("Tahoma", Font.BOLD, 30));
        lblEditeurDeSalle.setForeground(SystemColor.textHighlight);
        
        lblSiegeNormal = new JLabel("");
        lblSiegeNormal.setSize(new Dimension(44, 44));
        lblSiegeNormal.setPreferredSize(new Dimension(44, 44));
        lblSiegeNormal.setMinimumSize(new Dimension(44, 44));
        lblSiegeNormal.setMaximumSize(new Dimension(44, 44));
        lblSiegeNormal.setIcon(iconeSiege);
        
        lblSiegeHand = new JLabel("");
        lblSiegeHand.setBackground(SystemColor.windowBorder);
        lblSiegeHand.setSize(new Dimension(44, 44));
        lblSiegeHand.setPreferredSize(new Dimension(44, 44));
        lblSiegeHand.setMinimumSize(new Dimension(44, 44));
        lblSiegeHand.setMaximumSize(new Dimension(44, 44));
        lblSiegeHand.setIcon(iconeSiegeHand);
        
        lblInfos = new JLabel("Informations");
        lblInfos.setForeground(SystemColor.textHighlight);
        lblInfos.setFont(new Font("Tahoma", Font.BOLD, 13));
        
        btnPlacer = new JButton("Placement");
        btnPlacer.setEnabled(false);
        btnPlacer.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnPlacer.setForeground(Color.GRAY);
        
        lblSiegeOccupe = new JLabel("");
        lblSiegeOccupe.setSize(new Dimension(44, 44));
        lblSiegeOccupe.setPreferredSize(new Dimension(44, 44));
        lblSiegeOccupe.setMinimumSize(new Dimension(44, 44));
        lblSiegeOccupe.setMaximumSize(new Dimension(44, 44));
        lblSiegeOccupe.setBackground(SystemColor.windowBorder);
        lblSiegeOccupe.setIcon(iconeSiegeOccupe);
        ButtonGroup btnGroup = new ButtonGroup();
        
        txtrSelectionnezSurLe = new JTextArea();
        txtrSelectionnezSurLe.setBorder(null);
        txtrSelectionnezSurLe.setForeground(Color.WHITE);
        txtrSelectionnezSurLe.setBackground(Color.BLACK);
        txtrSelectionnezSurLe.setText("Selectionnez sur le plan l'emplacement\r\n des places que vous avez réservé. \r\nVous pouvez également opter \r\npour un placement automatique.");
        
        JLabel lblPlacement = new JLabel("Placement manuel");
        lblPlacement.setHorizontalAlignment(SwingConstants.CENTER);
        lblPlacement.setForeground(SystemColor.textHighlight);
        lblPlacement.setFont(new Font("Tahoma", Font.BOLD, 13));
        
        JLabel lblPlacesRestantes = new JLabel("Places restantes :");
        lblPlacesRestantes.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblPlacesRestantes.setForeground(Color.WHITE);
        
        lblNbPlace = new JLabel("0");
        lblNbPlace.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblNbPlace.setForeground(Color.WHITE);
        
        chckbxPlcmtAuto = new JCheckBox("Activer le placement automatique");
        chckbxPlcmtAuto.setForeground(Color.WHITE);
        chckbxPlcmtAuto.addActionListener(this);
        
        lblNewLabel = new JLabel("Places handicapées :");
        lblNewLabel.setForeground(Color.WHITE);
        
        txtNbPlaceHand = new JTextField();
        txtNbPlaceHand.setEnabled(false);
        txtNbPlaceHand.setColumns(10);
        
        lblPlacementAutomatique = new JLabel("Placement automatique");
        lblPlacementAutomatique.setHorizontalAlignment(SwingConstants.CENTER);
        lblPlacementAutomatique.setForeground(SystemColor.textHighlight);
        lblPlacementAutomatique.setFont(new Font("Tahoma", Font.BOLD, 13));
        
        lblLgende = new JLabel("Légende");
        lblLgende.setHorizontalAlignment(SwingConstants.CENTER);
        lblLgende.setForeground(SystemColor.textHighlight);
        lblLgende.setFont(new Font("Tahoma", Font.BOLD, 13));
        
        lblNewLabel_1 = new JLabel("Siège normal");
        lblNewLabel_1.setForeground(Color.WHITE);
        
        lblSigeHandicap = new JLabel("Siège handicapé");
        lblSigeHandicap.setForeground(Color.WHITE);
        
        lblSigeOccup = new JLabel("Siège occupé");
        lblSigeOccup.setForeground(Color.WHITE);
        
        lblNewLabel_2 = new JLabel("_________________________________");
        lblNewLabel_2.setForeground(SystemColor.textHighlight);
        
        btnAnnuler = new JButton("Annuler");
        btnAnnuler.setForeground(SystemColor.textHighlight);
        btnAnnuler.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnAnnuler.setEnabled(true);
        
        btnConfirmer = new JButton("Confirmer");
        btnConfirmer.setForeground(SystemColor.GRAY);
        btnConfirmer.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnConfirmer.setEnabled(false);
        GroupLayout gl_panel = new GroupLayout(panel);
        gl_panel.setHorizontalGroup(
            gl_panel.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_panel.createSequentialGroup()
                    .addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_panel.createSequentialGroup()
                            .addGap(70)
                            .addComponent(lblEditeurDeSalle))
                        .addGroup(gl_panel.createSequentialGroup()
                            .addGap(85)
                            .addComponent(lblInfos))
                        .addGroup(gl_panel.createSequentialGroup()
                            .addGap(21)
                            .addComponent(txtrSelectionnezSurLe, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addGroup(gl_panel.createSequentialGroup()
                            .addGap(64)
                            .addComponent(lblPlacement, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE))
                        .addGroup(gl_panel.createSequentialGroup()
                            .addGap(6)
                            .addComponent(lblPlacesRestantes)
                            .addGap(12)
                            .addComponent(lblNbPlace, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE))
                        .addGroup(gl_panel.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
                                .addGroup(gl_panel.createSequentialGroup()
                                    .addComponent(lblNewLabel)
                                    .addGap(12)
                                    .addComponent(txtNbPlaceHand, GroupLayout.PREFERRED_SIZE, 49, GroupLayout.PREFERRED_SIZE))
                                .addGroup(gl_panel.createSequentialGroup()
                                    .addGap(32)
                                    .addComponent(lblPlacementAutomatique, GroupLayout.PREFERRED_SIZE, 165, GroupLayout.PREFERRED_SIZE))
                                .addComponent(chckbxPlcmtAuto)))
                        .addGroup(gl_panel.createSequentialGroup()
                            .addGap(153)
                            .addComponent(btnPlacer)))
                    .addContainerGap(12, Short.MAX_VALUE))
                .addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
                    .addContainerGap(12, Short.MAX_VALUE)
                    .addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_panel.createSequentialGroup()
                            .addGap(7)
                            .addComponent(btnAnnuler, GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE)
                            .addGap(49)
                            .addComponent(btnConfirmer, GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE))
                        .addGroup(gl_panel.createSequentialGroup()
                            .addGap(55)
                            .addComponent(lblLgende, GroupLayout.PREFERRED_SIZE, 125, GroupLayout.PREFERRED_SIZE))
                        .addGroup(gl_panel.createSequentialGroup()
                            .addComponent(lblSiegeNormal, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addGap(18)
                            .addComponent(lblNewLabel_1))
                        .addGroup(gl_panel.createSequentialGroup()
                            .addComponent(lblSiegeHand, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addGap(18)
                            .addComponent(lblSigeHandicap, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE))
                        .addGroup(gl_panel.createSequentialGroup()
                            .addComponent(lblSiegeOccupe, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addGap(18)
                            .addComponent(lblSigeOccup, GroupLayout.PREFERRED_SIZE, 94, GroupLayout.PREFERRED_SIZE))
                        .addGroup(gl_panel.createSequentialGroup()
                            .addGap(7)
                            .addComponent(lblNewLabel_2)))
                    .addContainerGap())
        );
        gl_panel.setVerticalGroup(
            gl_panel.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_panel.createSequentialGroup()
                    .addComponent(lblEditeurDeSalle)
                    .addGap(6)
                    .addComponent(lblInfos)
                    .addGap(6)
                    .addComponent(txtrSelectionnezSurLe, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addGap(12)
                    .addComponent(lblPlacement)
                    .addGap(6)
                    .addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
                        .addComponent(lblPlacesRestantes)
                        .addComponent(lblNbPlace))
                    .addGap(18)
                    .addComponent(lblPlacementAutomatique)
                    .addGap(18)
                    .addComponent(chckbxPlcmtAuto)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_panel.createSequentialGroup()
                            .addGap(6)
                            .addComponent(lblNewLabel))
                        .addComponent(txtNbPlaceHand, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGap(35)
                    .addComponent(btnPlacer)
                    .addPreferredGap(ComponentPlacement.UNRELATED)
                    .addComponent(lblNewLabel_2)
                    .addGap(12)
                    .addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
                        .addComponent(btnAnnuler)
                        .addComponent(btnConfirmer))
                    .addGap(12)
                    .addComponent(lblLgende)
                    .addGap(12)
                    .addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
                        .addComponent(lblSiegeNormal, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addGroup(gl_panel.createSequentialGroup()
                            .addGap(15)
                            .addComponent(lblNewLabel_1)))
                    .addGap(12)
                    .addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
                        .addComponent(lblSiegeHand, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addGroup(gl_panel.createSequentialGroup()
                            .addGap(15)
                            .addComponent(lblSigeHandicap)))
                    .addGap(12)
                    .addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
                        .addComponent(lblSiegeOccupe, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addGroup(gl_panel.createSequentialGroup()
                            .addGap(15)
                            .addComponent(lblSigeOccup)))
                    .addGap(100))
        );
        panel.setLayout(gl_panel);

        plan = new JPanel();
        plan.setOpaque(false);
        scrollPane = new JScrollPane(plan);
        scrollPane.setAlignmentY(0.0f);
        scrollPane.setAlignmentX(0.0f);
        scrollPane.setWheelScrollingEnabled(false);
        GroupLayout gl_panel_1 = new GroupLayout(panel_1);
        gl_panel_1.setHorizontalGroup(
            gl_panel_1.createParallelGroup(Alignment.LEADING)
                .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 750, Short.MAX_VALUE)
        );
        gl_panel_1.setVerticalGroup(
            gl_panel_1.createParallelGroup(Alignment.LEADING)
                .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 708, Short.MAX_VALUE)
        );
        panel_1.setLayout(gl_panel_1);
        setLayout(groupLayout);
    }
    /**
     * @return le panel_1
     */
    public JPanel getPanel_1() {
        return panel_1;
    }
    /**
     * @return le scrollPane
     */
    public JScrollPane getScrollPane() {
        return scrollPane;
    }
    /**
     * @param scrollPane le scrollPane to set
     */
    public void setScrollPane(JScrollPane scrollPane) {
        this.scrollPane = scrollPane;
    }
    
    /**
     * @return le plan
     */
    public JPanel getPlan() {
        return plan;
    }
    /**
     * @param plan le plan to set
     */
    public void setPlan(JPanel plan) {
        this.plan = plan;
    }
    /**
     * @return le btnEnregistrer
     */
    public JButton getBtnEnregistrer() {
        return btnPlacer;
    }
    /**
     * @param btnEnregistrer le btnEnregistrer to set
     */
    public void setBtnEnregistrer(JButton btnEnregistrer) {
        this.btnPlacer = btnEnregistrer;
    }
   
    /**
     * @return le btnPlacer
     */
    public JButton getBtnPlacer() {
        return btnPlacer;
    }

    /**
     * @return le txtNbPlaceHand
     */
    public JTextField getTxtNbPlaceHand() {
        return txtNbPlaceHand;
    }
    /**
     * @return le lblNbPlace
     */
    public JLabel getLblNbPlace() {
        return lblNbPlace;
    }
    /**
     * @return le btnAnnuler
     */
    public JButton getBtnAnnuler() {
        return btnAnnuler;
    }
    /**
     * @return le btnConfirmer
     */
    public JButton getBtnConfirmer() {
        return btnConfirmer;
    }
    /**
     * Permet de lier les widgets avec le controller
     * @param roomCreatorController
     */
    public void setController(SeatChoiceController seatChoiceController) {
        btnPlacer.addActionListener(seatChoiceController);
        btnAnnuler.addActionListener(seatChoiceController);
        btnConfirmer.addActionListener(seatChoiceController);
    }
    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        if(event.getSource() == chckbxPlcmtAuto){
            if(chckbxPlcmtAuto.isSelected()){
                txtNbPlaceHand.setEnabled(true);
                btnPlacer.setEnabled(true);
                btnPlacer.setForeground(SystemColor.textHighlight);
            }else if(!chckbxPlcmtAuto.isSelected()){
                txtNbPlaceHand.setEnabled(false);
                btnPlacer.setEnabled(false);
                btnPlacer.setForeground(Color.GRAY);
            }
        }
        
    }
}
