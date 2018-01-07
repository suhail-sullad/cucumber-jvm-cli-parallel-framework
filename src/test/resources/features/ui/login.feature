Feature: To test for parallel run

Scenario: To verify google page is opened
	Given i open "http://www.google.com"
	Then the page should be opened

Scenario: To verify facebook page is opened
	Given i open "http://www.facebook.com"
	Then the page should be opened