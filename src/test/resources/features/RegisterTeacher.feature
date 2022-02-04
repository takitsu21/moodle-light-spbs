Feature: Register Teacher

  Background:
    Given a teacher with login "steve"
    And a teacher with login "sarah"
    And a module named "Gestion de projet"

  Scenario: Register Teacher
    When "steve" registers to module "Gestion de projet"
    Then last request status is 200
    And "steve" is registered to module "Gestion de projet"

  Scenario: Register Second Teacher
    When "sarah" registers to module "Gestion de projet"
    And "steve" registers to module "Gestion de projet"
    Then last request status is 400
    And "sarah" is registered to module "Gestion de projet"
    And "steve" is not registered to module "Gestion de projet"

