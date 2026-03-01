package biblio.ui;

import biblio.model.Emprunt;
import biblio.model.Livre;
import biblio.model.Membre;
import biblio.service.EmpruntService;
import biblio.service.LivreService;
import biblio.service.MembreService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import javax.swing.table.TableRowSorter;

public class EmpruntPanel extends JPanel {

    // Nos 3 services pour tout gérer
    private EmpruntService empruntService = new EmpruntService();
    private LivreService livreService = new LivreService();
    private MembreService membreService = new MembreService();
    private LivrePanel livrePanel;

    
    private JComboBox<Membre> cbMembres = new JComboBox<>();
    private JComboBox<Livre> cbLivres = new JComboBox<>();
    private JTextField txtRechercheRapide = new javax.swing.JTextField(20);
    private  TableRowSorter<javax.swing.table.DefaultTableModel> sorter;
    // Composants du tableau
    private JTable tableEmprunts;
    private DefaultTableModel tableModel;

    
    
    

   public EmpruntPanel(LivrePanel livrePanel) {
        this.livrePanel = livrePanel;
        setLayout(new BorderLayout(10, 10));
        

        // 1. Création du Formulaire (En haut)
        JPanel panelFormulaire = new JPanel(new GridLayout(1, 2, 10, 10));
        panelFormulaire.setBorder(BorderFactory.createTitledBorder("Nouvel Emprunt"));
        panelFormulaire.setPreferredSize(new Dimension(0, 80));

        panelFormulaire.add(new JLabel("Sélectionner un Membre :", SwingConstants.RIGHT));
        panelFormulaire.add(cbMembres);
        panelFormulaire.add(new JLabel("Sélectionner un Livre :", SwingConstants.RIGHT));
        panelFormulaire.add(cbLivres);

        // 2. Création des Boutons
        JPanel panelBoutons = new JPanel(new FlowLayout());
        JButton btnEmprunter = new JButton("Enregistrer l'emprunt");
        JButton btnRetourner = new JButton("Enregistrer un retour");
        JButton btnActualiser = new JButton("Actualiser les listes"); 
        

        // On met en valeur les boutons principaux
        btnEmprunter.setBackground(new Color(46, 204, 113)); // Vert
        btnEmprunter.setForeground(Color.BLACK);
        btnRetourner.setBackground(new Color(52, 152, 219)); // Bleu
        btnRetourner.setForeground(Color.BLACK);

        panelBoutons.add(btnEmprunter);
        panelBoutons.add(btnRetourner);
        panelBoutons.add(btnActualiser);

        // Assemblage Nord
        JPanel panelNord = new JPanel(new BorderLayout());
        panelNord.add(panelFormulaire, BorderLayout.CENTER);
        panelNord.add(panelBoutons, BorderLayout.SOUTH);
       panelNord.setPreferredSize(new Dimension(0, 120)); 

        add(panelNord, BorderLayout.NORTH);

        // 3. Création du Tableau (Au centre)
        String[] colonnes = {"ID Emprunt", "Membre", "Livre", "Date Emprunt", "Date Retour", "Statut"};
        tableModel = new javax.swing.table.DefaultTableModel(colonnes, 0);
        tableEmprunts = new javax.swing.JTable(tableModel);
        
        
        sorter = new javax.swing.table.TableRowSorter<>(tableModel);
        tableEmprunts.setRowSorter(sorter);

        
        javax.swing.JPanel panelRecherche = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        panelRecherche.add(new javax.swing.JLabel("🔍 Recherche rapide (Membre, Livre, Statut) : "));
        panelRecherche.add(txtRechercheRapide);

        
        javax.swing.JPanel panelCentre = new javax.swing.JPanel(new java.awt.BorderLayout());
        panelCentre.add(panelRecherche, java.awt.BorderLayout.NORTH);
        panelCentre.add(new javax.swing.JScrollPane(tableEmprunts), java.awt.BorderLayout.CENTER);

       
        add(panelCentre, java.awt.BorderLayout.CENTER);

        // 4. Initialisation des données
        actualiserListesDeroulantes();
        actualiserTableau();

        // 5. Écouteurs d'événements
        btnEmprunter.addActionListener(e -> enregistrerEmprunt());
        btnRetourner.addActionListener(e -> enregistrerRetour());
        btnActualiser.addActionListener(e -> {
            actualiserListesDeroulantes();
            actualiserTableau();
           if (this.livrePanel != null) {
                this.livrePanel.actualiserTableau();
            }
        });
        
        
        txtRechercheRapide.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filtrer(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filtrer(); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filtrer(); }
            
            private void filtrer() {
                String texte = txtRechercheRapide.getText();
                if (texte.trim().length() == 0) {
                    sorter.setRowFilter(null); 
                } else {
                   
                    sorter.setRowFilter(javax.swing.RowFilter.regexFilter("(?i)" + texte)); 
                }
            }
        });
    }

    // --- MÉTHODES D'AFFICHAGE ---

    /**
     * Charge les membres et UNIQUEMENT les livres disponibles dans les JComboBox
     */
    private void actualiserListesDeroulantes() {
        cbMembres.removeAllItems();
        cbLivres.removeAllItems();

        // Remplir les membres
        List<Membre> membres = membreService.obtenirTousLesMembres();
        for (Membre m : membres) {
            cbMembres.addItem(m);
        }

        // Remplir les livres (On ne garde que ceux qui sont disponibles !)
        List<Livre> livres = livreService.obtenirTousLesLivres();
        for (Livre l : livres) {
            if (l.isDisponible()) {
                cbLivres.addItem(l);
            }
        }
    }

    private void actualiserTableau() {
        tableModel.setRowCount(0);
        List<Emprunt> emprunts = empruntService.obtenirTousLesEmprunts();

        for (Emprunt e : emprunts) {
            String statut = (e.getDateRetour() == null) ? "En cours 🔴" : "Rendu ✔";
            String dateRetourAffichee = (e.getDateRetour() == null) ? "-" : e.getDateRetour().toString();

            Object[] ligne = {
                e.getIdEmprunt(),
                e.getMembre().getNom(),
                e.getLivre().getTitre(),
                e.getDateEmprunt().toString(),
                dateRetourAffichee,
                statut
            };
            tableModel.addRow(ligne);
        }
    }

    // --- MÉTHODES MÉTIERS ---

    private void enregistrerEmprunt() {
        // On récupère les objets directement depuis la JComboBox
        Membre membreSelectionne = (Membre) cbMembres.getSelectedItem();
        Livre livreSelectionne = (Livre) cbLivres.getSelectedItem();

        if (membreSelectionne == null || livreSelectionne == null) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un membre et un livre disponible.", "Avertissement", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // On appelle la logique métier de notre service
        boolean succes = empruntService.emprunterLivre(livreSelectionne.getIdLivre(), membreSelectionne.getIdMembre());

        if (succes) {
            JOptionPane.showMessageDialog(this, "Emprunt enregistré avec succès !");
            actualiserListesDeroulantes(); 
            actualiserTableau();
        } else {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement de l'emprunt.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void enregistrerRetour() {
        int ligneSelectionnee = tableEmprunts.getSelectedRow();

        if (ligneSelectionnee == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un emprunt dans le tableau pour le retourner.", "Avertissement", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Récupérer l'ID de l'emprunt 
        int idEmprunt = Integer.parseInt(tableModel.getValueAt(ligneSelectionnee, 0).toString());

        // On vérifie visuellement si le livre n'est pas déjà rendu 
        String statut = tableModel.getValueAt(ligneSelectionnee, 5).toString();
        if (statut.contains("Rendu")) {
            JOptionPane.showMessageDialog(this, "Ce livre a déjà été retourné !", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Demander confirmation
        int confirmation = JOptionPane.showConfirmDialog(this, "Confirmez-vous le retour de ce livre ?", "Confirmation de retour", JOptionPane.YES_NO_OPTION);
        
        if (confirmation == JOptionPane.YES_OPTION) {
            boolean succes = empruntService.retournerLivre(idEmprunt);

            if (succes) {
                JOptionPane.showMessageDialog(this, "Retour enregistré avec succès. Le livre est de nouveau disponible !");
                actualiserListesDeroulantes(); // Remet le livre dans la liste des disponibles
                actualiserTableau();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement du retour.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}