/*
 * ClientModel.java                                10 avr. 2016
 * CESI RILA 2015/2017
 */
package cineGOv02.client.model;

import java.sql.Timestamp;
import java.util.ArrayList;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import cineGOv02.common.entity.Cinema;
import cineGOv02.common.entity.Film;
import cineGOv02.common.entity.Reservation;
import cineGOv02.common.entity.Salle;
import cineGOv02.common.entity.Seance;
import cineGOv02.common.entity.User;
import cineGOv02.common.hibernate.MySQLDataSource;

/**
 * Modèle de données pour l'application client
 * @author Remi
 *
 */
public class ClientModel {
    /** Factory de session permettant de requéter la base de données */
    private static SessionFactory factory;

    public ClientModel(){
        factory = MySQLDataSource.getInstance().getFactory(); 
    }

    public void saveEntity(Object entity){
        Session session = factory.openSession();
        session.getTransaction().begin();
        session.flush();
        session.save(entity);
        session.getTransaction().commit();
        session.close();
    }

    public void updateEntity(Object entity){
        Session session = factory.openSession();
        session.getTransaction().begin();
        session.flush();
        session.update(entity);
        session.getTransaction().commit();
        session.close();
    }

    public void deleteEntity(Object entity){
        Session session = factory.openSession();
        session.getTransaction().begin();
        session.flush();
        session.delete(entity);
        session.getTransaction().commit();
        session.close();
    }

    public boolean cinemaExist(String nomCinema){
        Session session = factory.openSession();
        long nbCinema = (long) session.createQuery("SELECT COUNT(*) FROM Cinema WHERE nom = :nom")
                .setString("nom", nomCinema)
                .uniqueResult();
        session.close();
        return nbCinema > 0 ? true: false;
    }

    public Salle getSalle(int numSalle, Cinema cinema){
        Session session = factory.openSession();
        Salle salle = (Salle) session.createQuery("SELECT sa FROM Salle sa WHERE sa.numeroDeSalle = :numSalle AND sa.cinema = :cinema ")
                .setInteger("numSalle", numSalle)
                .setEntity("cinema", cinema)
                .uniqueResult();
        session.close();
        return salle;
    }

    private ArrayList<Integer> getListNumSalle(Cinema cinema){
        Session session = factory.openSession();
        ArrayList<Integer> salle = new ArrayList<Integer>(session.createQuery("SELECT sa.numeroDeSalle FROM Salle sa WHERE sa.cinema = :cinema ")
                .setEntity("cinema", cinema)
                .list());
        return salle;
    }

    public ArrayList<Salle> getAllSalles(Cinema cinema){
        Session session = factory.openSession();
        ArrayList<Salle> salles = new ArrayList<Salle>(session.createQuery("FROM Salle sa WHERE sa.cinema = :cinema ")
                .setEntity("cinema", cinema)
                .list());
        session.close();
        return salles;
    }

    public ArrayList<Film> getAllFilm(){
        Session session= factory.openSession();
        session.beginTransaction();
        ArrayList<Film> films = new ArrayList<Film>(session.createQuery("FROM Film")
                .list());
        return films;
    }

    public ArrayList<Cinema> getAllCinema(){
        Session session = factory.openSession();
        ArrayList<Cinema> cinema = new ArrayList<Cinema>(session.createQuery("FROM Cinema").list());
        session.close();
        return cinema;
    }

