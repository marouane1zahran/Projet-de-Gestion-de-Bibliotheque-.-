package biblio.dao; 

import biblio.util.Database;
import biblio.util.SecurityUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginDao {

    
    public boolean verifierLogin(String email, String passwordClair) {
        boolean estValide = false;
        String sql = "SELECT * FROM utilisateur WHERE email = ? AND password_hash = ?";
        
       
        String passwordHache = SecurityUtil.hashPassword(passwordClair);

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, email);
            ps.setString(2, passwordHache);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    estValide = true; 
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur de login : " + e.getMessage());
        }
        return estValide;
    }
    /**
     * Réinitialise le mot de passe, le sauvegarde et l'envoie par e-mail.
     */
    public boolean reinitialiserEtEnvoyerMotDePasse(String email) {
        
        String sqlCheck = "SELECT * FROM utilisateur WHERE email = ?";
        boolean utilisateurExiste = false;

        try (Connection conn = Database.getConnection();
             PreparedStatement psCheck = conn.prepareStatement(sqlCheck)) 
        {
            
            
            psCheck.setString(1, email);
            try (java.sql.ResultSet rs = psCheck.executeQuery()) {
                if (rs.next()) {
                    utilisateurExiste = true;
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la vérification de l'email : " + e.getMessage());
            return false;
        }

       
        if (!utilisateurExiste) {
            return false; 
        }

        
        String nouveauMotDePasseClair = biblio.util.SecurityUtil.genererMotDePasseTemporaire();

       
        String motDePasseHache = biblio.util.SecurityUtil.hashPassword(nouveauMotDePasseClair);

       
        String sqlUpdate = "UPDATE utilisateur SET password_hash = ? WHERE email = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate)) {
            
            psUpdate.setString(1, motDePasseHache);
            psUpdate.setString(2, email);
            psUpdate.executeUpdate();
            
        } catch (Exception e) {
            System.err.println("Erreur lors de la mise à jour du mot de passe : " + e.getMessage());
            return false;
        }

        
        String sujet = "Réinitialisation de votre mot de passe - Bibliothèque";
        String message = "Bonjour,\n\n"
                + "Votre mot de passe a été réinitialisé avec succès.\n"
                + "Voici votre nouveau mot de passe temporaire : " + nouveauMotDePasseClair + "\n\n"
                + "Nous vous conseillons de vous connecter et de le modifier dès que possible.\n\n"
                + "Cordialement,\nL'équipe de la Bibliothèque.";

        return biblio.util.EmailService.envoyerEmail(email, sujet, message);
    }
}