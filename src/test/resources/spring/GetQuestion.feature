#language: fr
#encoding: utf-8
  
Fonctionnalité: Accéder à une question
  
  Contexte:
    Etant donné le questionnaire "Interrogation" dans le module "Physique" auq
    Et l'étudiant "Charlie" dans le module "Physique" auq
    Et l'étudiant "Tanguy" sans module auq
    Et la question "Bonus" numéro 13 dans le questionnaire "Interrogation" du module "Physique" auq
    Et la question "Gratuite" numéro 14 dans le questionnaire "Interrogation" du module "Physique" auq
    
  Scénario: l'étudiant "Charlie" récupère la question "Bonus"
    Quand l'étudiant "Charlie" récupère la question "Bonus" du questionnaire "Interrogation" du module "Physique" auq
    Alors la réponse est 200 auq
    Et la question "Bonus" en renvoyée auq
    Et les tables sont videes

  Scénario: l'étudiant "Tanguy" récupère la question "Bonus"
    Quand l'étudiant "Tanguy" récupère la question "Bonus" du questionnaire "Interrogation" du module "Physique" auq
    Alors la réponse est 400 auq
    Et les tables sont videes

  Scénario: l'étudiant "Charlie" récupère toutes les questions de "Interrogation"
    Quand l'etudiant "Charlie" recupere les questions du questionnaire "Interrogation" du module "Physique" auq
    Alors la réponse est 200 auq
    Et les questions "Gratuite" et "Bonus" du questionnaire "Interrogation" du module "Physique" sont renvoyees
    Et les tables sont videes

  Scénario: l'étudiant "Tanguy" récupère toutes les questions de "Interrogation"
    Quand l'etudiant "Tanguy" recupere les questions du questionnaire "Interrogation" du module "Physique" auq
    Alors la réponse est 400 auq
    Et les tables sont videes