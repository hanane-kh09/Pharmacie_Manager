# 🏥 Gestion de Pharmacie

Application de gestion pour pharmacie développée en Java avec Swing et MySQL.

---

## À propos du projet

Ce projet a été réalisé dans le cadre du module Java Avancé. L'objectif était de créer une application desktop pour faciliter la gestion quotidienne d'une pharmacie.

### Problématique

Les pharmaciens ont souvent du mal à :
- Suivre les stocks en temps réel
- Enregistrer rapidement les ventes
- Avoir une vue d'ensemble sur les ventes par catégorie

Cette application apporte une solution simple et pratique pour gérer tout ça.

---

## Fonctionnalités

### Gestion des Médicaments
- Ajouter, modifier et supprimer des médicaments
- Rechercher par nom ou famille
- Alertes visuelles quand le stock est faible (moins de 10 unités)

### Gestion des Fournisseurs
- Gérer la liste des fournisseurs
- Associer les médicaments à leurs fournisseurs

### Gestion des Ventes
- Enregistrer une vente avec sélection de date
- Mise à jour automatique du stock
- Calcul automatique du prix total

### Statistiques
- Graphique des ventes par famille de médicaments
- Visualisation en temps réel

---

## Architecture

Le projet utilise l'architecture MVC (Modèle-Vue-Contrôleur) avec une couche DAO pour la base de données.

```
src/
├── com.pharmacie.model/      # Classes métier (Medicament, Vente, Fournisseur)
├── com.pharmacie.view/       # Interfaces graphiques Swing
├── com.pharmacie.dao/        # Accès aux données (requêtes SQL)
└── com.pharmacie.util/       # Utilitaires (connexion BDD)
```

---

## Base de Données

### Tables

**T_Fournisseur**
- id (INT, clé primaire)
- nom (VARCHAR)
- ville (VARCHAR)
- contact (VARCHAR)

**T_Medicament**
- id (INT, clé primaire)
- nom (VARCHAR)
- famille (VARCHAR)
- prix (DOUBLE)
- stock (INT)
- id_fournisseur (INT, clé étrangère)

**T_Vente**
- id (INT, clé primaire)
- id_medicament (INT, clé étrangère)
- date_vente (TIMESTAMP)
- quantite (INT)
- prix_total (DOUBLE)

Le script SQL complet est disponible dans `docs/schema.sql`.

---

## Technologies utilisées

- **Java SE 8+** - Langage de programmation
- **Swing** - Interface graphique
- **JDBC** - Connexion à la base de données
- **MySQL 5.7+** - Système de gestion de base de données
- **NetBeans 12+** - Environnement de développement

---

## Installation

### Méthode 1 : Installation automatique (recommandé)

Double-cliquez sur le fichier `INSTALL_APP.bat` à la racine du projet. L'assistant vous guidera pour :
1. Configurer la connexion MySQL
2. Créer la base de données
3. Lancer l'application

### Méthode 2 : Installation manuelle avec NetBeans

1. Ouvrir NetBeans
2. Aller dans `File` > `Open Project`
3. Sélectionner le dossier du projet
4. Clic droit sur le projet > `Properties` > `Libraries`
5. Ajouter le fichier `libs/mysql-connector-j-9.5.0.jar`
6. Configurer la base de données :
   - Créer une base MySQL nommée `pharmacie_db`
   - Exécuter le script `docs/schema.sql`
   - Modifier le fichier `db.properties` avec vos identifiants MySQL
7. Lancer le projet avec F6

### Configuration de la base de données

Éditez le fichier `db.properties` :
```properties
db.url=jdbc:mysql://localhost:3306/pharmacie_db
db.user=root
db.password=votre_mot_de_passe
```

---

## Utilisation

### Premier lancement

Au premier lancement, vous pouvez utiliser `SetupFrame` pour initialiser la base de données avec quelques données de test.

### Navigation

L'application contient 4 onglets :
- **Médicaments** : Gérer le catalogue
- **Fournisseurs** : Gérer les fournisseurs
- **Ventes** : Enregistrer les ventes
- **Statistiques** : Voir les graphiques

### Astuces

- Les lignes rouges dans le tableau des médicaments indiquent un stock faible
- Une alerte s'affiche au démarrage s'il y a des médicaments en rupture
- Le sélecteur de date dans les ventes permet de choisir facilement la date

---

## Auteur

Projet réalisé dans le cadre du module Java Avancé.