    /**
     * @return le factory
     */
    public SessionFactory getFactory() {
        return factory;
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
     * Récupération de la liste des film à l'affiche aujourd'hui
     * @param cinema
     * @param debut
     * @param fin
     * @return
     */
    public ArrayList<Film> getFilmJour(Cinema cinema, Timestamp debut, Timestamp fin) {
        Session session = factory.openSession();
        ArrayList<Film> listeFilmsDuJour = new ArrayList<Film>(session.createQuery(""
                + "SELECT DISTINCT se.film FROM Seance se JOIN se.salle.cinema WHERE se.salle.cinema = :cinema AND se.debut > :debut AND se.fin < :fin")
                .setEntity("cinema", cinema).setTimestamp("debut", debut).setTimestamp("fin", fin).list());
        session.close();
        return listeFilmsDuJour;
    }

    /**
     * Récupération de la liste des films à l'affiche
     * @param cinema
     * @param debut
     * @return
     */
    public ArrayList<Film> getFilms(Cinema cinema, Timestamp debut) {
        Session session = factory.openSession();
        ArrayList<Film> listeFilm = new ArrayList<Film>(session.createQuery(""
                + "SELECT DISTINCT se.film FROM Seance se JOIN se.salle.cinema WHERE se.salle.cinema = :cinema AND se.debut > :debut")
                .setEntity("cinema", cinema).setTimestamp("debut", debut).list());
        session.close();
        return listeFilm;
    }

    /**
     * Vérifie si un utilisateur existe en fonction de sa mail
     * @param mail
     * @return
     */
    public boolean userExist(String mail) {
        Session session = factory.openSession();
        long rep = (long) session.createQuery("SELECT count(*) FROM User WHERE mail = :mail")
                .setString("mail", mail)
                .uniqueResult();
        session.close();
        if(rep > 0){
            return true;
        }
        return false;
    }

    /**
     * Récupère le hash du mot de passe d'un utilisateur
     * @param mail
     * @return
     */
    public String getHash(String mail) {
        Session session = factory.openSession();
        String hash = (String) session.createQuery("SELECT motDePasse FROM User WHERE mail = :mail ")
                .setString( "mail",  mail).uniqueResult();
        session.close();
        return hash;
    }

    /**
     * Récupère les infos d'un utilisateur 
     * @param mail
     * @param hash
     * @return
     */
    public User getUser(String mail, String hash) {
        Session session = factory.openSession();
        User user = (User) session.createQuery("FROM User WHERE mail = :mail AND motDePasse = :hash")
                .setString("mail", mail)
                .setString("hash", hash)
                .uniqueResult();
        session.close();
        return user;
    }

    /**
     * Execute une requête d'update
     * @param hash
     * @param mail
     */
    public void executeUpdate(String hash, String mail) {
        Session session = factory.openSession();
        String hqlUpdate =
                "update User u set u.motDePasse = :newPasswd where u.mail = :mail " ;
        session.createQuery( hqlUpdate ).setString( "newPasswd",  hash)
        .setString( "mail", mail )
        .executeUpdate();
        session.close();
    }

    /**
     * Retourne l'user correspondant à cette mail si il existe
     * @param mail Mail saisie 
     * @return l'user correspondant à la mail ou rien si la mail n'est pas présente en BD
     */
    public User getUser(String mail){
        Session session = factory.openSession();
        session.beginTransaction();
        User user = (User) session.createQuery("from User where mail = '"+ mail +"'").uniqueResult();
        session.getTransaction().commit();
        session.close();
        return user;
    }

    /**
     * Récupère la liste des réservations 
     * @param user
     * @return
     */
    public ArrayList<Reservation> getAllResa(User user) {
        Session session = factory.openSession();
        ArrayList<Reservation> resa = new ArrayList<Reservation>(session.createQuery("Select re FROM Reservation re "
                + "WHERE re.user = :user "
                + "ORDER BY re.seance.debut DESC")
                .setEntity("user", user)
                .list());
        session.close();
        return resa;
    }

    /**
     * Récupère la liste des séances pour un film à partir d'une date
     * @param cinema
     * @param film
     * @param debut
     */
    public ArrayList<Seance> getSeances(Cinema cinema, Film film, Timestamp debut) {
        Session session = factory.openSession();
        session.getTransaction().begin();
        ArrayList<Seance> seances = (ArrayList<Seance>) session.createQuery("SELECT se FROM Seance se "
                + "JOIN se.salle.cinema "
                + "WHERE se.salle.cinema = :cinema "
                + "AND se.film = :film "
                + "AND se.debut > :debut "
                + "ORDER BY se.debut")
                .setEntity("cinema", cinema)
                .setEntity("film", film)
                .setTimestamp("debut", debut).list();
        session.close();
        return seances;
    }

    /**
     * Récupère une séance en particulier
     * @param debut
     * @param vf
     * @param vo
     * @param vost
     * @param vf3d
     * @param selectedItem
     * @return
     */
    public Seance getTheSeance(Cinema cinema, Film film, Timestamp debut, boolean vf, boolean vo, boolean vost, boolean vf3d, boolean vost3d, int numPlace) {
        Session session = factory.openSession();
        session.getTransaction().begin();
        Seance seance  = (Seance) session.createQuery("SELECT se FROM Seance se JOIN se.salle.cinema "
                + "WHERE se.salle.cinema = :cinema "
                + "AND se.salle.numeroDeSalle = :numSalle "
                + "AND se.film = :film "
                + "AND se.debut = :debut "
                + "AND se.VF = :vf "
                + "AND se.VF3D = :vf3d "
                + "AND se.VOST3D = :vost3d "
                + "AND se.VOST = :vost "
                + "AND se.VO = :vo")
                .setEntity("cinema", cinema)
                .setEntity("film", film)
                .setTimestamp("debut", debut)
                .setBoolean("vf", vf)
                .setBoolean("vo", vo)
                .setBoolean("vost", vost)
                .setBoolean("vf3d", vf3d)
                .setBoolean("vost3d", vost3d)
                .setInteger("numSalle", numPlace)
                .uniqueResult();
        session.close();
        return seance;
    }
}
