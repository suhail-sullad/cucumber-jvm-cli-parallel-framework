Feature: Sample Karate API Automation
  Background:
    * url 'http://services.groupkt.com'
  Scenario: States of country returned on GET
    Given   path '/state/get/IND/all'
     When method get
     Then status 200