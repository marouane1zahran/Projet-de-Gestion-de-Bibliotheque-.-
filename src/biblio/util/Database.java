
package biblio.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {
    
    private static final String url = "jdbc:mysql://localhost:3306/gestion_bibliotheque";
    private static final String user = "root";
    private static final String password = "";
    private static  Connection connection = null ;

    public Database() {
    }
    
    public static Connection getConnection(){
        
        try {
            
            if (connection == null || connection.isClosed()) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connection established successfully!");
        }
    } catch (ClassNotFoundException | SQLException e) {
                System.err.println("Erreur lors de la connexion à la base de données : " + e.getMessage());
            }
        return connection ;
    }
    
    
    
    
}
