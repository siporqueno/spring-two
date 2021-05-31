Feature: Delete brand

  Scenario Outline: Navigation to brands page and removal of a brand
    Given I open web browser
    When I navigate to login page
    And I provide username as "<username>" and password as "<password>"
    And I click on login button
    Then name should be "<name>"

    When I navigate to brands page
    Then The page which has been rendered shall be Brands
    When I click button to remove brand on the row of the brand "Brand 2"
    Then The page which has been rendered shall be Brands
    And The page does not contain row with brand "Brand 2" anymore

    Examples:
      | username | password | name |
      | admin | admin1 | admin |