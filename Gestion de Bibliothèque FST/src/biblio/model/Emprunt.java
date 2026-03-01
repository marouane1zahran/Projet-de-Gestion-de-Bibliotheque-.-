
package biblio.model;

import java.time.LocalDate;

public class Emprunt {
    private int idEmprunt;
    private Livre livre;
    private Membre membre;
    private LocalDate dateEmprunt;
    private LocalDate dateRetour;
    
    public Emprunt() {}

    
    public Emprunt(Livre livre, Membre membre, LocalDate dateEmprunt, LocalDate dateRetour) {
        this.livre = livre;
        this.membre = membre;
        this.dateEmprunt = dateEmprunt;
        this.dateRetour = dateRetour;
    }

    
    public Emprunt(int idEmprunt, Livre livre, Membre membre, LocalDate dateEmprunt, LocalDate dateRetour) {
        this.idEmprunt = idEmprunt;
        this.livre = livre;
        this.membre = membre;
        this.dateEmprunt = dateEmprunt;
        this.dateRetour = dateRetour;
    }

    public int getIdEmprunt() {
        return idEmprunt;
    }

    public void setIdEmprunt(int idEmprunt) {
        this.idEmprunt = idEmprunt;
    }

    public Livre getLivre() {
        return livre;
    }

    public void setLivre(Livre livre) {
        this.livre = livre;
    }

    public Membre getMembre() {
        return membre;
    }

    public void setMembre(Membre membre) {
        this.membre = membre;
    }

    public LocalDate getDateEmprunt() {
        return dateEmprunt;
    }

    public void setDateEmprunt(LocalDate dateEmprunt) {
        this.dateEmprunt = dateEmprunt;
    }

    public LocalDate getDateRetour() {
        return dateRetour;
    }

    public void setDateRetour(LocalDate dateRetour) {
        this.dateRetour = dateRetour;
    }
    
    
    
}
