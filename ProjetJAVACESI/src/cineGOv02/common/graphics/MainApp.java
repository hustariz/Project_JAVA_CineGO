/*
 * MainApp.java                                28 janv. 2016
 * CESI RILA 2015/2017
 */
package cineGOv02.common.graphics;

import javax.swing.JFrame;
import cineGOv02.common.entity.Cinema;
import cineGOv02.common.entity.Film;
import cineGOv02.common.entity.Reservation;
import cineGOv02.common.entity.Salle;
import cineGOv02.common.entity.Seance;
import cineGOv02.common.entity.User;

/**
 * Main frame permettant d'embarquer différents objets et de les faire transiter de vue en vue
 * @author Remi
 */
public class MainApp extends JFrame {
    /** Fenêtre principale */
    private Cinema cinema;
    private User user;
    private Reservation reservation;
    private Film film;
    private Seance seance;
    private Salle salle;
    
    /**
     * @return le cinema
     */
    public Cinema getCinema() {
        return cinema;
    }
    /**
     * @param cinema le cinema to set
     */
    public void setCinema(Cinema cinema) {
        this.cinema = cinema;
    }
    /**
     * @return le user
     */
    public User getUser() {
        return user;
    }
    /**
     * @param user le user to set
     */
    public void setUser(User user) {
        this.user = user;
    }
    /**
     * @return le reservation
     */
    public Reservation getReservation() {
        return reservation;
    }
    /**
     * @param reservation le reservation to set
     */
    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }
    /**
     * @return le film
     */
    public Film getFilm() {
        return film;
    }
    /**
     * @param film le film to set
     */
    public void setFilm(Film film) {
        this.film = film;
    }
    /**
     * @return le seance
     */
    public Seance getSeance() {
        return seance;
    }
    /**
     * @param seance le seance to set
     */
    public void setSeance(Seance seance) {
        this.seance = seance;
    }
    /**
     * @return le salle
     */
    public Salle getSalle() {
        return salle;
    }
    /**
     * @param salle le salle to set
     */
    public void setSalle(Salle salle) {
        this.salle = salle;
    }
}
