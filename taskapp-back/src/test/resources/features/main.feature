Feature: User API - Hello Endpoint
  As a user
  I want to call the main endpoint
  So that I can get a "hi" response

  Scenario: Call /users/hello endpoint
    Given I call the "/" endpoint
    Then I should receive a response main "OK"