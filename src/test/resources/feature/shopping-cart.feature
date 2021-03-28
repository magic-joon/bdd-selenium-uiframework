Feature: ShoppingCart
  As a Customer I should able to add items to shopping cart

  Scenario: Customer should able to add Summer Dress to shopping cart and should be able proceed to Sign-in section
  Given I am using the "chrome" browser
    And I open the URL "baseURL"
    And I click on "dresses"
    And I click on "summer-dresses"
    And I click on "printed-summer-dresses"
    And I click on "add-to-cart"
    And I click on "proceed-to-checkout"
    And I click on "proceed-to-checkout-summary"
    Then I verify "sign-in-button" is there in sign-in section

