package com.nerdery.ecommerce.service;

public interface WebhookService {
    void handleEvent(String payload, String signHeader);
}
