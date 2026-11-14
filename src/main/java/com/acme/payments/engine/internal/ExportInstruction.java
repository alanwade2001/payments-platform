package com.acme.payments.engine.internal;

import com.acme.payments.engine.SchemeId;

import java.time.LocalDate;

public record ExportInstruction(SchemeId schemeId, LocalDate executionDate) {
}
