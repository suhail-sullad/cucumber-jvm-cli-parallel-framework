@runthis
Feature: Sample Karate API Automation 
Background: 
	* url 'http://services.groupkt.com'

Scenario: States of country returned on GET 
	Given   path '/state/get/IND/all' 
	When method get 
	Then status 200 

Scenario: States of countrys returned on GET 
	Given   path '/state/get/IND/all' 
	When method get 
	Then status 500
