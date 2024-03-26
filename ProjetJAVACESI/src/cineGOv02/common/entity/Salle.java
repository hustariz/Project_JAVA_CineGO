package cineGOv02.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * @author Hugo
 *
 */
@Entity
@Table(name="Salle") 
public class Salle {
    /** ID DB de la salle */
    private int id;
    /** Numéro de la salle */
    private int numeroDeSalle;
    /** Nombre de rangées */
    private int nbRangees;
    /** nombre total de sièges */
    private int nbSieges;
    /** Places disponibles */
    private int placesDisponibles;
    /** Cinéma auquel appartient cette Salle */
    private Cinema cinema;
    /** Plan de la salle */
    private String planDeSalle;


    /**
     *  Constructeur par défaut (Hibernate)
     */
    public Salle() {
    }

    /**
     * Constructeur avec arguments
     * @param numeroDeSalle
     * @param nbRangees
     * @param nbSieges
     * @param placesDisponibles
     * @param cinema
     * @param planDeSalle
     */
    public Salle(int numeroDeSalle, int nbRangees, int nbSieges, int placesDisponibles, Cinema cinema,
            String planDeSalle) {
        super();
        this.numeroDeSalle = numeroDeSalle;
        this.nbRangees = nbRangees;
        this.nbSieges = nbSieges;
        this.placesDisponibles = placesDisponibles;
        this.cinema = cinema;
        this.planDeSalle = planDeSalle;
    }



    /**
     * @return the id
     */
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }
    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * @return the numeroDeSalle
     */
    @Column (name="numSalle")
    public int getNumeroDeSalle() {
        return numeroDeSalle;
    }
    /**
     * @param numeroDeSalle the numeroDeSalle to set
     */
    public void setNumeroDeSalle(int numeroDeSalle) {
        this.numeroDeSalle = numeroDeSalle;
    }
    /**
     * @return the nbRangees
     */
    @Column (name="nbRangees")
    public int getNbRangees() {
        return nbRangees;
    }
    /**
     * @param nbRangees the nbRangees to set
     */
    public void setNbRangees(int nbRangees) {
        this.nbRangees = nbRangees;
    }
    /**
     * @return the nbSieges
     */
    @Column (name="nbSieges")
    public int getNbSieges() {
        return nbSieges;
    }
    /**
     * @param nbSieges the nbSieges to set
     */
    public void setNbSieges(int nbSieges) {
        this.nbSieges = nbSieges;
    }
    /**
     * @return the placeDisponible
     */
    @Column (name="placeDispo")
    public int getPlacesDisponibles() {
        return placesDisponibles;
    }
    /**
     * @param placesDisponibles 
     * @param placeDisponible the placeDisponible to set
     */
    public void setPlacesDisponibles(int placesDisponibles) {
        this.placesDisponibles = placesDisponibles;
    }

    /**
     * @return le cinema
     */
    @OneToOne
    @JoinColumn(name="cinema")
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
     * @return le planDeSalle
     */
    @Column (name="planDeSalle",columnDefinition="LONGTEXT")
    public String getPlanDeSalle() {
        return planDeSalle;
    }

    /**
     * @param planDeSalle le planDeSalle to set
     */
    public void setPlanDeSalle(String planDeSalle) {
        this.planDeSalle = planDeSalle;
    }
    
    @Override
    public String toString(){
        return numeroDeSalle+"";
    }
    
}
