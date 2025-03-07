package com.example.Products.model;

public enum BasketConfirmation {
    PENDING(0),   // 0: Εκκρεμεί
    APPROVED(1),  // 1: Εγκρίθηκε
    DECLINED(2);  // 2: Απορρίφθηκε

    private final int value;

    // Κατασκευαστής για το enum που αντιστοιχεί την τιμή του enum
    BasketConfirmation(int value) {
        this.value = value;
    }

    // Λειτουργία για την επιστροφή της τιμής του enum
    public int getValue() {
        return value;
    }

    // Λειτουργία για να επιστρέφει το enum από τη τιμή (0, 1, 2)
    public static BasketConfirmation fromValue(int value) {
        for (BasketConfirmation status : BasketConfirmation.values()) {
            if (status.getValue() == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unexpected value: " + value);
    }
}
