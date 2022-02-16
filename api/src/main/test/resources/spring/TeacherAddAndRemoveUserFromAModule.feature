#language: fr
#encoding: utf-8

Fonctionnalité: En tant qu'enseignant je veux pouvoir ajouter et retirer des utilisateur a mon module

  Contexte:
    Etant donné le professeur "Paul" assigne au module "Maths"
    Et le professeur "Paul" assigne au module "Chimie"
    Et le professeur "Marie" assigne au module "Chimie"
    Et le professeur "Jean"
    Et l'etudiant "Dylann" assigne au module "Chimie"


  Scénario: Le professeur "Jean" assigne le professeur "Marie" au module "Maths"
    Quand le professeur "Jean" essaie d'assigner le professeur "Marie" au module "Maths"
    Et le code de retour est 400
    Alors "Marie" n'est pas assigner à "Maths"
    Et les tables sont videes

  Scénario: Le professeur "Paul" assigne le professeur "Marie" au module "Maths"
    Quand le professeur "Paul" essaie d'assigner le professeur "Marie" au module "Maths"
    Et le code de retour est 200
    Alors "Marie" est assigner à "Maths"
    Et les tables sont videes

  Scénario: Le professeur "Jean" assigne l'élève "Dylann" au module "Maths"
    Quand le professeur "Jean" essaie d assigner l élève "Dylann" au module "Maths"
    Et le code de retour est 400
    Alors "Dylann" n'est pas assigner à "Maths"
    Et les tables sont videes

  Scénario: Le professeur "Paul" assigne l'élève "Dylann" au module "Maths"
    Quand le professeur "Paul" essaie d assigner l élève "Dylann" au module "Maths"
    Et le code de retour est 200
    Alors "Dylann" est assigner à "Maths"
    Et les tables sont videes

  Scénario: Le professeur "Jean" retire le professeur "Marie" au module "Chimie"
    Quand le professeur "Jean" essaie de retirer le professeur "Marie" au module "Chimie"
    Et le code de retour est 400
    Alors "Marie" est assigner à "Chimie"
    Et les tables sont videes

  Scénario: Le professeur "Paul" retire le professeur "Marie" au module "Chimie"
    Quand le professeur "Paul" essaie de retirer le professeur "Marie" au module "Chimie"
    Et le code de retour est 200
    Alors "Marie" n'est pas assigner à "Chimie"
    Et les tables sont videes

  Scénario: Le professeur "Jean" retire l'élève "Dylann" au module "Chimie"
    Quand le professeur "Jean" essaie de retirer l élève "Dylann" au module "Chimie"
    Et le code de retour est 400
    Alors "Dylann" est assigner à "Chimie"
    Et les tables sont videes

  Scénario: Le professeur "Paul" retire l'élève "Dylann" au module "Chimie"
    Quand le professeur "Paul" essaie de retirer l élève "Dylann" au module "Chimie"
    Et le code de retour est 200
    Alors "Dylann" n'est pas assigner à "Chimie"
    Et les tables sont videes

