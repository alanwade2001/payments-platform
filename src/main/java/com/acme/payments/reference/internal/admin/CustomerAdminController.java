package com.acme.payments.reference.internal.admin;

import com.acme.payments.model.Iban;
import com.acme.payments.reference.*;
import com.acme.payments.reference.internal.account.AccountService;
import com.acme.payments.reference.internal.customer.CustomerService;
import com.acme.payments.reference.internal.product.ProductEntitlementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/reference")
@RequiredArgsConstructor
public class CustomerAdminController {

    private final CustomerService customerService;
    private final AccountService accountService;
    private final ProductEntitlementService productEntitlementService;

    @PostMapping("/customers")
    public CustomerId create(@RequestBody CreateCustomerRequest request) {
        return customerService.create(request.name());
    }

    @GetMapping("/customers/{customerId}")
    public ResponseEntity<CustomerDetails> getCustomerDetails(@PathVariable("customerId") CustomerId customerId) {
        var result = customerService.getCustomer(customerId);
        return ResponseEntity.of(result);
    }

    @PostMapping("/customers/{customerId}/accounts")
    public AccountId addAccount(
            @PathVariable CustomerId customerId,
            @RequestBody CreateAccountRequest request) {

        return accountService.addAccount(
                customerId,
                Iban.of(request.iban()),
                request.currency()
        );
    }

    @GetMapping("/accounts/{accountId}")
    public ResponseEntity<AccountDetails> getAccountDetails(@PathVariable("accountId") AccountId accountId) {
        var result = accountService.getAccount(accountId);
        return ResponseEntity.of(result);
    }



    @PostMapping("/{customerId}/productEntitlements")
    public ProductEntitlementId addProductEntitlement(
            @PathVariable CustomerId customerId,
            @RequestBody CreateProductEntitlementRequest request) {

        return productEntitlementService.addProductEntitlement(
                customerId,
                ProductCode.valueOf(request.productCode())
        );
    }
}
