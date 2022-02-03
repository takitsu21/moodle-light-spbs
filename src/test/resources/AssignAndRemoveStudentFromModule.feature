#language: fr
#encoding: utf-8

Fonctionnalité: Ajouté ou enlever un étudiant d'un module

  Contexte:
    Etant donné un module "math"
    Et un étudiant "Marcel"


  Scénario: Ajout d'un étudiant
    Quand "Marcel" n'est pas inscrit dans le module "math"
    Alors "Marcel" est ajouté au module "math"

  Scénario: Enlever un étudiant
    Quand "Marcel" est déjà inscrit dans le module "math"
    Alors "Marcel" est enlever du module "math"