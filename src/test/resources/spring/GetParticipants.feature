#language: fr
#encoding: utf-8

Fonctionnalité: En tant qu'utilisateur je veux pouvoir voir les participants de mes modules

  Contexte:
    Etant donné le professeur "Paul" assigné au module de "Maths" gp
    Et le professeur "Jean" qui n'a aucun module gp
    Et l'élève "Juliette" est assigné au cours "Maths" gp
    Et l'élève "Mia" assigné a aucun module gp

  Scénario: Le professeur "Paul" get les participants du module "Maths"
    Quand L'utilisateur "Paul" get les participants du module "Maths"
    Et le dernier status de request est 200 gp
    Alors les participants sont "Paul" et "Juliette"
    Et les tables sont videes

  Scénario: Le professeur "Jean" get les participants du module "Maths"
    Quand L'utilisateur "Jean" get les participants du module "Maths"
    Et le dernier status de request est 400 gp
    Alors il n'y a pas de participant
    Et les tables sont videes

  Scénario: L'élève "Juliette" get les participants du module "Maths"
    Quand L'utilisateur "Juliette" get les participants du module "Maths"
    Et le dernier status de request est 200 gp
    Alors les participants sont "Paul" et "Juliette"
    Et les tables sont videes

  Scénario: L'élève "Mia" get les participants du module "Maths"
    Quand L'utilisateur "Mia" get les participants du module "Maths"
    Et le dernier status de request est 400 gp
    Alors il n'y a pas de participant
    Et les tables sont videes
