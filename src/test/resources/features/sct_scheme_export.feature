@regression @sepa-ct @scheme-export
Feature: SEPA Credit Transfer scheme export

  Scenario: Export accepted payment transactions through SEPA Credit Transfer
    Given the valid pain001 instruction "pain.001-sepa-ct.xml" has been accepted
    And the accepted transactions have been prepared for execution
    When the ready transactions are exported to the "SCT" scheme
    Then a pacs008 message is sent to the SEPA Credit Transfer gateway
    And the pacs008 message contains all prepared transactions
    And the execution transaction has status "EXECUTED"