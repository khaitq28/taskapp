Feature: User API
  As a user
  I want to call the api/v1/users/hi endpoint
  So that I can get a "hi" response

  Scenario: Call api/v1/users/hi endpoint
    Given the application is running
    When I send a GET request to "api/v1/users/hi"
    Then I should receive a response "hi"


  Scenario: Call /api/v1 endpoint
    Given the application is running
    When I send a GET request to "api/v1/"
    Then I should receive a response "OK"