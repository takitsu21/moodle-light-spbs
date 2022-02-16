#language: fr
#encoding: utf-8

Fonctionnalité: En tant qu'utilisateur je veux pouvoir voir les participants de mes modules

  Contexte:
    Etant donné le professeur "Paul" assigne au module "Maths"
    Et le professeur "Jean"
    Et l'etudiant "Juliette" assigne au module "Maths"
    Et l'etudiant "Mia"

  Scénario: Le professeur "Paul" get les participants du module "Maths"
    Quand L'utilisateur "Paul" get les participants du module "Maths"
    Et le code de retour est 200
    Alors les participants sont "Paul" et "Juliette"
    Et les tables sont videes

  Scénario: Le professeur "Jean" get les participants du module "Maths"
    Quand L'utilisateur "Jean" get les participants du module "Maths"
    Et le code de retour est 400
    Alors il n'y a pas de participant
    Et les tables sont videes

  Scénario: L'élève "Juliette" get les participants du module "Maths"
    Quand L'utilisateur "Juliette" get les participants du module "Maths"
    Et le code de retour est 200
    Alors les participants sont "Paul" et "Juliette"
    Et les tables sont videes

  Scénario: L'élève "Mia" get les participants du module "Maths"
    Quand L'utilisateur "Mia" get les participants du module "Maths"
    Et le code de retour est 400
    Alors il n'y a pas de participant
    Et les tables sont videes
