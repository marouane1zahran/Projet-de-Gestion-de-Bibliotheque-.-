package biblio.ui;

import biblio.ui.LivrePanel;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {
       
        setTitle("Gestion de Bibliothèque - FST");
        setSize(900, 600); // Une taille confortable pour afficher les tableaux
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Ferme l'application quand on clique sur la croix
        setLocationRelativeTo(null); // Centre la fenêtre sur l'écran

        
        JTabbedPane tabbedPane = new JTabbedPane();

        
        LivrePanel panelLivres = new LivrePanel();
        MembrePanel panelMembres = new MembrePanel();
        EmpruntPanel panelEmprunts = new EmpruntPanel(panelLivres);

        // 4. Ajout des panneaux aux onglets
        tabbedPane.addTab("📚 Livres", panelLivres);
        tabbedPane.addTab("👥 Membres", panelMembres);
        tabbedPane.addTab("🔄 Emprunts", panelEmprunts);

        // 5. Ajout du gestionnaire d'onglets à la fenêtre principale
        add(tabbedPane, BorderLayout.CENTER);
    }

    /**
     * Méthode temporaire pour créer des panneaux de remplissage.
     */
    private JPanel createPlaceholderPanel(String text) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        label.setForeground(Color.GRAY);
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    // Méthode main pour lancer l'interface graphique
    public static void main(String[] args) {
        // Bonne pratique Swing : Lancer l'interface dans le thread de distribution d'événements
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true); // Rend la fenêtre visible
        });
    }
}