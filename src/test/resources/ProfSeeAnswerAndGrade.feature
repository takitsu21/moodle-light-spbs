# language: fr
# encoding: utf-8

Fonctionnalité: En tant que prof je veux pouvoir voir les réponses et les notes des élèves inscrit a mon cours

  Contexte:
    Etant donné un professeur "Michèle" ayant comme cours "TER" mais pas le cours "Génie Logiciel"
    Et un cours "TER" contenant un questionnaire "Test"
    Et un cours "Génie Logiciel" contenant un questionnaire "CC"
    Et un questionnaire "Test" contenant une question QCM "Q1" et une question QCM "Q2"
    Et une question "Q1" possédant 4 réponses différentes dont la 1 est la bonne
    Et une question "Q2" possédant 4 réponses différentes dont la 2 est la bonne
    Et un élève "Antoine" ayant validé son questionnaire "Test" choisissant la réponse 1 pour la "Q1" et la 1 pour la réponse "Q2"
    Et un élève "Léa" n'ayant pas validé son questionnaire "Test"
    Et un élève "Jean-Batiste" n'ayant pas le cours "TER"

    Scénario: Regarder la notes d'un élève dans dans le cours
      Quand "Michèle" regarde la note de "Antoine" sur le questionnaire "Test"
      Alors le status de la dernière requète est 200
      Et "Michèle" voit la note 10

    Scénario: Regarder la réponse a une question d'un élève
      Quand "Michèle" regarde la réponse à "Q2" du questionnaire "Test"
      Alors le status de la dernière requète est 200
      Et "Michèle" voit la réponse 1

    Scénario: Regarder la réponse d'une question qui n'est pas dans le test
      Quand "Michèle" regarde la réponse à "Q3" du questionnaire "Test"
      Alors le status de la dernière reuqète est 400
      Et "Michèle" n'as pas de réponse

    Scénario: Regarder la note d'un élève d'un cours qu'on n'a pas
      Quand "Michèle" regarde la note de "Antoine" sur le questionnaire "CC" du cours "Génie Logiciel"
      Alors le status de la dernière requète est 400
      Et "Michèle" n'a pas de réponse

    Scénario: Regarder la note d'un élève n'ayant pas valider le test
      Quand "Michèle" regarde la note de "Léa" sur le questionnaire "Test"
      Alors le status de la dernière requète est 400
      Et "Michèle" n'a pas de réponse

    Scénario: Regarder la note d'un élève ne possédant pas la matière
      Quand "Michèle" regarde la note de "Jean-Batiste" sur le questionnaire "Test"
      Alors le status de la dernière requète est 400
      Et "Michèle" n'a pas de réponse
