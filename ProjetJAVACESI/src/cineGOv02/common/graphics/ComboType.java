/*
 * ComboType.java                                7 févr. 2016
 * CESI RILA 2015/2017
 */
package cineGOv02.common.graphics;

import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;

/**
 * Combobox permettant d'aaficher les seance par type
 * @author Remi
 *
 */
public class ComboType extends JComboBox {

    /**
     * Constructeur par défaut
     */
    public ComboType() {
        super();
    }

    /**
     * Constructeur parent redéfinit
     * @param aModel
     */
    public ComboType(ComboBoxModel aModel) {
        super(aModel);
    }

    /**
     * Constructeur parent redéfinit
     * @param items
     */
    public ComboType(Object[] items) {
        super(items);
    }

    /**
     * Constructeur parent redéfinit
     * @param items
     */
    public ComboType(Vector items) {
        super(items);
    }

}
