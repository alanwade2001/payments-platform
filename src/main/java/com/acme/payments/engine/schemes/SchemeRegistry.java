package com.acme.payments.engine.schemes;

import com.acme.payments.engine.SchemeId;
import com.acme.payments.engine.internal.UnsupportedSchemeException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class SchemeRegistry {

    private final Map<SchemeId, Scheme> schemes;

    public SchemeRegistry(List<Scheme> schemes) {
        this.schemes = schemes.stream()
                .collect(Collectors.toUnmodifiableMap(
                        Scheme::id,
                        Function.identity()
                ));
    }

    public Scheme get(SchemeId schemeId) {
        Scheme scheme = schemes.get(schemeId);

        if (scheme == null) {
            throw new UnsupportedSchemeException(schemeId);
        }

        return scheme;
    }
}
