#language: fr
#encoding: utf-8

Fonctionnalité: En tant que professeur je veux pouvoir m'assigner un module

  Contexte:
    Etant donné un professeur "Paul" avec le mot de passe "123" voulant s assigner au module "Maths" qui n'a pas de professeur et au module "Chimie" qui a un professeur assigné


  Scénario: Le module "Maths" n'a pas de professeur assigné
    Quand le professeur essaie de s'assigner au module "Maths"
    Alors le professeur reussi à s'assigner

  Scénario: Le module "Chimie" a déja un professeur assigné
    Quand le professeur essaie de s'assigner au module "Chimie"
    Alors le professeur ne reussi pas à s'assigner

