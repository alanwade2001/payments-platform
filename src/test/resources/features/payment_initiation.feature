Feature: Payment initiation

  Background:
    Given customer "customer-1" exists
    And customer "customer-1" has an account with IBAN "IE29AIBK93115212345678"
    And customer "customer-1" is entitled to "SEPA_BULK_CT"

  Scenario: Submit a valid SCT payment initiation
    Given an SCT pain.001 payment initiation
    And the debtor IBAN is "IE29AIBK93115212345678"
    When the payment initiation is submitted
    Then the payment initiation is "ACCEPTED"
    And an instructed transaction is created
    And an execution transaction is created
    And the execution transaction has status "READY"

  Scenario: Submit an SCT payment from an unknown account
    Given an SCT pain.001 payment initiation
    And the debtor IBAN is "IE64IRCE92050112345678"
    When the payment initiation is submitted
    Then the rejection reason is "Account not found"
    And no instructed transaction is created