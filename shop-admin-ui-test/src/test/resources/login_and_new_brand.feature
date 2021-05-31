Feature: Login and add new brand

  Scenario Outline: Successful Login to the page, adding new brand and logout after
    Given I open web browser
    When I navigate to login page
    And I provide username as "<username>" and password as "<password>"
    And I click on login button
    Then name should be "<name>"

    When I click on Brands button
    Then The page which has been rendered shall be Brands
    When I click on Add brand button
    Then Brand form is provided
    And I provide brand name as "Brand 2"
    And I click on Submit button
    Then The page which has been rendered shall be Brands
    And The Brand with the name "Brand 2" has been added

    When Open dropdown menu
    And click logout button
    Then user logged out

    Examples:
      | username | password | name |
      | admin | admin1 | admin |