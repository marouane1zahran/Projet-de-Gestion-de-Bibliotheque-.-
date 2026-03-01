package biblio.service;

import biblio.dao.LivreDao;
import biblio.model.Livre;

import java.util.List;

public class LivreService {

    private LivreDao livreDao = new LivreDao();

    public boolean ajouterLivre(Livre livre) {
        
        if (livre.getTitre() == null || livre.getTitre().trim().isEmpty()) {
            System.err.println("Erreur : Le titre du livre est obligatoire.");
            return false;
        }
        if (livre.getAuteur() == null || livre.getAuteur().trim().isEmpty()) {
            System.err.println("Erreur : L'auteur du livre est obligatoire.");
            return false;
        }
        
        
        livre.setDisponible(true); 

        return livreDao.create(livre);
    }

    public boolean modifierLivre(Livre livre) {
        if (livre.getIdLivre() <= 0) {
            System.err.println("Erreur : ID du livre invalide.");
            return false;
        }
        return livreDao.update(livre);
    }

    public boolean supprimerLivre(int idLivre) {
        Livre livre = livreDao.findById(idLivre);
        if (livre != null) {
            return livreDao.delete(livre);
        }
        return false;
    }

    public List<Livre> obtenirTousLesLivres() {
        return livreDao.findAll();
    }
    
    public List<Livre> rechercherLivresFormulaire(String titre, String auteur, String genre, int annee) {
        return livreDao.rechercherParCriteres(titre, auteur, genre, annee);
    }
}