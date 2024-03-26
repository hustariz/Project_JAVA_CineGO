/*
 * SiegeIcon.java                                10 févr. 2016
 * CESI RILA 2015/2017
 */
package cineGOv02.common.graphics;

import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Réprésentation d'un siège
 * @author Remi
 *
 */
public class SiegeIcon extends SalleIcon {
    private boolean normal = false;
    private boolean hand = false;
    private boolean allee = false;
    private boolean occupe = false;
    
    private boolean selected = false;
    /**
     * Constructeur par défaut
     */
    public SiegeIcon(){
    }
   
    /**
     * @return le normal
     */
    public boolean isNormal() {
        return normal;
    }
    /**
     * @param normal le normal to set
     */
    public void setNormal(boolean normal) {
        this.normal = normal;
    }
    /**
     * @return le hand
     */
    public boolean isHand() {
        return hand;
    }
    /**
     * @param hand le hand to set
     */
    public void setHand(boolean hand) {
        this.hand = hand;
    }
    /**
     * @return le allee
     */
    public boolean isAllee() {
        return allee;
    }
    /**
     * @param allee le allee to set
     */
    public void setAllee(boolean allee) {
        this.allee = allee;
    }

    /**
     * @return le occupe
     */
    public boolean isOccupe() {
        return occupe;
    }

    /**
     * @param occupe le occupe to set
     */
    public void setOccupe(boolean occupe) {
        this.occupe = occupe;
    }

    /**
     * @return le selected
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * @param selected le selected to set
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object arg0) {
        return arg0 instanceof SiegeIcon 
                && this.getX() == ((SiegeIcon)arg0).getX()
                && this.getY() == ((SiegeIcon)arg0).getY();
    }
    
    @Override
    public String toString(){
        return this.getAnX() + " " + this.getAnY();
    }
    
}
