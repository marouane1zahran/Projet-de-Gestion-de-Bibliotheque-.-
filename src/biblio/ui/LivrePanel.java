package biblio.ui;

import biblio.model.Livre;
import biblio.service.LivreService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class LivrePanel extends JPanel {

    // Service pour communiquer avec la base de données
    private final LivreService livreService = new LivreService();

    // Composants du formulaire
    private final JTextField txtTitre = new JTextField(15);
    private final JTextField txtAuteur = new JTextField(15);
    private final JTextField txtGenre = new JTextField(15);
    private final JTextField txtAnnee = new JTextField(10);

    // Composants du tableau
    private final JTable tableLivres;
    private final DefaultTableModel tableModel;

    public LivrePanel() {
        // Organisation générale du panneau (Haut, Centre, Bas)
        setLayout(new BorderLayout(10, 10));
       

        // 1. Création du Formulaire (En haut)
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

        // 2. Création des Boutons (Au centre)
        JPanel panelBoutons = new JPanel(new FlowLayout());
        JButton btnAjouter = new JButton("Ajouter");
        JButton btnModifier = new JButton("Modifier");
        JButton btnSupprimer = new JButton("Supprimer");
        JButton btnVider = new JButton("Vider les champs");

        panelBoutons.add(btnAjouter);
        panelBoutons.add(btnModifier);
        panelBoutons.add(btnSupprimer);
        panelBoutons.add(btnVider);

        // Regroupement du formulaire et des boutons au Nord
        JPanel panelNord = new JPanel(new BorderLayout());
        panelNord.add(panelFormulaire, BorderLayout.CENTER);
        panelNord.add(panelBoutons, BorderLayout.SOUTH);
        add(panelNord, BorderLayout.NORTH);

        // 3. Création du Tableau (Au centre/sud de l'écran)
        String[] colonnes = {"ID", "Titre", "Auteur", "Genre", "Année", "Disponible"};
        tableModel = new DefaultTableModel(colonnes, 0);
        tableLivres = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tableLivres);
        add(scrollPane, BorderLayout.CENTER);

        // 4. Charger les données initiales dans le tableau
        actualiserTableau();

        // 5. Action du bouton "Ajouter"
        btnAjouter.addActionListener(e -> ajouterLivre());
        
        // Action pour vider les champs
        btnVider.addActionListener(e -> viderChamps());
        btnModifier.addActionListener(e -> modifierLivre());
        btnSupprimer.addActionListener(e -> supprimerLivre());
        
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
        // On vide le tableau visuel
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
                actualiserTableau(); // On met à jour l'affichage
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
            return; // On arrête la méthode ici
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
}