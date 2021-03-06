#language: fr
#encoding: utf-8

Fonctionnalité: En tant que professeur je veux pouvoir m'assigner un module

  Contexte:
    Etant donné le professeur "Pierre"
    Et un module "Maths" qui n'a pas de professeur
    Et le professeur "Pedro" assigne au module "Chimie"


  Scénario: Le module "Maths" n'a pas de professeur assigné
    Quand le professeur "Pierre" essaie de s'assigner au module "Maths"
    Et le code de retour est 200
    Alors le professeur "Pierre" est assigner à "Maths"
    Et les tables sont videes

  Scénario: Le module "Chimie" a déja un professeur assigné
    Quand le professeur "Pierre" essaie de s'assigner au module "Chimie"
    Et le code de retour est 400
    Alors le professeur "Pierre" n'est pas assigner à "Chimie"
    Et les tables sont videes

