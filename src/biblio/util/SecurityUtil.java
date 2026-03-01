package biblio.util; 

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecurityUtil {
    
    
    public static String hashPassword(String passwordToHash) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    
    public static String genererMotDePasseTemporaire() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#";
        StringBuilder mdp = new StringBuilder();
        java.util.Random rnd = new java.util.Random();
        while (mdp.length() < 8) { 
            int index = (int) (rnd.nextFloat() * caracteres.length());
            mdp.append(caracteres.charAt(index));
        }
        return mdp.toString();
    }
}