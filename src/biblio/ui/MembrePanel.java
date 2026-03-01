
package biblio.ui;

import biblio.model.Membre;
import biblio.service.MembreService;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author speed
 */
public class MembrePanel extends JPanel {
    
    private final MembreService membreService = new MembreService() ;
    private JTextField txtNom = new JTextField(15);
    private JTextField txtEmail = new JTextField(15);
    private JTextField txtDateInscription = new JTextField(10); // Format YYYY-MM-DD
    
    private JTable tableMembres;
    private DefaultTableModel tableModel;
    
    public MembrePanel(){
       
        setLayout(new BorderLayout(10, 10));
        

       
        JPanel panelFormulaire = new JPanel(new GridLayout(4, 4, 5, 5));
        panelFormulaire.setBorder(BorderFactory.createTitledBorder("Informations du Membre"));

        panelFormulaire.add(new JLabel("Nom complet :"));
        panelFormulaire.add(txtNom);
        panelFormulaire.add(new JLabel("Email :"));
        panelFormulaire.add(txtEmail);
        panelFormulaire.add(new JLabel("Date Inscription (AAAA-MM-JJ) :"));
        panelFormulaire.add(txtDateInscription);
        
        txtDateInscription.setText(LocalDate.now().toString());
        txtEmail.setText("nom@gmail");
        
        JPanel panelBoutons = new JPanel(new FlowLayout());
        JButton btnAjouter = new JButton("Ajouter");
        JButton btnModifier = new JButton("Modifier");
        JButton btnSupprimer = new JButton("Supprimer");
        JButton btnVider = new JButton("Vider les champs");
        JButton btnChercher = new JButton("Chercher");
        btnChercher.setBackground(new java.awt.Color(255, 204, 0));
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
        
        String[] colonnes = {"ID", "Nom", "Email", "Date d'inscription"};
        tableModel = new DefaultTableModel(colonnes, 0);
        tableMembres = new JTable(tableModel);
        add(new JScrollPane(tableMembres), BorderLayout.CENTER);
        
        actualiserTableau();
 
        btnAjouter.addActionListener(e -> ajouterMembre());
        btnModifier.addActionListener(e -> modifierMembre());
        btnSupprimer.addActionListener(e -> supprimerMembre());
        btnVider.addActionListener(e -> viderChamps());
        btnChercher.addActionListener(e ->ChercherMembre());
        
        btnActualiser.addActionListener(e -> {
            
            txtEmail.setText("");
            txtDateInscription.setText("");
            txtNom.setText("");

    actualiserTableau(); 
    
});

        
        tableMembres.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tableMembres.getSelectedRow() != -1) {
                remplirChampsDepuisTableau();
            }
        });
    }

    private void actualiserTableau() {
        tableModel.setRowCount(0);
        List<Membre> membres = membreService.obtenirTousLesMembres();
        for (Membre m : membres) {
            Object[] ligne = {
                m.getIdMembre(), 
                m.getNom(), 
                m.getEmail(), 
                m.getDateInscription().toString()
            };
            tableModel.addRow(ligne);
        }
    }

    private void remplirChampsDepuisTableau() {
        int ligne = tableMembres.getSelectedRow();
        txtNom.setText(tableModel.getValueAt(ligne, 1).toString());
        txtEmail.setText(tableModel.getValueAt(ligne, 2).toString());
        txtDateInscription.setText(tableModel.getValueAt(ligne, 3).toString());
    }

    private void viderChamps() {
        txtNom.setText("");
        txtEmail.setText("");
        txtDateInscription.setText(LocalDate.now().toString()); 
        tableMembres.clearSelection(); 
    }

    private void ajouterMembre() {
        try {
            String nom = txtNom.getText();
            String email = txtEmail.getText();
            // Conversion du texte en LocalDate
            LocalDate dateInscription = LocalDate.parse(txtDateInscription.getText());

            Membre nouveauMembre = new Membre(nom, email, dateInscription);

            if (membreService.ajouterMembre(nouveauMembre)) {
                JOptionPane.showMessageDialog(this, "Membre ajouté avec succès !");
                actualiserTableau();
                viderChamps();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout. Vérifiez les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Format de date invalide. Utilisez AAAA-MM-JJ.", "Erreur de saisie", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void modifierMembre() {
        int ligne = tableMembres.getSelectedRow();
        if (ligne == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un membre à modifier.", "Avertissement", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int idMembre = Integer.parseInt(tableModel.getValueAt(ligne, 0).toString());
            String nom = txtNom.getText();
            String email = txtEmail.getText();
            LocalDate dateInscription = LocalDate.parse(txtDateInscription.getText());

            Membre membreModifie = new Membre(idMembre, nom, email, dateInscription);

            if (membreService.modifierMembre(membreModifie)) {
                JOptionPane.showMessageDialog(this, "Membre modifié avec succès !");
                actualiserTableau();
                viderChamps();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la modification.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Format de date invalide. Utilisez AAAA-MM-JJ.", "Erreur de saisie", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void supprimerMembre() {
        int ligne = tableMembres.getSelectedRow();
        if (ligne == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un membre à supprimer.", "Avertissement", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmation = JOptionPane.showConfirmDialog(this, 
                "Êtes-vous sûr de vouloir supprimer ce membre ?", 
                "Confirmation", 
                JOptionPane.YES_NO_OPTION);

        if (confirmation == JOptionPane.YES_OPTION) {
            try {
                int idMembre = Integer.parseInt(tableModel.getValueAt(ligne, 0).toString());
                if (membreService.supprimerMembre(idMembre)) {
                    JOptionPane.showMessageDialog(this, "Membre supprimé avec succès !");
                    actualiserTableau();
                    viderChamps();
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la suppression.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erreur inattendue : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void ChercherMembre() {
          String nom = txtNom.getText().trim();
            String email = txtEmail.getText().trim();
            String dateInscription = txtDateInscription.getText().trim();

            
            if (nom.isEmpty() || email.isEmpty() || dateInscription.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(this, 
                        "Veuillez remplir le Nom, l'Email et la Date d'inscription (ex: YYYY-MM-DD) pour chercher.", 
                        "Champs manquants", 
                        javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }

            
            List<Membre> resultats = membreService.rechercherMembresFormulaire(nom, email, dateInscription);
            
            
            tableModel.setRowCount(0); 
            
           
            for (Membre m : resultats) {
                Object[] ligne = {
                    m.getIdMembre(), 
                    m.getNom(), 
                    m.getEmail(), 
                    m.getDateInscription() 
                };
                tableModel.addRow(ligne);
            }
            
            if (resultats.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(this, "Aucun membre trouvé avec ces informations exactes.");
                actualiserTableau(); 
            }
        
    }
    
    
}
