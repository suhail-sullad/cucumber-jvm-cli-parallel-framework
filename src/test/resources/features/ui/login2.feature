Feature: To test for ecommerce parallel run

Scenario Outline: To verify if websites are loaded from file
	Given i have numerous examples
    When i send "<site name>" and "<site url>"
    Then the user should be prompted with"<title>"

    Examples: Path:C:\Users\suhail_sullad\Downloads\Book1.xlsx  SheetIndex:0
      | site name | site url				  | title	 |
      |		a	  |			http://google.com |	 google	 |
	