#language: fr
#encoding: utf-8

Fonctionnalité: En tant qu'enseignant je veux pouvoir ajouter et supprimer des textes dans mes cours

  Contexte:
    Etant donné le professeur "Paul" assigné au module de "Maths" atc
    Et le module de "Maths" a une ressource "Pomme" qui a un texte 1 de contenu "Fruit"
    Et le professeur "Jean" qui n'a aucun module atc

  Scénario: Le professeur "Paul" ajoute un text a la ressource "Pomme" du module "Maths"
    Quand Le professeur "Paul" ajoute un text 2 de contenu "Arbre" a la ressource "Pomme" du module "Maths"
    Et le dernier status de request est 200 at
    Alors le text 2 de la ressource "Pomme" est dans le module "Maths"
    Et les tables sont videes

  Scénario: Le professeur "Paul" retire un text a la ressource "Pomme" du module "Maths"
    Quand Le professeur "Paul" retire un text 1 a la ressource "Pomme" du module "Maths"
    Et le dernier status de request est 200 at
    Alors le text 1 de la ressource "Pomme" n'est pas dans le module "Maths"
    Et les tables sont videes

  Scénario: Le professeur "Jean" ajoute un text a la ressource "Pomme" du module "Maths"
    Quand Le professeur "Jean" ajoute un text 3 de contenu "Fleur" a la ressource "Pomme" du module "Maths"
    Et le dernier status de request est 400 at
    Alors le text 3 de la ressource "Pomme" n'est pas dans le module "Maths"
    Et les tables sont videes
