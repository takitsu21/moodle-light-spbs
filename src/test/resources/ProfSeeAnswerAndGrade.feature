# language: fr
# encoding: utf-8

Fonctionnalité: En tant que prof je veux pouvoir voir les réponses et les notes des élèves inscrit a mon module

  Contexte:
    Etant donné un professeur "Michèle" ayant comme module "TER" mais pas le module "Génie Logiciel"
    Et un module "TER" contenant un questionnaire "Test"
    Et un module "Génie Logiciel" contenant un questionnaire "CC"
    Et un questionnaire "Test" contenant une question QCM "Q1" et une question QCM "Q2"
    Et une question "Q1" possédant 4 réponses différentes dont la 1 est la bonne
    Et une question "Q2" possédant 4 réponses différentes dont la 2 est la bonne
    Et un élève "Antoine" ayant validé son questionnaire "Test" choisissant la réponse 1 pour la "Q1" et la 1 pour la réponse "Q2"
    Et un élève "Léa" n'ayant pas validé son questionnaire "Test"
    Et un élève "Jean-Batiste" n'ayant pas le module "TER"

    Scénario: Regarder la notes d'un élève dans dans le module
      Quand "Michèle" regarde la note de "Antoine" sur le questionnaire "Test" sag1
      Alors le status de la dernière requète est 200 sag2
      Et "Michèle" voit la note 1 / 2 sag3

    Scénario: Regarder la réponse a une question d'un élève
      Quand "Michèle" regarde la réponse à "Q2" du questionnaire "Test" de l'élève "Antoine" sag4
      Alors le status de la dernière requète est 200 sag5
      Et "Michèle" voit la réponse 1 sag6

    Scénario: Regarder la réponse d'une question qui n'est pas dans le test
      Quand "Michèle" regarde la réponse à "Q3" du questionnaire "Test" de l'élève "Antoine" sag7
      Alors le status de la dernière requète est 400 sag8
      Et "Michèle" n'as pas de réponse sag9

    Scénario: Regarder la note d'un élève d'un module qu'on n'a pas
      Quand "Michèle" regarde la note de "Antoine" sur le questionnaire "CC" du module "Génie Logiciel" sag10
      Alors le status de la dernière requète est 400 sag11
      Et "Michèle" n'a pas de réponse sag12

    Scénario: Regarder la note d'un élève n'ayant pas valider le test
      Quand "Michèle" regarde la note de "Léa" sur le questionnaire "Test" sag13
      Alors le status de la dernière requète est 400 sag14
      Et "Michèle" n'a pas de réponse sag15

    Scénario: Regarder la note d'un élève ne possédant pas la matière
      Quand "Michèle" regarde la note de "Jean-Batiste" sur le questionnaire "Test" sag16
      Alors le status de la dernière requète est 400 sag17
      Et "Michèle" n'a pas de réponse sag18
