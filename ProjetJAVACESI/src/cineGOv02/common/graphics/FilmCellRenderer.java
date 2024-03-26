/*
 * ResaCellRenderer.java                                9 févr. 2016
 * CESI RILA 2015/2017
 */
package cineGOv02.common.graphics;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextPane;
import javax.swing.ListCellRenderer;

import cineGOv02.common.entity.Film;

/**
 * Classe de rendu d'une cellule de JList
 * @author Remi
 *
 */
public class FilmCellRenderer extends JTextPane implements ListCellRenderer {
    private JLabel lab = new JLabel();
    JTextPane pane = new JTextPane();
    private ImageIcon image;
    /**
     * Constructeur d'une cellule d'affichage d'un film dans une JList
     */
    public FilmCellRenderer() {
        setOpaque(false);
        this.setLayout(new BorderLayout());
        lab.setOpaque(false);
        pane.setOpaque(false);
    }

    public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {
        Film film = (Film) value;
        try {
            BufferedImage buff = ImageIO.read(film.getImage().getBinaryStream());
            ImageIcon icon = new ImageIcon(buff);
            Image img = icon.getImage();
            Image newImg = img.getScaledInstance(67, 90, Image.SCALE_SMOOTH);
            image = new ImageIcon(newImg);
            lab.setIcon(image);
            pane.setText(film.toString());
            this.add(lab, BorderLayout.WEST);
            this.add(pane, BorderLayout.CENTER);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return this;
    }
}
