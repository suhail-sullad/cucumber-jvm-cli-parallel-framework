Feature: To test for ecommerce parallel run

Scenario: To verify flipkart page is opened
	Given i open "http://www.flipkart.com"
	Then the page should be opened

Scenario: To verify amazon page is opened
	Given i open "http://www.amazon.com"
	Then the page should be opened
	