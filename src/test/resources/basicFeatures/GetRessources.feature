#language: fr
#encoding: utf-8

Fonctionnalité: En tant qu'utilisateur je veux pouvoir voir les ressources d'un module

  Contexte:
    Etant donné le professeur "Paul" assigné au module de "Maths" gr
    Et le module "Maths" a un cours "Pomme" invisible gr
    Et le module "Maths" a un cours "Poir" visible gr
    Et le professeur "Jean" qui n'a aucun module gr
    Et l'élève "Juliette" est assigné au cours "Maths"
    Et l'élève "Mia" assigné a aucun module

  Scénario: Le professeur "Paul" get les ressources du module "Maths"
    Quand Le professeur "Paul" get les ressources du module "Maths"
    Et le dernier status de request est 200 gr
    Alors le cours "Poir" est renvoyé

  Scénario: Le professeur "Juliette" get les ressources du module "Maths"
    Quand Le professeur "Juliette" get les ressources du module "Maths"
    Et le dernier status de request est 200 gr
    Alors le cours "Poir" est renvoyé

  Scénario: Le professeur "Jean" get les ressources du module "Maths"
    Quand Le professeur "Jean" get les ressources du module "Maths"
    Et le dernier status de request est 400 gr
    Alors aucun cours n'est renvoyé

  Scénario: Le professeur "Mia" get les ressources du module "Maths"
    Quand Le professeur "Mia" get les ressources du module "Maths"
    Et le dernier status de request est 400 gr
    Alors aucun cours n'est renvoyé

