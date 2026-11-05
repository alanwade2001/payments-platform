package com.acme.payments.initiation.internal.ingest;

import com.acme.payments.initiation.internal.ingest.business.DailyFileLimitRule;
import com.acme.payments.initiation.internal.ingest.content.AmountRangeRule;
import org.hibernate.tool.schema.spi.SchemaValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class Pain001AcceptanceServiceTest {

    Pain001AcceptanceService importService;

    @BeforeEach
    void setUp() throws Exception {



        Pain001Configuration pain001Configuration = new Pain001Configuration();

        var schemaValidator = new Pain001SchemaValidator(pain001Configuration.pain001Schema());
        var contentValidator = new Pain001ContentValidator(List.of(new AmountRangeRule()));
        var businessValidator = new Pain001BusinessValidator(List.of(new DailyFileLimitRule()));
        importService = new Pain001AcceptanceService(schemaValidator
                , contentValidator
                , businessValidator
                , pain001Configuration.pain001Marshaller()
        );
    }

    @Test
    void importsValidPain001() throws IOException {

        var xml = load("pain001/valid-pain001.xml");

        try {
            var document = importService.importXml(xml);
            assertThat(document).isNotNull();
            assertThat(
                    document
                            .getCstmrCdtTrfInitn()
                            .getGrpHdr()
                            .getMsgId())
                    .isEqualTo("MSG-001");

        } catch (Pain001ImportException ex) {

            ex.printStackTrace();

            if (ex.getCause() != null) {
                ex.getCause().printStackTrace();
            }

            throw ex;
        }


    }

    @Test
    void rejectsPain001ThatFailsXsdValidation()
            throws IOException {

        var xml = load("pain001/invalid-pain001.xml");

        assertThatThrownBy(
                () ->
                        System.out.println(importService.importXml(xml)))
                .isInstanceOf(Pain001ImportException.class);
    }

    private String load(String path) throws IOException {
        return new ClassPathResource(path)
                .getContentAsString(StandardCharsets.UTF_8);
    }
}