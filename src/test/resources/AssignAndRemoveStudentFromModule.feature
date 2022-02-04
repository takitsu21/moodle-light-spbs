#language: fr
#encoding: utf-8

Fonctionnalité: Ajouté ou enlever un étudiant d'un module

  Contexte:
    Etant donné Un enseignant "Jacques" avec le mot de passe "admin"
      Et un étudiant "Jean"
      Et un module "Math" et "Français" de ressource vide

  Scénario: Ajout d'un étudiant a un module
    Quand L'enseignant ajoute "Jean" au module de "Français"
    Alors L'élève est ajouter au module

  Scénario: Enlever un étudiant d'un module
    Quand L'enseignant veut enlever "Jean" du module de "Math"
    Alors L'élève est enlever du module