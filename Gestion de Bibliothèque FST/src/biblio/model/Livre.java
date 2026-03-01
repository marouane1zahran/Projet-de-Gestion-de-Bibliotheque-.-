
package biblio.model;


public class Livre {
    private int idLivre;
    private String titre;
    private String auteur;
    private String genre;
    private int anneePublication;
    private boolean disponible;

    public Livre() {
    }

    public Livre(int idLivre, String titre, String auteur, String genre, int anneePublication, boolean disponible) {
        this.idLivre = idLivre;
        this.titre = titre;
        this.auteur = auteur;
        this.genre = genre;
        this.anneePublication = anneePublication;
        this.disponible = disponible;
    }

    public Livre(String titre, String auteur, String genre, int anneePublication, boolean disponible) {
        this.titre = titre;
        this.auteur = auteur;
        this.genre = genre;
        this.anneePublication = anneePublication;
        this.disponible = disponible;
    }

    public int getIdLivre() {
        return idLivre;
    }

    public void setIdLivre(int idLivre) {
        this.idLivre = idLivre;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getAnneePublication() {
        return anneePublication;
    }

    public void setAnneePublication(int anneePublication) {
        this.anneePublication = anneePublication;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
    
    @Override
    public String toString() {
        return titre + " (" + auteur + ")"; 
    }
   
    
}
