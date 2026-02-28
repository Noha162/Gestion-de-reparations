# Système de gestion des réparations

Application de gestion des réparations développée en Java, basée sur une architecture en couches et le pattern DAO (Data Access Object).

Le système permet de gérer les clients, les appareils, les réparations, les lignes de réparation, les transactions ainsi que les utilisateurs, tout en assurant une séparation claire entre la présentation, la logique métier et l’accès aux données.

---

## Technologies utilisées

- Java
- Pattern DAO
- Architecture en couches
- Base de données relationnelle (SQL)
- Gestion d’exceptions personnalisées

---

## Structure du projet

L’application est organisée en plusieurs couches :

### metier
- dto : objets de transfert de données
- interfaces : contrats métier
- impl : implémentations de la logique métier

### presentation
- controller
- ui
- point d’entrée de l’application (Main)

### exception
- AuthException
- ValidationException
- NotFoundException
- MetierException

### database
- Gestion de la persistance des données

---

## Fonctionnalités principales

- Gestion des clients
- Gestion des appareils
- Création et suivi des réparations
- Gestion des lignes de réparation
- Gestion des transactions
- Authentification et gestion des utilisateurs

---

## Principes d’architecture

- Utilisation du pattern DAO pour l’accès aux données
- Séparation des responsabilités
- Code modulaire et maintenable
- Gestion centralisée des exceptions
- Architecture claire et structurée

---

## Objectif du projet

Ce projet a été réalisé dans un cadre académique afin de mettre en pratique :

- Les bonnes pratiques d’architecture logicielle
- L’utilisation des design patterns
- La structuration d’une application Java en couches
- Le développement d’une application métier complète
