# language: fr
# encoding: utf-8

Fonctionnalité: En tant que professeur je veux pouvoir créer une question ouverte

  Contexte:
    Etant donné un professeur "Margaux" ayant le module "Graphes Avancés"
    Et un professeur "Dylann" n'ayant pas le module "Graphes Avancés"
    Et un module "Graphes Avancés" possédant un questionnaire "Test 1/20"
    Et un questionnaire "Test 1/20" possédant une question "Q1"

    Scénario: Un professeur ajoute une question a un questionnaire de son module
      Quand "Margaux" ajoute la question ouverte "Q1" au questionnaire "Test 1/20" avec 3 réponses possibles et les 2 premières sont les bonne coq1
      Alors le status de la dernière requète est 200 coq2
      Et la question "Q1" est ajouté au questionnaire "Test 1/20" coq3
      Et la question "Q1" contient 3 réponses possibles et les 2 première sont les bonnes coq4

    Scénario: Un professeur ajoute une question a un questionnaire d'un module qui ne lui appartient pas
      Quand "Dylann" ajoute la question ouverte "Q1" au questionnaire "Test 1/20" coq5
      Alors le status de la dernière requète est 400 coq6
      Et la question "Q1" n'est pas ajouté au questionnaire "Test 1/20" coq7

    Scénario: Un professeur ajoute une question avec 0 réponses possibles
      Quand "Margaux" ajoute la question ouverte "Q2" ne possédant pas de réponses possible au questionnaire "Test 1/20" coq8
      Alors le status de la dernière requète est 400 coq9
      Et la question n'est pas ajouter au questionnaire "Test 1/20" coq10

    Scénario: Un professeur ajoute une question avec 0 réponses sur plusieurs réponses possible
      Quand "Margaux" ajoute la question ouverte "Q2" ne possédant pas de réponses sur plusieurs réponses possibles au questionnaire "Test 1/20" coq11
      Alors le status de la dernière requète est 400 coq12
      Et la question n'est pas ajouté au questionnaire "Test 1/20" coq13



