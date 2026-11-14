package com.acme.payments.engine.schemes.sepa.ct;

import iso.std.iso._20022.tech.xsd.pacs_008_001.SettlementMethod1Code;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("payments.schemes.sepa-ct")
public record SepaCTProperties(
        String instructingAgentBic,
        String instructedAgentBic,
        SettlementMethod1Code settlementMethod
) {
}
