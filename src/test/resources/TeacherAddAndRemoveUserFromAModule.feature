#language: fr
#encoding: utf-8

Fonctionnalité: En tant qu'enseignant je veux pouvoir ajouter et retirer des utilisateur a mon module

  Contexte:
    Etant donné le professeur "Paul" assigné au module de "Maths"
    Et le professeur "Paul" assigné au module de "Chimie"
    Et le professeur "Marie" assigné au module de "Chimie"
    Et le professeur "Jean" qui n'est assigné a aucun module
    Et l'élève "Dylann" assigné au cours de "Chimie"

  Scénario: Le professeur "Paul" assigne le professeur "Marie" au module "Maths"
    Quand le professeur "Paul" essaie d'assigner le professeur "Marie" au module "Maths"
    Alors le professeur reussi à l'assigner

  Scénario: Le professeur "Jean" assigne le professeur "Marie" au module "Maths"
    Quand le professeur "Jean" essaie d'assigner le professeur "Marie" au module "Maths"
    Alors le professeur ne reussi pas à l'assigner

  Scénario: Le professeur "Paul" retire le professeur "Marie" au module "Chimie"
    Quand le professeur "Paul" essaie de retirer le professeur "Marie" au module "Chimie"
    Alors le professeur reussi à l'assigner

  Scénario: Le professeur "Jean" retire le professeur "Marie" au module "Chimie"
    Quand le professeur "Jean" essaie de retirer le professeur "Marie" au module "Chimie"
    Alors le professeur ne reussi pas à l'assigner

  Scénario: Le professeur "Paul" assigne l'élève "Dylann" au module "Maths"
    Quand le professeur "Paul" essaie d assigner l élève "Dylann" au module "Maths"
    Alors le professeur reussi à l'assigner

  Scénario: Le professeur "Jean" assigne l'élève "Dylann" au module "Maths"
    Quand le professeur "Jean" essaie d assigner l élève "Dylann" au module "Maths"
    Alors le professeur ne reussi pas à l'assigner

  Scénario: Le professeur "Paul" retire l'élève "Dylann" au module "Chimie"
    Quand le professeur "Paul" essaie de retirer l élève "Dylann" au module "Chimie"
    Alors le professeur reussi à l'assigner

  Scénario: Le professeur "Jean" retire l'élève "Dylann" au module "Chimie"
    Quand le professeur "Jean" essaie de retirer l élève "Dylann" au module "Chimie"
    Alors le professeur ne reussi pas à l'assigner
