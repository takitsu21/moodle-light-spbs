#language: fr
#encoding: utf-8

Fonctionnalité: En tant qu'enseignant je veux pouvoir ajouter et supprimer des textes dans mes cours

  Contexte:
    Etant donné le professeur "Paul" assigné au module de "Maths"
    Et le professeur "Paul" assigné au module de "Chimie"
    Et le cours de math a une ressource "Pomme" qui a un texte "Fruit"
    Et le professeur "Jean" qui n'a aucun module

  Scénario: Le professeur "Paul" retire un text a la ressource "Pomme" du module "Maths"
    Quand Le professeur "Paul" retire un text "Fruit" a la ressource "Pomme" du module "Maths"
    Et 3le dernier status de request est 200
    Alors le text "Fruit" de la ressource "Pomme" n'est pas dans le module "Maths"

  Scénario: Le professeur "Paul" ajoute un text a la ressource "Pomme" du module "Maths"
    Quand Le professeur "Paul" ajoute un text "Arbre" a la ressource "Pomme" du module "Maths"
    Et 3le dernier status de request est 200
    Alors le text "Arbre" de la ressource "Pomme" est dans le module "Maths"

  Scénario: Le professeur "Jean" retire un text a la ressource "Arbre" du module "Maths"
    Quand Le professeur "Jean" retire un text "Arbre" a la ressource "Pomme" du module "Maths"
    Et 3le dernier status de request est 400
    Alors le text "Arbre" de la ressource "Pomme" est dans le module "Maths"

  Scénario: Le professeur "Jean" ajoute un text a la ressource "Pomme" du module "Maths"
    Quand Le professeur "Jean" ajoute un text "Fruit" a la ressource "Pomme" du module "Maths"
    Et 3le dernier status de request est 200
    Alors le text "Fruit" de la ressource "Pomme" n'est pas dans le module "Maths"
