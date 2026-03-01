# 📚 Système de Gestion de Bibliothèque (Library Management System)

<div align="center">
  <img width="2816" height="1536" alt="Gemini_Generated_Image_ibh2e4ibh2e4ibh2" src="https://github.com/user-attachments/assets/e9390744-283d-442d-b42b-91053c753adf" />

</div>

Une plateforme de bureau complète pour la gestion des livres, des membres et des emprunts, dotée d'un tableau de bord analytique.

---

## 📁 Table des matières

- [🗂 Contexte](#-contexte)
- [❓ Problématique](#-problématique)
- [🎯 Objectif](#-objectif)
- [📊 Diagrammes](#-diagrammes)
- [🗃 Tables de Données](#-tables-de-données)
- [✨ Fonctionnalités Principales](#-fonctionnalités-principales)
- [🔍 Requêtes SQL](#-requêtes-sql)
- [🏛 Architecture](#-architecture)
- [🛠 Technologies Utilisées](#-technologies-utilisées)
- [🎥 Démo Vidéo](#-démo-vidéo)
- [📦 Installation & Executable](#-installation--executable)

---

## 🗂 Contexte

Dans le cadre de la gestion des bibliothèques, il est primordial d'assurer un suivi rigoureux du flux des ouvrages et des adhésions. Les méthodes de gestion traditionnelles (registres papier ou tableurs simples) atteignent vite leurs limites, entraînant des pertes de livres, des retards non sanctionnés et une charge de travail administrative chronophage. La digitalisation de ce processus est essentielle pour garantir l'intégrité du fonds documentaire et fluidifier les interactions avec les lecteurs.

---

## ❓ Problématique

Les bibliothèques gérées manuellement ou avec des outils obsolètes font face à plusieurs défis majeurs :
- Pertes fréquentes d'ouvrages dues à un mauvais suivi des emprunts.
- Difficulté à identifier rapidement les membres ayant des retards de restitution.
- Manque de visibilité globale sur l'inventaire (quels genres sont les plus empruntés ?).
- Processus d'authentification et de sécurité souvent inexistants ou vulnérables.

---

## 🎯 Objectif

L'objectif de ce projet est de concevoir et développer une application de bureau robuste, ergonomique et sécurisée, permettant d'automatiser l'ensemble des processus de gestion d'une bibliothèque.

L'application doit :
- Garantir un suivi précis et en temps réel des emprunts et des retours.
- Centraliser la gestion de l'inventaire des livres et de la base des membres.
- Fournir des indicateurs visuels (statistiques) pour aider à la prise de décision.
- Assurer un niveau de sécurité professionnel (chiffrement des mots de passe, récupération par e-mail).

---

## 📊 Diagrammes

### Diagramme de Cas d'Utilisation
![UseCaseDiagram1](https://github.com/user-attachments/assets/fcebf786-8ba7-4340-b5a3-7d80f3ea86e8)



### Diagramme de Classes
![ClassDiagram1](https://github.com/user-attachments/assets/f1fb3268-a4ba-4187-9068-da4b95c645aa)



---

## 🗃 Tables de Données

- **Livre** (id, titre, auteur, genre, quantite)
- **Membre** (id, nom, prenom, email, telephone)
- **Emprunt** (id, livre_id, membre_id, date_emprunt, statut)
- **Utilisateur** (id, email, password_hash)

---

## ✨ Fonctionnalités Principales

### 1. Gestion du Fonds Documentaire (Livres)
- **Inventaire** : Ajouter, modifier ou supprimer des ouvrages.
- **Catégorisation** : Classement par genre (Science-Fiction, Roman, etc.).

### 2. Gestion des Adhérents (Membres)
- **Administration** : Inscription de nouveaux membres et mise à jour de leurs coordonnées.

### 3. Gestion des Emprunts
- **Suivi** : Assigner un livre à un membre avec date d'emprunt.
- **Restitution** : Mettre à jour le statut du livre lors de son retour pour ajuster le stock disponible.

### 4. Tableau de Bord Analytique (Dashboard)
- **Statistiques en temps réel** : Affichage graphique (Diagramme en barres) de la répartition des livres par genre littéraire.

### 5. Sécurité et Authentification
- **Espace Administrateur** : Connexion sécurisée avec hachage des mots de passe (SHA-256).
- **Récupération de mot de passe** : Génération d'un mot de passe temporaire automatisé et envoi via le protocole SMTP (JavaMail).

---

## 🔍 Requêtes SQL

### Création des tables de la base de données :

```sql
CREATE TABLE Livre (
    id INT NOT NULL AUTO_INCREMENT,
    titre VARCHAR(150) NOT NULL,
    auteur VARCHAR(100) NOT NULL,
    genre VARCHAR(50) NOT NULL,
    quantite INT DEFAULT 1,
    PRIMARY KEY (id)
);

CREATE TABLE Membre (
    id INT NOT NULL AUTO_INCREMENT,
    nom VARCHAR(50) NOT NULL,
    prenom VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    telephone VARCHAR(15),
    PRIMARY KEY (id)
);

CREATE TABLE Emprunt (
    id INT NOT NULL AUTO_INCREMENT,
    livre_id INT NOT NULL,
    membre_id INT NOT NULL,
    date_emprunt DATE DEFAULT CURRENT_DATE,
    statut VARCHAR(20) DEFAULT 'En cours',
    PRIMARY KEY (id),
    FOREIGN KEY (livre_id) REFERENCES Livre(id) ON DELETE CASCADE,
    FOREIGN KEY (membre_id) REFERENCES Membre(id) ON DELETE CASCADE
);

CREATE TABLE Utilisateur (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL
);
```

---

## 🏛 Architecture

Le projet respecte l'architecture logicielle **MVC (Modèle-Vue-Contrôleur)** couplée au design pattern **DAO (Data Access Object)** et **Singleton** (pour la connexion à la base de données), garantissant un code propre, modulaire et maintenable.

<img width="2304" height="1856" alt="architecture" src="https://github.com/user-attachments/assets/1e5b7513-3e20-40d9-902a-adfe6b06b605" />



---

## 🛠 Technologies Utilisées

- **Langage & Interface :** Java / Java Swing (UI)
- **Base de données :** MySQL (via phpMyAdmin ou XAMPP)
- **Accès aux données :** JDBC (Java Database Connectivity)
- **Bibliothèques externes :** - `JFreeChart` & `JCommon` (Génération des graphiques)
  - `JavaMail API` (Envoi d'e-mails SMTP)
- **Outils de développement :** NetBeans IDE, StarUML
- **Packaging :** Inno Setup (Génération de l'installateur Windows)

---

## 🎥 Démo Vidéo



https://github.com/user-attachments/assets/847b4a07-d4dc-4f74-b217-173c661f69f9


BASE DE DONNES :

https://github.com/user-attachments/assets/bf4a918f-2869-4e89-9bff-6d86f98a7d4b


---

## 📦 Installation & Executable

L'application a été empaquetée pour un déploiement facile sous Windows.

1. Importez la base de données `gestion_biblio.sql` dans votre serveur MySQL local (WAMP/XAMPP).
2. Téléchargez le fichier d'installation `Setup_Biblio.exe` disponible dans les **Releases** du dépôt GitHub.
3. Lancez l'installation et suivez les instructions.
4. Identifiants administrateur par défaut :
   - **Email :** admin@gmail.com
   - **Mot de passe :** admin123
