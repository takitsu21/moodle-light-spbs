#language: fr
#encoding: utf-8

Fonctionnalité: En tant que enseignant je veux pouvoir modifier la visibilité des ressources

  Contexte:
    Etant donné le professeur "Paul" assigné au module de "Maths" qui a une ressource "Pomme"
    Et le professeur "Jean" qui n'a aucun module

  Scénario: Le professeur "Paul" change la visibilité de la ressource "Pomme" du module "Maths"
    Quand le professeur "Paul" essaie de changer la visibilité de la ressource "Pomme" du module "Maths"
    Et 2le dernier status de request est 200
    Alors le professeur reussi à changer la visibilité

  Scénario: Le professeur "Paul" change la visibilité de la ressource "Pomme" du module "Maths"
    Quand le professeur "Jean" essaie de changer la visibilité de la ressource "Pomme" du module "Maths"
    Et 2le dernier status de request est 400
    Alors le professeur reussi à changer la visibilité
