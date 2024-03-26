/*
 * GestionCinemaView.java                                27 janv. 2016
 * IUT Info1 2013-2014 Groupe 3
 */
package cineGOv02.admin.view;

import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.SystemColor;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.border.BevelBorder;

import cineGOv02.admin.controller.GestionCinemaController;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

/**
 * Vue où l'on va éditer toutes les informations d'un cinéma.
 * Javadoc Done
 * @author hustariz
 *
 */
public class GestionCinemaView extends JPanel {
    /** Label nom du cinéma. */
    JLabel lblCinemaNom;
    /** Label clickable, renvoie au launcher. */
    JLabel lblSlectionnerUnAutre;
    /** Champ tarif du cinéma. */
    private JTextField txtFieldTarifNormal;
    /** Champ tarif étudiant du cinéma. */
    private JTextField txtFieldTarifEtu;
    /** Champ nom du cinéma. */
    private JTextField txtFieldNomCine;
    /** Champ tarif 3D du cinéma. */
    private JTextField txtFieldTarif3D;
    /** Bouton de sauvegarde des changements Nom/Tarifs du cinéma. */
    JButton btnSaveInfo;


    /** TextField numéro de la salle à ajouter. */
    private JTextField txtFieldAddSalle;
    /** TextField nombre siège par rangée */
    private JTextField textFieldNbSiege;
    /** TextField nombre rangée de la salle. */
    private JTextField textFieldNbRangee;
    /** Bouton AjoutSalle (numéro, nbSièges et nbRangées.*/
    JButton btnAddSalle;

    /** Combobox sélection salle à supprimer. */
    JComboBox comboBoxSupprSalle;
    /** Bouton SupprimerSalle (numéro).*/
    JButton btnSupprSalle;


    /** Bouton de téléchargement du plan en XML. */
    JButton btnDownloadPlan;
    /** Bouton d'édition du plan de la salle. */
    JButton btnEditPlan;

    /** Bouton Edition du planning. */
    JButton btnEditPlanning;
    /** Bouton pour ajouter un film manuellement.*/
    JButton btnAjouterFilm;
    private JComboBox cmbPlanSalle;
    private JButton btnImporter;


