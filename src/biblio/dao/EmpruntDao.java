
package biblio.dao;

import biblio.model.Emprunt;
import biblio.model.Livre;
import biblio.model.Membre;
import biblio.util.Database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmpruntDao implements IDao<Emprunt> {

    private Connection connection = Database.getConnection();

    @Override
    public boolean create(Emprunt emprunt) {
        String query = "INSERT INTO emprunt (id_livre, id_membre, date_emprunt, date_retour) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, emprunt.getLivre().getIdLivre()); 
            ps.setInt(2, emprunt.getMembre().getIdMembre());
            ps.setDate(3, Date.valueOf(emprunt.getDateEmprunt()));
            
            // La date de retour peut être nulle lors de la création d'un emprunt
            if (emprunt.getDateRetour() != null) {
                ps.setDate(4, Date.valueOf(emprunt.getDateRetour()));
            } else {
                ps.setNull(4, java.sql.Types.DATE);
            }
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur création emprunt : " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(Emprunt emprunt) {
        String query = "UPDATE emprunt SET id_livre = ?, id_membre = ?, date_emprunt = ?, date_retour = ? WHERE id_emprunt = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, emprunt.getLivre().getIdLivre());
            ps.setInt(2, emprunt.getMembre().getIdMembre());
            ps.setDate(3, Date.valueOf(emprunt.getDateEmprunt()));
            
            if (emprunt.getDateRetour() != null) {
                ps.setDate(4, Date.valueOf(emprunt.getDateRetour()));
            } else {
                ps.setNull(4, java.sql.Types.DATE);
            }
            ps.setInt(5, emprunt.getIdEmprunt());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur mise à jour emprunt : " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(Emprunt emprunt) {
        String query = "DELETE FROM emprunt WHERE id_emprunt = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, emprunt.getIdEmprunt());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur suppression emprunt : " + e.getMessage());
            return false;
        }
    }

    @Override
    public Emprunt findById(int id) {
        // Requête avec JOIN pour récupérer toutes les infos en une seule fois
        String query = "SELECT e.*, l.*, m.* FROM emprunt e " +
                       "JOIN livre l ON e.id_livre = l.id_livre " +
                       "JOIN membre m ON e.id_membre = m.id_membre " +
                       "WHERE e.id_emprunt = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return extractEmpruntFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erreur recherche emprunt : " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Emprunt> findAll() {
        String query = "SELECT e.*, l.*, m.* FROM emprunt e " +
                       "JOIN livre l ON e.id_livre = l.id_livre " +
                       "JOIN membre m ON e.id_membre = m.id_membre";
        List<Emprunt> emprunts = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                emprunts.add(extractEmpruntFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur récupération emprunts : " + e.getMessage());
        }
        return emprunts;
    }

    // Méthode utilitaire pour éviter la duplication de code entre findById et findAll
    private Emprunt extractEmpruntFromResultSet(ResultSet rs) throws SQLException {
        // 1. Reconstruire le Livre
        Livre livre = new Livre(
            rs.getInt("l.id_livre"),
            rs.getString("l.titre"),
            rs.getString("l.auteur"),
            rs.getString("l.genre"),
            rs.getInt("l.annee_publication"),
            rs.getBoolean("l.disponible")
        );

        // 2. Reconstruire le Membre
        Membre membre = new Membre(
            rs.getInt("m.id_membre"),
            rs.getString("m.nom"),
            rs.getString("m.email"),
            rs.getDate("m.date_inscription").toLocalDate()
        );

        // 3. Gérer la date de retour potentiellement nulle
        LocalDate dateRetour = null;
        if (rs.getDate("e.date_retour") != null) {
            dateRetour = rs.getDate("e.date_retour").toLocalDate();
        }

        // 4. Retourner l'Emprunt complet
        return new Emprunt(
            rs.getInt("e.id_emprunt"),
            livre,
            membre,
            rs.getDate("e.date_emprunt").toLocalDate(),
            dateRetour
        );
    }
}