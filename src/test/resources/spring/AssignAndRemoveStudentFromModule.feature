#language: fr
#encoding: utf-8

Fonctionnalité: Ajouté ou enlever un étudiant d'un module

  Contexte:
    Etant donné Un enseignant avec le nom de connexion "Jacques"
      Et un étudiant "Jean"
      Et un étudiant "Margaux"
      Et un module "Math" qui a un enseignant "Jacques"
      Et l enseignant inscrit l'étudiant "Margaux" au module "Math"

  Scénario: Ajout d'un étudiant à un module
    Quand l'enseignant "Jacques" veut ajouter "Jean" au module de "Math"
    Alors le dernier status de réponse est 200
    Et l'étudiant "Jean" est ajouter au module "Math"

  Scénario: Enlever un étudiant d'un module
    Quand l'enseignant "Jacques" veut enlever "Margaux" du module de "Math"
    Alors le dernier status de réponse est 200
    Et "Margaux" est enlever du module "Math"