
package biblio.model;

import java.time.LocalDate;


public class Membre {
    private int idMembre;
    private String nom;
    private String email;
    private LocalDate dateInscription ; 
    
    public Membre() {}

    public Membre(String nom, String email, LocalDate dateInscription) {
        this.nom = nom;
        this.email = email;
        this.dateInscription = dateInscription;
    }

    public Membre(int idMembre, String nom, String email, LocalDate dateInscription) {
        this.idMembre = idMembre;
        this.nom = nom;
        this.email = email;
        this.dateInscription = dateInscription;
    }

    public int getIdMembre() {
        return idMembre;
    }

    public void setIdMembre(int idMembre) {
        this.idMembre = idMembre;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(LocalDate dateInscription) {
        this.dateInscription = dateInscription;
    }
    
    
    @Override
    public String toString() {
        return nom + " (" + email + ")";
    }
    
}
