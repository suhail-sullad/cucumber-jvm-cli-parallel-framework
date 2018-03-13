Feature: Verify that products are properly returned by the API
  Background:
    * url 'https://github.com/'
  @runthis2  
  Scenario: Products are returned on GET
    Given   path 'neillfontes'
     When method get
     Then status 500
     
  Scenario: Valid Products are returned on GET
    Given   path 'neillfontes'
     When method get
     Then status 200