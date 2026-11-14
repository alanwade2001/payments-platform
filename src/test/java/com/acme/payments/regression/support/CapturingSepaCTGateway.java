package com.acme.payments.regression.support;

import com.acme.payments.engine.schemes.sepa.ct.SepaCTGateway;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Primary
public final class CapturingSepaCTGateway implements SepaCTGateway {

    private final List<byte[]> messages = new ArrayList<>();

    @Override
    public void send(byte[] message) {

        messages.add(message.clone());
    }

    public byte[] lastMessage() {
        if (messages.isEmpty()) {
            throw new IllegalStateException(
                    "No pacs.008 message has been sent to the test gateway");
        }

        return messages.getLast().clone();
    }

    public int messageCount() {
        return messages.size();
    }

    public void reset() {
        messages.clear();
    }
}