    /**
     * Create the panel.
     */
    public GestionCinemaView() {

        lblCinemaNom = new JLabel("Gaumont Labège");
        lblCinemaNom.setHorizontalTextPosition(SwingConstants.CENTER);
        lblCinemaNom.setHorizontalAlignment(SwingConstants.CENTER);
        lblCinemaNom.setFont(new Font("Tahoma", Font.BOLD, 26));

        lblSlectionnerUnAutre = new JLabel("Sélectionner un autre cinéma");
        lblSlectionnerUnAutre.setForeground(SystemColor.textHighlight);
        lblSlectionnerUnAutre.setFont(new Font("Tahoma", Font.BOLD, 16));

        JPanel panel = new JPanel();
        panel.setBorder(new BevelBorder(BevelBorder.LOWERED, SystemColor.desktop, new Color(0, 0, 0), new Color(0, 0, 0), SystemColor.textHighlight));

        JPanel panel_2 = new JPanel();
        panel_2.setBorder(new BevelBorder(BevelBorder.LOWERED, SystemColor.desktop, new Color(0, 0, 0), new Color(0, 0, 0), SystemColor.textHighlight));

        JLabel lblAjouterUneSalle = new JLabel("Ajouter une salle");
        lblAjouterUneSalle.setFont(new Font("Tahoma", Font.BOLD, 16));

        btnAddSalle = new JButton("Ajouter");
        btnAddSalle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });

        JLabel lblNumro = new JLabel("Par Numéro");
        lblNumro.setFont(new Font("Tahoma", Font.BOLD, 14));

        txtFieldAddSalle = new JTextField();
        txtFieldAddSalle.setFont(new Font("Tahoma", Font.PLAIN, 14));
        txtFieldAddSalle.setHorizontalAlignment(SwingConstants.RIGHT);
        txtFieldAddSalle.setColumns(10);

        JLabel lblNombreSiges = new JLabel("Nombre sièges");
        lblNombreSiges.setFont(new Font("Tahoma", Font.BOLD, 14));

        JLabel lblNombreRanges = new JLabel("Nombre rangées");
        lblNombreRanges.setFont(new Font("Tahoma", Font.BOLD, 14));

        textFieldNbSiege = new JTextField();
        textFieldNbSiege.setFont(new Font("Tahoma", Font.PLAIN, 14));
        textFieldNbSiege.setHorizontalAlignment(SwingConstants.RIGHT);
        textFieldNbSiege.setColumns(10);

        textFieldNbRangee = new JTextField();
        textFieldNbRangee.setFont(new Font("Tahoma", Font.PLAIN, 14));
        textFieldNbRangee.setHorizontalAlignment(SwingConstants.RIGHT);
        textFieldNbRangee.setColumns(10);
        GroupLayout gl_panel_2 = new GroupLayout(panel_2);
        gl_panel_2.setHorizontalGroup(
                gl_panel_2.createParallelGroup(Alignment.TRAILING)
                .addGroup(gl_panel_2.createSequentialGroup()
                        .addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
                                .addGroup(gl_panel_2.createSequentialGroup()
                                        .addGap(22)
                                        .addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
                                                .addComponent(lblNombreSiges)
                                                .addComponent(lblNumro)
                                                .addComponent(lblNombreRanges))
                                        .addPreferredGap(ComponentPlacement.RELATED, 80, Short.MAX_VALUE)
                                        .addGroup(gl_panel_2.createParallelGroup(Alignment.TRAILING)
                                                .addGroup(gl_panel_2.createParallelGroup(Alignment.TRAILING, false)
                                                        .addComponent(textFieldNbRangee, 0, 0, Short.MAX_VALUE)
                                                        .addComponent(txtFieldAddSalle, 0, 0, Short.MAX_VALUE)
                                                        .addComponent(textFieldNbSiege, GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE))
                                                .addComponent(btnAddSalle, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE)))
                                .addGroup(gl_panel_2.createSequentialGroup()
                                        .addGap(104)
                                        .addComponent(lblAjouterUneSalle)))
                        .addGap(36))
                );
        gl_panel_2.setVerticalGroup(
                gl_panel_2.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_panel_2.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblAjouterUneSalle)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
                                .addGroup(gl_panel_2.createSequentialGroup()
                                        .addComponent(txtFieldAddSalle, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(ComponentPlacement.RELATED)
                                        .addComponent(textFieldNbSiege, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(ComponentPlacement.RELATED)
                                        .addComponent(textFieldNbRangee, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(ComponentPlacement.RELATED)
                                        .addComponent(btnAddSalle, GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE))
                                .addGroup(gl_panel_2.createSequentialGroup()
                                        .addGap(7)
                                        .addComponent(lblNumro)
                                        .addPreferredGap(ComponentPlacement.UNRELATED)
                                        .addComponent(lblNombreSiges, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(ComponentPlacement.UNRELATED)
                                        .addComponent(lblNombreRanges, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
                );
        panel_2.setLayout(gl_panel_2);

        JPanel panel_3 = new JPanel();
        panel_3.setBorder(new BevelBorder(BevelBorder.LOWERED, SystemColor.desktop, new Color(0, 0, 0), new Color(0, 0, 0), SystemColor.textHighlight));

        JLabel lblSupprimerUneSalle = new JLabel("Supprimer une salle");
        lblSupprimerUneSalle.setFont(new Font("Tahoma", Font.BOLD, 16));

        JLabel lblParNumro = new JLabel("Par Numéro");
        lblParNumro.setFont(new Font("Tahoma", Font.BOLD, 14));

        comboBoxSupprSalle = new JComboBox();
        comboBoxSupprSalle.setFont(new Font("Tahoma", Font.PLAIN, 14));

        btnSupprSalle = new JButton("Supprimer");
        GroupLayout gl_panel_3 = new GroupLayout(panel_3);
        gl_panel_3.setHorizontalGroup(
                gl_panel_3.createParallelGroup(Alignment.TRAILING)
                .addGroup(Alignment.LEADING, gl_panel_3.createSequentialGroup()
                        .addGroup(gl_panel_3.createParallelGroup(Alignment.TRAILING)
                                .addGroup(gl_panel_3.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(btnSupprSalle, GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE))
                                .addGroup(Alignment.LEADING, gl_panel_3.createSequentialGroup()
                                        .addGap(90)
                                        .addComponent(lblSupprimerUneSalle))
                                .addGroup(Alignment.LEADING, gl_panel_3.createSequentialGroup()
                                        .addGap(38)
                                        .addComponent(lblParNumro)
                                        .addGap(136)
                                        .addComponent(comboBoxSupprSalle, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(35, Short.MAX_VALUE))
                );
        gl_panel_3.setVerticalGroup(
                gl_panel_3.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_panel_3.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblSupprimerUneSalle)
                        .addGap(26)
                        .addGroup(gl_panel_3.createParallelGroup(Alignment.BASELINE)
                                .addComponent(comboBoxSupprSalle, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblParNumro))
                        .addGap(18)
                        .addComponent(btnSupprSalle, GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
                        .addContainerGap())
                );
        panel_3.setLayout(gl_panel_3);

        JPanel panel_4 = new JPanel();
        panel_4.setBorder(new BevelBorder(BevelBorder.LOWERED, SystemColor.desktop, new Color(0, 0, 0), new Color(0, 0, 0), SystemColor.textHighlight));

        JLabel label = new JLabel("Editer le planning");
        label.setFont(new Font("Tahoma", Font.BOLD, 16));

        JLabel label_1 = new JLabel("Editer le planning");
        label_1.setFont(new Font("Tahoma", Font.BOLD, 14));

        btnEditPlanning = new JButton("Editer");

        JLabel lblAjouterUnFilm = new JLabel("Ajouter un film");
        lblAjouterUnFilm.setFont(new Font("Tahoma", Font.BOLD, 14));

        btnAjouterFilm = new JButton("Ajouter");

        JLabel lblEditerLesInfos = new JLabel("Editer les infos du cinéma");
        lblEditerLesInfos.setFont(new Font("Tahoma", Font.BOLD, 16));

        JLabel lblNom = new JLabel("Nom");
        lblNom.setFont(new Font("Tahoma", Font.BOLD, 14));

        JLabel lblTarif = new JLabel("Tarif normal");
        lblTarif.setFont(new Font("Tahoma", Font.BOLD, 14));

        JLabel lblTariftudiant = new JLabel("Tarif étudiant");
        lblTariftudiant.setFont(new Font("Tahoma", Font.BOLD, 14));

        JLabel lblTarifd = new JLabel("Tarif 3D");
        lblTarifd.setFont(new Font("Tahoma", Font.BOLD, 14));

        txtFieldNomCine = new JTextField();
        txtFieldNomCine.setFont(new Font("Tahoma", Font.PLAIN, 14));
        txtFieldNomCine.setHorizontalAlignment(SwingConstants.RIGHT);
        txtFieldNomCine.setColumns(10);

        txtFieldTarifNormal = new JTextField();
        txtFieldTarifNormal.setFont(new Font("Tahoma", Font.PLAIN, 14));
        txtFieldTarifNormal.setHorizontalAlignment(SwingConstants.RIGHT);
        txtFieldTarifNormal.setColumns(10);

        txtFieldTarifEtu = new JTextField();
        txtFieldTarifEtu.setFont(new Font("Tahoma", Font.PLAIN, 14));
        txtFieldTarifEtu.setHorizontalAlignment(SwingConstants.RIGHT);
        txtFieldTarifEtu.setColumns(10);

        txtFieldTarif3D = new JTextField();
        txtFieldTarif3D.setFont(new Font("Tahoma", Font.PLAIN, 14));
        txtFieldTarif3D.setHorizontalAlignment(SwingConstants.RIGHT);
        txtFieldTarif3D.setColumns(10);

        btnSaveInfo = new JButton("Sauvegarder");

        JPanel panel_5 = new JPanel();
        panel_5.setBorder(new BevelBorder(BevelBorder.LOWERED, SystemColor.desktop, new Color(0, 0, 0), new Color(0, 0, 0), new Color(51, 153, 255)));

        JLabel lblEditerLePlan = new JLabel("Editer le plan");
        lblEditerLePlan.setFont(new Font("Tahoma", Font.BOLD, 16));

        JLabel lblTelechargerLePlan = new JLabel("Téléchargement du plan");
        lblTelechargerLePlan.setFont(new Font("Tahoma", Font.BOLD, 14));

        btnDownloadPlan = new JButton("Téléchager");

        JLabel lblEditerLePlan_1 = new JLabel("Editer le plan");
        lblEditerLePlan_1.setFont(new Font("Tahoma", Font.BOLD, 14));

        btnEditPlan = new JButton("Editer");
        
        cmbPlanSalle = new JComboBox();
        
        JLabel lblSelectionDeLa = new JLabel("Selection de la salle");
        lblSelectionDeLa.setFont(new Font("Tahoma", Font.BOLD, 14));
        GroupLayout gl_panel_5 = new GroupLayout(panel_5);
        gl_panel_5.setHorizontalGroup(
            gl_panel_5.createParallelGroup(Alignment.TRAILING)
                .addGroup(gl_panel_5.createSequentialGroup()
                    .addGap(32)
                    .addGroup(gl_panel_5.createParallelGroup(Alignment.LEADING)
                        .addComponent(lblEditerLePlan)
                        .addGroup(gl_panel_5.createSequentialGroup()
                            .addGroup(gl_panel_5.createParallelGroup(Alignment.LEADING)
                                .addGroup(gl_panel_5.createSequentialGroup()
                                    .addComponent(lblEditerLePlan_1)
                                    .addGap(129)
                                    .addComponent(btnEditPlan, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE))
                                .addGroup(gl_panel_5.createParallelGroup(Alignment.TRAILING, false)
                                    .addGroup(gl_panel_5.createSequentialGroup()
                                        .addComponent(lblTelechargerLePlan)
                                        .addGap(50)
                                        .addComponent(btnDownloadPlan))
                                    .addGroup(gl_panel_5.createSequentialGroup()
                                        .addComponent(lblSelectionDeLa, GroupLayout.PREFERRED_SIZE, 187, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(cmbPlanSalle, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE))))
                            .addGap(19)))
                    .addGap(0, 0, Short.MAX_VALUE))
        );
        gl_panel_5.setVerticalGroup(
            gl_panel_5.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_panel_5.createSequentialGroup()
                    .addGroup(gl_panel_5.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_panel_5.createSequentialGroup()
                            .addGap(6)
                            .addComponent(lblEditerLePlan))
                        .addGroup(gl_panel_5.createSequentialGroup()
                            .addGap(47)
                            .addGroup(gl_panel_5.createParallelGroup(Alignment.BASELINE)
                                .addComponent(cmbPlanSalle, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblSelectionDeLa, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE))))
                    .addPreferredGap(ComponentPlacement.UNRELATED)
                    .addGroup(gl_panel_5.createParallelGroup(Alignment.BASELINE)
                        .addComponent(btnDownloadPlan, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblTelechargerLePlan))
                    .addGap(18)
                    .addGroup(gl_panel_5.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_panel_5.createSequentialGroup()
                            .addGap(9)
                            .addComponent(lblEditerLePlan_1))
                        .addComponent(btnEditPlan, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE))
                    .addContainerGap())
        );
        panel_5.setLayout(gl_panel_5);
        
        JLabel lblImporterLesFilms = new JLabel("Importer les films");
        lblImporterLesFilms.setFont(new Font("Tahoma", Font.BOLD, 14));
        
        btnImporter = new JButton("Importer");
        GroupLayout gl_panel_4 = new GroupLayout(panel_4);
        gl_panel_4.setHorizontalGroup(
            gl_panel_4.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_panel_4.createSequentialGroup()
                    .addGroup(gl_panel_4.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_panel_4.createSequentialGroup()
                            .addGap(100)
                            .addComponent(label))
                        .addGroup(gl_panel_4.createSequentialGroup()
                            .addGap(37)
                            .addGroup(gl_panel_4.createParallelGroup(Alignment.LEADING)
                                .addComponent(label_1)
                                .addComponent(lblAjouterUnFilm)
                                .addComponent(lblImporterLesFilms, GroupLayout.PREFERRED_SIZE, 136, GroupLayout.PREFERRED_SIZE))
                            .addGap(81)
                            .addGroup(gl_panel_4.createParallelGroup(Alignment.LEADING)
                                .addComponent(btnImporter, GroupLayout.PREFERRED_SIZE, 87, GroupLayout.PREFERRED_SIZE)
                                .addGroup(gl_panel_4.createParallelGroup(Alignment.LEADING, false)
                                    .addComponent(btnAjouterFilm, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnEditPlanning, GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE)))))
                    .addGap(18))
        );
        gl_panel_4.setVerticalGroup(
            gl_panel_4.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_panel_4.createSequentialGroup()
                    .addGroup(gl_panel_4.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_panel_4.createSequentialGroup()
                            .addGap(11)
                            .addComponent(label)
                            .addGap(23)
                            .addComponent(label_1))
                        .addGroup(gl_panel_4.createSequentialGroup()
                            .addGap(49)
                            .addComponent(btnEditPlanning, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)))
                    .addPreferredGap(ComponentPlacement.UNRELATED)
                    .addGroup(gl_panel_4.createParallelGroup(Alignment.BASELINE)
                        .addComponent(btnAjouterFilm, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblAjouterUnFilm))
                    .addGap(18)
                    .addGroup(gl_panel_4.createParallelGroup(Alignment.BASELINE)
                        .addComponent(lblImporterLesFilms, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnImporter, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(34, Short.MAX_VALUE))
        );
        panel_4.setLayout(gl_panel_4);
        GroupLayout gl_panel = new GroupLayout(panel);
        gl_panel.setHorizontalGroup(
                gl_panel.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_panel.createSequentialGroup()
                        .addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
                                .addGroup(gl_panel.createSequentialGroup()
                                        .addGap(82)
                                        .addComponent(lblEditerLesInfos))
                                .addGroup(gl_panel.createSequentialGroup()
                                        .addGap(42)
                                        .addComponent(lblNom)
                                        .addGap(114)
                                        .addComponent(txtFieldNomCine, GroupLayout.PREFERRED_SIZE, 136, GroupLayout.PREFERRED_SIZE))
                                .addGroup(gl_panel.createSequentialGroup()
                                        .addGap(42)
                                        .addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
                                                .addGroup(gl_panel.createSequentialGroup()
                                                        .addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
                                                                .addComponent(lblTariftudiant)
                                                                .addComponent(lblTarifd))
                                                        .addGap(54)
                                                        .addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
                                                                .addComponent(txtFieldTarifEtu, GroupLayout.PREFERRED_SIZE, 136, GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(txtFieldTarif3D, GroupLayout.PREFERRED_SIZE, 136, GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(btnSaveInfo, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE)))
                                                .addGroup(gl_panel.createSequentialGroup()
                                                        .addComponent(lblTarif)
                                                        .addGap(64)
                                                        .addComponent(txtFieldTarifNormal, GroupLayout.PREFERRED_SIZE, 136, GroupLayout.PREFERRED_SIZE)))))
                        .addContainerGap(34, Short.MAX_VALUE))
                );
        gl_panel.setVerticalGroup(
                gl_panel.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_panel.createSequentialGroup()
                        .addGap(11)
                        .addComponent(lblEditerLesInfos)
                        .addGap(16)
                        .addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
                                .addGroup(gl_panel.createSequentialGroup()
                                        .addGap(4)
                                        .addComponent(lblNom))
                                .addComponent(txtFieldNomCine, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addGap(5)
                        .addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
                                .addGroup(gl_panel.createSequentialGroup()
                                        .addGap(6)
                                        .addComponent(lblTarif))
                                .addComponent(txtFieldTarifNormal, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addGap(3)
                        .addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
                                .addComponent(txtFieldTarifEtu, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblTariftudiant))
                        .addGap(6)
                        .addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
                                .addComponent(txtFieldTarif3D, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblTarifd))
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(btnSaveInfo, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
                        .addGap(32))
                );
        panel.setLayout(gl_panel);
        GroupLayout groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(
            groupLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(groupLayout.createSequentialGroup()
                    .addGap(400)
                    .addComponent(lblCinemaNom, GroupLayout.PREFERRED_SIZE, 225, GroupLayout.PREFERRED_SIZE))
                .addGroup(groupLayout.createSequentialGroup()
                    .addGap(393)
                    .addComponent(lblSlectionnerUnAutre))
                .addGroup(groupLayout.createSequentialGroup()
                    .addGap(331)
                    .addComponent(panel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGroup(groupLayout.createSequentialGroup()
                    .addGap(77)
                    .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                            .addComponent(panel_3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addGap(159)
                            .addComponent(panel_4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addGroup(groupLayout.createSequentialGroup()
                            .addComponent(panel_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addGap(159)
                            .addComponent(panel_5, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
        );
        groupLayout.setVerticalGroup(
            groupLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(groupLayout.createSequentialGroup()
                    .addGap(6)
                    .addComponent(lblCinemaNom)
                    .addGap(18)
                    .addComponent(lblSlectionnerUnAutre)
                    .addGap(12)
                    .addComponent(panel, GroupLayout.PREFERRED_SIZE, 229, GroupLayout.PREFERRED_SIZE)
                    .addGap(18)
                    .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                        .addComponent(panel_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(panel_5, GroupLayout.PREFERRED_SIZE, 203, GroupLayout.PREFERRED_SIZE))
                    .addGap(12)
                    .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                        .addComponent(panel_3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(panel_4, GroupLayout.PREFERRED_SIZE, 223, GroupLayout.PREFERRED_SIZE)))
        );
        setLayout(groupLayout);

    }
    /**Getters and setters*/

    /**
     * @return le btnImporter
     */
    public JButton getBtnImporter() {
        return btnImporter;
    }

    /**
     * @return le cmbPlanSalle
     */
    public JComboBox getCmbPlanSalle() {
        return cmbPlanSalle;
    }

    /**
     * @return le btnAjouterFilm
     */
    public JButton getBtnAjouterFilm() {
        return btnAjouterFilm;
    }

    /**
     * @param btnAjouterFilm le btnAjouterFilm to set
     */
    public void setBtnAjouterFilm(JButton btnAjouterFilm) {
        this.btnAjouterFilm = btnAjouterFilm;
    }

    /**
     * @return comboBoxSupprSalle
     */
    public JComboBox getComboBoxSupprSalle() {
        return comboBoxSupprSalle;
    }

    /**
     * @param comboBoxSupprSalle
     */
    public void setComboBoxSupprSalle(JComboBox comboBoxSupprSalle) {
        this.comboBoxSupprSalle = comboBoxSupprSalle;
    }

    /**
     * @return textFieldNbSiege
     */
    public JTextField getTextFieldNbSiege() {
        return textFieldNbSiege;
    }

    /**
     * @param textFieldNbSiege
     */
    public void setTextFieldNbSiege(JTextField textFieldNbSiege) {
        this.textFieldNbSiege = textFieldNbSiege;
    }

    /**
     * @return textFieldNbRangee
     */
    public JTextField getTextFieldNbRangee() {
        return textFieldNbRangee;
    }

    /**
     * @param textFieldNbRangee
     */
    public void setTextFieldNbRangee(JTextField textFieldNbRangee) {
        this.textFieldNbRangee = textFieldNbRangee;
    }

    /**
     * @return lblCinemaNom
     */
    public JLabel getLblCinemaNom() {
        return lblCinemaNom;
    }

    /**
     * @param lblCinemaNom
     */
    public void setLblCinemaNom(JLabel lblCinemaNom) {
        this.lblCinemaNom = lblCinemaNom;
    }

    /**
     * @return txtFieldTarifNormal
     */
    public JTextField getTxtFieldTarifNormal() {
        return txtFieldTarifNormal;
    }

    /**
     * @param txtFieldTarifNormal
     */
    public void setTxtFieldTarifNormal(JTextField txtFieldTarifNormal) {
        this.txtFieldTarifNormal = txtFieldTarifNormal;
    }

    /**
     * @return txtFieldTarifEtu
     */
    public JTextField getTxtFieldTarifEtu() {
        return txtFieldTarifEtu;
    }

    /**
     * @param txtFieldTarifEtu
     */
    public void setTxtFieldTarifEtu(JTextField txtFieldTarifEtu) {
        this.txtFieldTarifEtu = txtFieldTarifEtu;
    }

    /**
     * @return txtFieldNomCine
     */
    public JTextField getTxtFieldNomCine() {
        return txtFieldNomCine;
    }

    /**
     * @param txtFieldNomCine
     */
    public void setTxtFieldNomCine(JTextField txtFieldNomCine) {
        this.txtFieldNomCine = txtFieldNomCine;
    }

    /**
     * @return txtFieldTarif3D
     */
    public JTextField getTxtFieldTarif3D() {
        return txtFieldTarif3D;
    }

    /**
     * @param txtFieldTarif3D
     */
    public void setTxtFieldTarif3D(JTextField txtFieldTarif3D) {
        this.txtFieldTarif3D = txtFieldTarif3D;
    }

    /**
     * @return btnAddSalle
     */
    public JButton getBtnAddSalle() {
        return btnAddSalle;
    }

    /**
     * @param btnAddSalle
     */
    public void setBtnAddSalle(JButton btnAddSalle) {
        this.btnAddSalle = btnAddSalle;
    }

    /**
     * @return btnSupprSalle
     */
    public JButton getBtnSupprSalle() {
        return btnSupprSalle;
    }

    /**
     * @param btnSupprSalle
     */
    public void setBtnSupprSalle(JButton btnSupprSalle) {
        this.btnSupprSalle = btnSupprSalle;
    }

    /**
     * @return btnEditPlanning
     */
    public JButton getBtnEditPlanning() {
        return btnEditPlanning;
    }

    /**
     * @param btnEditPlanning
     */
    public void setBtnEditPlanning(JButton btnEditPlanning) {
        this.btnEditPlanning = btnEditPlanning;
    }

    /**
     * @return btnSaveInfo
     */
    public JButton getBtnSaveInfo() {
        return btnSaveInfo;
    }

    /**
     * @param btnSaveInfo
     */
    public void setBtnSaveInfo(JButton btnSaveInfo) {
        this.btnSaveInfo = btnSaveInfo;
    }

    /**
     * @return btnDownloadPlan
     */
    public JButton getBtnDownloadPlan() {
        return btnDownloadPlan;
    }

    /**
     * @param btnDownloadPlan
     */
    public void setBtnDownloadPlan(JButton btnDownloadPlan) {
        this.btnDownloadPlan = btnDownloadPlan;
    }

    /**
     * @return btnEditPlan
     */
    public JButton getBtnEditPlan() {
        return btnEditPlan;
    }

    /**
     * @param btnEditPlan
     */
    public void setBtnEditPlan(JButton btnEditPlan) {
        this.btnEditPlan = btnEditPlan;
    }

    /**
     * @return txtFieldAddSalle
     */
    public JTextField getTxtFieldAddSalle() {
        return txtFieldAddSalle;
    }

    /**
     * @param txtFieldAddSalle
     */
    public void setTxtFieldAddSalle(JTextField txtFieldAddSalle) {
        this.txtFieldAddSalle = txtFieldAddSalle;
    }

    /**
     * @return le lblSlectionnerUnAutre
     */
    public JLabel getLblSlectionnerUnAutre() {
        return lblSlectionnerUnAutre;
    }

    /**
     * @param lblSlectionnerUnAutre le lblSlectionnerUnAutre to set
     */
    public void setLblSlectionnerUnAutre(JLabel lblSlectionnerUnAutre) {
        this.lblSlectionnerUnAutre = lblSlectionnerUnAutre;
    }
    /**
     * Methode où l'on va lier les éléments de la vue à leur controller respectif.
     * @param controller spécifique à la vue.
     */
    public void setController(GestionCinemaController controller) {
        lblSlectionnerUnAutre.addMouseListener(controller);
        btnEditPlanning.addActionListener(controller);
        btnAddSalle.addActionListener(controller);
        btnSupprSalle.addActionListener(controller);
        btnSaveInfo.addActionListener(controller);
        btnDownloadPlan.addActionListener(controller);
        btnEditPlan.addActionListener(controller);
        btnAjouterFilm.addActionListener(controller);
        txtFieldTarifNormal.addKeyListener(controller);
        txtFieldTarifEtu.addKeyListener(controller);
        txtFieldTarif3D.addKeyListener(controller);
        txtFieldAddSalle.addKeyListener(controller);
        textFieldNbSiege.addKeyListener(controller);
        textFieldNbRangee.addKeyListener(controller);
        btnImporter.addActionListener(controller);
    }
}