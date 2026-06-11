package akendo.orderservice.domain;

public enum OrderOutboxStatus {
    // Event is stored in the DB and waits for the outbox publisher.
    PENDING,

    // Event was successfully sent to Kafka and should not be sent again.
    SENT,

    // Last publish attempt failed. A retry job can pick it up later.
    FAILED
}
