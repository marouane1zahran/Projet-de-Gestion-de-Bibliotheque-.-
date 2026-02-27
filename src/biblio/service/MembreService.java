
package biblio.service;



import biblio.dao.MembreDao;
import biblio.model.Membre;

import java.time.LocalDate;
import java.util.List;

public class MembreService {

    private MembreDao membreDao = new MembreDao();

    public boolean ajouterMembre(Membre membre) {
        if (membre.getNom() == null || membre.getNom().trim().isEmpty()) {
            System.err.println("Erreur : Le nom du membre est obligatoire.");
            return false;
        }
        if (membre.getEmail() == null || !membre.getEmail().contains("@")) {
            System.err.println("Erreur : L'email est invalide.");
            return false;
        }
        
        // Règle métier : Forcer la date d'inscription à aujourd'hui si elle n'est pas fournie
        if (membre.getDateInscription() == null) {
            membre.setDateInscription(LocalDate.now());
        }

        return membreDao.create(membre);
    }

    public boolean modifierMembre(Membre membre) {
        return membreDao.update(membre);
    }

    public boolean supprimerMembre(int idMembre) {
        Membre membre = membreDao.findById(idMembre);
        if (membre != null) {
            return membreDao.delete(membre);
        }
        return false;
    }

    public List<Membre> obtenirTousLesMembres() {
        return membreDao.findAll();
    }
}