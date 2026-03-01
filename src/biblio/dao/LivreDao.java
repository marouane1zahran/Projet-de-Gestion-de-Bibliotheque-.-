
package biblio.dao;

import biblio.model.Livre;
import biblio.util.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class LivreDao implements IDao<Livre> {
    
    private Connection con = Database.getConnection();

    @Override
    public boolean create(Livre livre) {
        
        String query = "INSERT INTO livre (titre, auteur, genre, annee_publication, disponible) VALUES (?, ?, ?, ?, ?)";
        try {
    
            
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, livre.getTitre());
            ps.setString(2, livre.getAuteur());
            ps.setString(3, livre.getGenre());
            ps.setInt(4, livre.getAnneePublication());
            ps.setBoolean(5, livre.isDisponible());
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException ex) {
            System.err.println("Erreur lors de l'ajout du livre : " + ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(Livre livre) {
        String query = "UPDATE livre SET titre = ?, auteur = ?, genre = ?, annee_publication = ?, disponible = ? WHERE id_livre = ?";
        try {
            
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, livre.getTitre());
            ps.setString(2, livre.getAuteur());
            ps.setString(3, livre.getGenre());
            ps.setInt(4, livre.getAnneePublication());
            ps.setBoolean(5, livre.isDisponible());
            ps.setInt(6, livre.getIdLivre());
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du livre : " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(Livre livre) {
     
        String query = "DELETE FROM livre WHERE id_livre = ?";
        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, livre.getIdLivre());
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du livre : " + e.getMessage());
            return false;
        }
    }

    @Override
    public Livre findById(int id) {
        
        String query = "SELECT * FROM livre WHERE id_livre = ?";
        Livre livre = null;
        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                livre = new Livre(
                    rs.getInt("id_livre"),
                    rs.getString("titre"),
                    rs.getString("auteur"),
                    rs.getString("genre"),
                    rs.getInt("annee_publication"),
                    rs.getBoolean("disponible")
                );
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche du livre : " + e.getMessage());
        }
        return livre;
    }

    @Override
    public List<Livre> findAll() {
        
        String query = "SELECT * FROM livre";
        List<Livre> listeLivres = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                listeLivres.add(new Livre(
                    rs.getInt("id_livre"),
                    rs.getString("titre"),
                    rs.getString("auteur"),
                    rs.getString("genre"),
                    rs.getInt("annee_publication"),
                    rs.getBoolean("disponible")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des livres : " + e.getMessage());
        }
        return listeLivres;
    }
    
    /**
     * Recherche multicritère stricte : tous les champs doivent correspondre.
     */
  public List<Livre> rechercherParCriteres(String titre, String auteur, String genre, int annee) {
        List<Livre> livresTrouves = new ArrayList<>();
        String sql = "SELECT * FROM livre WHERE titre = ? AND auteur = ? AND genre = ? AND annee_publication = ?";
        
        
        try (
             java.sql.PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, titre);
            ps.setString(2, auteur);
            ps.setString(3, genre);
            ps.setInt(4, annee);
            
            
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Livre livre = new Livre(
                        rs.getInt("id_Livre"), 
                        rs.getString("titre"),
                        rs.getString("auteur"),
                        rs.getString("genre"),
                        rs.getInt("annee_publication"), 
                        rs.getBoolean("disponible")
                    );
                    livresTrouves.add(livre);
                }
            }
        } catch (java.sql.SQLException e) {
            System.err.println("Erreur lors de la recherche multicritère : " + e.getMessage());
        }
       
        return livresTrouves;
        
       
    }
  
  
    public Map<String, Integer> compterLivresParGenre() {
        java.util.Map<String, Integer> statistiques = new java.util.HashMap<>();
        String sql = "SELECT genre, COUNT(*) as total FROM livre GROUP BY genre";

       
        try {
           
           Connection conn = Database.getConnection(); 
           PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                statistiques.put(rs.getString("genre"), rs.getInt("total"));
            }

           
            rs.close();
            ps.close();

        } catch (java.sql.SQLException e) {
            System.err.println("Erreur lors de la récupération des stats pour le graphique : " + e.getMessage());
        }

        return statistiques;
    }
    
    
    
    
}
