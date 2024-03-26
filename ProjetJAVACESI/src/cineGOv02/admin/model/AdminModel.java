/*
 * AdminModel.java                                10 avr. 2016
 * CESI RILA 2015/2017
 */
package cineGOv02.admin.model;

import java.sql.Timestamp;
import java.util.ArrayList;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import cineGOv02.common.entity.Cinema;
import cineGOv02.common.entity.Film;
import cineGOv02.common.entity.Salle;
import cineGOv02.common.entity.Seance;
import cineGOv02.common.hibernate.MySQLDataSource;

/**
 * Modèle de données pour l'application admin
 * @author Remi
 *
 */
public class AdminModel {
    /** Factory Hibernate permettant de requéter la base de données */
    private static SessionFactory factory;

    public AdminModel(){
        factory = MySQLDataSource.getInstance().getFactory(); 
    }
    /**
     * Méthode de sauvegarde d'une entité
     * @param entity
     */
    public void saveEntity(Object entity){
        Session session = factory.openSession();
        session.getTransaction().begin();
        session.flush();
        session.save(entity);
        session.getTransaction().commit();
        session.close();
    }
    /**
     * Méthode de mise à jour d'une entité
     * @param entity
     */
    public void updateEntity(Object entity){
        Session session = factory.openSession();
        session.getTransaction().begin();
        session.flush();
        session.update(entity);
        session.getTransaction().commit();
        session.close();
    }
    /**
     * Méthode de suppression d'une entité
     * @param entity
     */
    public void deleteEntity(Object entity){
        Session session = factory.openSession();
        session.getTransaction().begin();
        session.flush();
        session.delete(entity);
        session.getTransaction().commit();
        session.close();
    }
    /**
     * Permet de vérifier si un cinéla existe à partir de son nom
     * @param nomCinema
     * @return
     */
    public boolean cinemaExist(String nomCinema){
        Session session = factory.openSession();
        long nbCinema = (long) session.createQuery("SELECT COUNT(*) FROM Cinema WHERE nom = :nom")
                .setString("nom", nomCinema)
                .uniqueResult();
        session.close();
        return nbCinema > 0 ? true: false;
    }
    /**
     * Méthode de récupération d'une salle avec son numéro et du cinéma
     * @param numSalle
     * @param cinema
     * @return
     */
    public Salle getSalle(int numSalle, Cinema cinema){
        Session session = factory.openSession();
        Salle salle = (Salle) session.createQuery("SELECT sa FROM Salle sa WHERE sa.numeroDeSalle = :numSalle AND sa.cinema = :cinema ")
                .setInteger("numSalle", numSalle)
                .setEntity("cinema", cinema)
                .uniqueResult();
        session.close();
        return salle;
    }
    
    /**
     * Récupération de la liste des numéro de salles
     * @param cinema
     * @return
     */
    private ArrayList<Integer> getListNumSalle(Cinema cinema){
        Session session = factory.openSession();
        ArrayList<Integer> salle = new ArrayList<Integer>(session.createQuery("SELECT sa.numeroDeSalle FROM Salle sa WHERE sa.cinema = :cinema ")
                .setEntity("cinema", cinema)
                .list());
        return salle;
    }
    /**
     * Récupération de la liste des salles pour ce cinéma
     * @param cinema
     * @return
     */
    public ArrayList<Salle> getAllSalles(Cinema cinema){
        Session session = factory.openSession();
        ArrayList<Salle> salles = new ArrayList<Salle>(session.createQuery("FROM Salle sa WHERE sa.cinema = :cinema ")
                .setEntity("cinema", cinema)
                .list());
        session.close();
        System.out.println(salles.size());
        return salles;
    }
    
    /**
     * Récupération de la liste des films
     * @return
     */
    public ArrayList<Film> getAllFilm(){
        Session session= factory.openSession();
        session.beginTransaction();
        ArrayList<Film> films = new ArrayList<Film>(session.createQuery("FROM Film")
                .list());
        return films;
    }
    
    /**
     * Récupère de liste des cinéma
     * @return
     */
    public ArrayList<Cinema> getAllCinema(){
        Session session = factory.openSession();
        ArrayList<Cinema> cinema = new ArrayList<Cinema>(session.createQuery("FROM Cinema").list());
        session.close();
        return cinema;
    }
    
    /**
     * Récupération dd'une séance en fonction de son horaire de début et de fin
     * @param idSalle
     * @param debut
     * @param fin
     * @return 
     */
    public ArrayList<Seance> getSeance(int idSalle, Timestamp debut, Timestamp fin) {
        Session session = factory.openSession();
        ArrayList<Seance> seances = new ArrayList<Seance>(session.createQuery("FROM Seance se WHERE se.salle = :idSalle "
                + "AND se.debut >= :debut "
                + "AND se.debut <= :fin")
                .setInteger ("idSalle", idSalle)
                .setTimestamp("debut", debut)
                .setTimestamp("fin", fin)
                .list());
        session.close();
        return seances;
    }
    
    
    /**
     * @return le factory
     */
    public SessionFactory getFactory() {
        return factory;
    }
}
