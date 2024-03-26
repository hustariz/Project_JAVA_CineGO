/*
 * Controller.java                                31 janv. 2016
 * CESI RILA 2015/2017
 */
package cineGOv02.common.graphics;

import cineGOv02.common.entity.User;

/**
 * Interface d'un controlleur manipulant des objet de type User
 * @author Remi
 *
 */
public interface Controller {
    /**
     * 
     * Méthode de chargement d'un utilisateur
     * @param user
     */
    public void loadUser(User user);
}
