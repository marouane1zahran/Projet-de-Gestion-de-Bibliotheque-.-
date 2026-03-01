
package biblio.dao;


import biblio.model.Membre;
import biblio.util.Database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MembreDao implements IDao<Membre> {

    private Connection connection = Database.getConnection();

    @Override
    public boolean create(Membre membre) {
        String query = "INSERT INTO membre (nom, email, date_inscription) VALUES (?, ?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, membre.getNom());
            ps.setString(2, membre.getEmail());
            //LocalDate -> java.sql.Date
            ps.setDate(3, Date.valueOf(membre.getDateInscription()));
            
            return ps.executeUpdate() > 0;
            
           
            
            
             
        } catch (SQLException e) {
            System.err.println("Erreur création membre : " + e.getMessage());
            return false;
        }
       
    }

    @Override
    public boolean update(Membre membre) {
        String query = "UPDATE membre SET nom = ?, email = ?, date_inscription = ? WHERE id_membre = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, membre.getNom());
            ps.setString(2, membre.getEmail());
            ps.setDate(3, Date.valueOf(membre.getDateInscription()));
            ps.setInt(4, membre.getIdMembre());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur mise à jour membre : " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(Membre membre) {
        String query = "DELETE FROM membre WHERE id_membre = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, membre.getIdMembre());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur suppression membre : " + e.getMessage());
            return false;
        }
    }

    @Override
    public Membre findById(int id) {
        String query = "SELECT * FROM membre WHERE id_membre = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return new Membre(
                    rs.getInt("id_membre"),
                    rs.getString("nom"),
                    rs.getString("email"),
                    //java.sql.Date -> LocalDate
                    rs.getDate("date_inscription").toLocalDate()
                );
            }
        } catch (SQLException e) {
            System.err.println("Erreur recherche membre : " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Membre> findAll() {
        String query = "SELECT * FROM membre";
        List<Membre> membres = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                membres.add(new Membre(
                    rs.getInt("id_membre"),
                    rs.getString("nom"),
                    rs.getString("email"),
                    rs.getDate("date_inscription").toLocalDate()
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erreur récupération membres : " + e.getMessage());
        }
        return membres;
    }
   
    public List<Membre> rechercherParCriteresSecurise(String nom, String email, String dateInscription) {
        List<Membre> membresTrouves = new ArrayList<>();
        
        String sql = "SELECT * FROM membre WHERE nom = ? AND email = ? AND date_inscription = ?";
        
        
        try {
            
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, nom);
            ps.setString(2, email);
            ps.setString(3, dateInscription); 
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Membre membre = new Membre(
                        rs.getInt("id_Membre"), 
                        rs.getString("nom"),
                        rs.getString("email"),                     
                        rs.getDate("date_inscription").toLocalDate()
                    );
                    membresTrouves.add(membre);
                }
            }
        } catch (java.sql.SQLException e) {
            System.err.println("Erreur dans la recherche membre : " + e.getMessage());
        }
        
        return membresTrouves;
    }
}