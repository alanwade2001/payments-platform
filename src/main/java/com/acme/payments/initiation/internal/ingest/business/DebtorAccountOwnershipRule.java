package com.acme.payments.initiation.internal.ingest.business;

import com.acme.payments.initiation.internal.ingest.Pain001CustomerIdResolver;
import com.acme.payments.initiation.internal.ingest.content.Pain001ContentRule;
import com.acme.payments.reference.AccountStatus;
import com.acme.payments.reference.Accounts;
import com.acme.payments.reference.Iban;
import iso.std.iso._20022.tech.xsd.pain_001_001.Document;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class DebtorAccountOwnershipRule implements Pain001BusinessRule {

    private final Accounts accounts;

    @Override
    public List<String> validate(Document document) {

        var errors = new ArrayList<String>();

        var customerId =
                Pain001CustomerIdResolver.resolve(document);

        var debtorIbans = document.getCstmrCdtTrfInitn()
                .getPmtInves()
                .stream()
                .map(p -> p.getDbtrAcct().getId().getIBAN())
                .map(Iban::new)
                .toList();

        for (var debtorAccount : debtorIbans) {

            var accountDetails =
                    accounts.getAccount(customerId, debtorAccount);

            if (accountDetails == null) {
                errors.add(
                        "debtor account: %s not found for customer: %s"
                                .formatted(
                                        debtorAccount,
                                        customerId));

            } else if (!accountDetails.status()
                    .equals(AccountStatus.ACTIVE)) {

                errors.add(
                        "debtor account: %s status: %s"
                                .formatted(
                                        debtorAccount,
                                        accountDetails.status()));
            }
        }

        return errors;
    }
}