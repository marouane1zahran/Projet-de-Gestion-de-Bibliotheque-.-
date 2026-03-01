package biblio.ui; 
import biblio.dao.LivreDao; 
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class AccueilPanel extends JPanel {

    private LivreDao livreDao = new LivreDao();

    public AccueilPanel() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // 1. Titre de la page
        JLabel lblTitre = new JLabel("Tableau de bord - Statistiques de la Bibliothèque", SwingConstants.CENTER);
        lblTitre.setFont(new Font("Arial", Font.BOLD, 24));
        add(lblTitre, BorderLayout.NORTH);

        // 2. Création et ajout du graphique JFreeChart
        ChartPanel chartPanel = creerGraphiqueBarres();
        add(chartPanel, BorderLayout.CENTER);
    }

    /**
     * Génère le graphique en barres avec les données de la base
     */
    private ChartPanel creerGraphiqueBarres() {
        //  Création de l'ensemble de données (Dataset)
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        // On interroge la BDD avec notre nouvelle méthode propre
        Map<String, Integer> stats = livreDao.compterLivresParGenre();
        
        // On injecte les données dans le dataset
        for (Map.Entry<String, Integer> entry : stats.entrySet()) {
            dataset.setValue(entry.getValue(), "Nombre de livres", entry.getKey());
        }

        // B. Configuration visuelle du graphique
        JFreeChart barChart = ChartFactory.createBarChart(
                "Répartition des livres par Genre", // Titre du graphique
                "Genres littéraires",             // Titre de l'axe X
                "Quantité",                       // Titre de l'axe Y
                dataset,                          // Les données
                PlotOrientation.VERTICAL,         // Orientation
                false,                            // Légende (inutile ici car on n'a qu'une couleur)
                true,                             // Info-bulles au survol
                false                             // URLs
        );

        // Fond blanc pour faire plus propre
        barChart.setBackgroundPaint(Color.WHITE);
        barChart.getPlot().setBackgroundPaint(new Color(245, 245, 245));

        // . On retourne le panneau Swing qui contient le graphique
        return new ChartPanel(barChart);
    }
    
    /**
     * Cette méthode permet au Dashboard de forcer la mise à jour 
     * du graphique quand on clique sur l'onglet Accueil.
     */
    public void actualiserGraphique() {
        this.removeAll();
        
        JLabel lblTitre = new JLabel("Tableau de bord - Statistiques de la Bibliothèque", SwingConstants.CENTER);
        lblTitre.setFont(new Font("Arial", Font.BOLD, 24));
        add(lblTitre, BorderLayout.NORTH);
        
        add(creerGraphiqueBarres(), BorderLayout.CENTER); 
        
        this.revalidate();
        this.repaint();
    }
}