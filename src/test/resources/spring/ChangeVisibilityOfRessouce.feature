#language: fr
#encoding: utf-8

Fonctionnalité: En tant que enseignant je veux pouvoir modifier la visibilité des ressources

  Contexte:
    Etant donné le professeur "Paul" assigné au module de "Maths"
    Et le module "Maths" a une ressource "Pomme" invisible
    Et le module "Maths" a une ressource "Poir" visible
    Et le professeur "Jean" qui n'a aucun module

  Scénario: Le professeur "Paul" rend visible la ressource "Pomme" du module "Maths"
    Quand le professeur "Paul" essaie de rendre la ressource "Pomme" du module "Maths" visible
    Et le dernier status de request est 200 cv
    Alors la ressource "Pomme" du module "Maths" est visible

  Scénario: Le professeur "Jean" rend visible la ressource "Pomme" du module "Maths"
    Quand le professeur "Jean" essaie de rendre la ressource "Pomme" du module "Maths" visible
    Et le dernier status de request est 400 cv
    Alors la ressource "Pomme" du module "Maths" est invisible

  Scénario: Le professeur "Paul" rend invisible la ressource "Pomme" du module "Maths"
    Quand le professeur "Paul" essaie de rendre la ressource "Pomme" du module "Maths" invisible
    Et le dernier status de request est 200 cv
    Alors la ressource "Pomme" du module "Maths" est invisible

  Scénario: Le professeur "Jean" rend invisible la ressource "Pomme" du module "Maths"
    Quand le professeur "Jean" essaie de rendre la ressource "Pomme" du module "Maths" invisible
    Et le dernier status de request est 400 cv
    Alors la ressource "Pomme" du module "Maths" est invisible

  Scénario: Le professeur "Paul" rend invisible la ressource "Poir" du module "Maths"
    Quand le professeur "Paul" essaie de rendre la ressource "Poir" du module "Maths" invisible
    Et le dernier status de request est 200 cv
    Alors la ressource "Poir" du module "Maths" est invisible

  Scénario: Le professeur "Jean" rend invisible la ressource "Poir" du module "Maths"
    Quand le professeur "Jean" essaie de rendre la ressource "Poir" du module "Maths" invisible
    Et le dernier status de request est 400 cv
    Alors la ressource "Poir" du module "Maths" est visible

  Scénario: Le professeur "Paul" rend visible la ressource "Poir" du module "Maths"
    Quand le professeur "Paul" essaie de rendre la ressource "Poir" du module "Maths" visible
    Et le dernier status de request est 200 cv
    Alors la ressource "Poir" du module "Maths" est visible

  Scénario: Le professeur "Jean" rend visible la ressource "Poir" du module "Maths"
    Quand le professeur "Jean" essaie de rendre la ressource "Poir" du module "Maths" visible
    Et le dernier status de request est 400 cv
    Alors la ressource "Poir" du module "Maths" est visible