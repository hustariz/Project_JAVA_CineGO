package cineGOv02.common.entity;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author user
 *
 */
@Entity
@Table(name="Cinema")
public class Cinema implements Serializable{
    /** ID du Cinéma */
    private int id;
    /** Nom du Cinéma*/
    private String nom;
    /** Nom du Cinéma */
    private double tarifNormal;
    /** Tarif de la séance en 3d */
    private double tarif3D;
    /** Montant de la réduction étudiant */
    private double reductionEtudiant;

    /**
     *  Constructeur par défaut
     */
    public Cinema(){
    }
    
    /**
     * Constructeur avec arguments
     * @param nom
     * @param tarifNormal
     * @param tarif3d
     * @param reductionEtudiant
     */
    public Cinema(String nom, double tarifNormal, double tarif3d, double reductionEtudiant) {
        super();
        this.nom = nom;
        this.tarifNormal = tarifNormal;
        tarif3D = tarif3d;
        this.reductionEtudiant = reductionEtudiant;
    }

    /**
     * @return le id
     */
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }
    
    /**
     * @param id le id to set
     */
    public void setId(int id) {
        this.id = id;
    }
    
    /**
     * @return le nom
     */
    @Column (name="nom")
    public String getNom() {
        return nom;
    }
    /**
     * @param nom le nom to set
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * @return le tarifNormal
     */
    @Column (name="tarifNormal")
    public double getTarifNormal() {
        return tarifNormal;
    }
    /**
     * @param tarifNormal le tarifNormal to set
     */
    public void setTarifNormal(double tarifNormal) {
        this.tarifNormal = tarifNormal;
    }

    /**
     * @return le tarif3D
     */
    @Column (name="tarif3D")
    public double getTarif3D() {
        return tarif3D;
    }
    /**
     * @param tarif3d le tarif3D to set
     */
    public void setTarif3D(double tarif3d) {
        tarif3D = tarif3d;
    }

    /**
     * @return le tarifEtudiant
     */
    @Column (name="reductionEtudiant")
    public double getReductionEtudiant() {
        return reductionEtudiant;
    }
    /**
     * @param tarifEtudiant le tarifEtudiant to set
     */
    public void setReductionEtudiant(double reductionEtudiant) {
        this.reductionEtudiant = reductionEtudiant;
    }
    
    @Override
    public String toString(){
        return this.nom;
    }
    
}
