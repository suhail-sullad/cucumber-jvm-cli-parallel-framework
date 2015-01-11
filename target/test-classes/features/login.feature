Feature: Simple login test

  Scenario: To check whether user is able to login
    Given i am on login page
    When i enter the login credentials
    Then appropriate action has to be executed

  Scenario Outline: To check the excel file reading functionality
    Given i have numerous examples
    When i send "<emailid>" and "<password>"
    Then the user should be prompted with"<message>"

    Examples: Path:C:\Users\CHAMP ONE\Desktop\mysampleworkbook.xlsx  SheetIndex:0
      | emailid | password | message |
      |         |          |         |
