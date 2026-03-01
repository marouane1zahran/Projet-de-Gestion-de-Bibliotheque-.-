package biblio.service;

import biblio.dao.EmpruntDao;
import biblio.dao.LivreDao;
import biblio.dao.MembreDao;
import biblio.model.Emprunt;
import biblio.model.Livre;
import biblio.model.Membre;

import java.time.LocalDate;
import java.util.List;

public class EmpruntService {

    private final EmpruntDao empruntDao = new EmpruntDao();
    private final LivreDao livreDao = new LivreDao();
    private final MembreDao membreDao = new MembreDao();
    

 
    public boolean emprunterLivre(int idLivre, int idMembre) {
        Livre livre = livreDao.findById(idLivre);
        Membre membre = membreDao.findById(idMembre);

        // 1. Vérifications de base
        if (livre == null || membre == null) {
            System.err.println("Erreur : Livre ou Membre introuvable.");
            return false;
        }

        // 2. Le livre doit être disponible !
        if (!livre.isDisponible()) {
            System.err.println("Erreur : Le livre '" + livre.getTitre() + "' n'est pas disponible actuellement.");
            return false;
        }

        // 3. Création de l'emprunt (date de retour à null car il n'est pas encore rendu)
        Emprunt nouvelEmprunt = new Emprunt(livre, membre, LocalDate.now(), null);
        boolean empruntCree = empruntDao.create(nouvelEmprunt);

        // 4. Si l'emprunt est bien enregistré, on met à jour le statut du livre
        if (empruntCree) {
            livre.setDisponible(false);
            livreDao.update(livre);
            return true;
        }

        return false;
    }

    /**
     *  Retourner un livre
     */
    public boolean retournerLivre(int idEmprunt) {
        Emprunt emprunt = empruntDao.findById(idEmprunt);

        if (emprunt == null) {
            System.err.println("Erreur : Cet emprunt n'existe pas.");
            return false;
        }
        if (emprunt.getDateRetour() != null) {
            System.err.println("Erreur : Ce livre a déjà été retourné le " + emprunt.getDateRetour());
            return false;
        }

        // 1. Enregistrer la date de retour
        emprunt.setDateRetour(LocalDate.now());
        boolean empruntMisAJour = empruntDao.update(emprunt);

        // 2. Rendre le livre à nouveau disponible
        if (empruntMisAJour) {
            Livre livre = emprunt.getLivre();
            livre.setDisponible(true);
            return livreDao.update(livre);
        }

        return false;
    }

    public List<Emprunt> obtenirTousLesEmprunts() {
        return empruntDao.findAll();
    }
}