/*
 * Genre.java                                21 janv. 2016
 * CESI RILA 2015/2017
 */
package cineGOv02.common.graphics;

import javax.swing.JLabel;

/**
 * Genre
 * @author Remi
 */
public class Genre extends JLabel {
    
    /** Vaut true si ce genre est actif */
    private boolean actif = false;
    
    /** Libellé du genre */
    private String genre = "";
    
    /**
     * Constructeur d'un Genre de Film
     * @param text
     */
    public Genre(String text){
        super(text);
    }

    /**
     * @return le actif
     */
    public boolean isActif() {
        return actif;
    }

    /**
     * @param actif le actif to set
     */
    public void setActif(boolean actif) {
        this.actif = actif;
    }

    /**
     * @return le genre
     */
    public String getGenre() {
        return genre;
    }

    /**
     * @param genre le genre to set
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }
}
