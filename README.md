# Gestion de Pharmacie

Application desktop de gestion de pharmacie développée en Java (Swing) avec une base de données MySQL.

---

## À propos du projet

Ce projet a été réalisé dans le cadre du module Java Avancé. Il vise à proposer une solution simple, efficace et ergonomique pour aider les pharmaciens dans la gestion quotidienne de leur activité.

### Problématique

Dans la pratique, les pharmacies rencontrent plusieurs difficultés :

* Suivi du stock en temps réel
* Enregistrement rapide et fiable des ventes
* Analyse des ventes par famille de médicaments

Cette application répond à ces besoins via une interface claire et des fonctionnalités automatisées.

---

## Fonctionnalités

### Gestion des Médicaments

* Ajouter, modifier et supprimer des médicaments
* Recherche par nom ou famille
* Alerte visuelle lorsque le stock est faible (moins de 10 unités)

### Gestion des Fournisseurs

* Gestion complète des fournisseurs
* Association des médicaments à leurs fournisseurs

### Gestion des Ventes

* Enregistrement des ventes avec sélection de la date
* Mise à jour automatique du stock
* Calcul automatique du prix total

### Statistiques

* Graphiques des ventes par famille de médicaments
* Visualisation dynamique et en temps réel

---

## Architecture du projet

Le projet est basé sur l’architecture MVC (Modèle – Vue – Contrôleur) avec une couche DAO pour la gestion de la base de données.

```
src/
├── com.pharmacie.model/      # Classes métier (Medicament, Vente, Fournisseur)
├── com.pharmacie.view/       # Interfaces graphiques Swing
├── com.pharmacie.dao/        # Accès aux données (JDBC / SQL)
└── com.pharmacie.util/       # Outils et connexion à la base de données
```

### Architecture MVC et DAO
<img width="2816" height="1536" alt="architecture" src="https://github.com/user-attachments/assets/df347d16-fbc6-40c8-92a3-0ffe552624bd" />

---

### Diagramme de cas d’utilisation
<img width="2816" height="1536" alt="Diagramme de cas d’utilisation" src="https://github.com/user-attachments/assets/f63b9e74-81e8-48da-8960-456dcd56fdc0" />


Ce diagramme présente les interactions entre l’utilisateur (pharmacien) et le système.



### Diagramme de classes

Le diagramme de classes décrit la structure statique de l’application et les relations entre les différentes entités.

<img width="2816" height="1536" alt="diagramme-de-classe" src="https://github.com/user-attachments/assets/b9187572-62fe-453c-93b7-eddd06a6d5dd" />


---

## Base de Données

### Tables principales

T_Fournisseur

* id (INT, clé primaire)
* nom (VARCHAR)
* ville (VARCHAR)
* contact (VARCHAR)

T_Medicament

* id (INT, clé primaire)
* nom (VARCHAR)
* famille (VARCHAR)
* prix (DOUBLE)
* stock (INT)
* id_fournisseur (INT, clé étrangère)

T_Vente

* id (INT, clé primaire)
* id_medicament (INT, clé étrangère)
* date_vente (TIMESTAMP)
* quantite (INT)
* prix_total (DOUBLE)

Le script SQL complet est disponible dans le fichier docs/schema.sql.

---

## Technologies utilisées

* Java SE 8+
* Swing
* JDBC
* MySQL 5.7+
* NetBeans 12+

---

## Installation

### Méthode 1 : Installation automatique (recommandée)

Lancer le fichier INSTALL_APP.bat situé à la racine du projet. L’assistant permet de :

1. Configurer MySQL
2. Créer la base de données
3. Lancer l’application

### Méthode 2 : Installation manuelle avec NetBeans

1. Ouvrir NetBeans
2. Aller dans File > Open Project
3. Sélectionner le dossier du projet
4. Clic droit sur le projet > Properties > Libraries
5. Ajouter le fichier libs/mysql-connector-j-9.5.0.jar
6. Créer une base MySQL nommée pharmacie_db
7. Exécuter le script docs/schema.sql
8. Modifier le fichier db.properties
9. Lancer le projet avec F6

### Configuration de la base de données

```properties
db.url=jdbc:mysql://localhost:3306/pharmacie_db
db.user=root
db.password=votre_mot_de_passe
```

---

## Utilisation

### Premier lancement

Au premier lancement, il est possible d’utiliser SetupFrame pour initialiser la base de données avec des données de test.

### Navigation

L’application contient quatre onglets :

* Médicaments
* Fournisseurs
* Ventes
* Statistiques

### Remarques

* Les lignes colorées en rouge indiquent un stock faible
* Une alerte s’affiche au démarrage en cas de rupture de stock
* Le sélecteur de date facilite la saisie des ventes

---

## Auteur

Projet académique réalisé dans le cadre du module Java Avancé.

## Vidéo sur les interfaces de l'application

https://github.com/user-attachments/assets/50ac8fea-2ab6-4fd6-827e-3218b826276a


