package com.acme.payments.regression.clients;

import com.acme.payments.reference.*;
import com.acme.payments.reference.internal.admin.CreateProductEntitlementRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.Currency;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class ReferenceAdmin {

    private final TestApiClient testApiClient;
    private final String base = "/api/admin/reference";

    public Optional<CustomerDetails> getCustomer(String name ) {

        String local = "/customers/%s";
        String path = base + local;
        String uri = String.format( path, name );

        try{
            var details = testApiClient.get(uri, CustomerDetails.class);
            return Optional.of(details);
        } catch( Exception e ){
            return Optional.empty();
        }
    }

    public CustomerId createCustomer(String name ) {
        String local = "/customers";
        String uri = base + local;
        var request = new CreateCustomerRequest(name);

        try{
            return testApiClient.post(uri, request, CustomerId.class);
        } catch( RuntimeException e ){
            log.error( "Failed to create customer: {}", name,e);
            throw e;
        }
    }

    public boolean customerAccountExists(String customerName, String debtorIban) {
        String local = "/accounts/%s";
        String path = base + local;
        String uri = String.format( path, debtorIban );

        try{
            testApiClient.get(uri, String.class);
            return true;
        } catch( Exception e ){
            return false;
        }
    }

    public Optional<AccountDetails> getCustomerAccount(CustomerDetails customer, String debtorIban) {
        String local = "/accounts/%s";
        String path = base + local;
        String uri = String.format( path, debtorIban );

        try {
            var accountDetails = testApiClient.get(uri, AccountDetails.class);
            if( accountDetails.customerId().value().compareToIgnoreCase(customer.customerId().value()) == 0 ){
                return Optional.of(accountDetails);
            }else {
                return Optional.empty();
            }
        } catch( Exception e ){
            return Optional.empty();
        }
    }

    public AccountId createCustomerAccount(CustomerId id, String iban, Currency currency) {
        String local = "/customers/%s/accounts";
        String path = base + local;
        String uri = String.format( path, id.value() );
        var request = new CreateAccountRequest(iban, currency.getCurrencyCode());

        try{
            return testApiClient.post(uri, request, AccountId.class);
        } catch( RuntimeException e ){
            log.error( "Failed to create customer account: {} {}", id, iban ,e);
            throw e;
        }
    }

    public Optional<ProductEntitlement> getCustomerEntitlement(CustomerId id, ProductCode productCode) {
        String local = "/customers/%s/productEntitlements";
        String path = base + local;
        String uri = String.format( path, id.value() );

        var typeRef = new ParameterizedTypeReference<List<ProductEntitlement>>() {};

        try {
            List<ProductEntitlement> productEntitlements = testApiClient.get(uri,typeRef);

            return productEntitlements.stream()
                    .filter(pe-> pe.productCode().equals(productCode))
                    .findFirst();
        } catch( Exception e ){
            return Optional.empty();
        }
    }

    public ProductEntitlementId createCustomerEntitlement(CustomerId id, String productCode) {
        String local = "/customers/%s/productEntitlements";
        String path = base + local;
        String uri = String.format( path, id.value() );

        var request = new CreateProductEntitlementRequest(productCode);

        try {
            return testApiClient.post(uri,request, ProductEntitlementId.class);

        } catch( RuntimeException e ){
            log.error( "Failed to create customer product entitlement: {} {}", id, productCode ,e);
            throw e;
        }
    }
}
