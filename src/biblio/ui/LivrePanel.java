package biblio.ui;

import biblio.model.Livre;
import biblio.service.LivreService;
import biblio.util.Database;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.util.List;
import javax.swing.table.TableRowSorter;

public class LivrePanel extends JPanel {

    
    private final LivreService livreService = new LivreService();

   
    private final JTextField txtTitre = new JTextField(15);
    private final JTextField txtAuteur = new JTextField(15);
    private final JTextField txtGenre = new JTextField(15);
    private final JTextField txtAnnee = new JTextField(10);
    private JTextField txtRecherche = new JTextField(20);
    private TableRowSorter<DefaultTableModel> sorter;

   
    private final JTable tableLivres;
    private final DefaultTableModel tableModel;

    public LivrePanel() {
       
        setLayout(new BorderLayout(10, 10));
       

       
        JPanel panelFormulaire = new JPanel(new GridLayout(2, 4, 10, 10));
        panelFormulaire.setBorder(BorderFactory.createTitledBorder("Informations du Livre"));
        
        panelFormulaire.add(new JLabel("Titre :"));
        panelFormulaire.add(txtTitre);
        panelFormulaire.add(new JLabel("Auteur :"));
        panelFormulaire.add(txtAuteur);
        panelFormulaire.add(new JLabel("Genre :"));
        panelFormulaire.add(txtGenre);
        panelFormulaire.add(new JLabel("Année :"));
        panelFormulaire.add(txtAnnee);

      
        JPanel panelBoutons = new JPanel(new FlowLayout());
        JButton btnAjouter = new JButton("Ajouter");
        JButton btnModifier = new JButton("Modifier");
        JButton btnSupprimer = new JButton("Supprimer");
        JButton btnVider = new JButton("Vider les champs");
        JButton btnChercher = new JButton("Chercher");
        btnChercher.setBackground(new Color(255, 204, 0));
        JButton btnActualiser = new JButton("Actualiser");

        panelBoutons.add(btnAjouter);
        panelBoutons.add(btnModifier);
        panelBoutons.add(btnSupprimer);
        panelBoutons.add(btnVider);
        panelBoutons.add(btnChercher);
        panelBoutons.add(btnActualiser);

        
        JPanel panelNord = new JPanel(new BorderLayout());
        panelNord.add(panelFormulaire, BorderLayout.CENTER);
        panelNord.add(panelBoutons, BorderLayout.SOUTH);
        add(panelNord, BorderLayout.NORTH);

       
       String[] colonnes = {"ID", "Titre", "Auteur", "Genre", "Année", "Disponible"};
        tableModel = new DefaultTableModel(colonnes, 0);
        tableLivres = new JTable(tableModel);
        
        
        sorter = new TableRowSorter<>(tableModel);
        tableLivres.setRowSorter(sorter);

        
        JPanel panelRecherche = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelRecherche.add(new JLabel("🔍 Rechercher (Titre, Auteur, etc.) : "));
        panelRecherche.add(txtRecherche);

        
        JPanel panelCentre = new JPanel(new BorderLayout());
        panelCentre.add(panelRecherche, BorderLayout.NORTH);
        panelCentre.add(new JScrollPane(tableLivres), BorderLayout.CENTER);
        
        
        add(panelCentre, BorderLayout.CENTER);

        
       

       
        btnAjouter.addActionListener(e -> ajouterLivre());
        
        
        btnVider.addActionListener(e -> viderChamps());
        btnModifier.addActionListener(e -> modifierLivre());
        btnSupprimer.addActionListener(e -> supprimerLivre());
        btnChercher.addActionListener(e -> rechercherLivresFormulaire());
        btnActualiser.addActionListener(e -> {
    txtTitre.setText("");
    txtAuteur.setText("");
    txtGenre.setText("");
    txtAnnee.setText("");
    
    actualiserTableau();
});
      
        
         actualiserTableau();
         
        txtRecherche.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filtrerTableau(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filtrerTableau(); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filtrerTableau(); }
            
            private void filtrerTableau() {
                String texte = txtRecherche.getText();
                if (texte.trim().length() == 0) {
                    sorter.setRowFilter(null); 
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texte)); 
                }
            }
        });
        
        tableLivres.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tableLivres.getSelectedRow() != -1) {
                remplirChampsDepuisTableau();
            }
        });
        
    }

    /**
     * Méthode pour récupérer les livres depuis la BDD et remplir le JTable
     */
    public void actualiserTableau() {
      
        tableModel.setRowCount(0); 
        
        
        List<Livre> livres = livreService.obtenirTousLesLivres();
        
       
        for (Livre l : livres) {
            Object[] ligne = {
                l.getIdLivre(), 
                l.getTitre(), 
                l.getAuteur(), 
                l.getGenre(), 
                l.getAnneePublication(), 
                l.isDisponible() ? "Oui" : "Non"
            };
            tableModel.addRow(ligne);
        }
    }

    /**
     * Méthode exécutée au clic sur "Ajouter"
     */
    private void ajouterLivre() {
        try {
            String titre = txtTitre.getText();
            String auteur = txtAuteur.getText();
            String genre = txtGenre.getText();
            int annee = Integer.parseInt(txtAnnee.getText());

            Livre nouveauLivre = new Livre(titre, auteur, genre, annee, true);
            
            if (livreService.ajouterLivre(nouveauLivre)) {
                JOptionPane.showMessageDialog(this, "Livre ajouté avec succès !");
                actualiserTableau(); 
                viderChamps();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Veuillez saisir une année valide (nombre).", "Erreur de saisie", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void viderChamps() {
        txtTitre.setText("");
        txtAuteur.setText("");
        txtGenre.setText("");
        txtAnnee.setText("");
    }
    
    private void remplirChampsDepuisTableau() {
        int ligneSelectionnee = tableLivres.getSelectedRow();
        
       
        txtTitre.setText(tableModel.getValueAt(ligneSelectionnee, 1).toString());
        txtAuteur.setText(tableModel.getValueAt(ligneSelectionnee, 2).toString());
        txtGenre.setText(tableModel.getValueAt(ligneSelectionnee, 3).toString());
        txtAnnee.setText(tableModel.getValueAt(ligneSelectionnee, 4).toString());
    }

   private void modifierLivre() {
        int ligneSelectionnee = tableLivres.getSelectedRow();
        
        if (ligneSelectionnee == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un livre à modifier dans le tableau.", "Avertissement", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            
            int idLivre = (int) tableModel.getValueAt(ligneSelectionnee, 0);
            
            
            String titre = txtTitre.getText();
            String auteur = txtAuteur.getText();
            String genre = txtGenre.getText();
            int annee = Integer.parseInt(txtAnnee.getText());

            
            boolean estDisponible = tableModel.getValueAt(ligneSelectionnee, 5).toString().equals("Oui");
            Livre livreModifie = new Livre(idLivre, titre, auteur, genre, annee, estDisponible);
            
           
            if (livreService.modifierLivre(livreModifie)) {
                JOptionPane.showMessageDialog(this, "Livre modifié avec succès !");
                actualiserTableau();
                viderChamps();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la modification.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Veuillez saisir une année valide.", "Erreur", JOptionPane.WARNING_MESSAGE);
        }
    }
    


    private void supprimerLivre() {
       
        int ligneSelectionnee = tableLivres.getSelectedRow();

        
        if (ligneSelectionnee == -1) {
            JOptionPane.showMessageDialog(this, 
                    "Veuillez sélectionner un livre dans le tableau avant de le supprimer.", 
                    "Aucune sélection", 
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

       
        int confirmation = JOptionPane.showConfirmDialog(this, 
                "Êtes-vous sûr de vouloir supprimer ce livre définitivement ?", 
                "Confirmation de suppression", 
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

       
        if (confirmation == JOptionPane.YES_OPTION) {
            try {
               
                int idLivre = Integer.parseInt(tableModel.getValueAt(ligneSelectionnee, 0).toString());

                
                boolean suppressionReussie = livreService.supprimerLivre(idLivre);

               
                if (suppressionReussie) {
                    JOptionPane.showMessageDialog(this, 
                            "Le livre a été supprimé avec succès !", 
                            "Succès", 
                            JOptionPane.INFORMATION_MESSAGE);
                    
                    
                    actualiserTableau(); 
                   
                    viderChamps(); 
                } else {
                    JOptionPane.showMessageDialog(this, 
                            "Erreur : Impossible de supprimer ce livre dans la base de données.", 
                            "Erreur de suppression", 
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                
                JOptionPane.showMessageDialog(this, 
                        "Une erreur inattendue est survenue : " + e.getMessage(), 
                        "Erreur système", 
                        JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private void rechercherLivresFormulaire() {
        String titre = txtTitre.getText().trim();
            String auteur = txtAuteur.getText().trim();
            String genre = txtGenre.getText().trim();
            String anneeStr = txtAnnee.getText().trim();

            if (titre.isEmpty() || auteur.isEmpty() || genre.isEmpty() || anneeStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                        "Veuillez remplir tous les champs du formulaire (Titre, Auteur, Genre, Année) pour lancer la recherche.", 
                        "Champs manquants", 
                        JOptionPane.WARNING_MESSAGE);
                return; 
            }

            try {
                
                int annee = Integer.parseInt(anneeStr);
                
                
                List<Livre> resultats = livreService.rechercherLivresFormulaire(titre, auteur, genre, annee);
                
                
                tableModel.setRowCount(0);
                
                for (Livre l : resultats) {
                    Object[] ligne = {
                        l.getIdLivre(), 
                        l.getTitre(), 
                        l.getAuteur(), 
                        l.getGenre(), 
                        l.getAnneePublication(), 
                        l.isDisponible() ? "Oui" : "Non"
                    };
                    tableModel.addRow(ligne);
                }
                
               
                if (resultats.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Aucun livre ne correspond exactement à ces critères dans la base de données.");
                    actualiserTableau(); 
                }
               
                
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "L'année doit être un nombre valide (ex: 2024).", "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
            }
    }
    
    
}