package com.acme.payments.engine.internal;

import com.acme.payments.engine.SchemeId;

public class UnsupportedSchemeException extends RuntimeException {
    public UnsupportedSchemeException(SchemeId schemeId) {
        super("Unsupported payment schemeId: " + schemeId);
    }
}
