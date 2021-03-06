#language: fr
#encoding: utf-8

Fonctionnalité: Ajouté ou enlever un étudiant d'un module

  Contexte:
    Etant donné le professeur "Jacques" assigne au module "Math"
    Et le professeur "Jacques" assigne au module "Français"
    Et l'etudiant "Jean"
    Et l'etudiant "Margaux"
    Et l'etudiant "Franck" assigne au module "Français"
    Et l enseignant inscrit l'étudiant "Margaux" au module "Math"

  Scénario: Ajout d'un étudiant à un module
    Quand l'enseignant "Jacques" veut ajouter "Jean" au module de "Math" arm
    Alors le code de retour est 200
    Et l'étudiant "Jean" est ajouter au module "Math" arm
    Et les tables sont videes

  Scénario: Enlever un étudiant d'un module
    Quand l'enseignant "Jacques" veut enlever "Margaux" du module de "Math" arm
    Alors le code de retour est 200
    Et "Margaux" est enlever du module "Math" arm
    Et les tables sont videes

  Scénario: Ajout d'un étudiant appartenant déjà au module
    Quand l'enseignant "Jacques" veut ajouter "Franck" au module de "Français" arm
    Alors le code de retour est 400
    Et "Franck" est déjà dans le module "Français" arm
    Et les tables sont videes

  Scénario: Enlever un étudiant qui n'est pas dans le module
    Quand l'enseignant "Jacques" veut enlever "Jean" du module de "Math" arm
    Alors le code de retour est 400
    Et "Jean" n'est pas dans le module "Math" arm
    Et les tables sont videes
