package cineGOv02.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Hugo
 */
@Entity
@Table(name="User")  
public class User {
    /** ID BD de l'user */
    private int id;
    /** Numero de fidélité */
    private String numeroFidelite;
    /** Nom de l'utilisateur */
    private String nom;
    /** Prenom de l'utilisateur */
    private String prenom;
    /** Mail de l'utilisateur */
    private String mail;
    /** Caractère Admin de l'utilisateur */
    private boolean admin = false;
    /** Mot de passe de l'utilisateur */
    private String motDePasse;
    /** Etudiant ou non */
    private boolean etudiant;
    /** Avoir appliqué à l'utilisateur */
    private double avoir;

    /**
     *  Constructeur par défaut
     */
    public User() {
    }

    /**
     * Constructeur avec arguments
     * @param numeroFidelite
     * @param nom
     * @param prenom
     * @param mail
     * @param admin
     * @param motDePasse
     * @param etudiant
     * @param avoir
     */
    public User(String numeroFidelite, String nom, String prenom, String mail, boolean admin, String motDePasse,
            boolean etudiant, double avoir) {
        super();
        this.numeroFidelite = numeroFidelite;
        this.nom = nom;
        this.prenom = prenom;
        this.mail = mail;
        this.admin = admin;
        this.motDePasse = motDePasse;
        this.etudiant = etudiant;
        this.avoir = avoir;
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
     * @return the numeroFidelite
     */
    @Column (name="numFid")
    public String getNumeroFidelite() {
        return numeroFidelite;
    }
    /**
     * @param numeroFidelite the numeroFidelite to set
     */
    public void setNumeroFidelite(String numeroFidelite) {
        this.numeroFidelite = numeroFidelite;
    }
    /**
     * @return the nom
     */
    @Column (name="nom")
    public String getNom() {
        return nom;
    }
    /**
     * @param nom the nom to set
     */
    public void setNom(String nom) {
        this.nom = nom;
    }
    /**
     * @return the prenom
     */
    @Column (name="prenom")
    public String getPrenom() {
        return prenom;
    }
    /**
     * @param prenom the prenom to set
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    /**
     * @return the mail
     */
    @Column (name="mail")
    public String getMail() {
        return mail;
    }
    /**
     * @param mail the mail to set
     */
    public void setMail(String mail) {
        this.mail = mail;
    }
    
    /**
     * @return le admin
     */
    @Column (name="admin")
    public boolean isAdmin() {
        return admin;
    }
    /**
     * @param admin le admin to set
     */
    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    /**
     * @return le motDePasse
     */
    @Column (name="mdp")
    public String getMotDePasse() {
        return motDePasse;
    }
    /**
     * @param motDePasse le motDePasse to set
     */
    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    /**
     * @return le etudiant
     */
    @Column (name="etudiant")
    public boolean isEtudiant() {
        return etudiant;
    }

    /**
     * @param etudiant le etudiant to set
     */
    public void setEtudiant(boolean etudiant) {
        this.etudiant = etudiant;
    }



    /**
     * @return le avoir
     */
    @Column (name="avoir")
    public double getAvoir() {
        return avoir;
    }



    /**
     * @param avoir le avoir to set
     */
    public void setAvoir(double avoir) {
        this.avoir = avoir;
    }
   
}
