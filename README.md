# 🏨 Hôtel CRESCENT — Système de Gestion

Application Java Swing de gestion hôtelière, reconstruite with une architecture MVC propre.

---

## Structure du projet

```
hotel-management/
├── src/hotel/
│   ├── Main.java                        ← Point d'entrée
│   ├── config/
│   │   └── DatabaseConnection.java      ← Singleton connexion DB
│   ├── model/
│   │   ├── User.java
│   │   ├── Room.java
│   │   ├── Customer.java
│   │   ├── Dish.java
│   │   ├── Booking.java
│   │   └── Order.java
│   ├── dao/
│   │   ├── Dao.java                     ← Interface générique CRUD
│   │   ├── UserDao.java
│   │   ├── RoomDao.java
│   │   ├── CustomerDao.java
│   │   ├── DishDao.java
│   │   └── BookingDao.java
│   ├── controller/
│   │   ├── AuthController.java          ← Login + hash SHA-256
│   │   ├── RoomController.java
│   │   ├── CustomerController.java
│   │   ├── BookingController.java
│   │   └── DishController.java
│   ├── view/
│   │   ├── LoginView.java
│   │   ├── DashboardView.java
│   │   ├── RoomManagementView.java
│   │   ├── CustomerView.java
│   │   ├── BookingView.java
│   │   └── RestaurantView.java
│   └── util/
│       └── Theme.java                   ← Thème centralisé (couleurs, polices, composants)
└── database.sql                         ← Schéma + données de démo
```

---

## Prérequis

- **Java 17+**
- **MySQL 8+**
- Driver JDBC MySQL : `mysql-connector-j-8.x.jar` (à ajouter dans `lib/`)

---

## Installation

### 1. Base de données

```bash
mysql -u root -p < database.sql
```

Cela crée la base `hotel_management` avec les tables, les données de démo, et un utilisateur admin.

### 2. Mot de passe admin par défaut

| Champ    | Valeur     |
|----------|------------|
| Username | `admin`    |
| Password | `admin123` |

### 3. Compilation (avec javac)

```bash
# Depuis le dossier hotel-management/
javac -cp "lib/mysql-connector-j-8.x.jar" -d bin \
  $(find src -name "*.java")
```

### 4. Lancement

```bash
java -cp "bin:lib/mysql-connector-j-8.x.jar" hotel.Main
```

*(Sur Windows, remplacer `:` par `;` dans le classpath)*

### Avec un IDE (IntelliJ / Eclipse / NetBeans)

1. Importer le dossier `hotel-management/` comme projet Java
2. Ajouter `mysql-connector-j-8.x.jar` dans les dépendances
3. Lancer `hotel.Main`

---

## Améliorations par rapport à l'original

| Problème original            | Solution apportée         |
|------------------------------|---------------------------|
| Pas d'architecture           | MVC strict : model / dao / controller / view |
| Mots de passe en clair       | `AuthController`          |
| `null` layout partout        | BoxLayout + BorderLayout composés |
| Connexion recréée à chaque fois | Singleton `DatabaseConnection` |
| `==` pour comparer String    | `.equals()` + logique dans controller |
| Nommage incohérent           | Conventions Java respectées partout |
| UI datée                     | Thème Luxury Dark centralisé dans `Theme` |
| DB hardcodée en dur partout  | Centralisée dans `DatabaseConnection` |
| Pas de validation            | Validation dans chaque controller |

---

## Fonctionnalités

- **Authentification** sécurisée avec rôles (ADMIN / STAFF)
- **Chambres** : CRUD complet, statuts AVAILABLE / OCCUPIED
- **Clients** : CRUD + recherche par nom
- **Réservations** : création, check-out, annulation, calcul automatique du total
- **Restaurant** : gestion du menu par catégorie (entrée, plat, dessert, boisson)
