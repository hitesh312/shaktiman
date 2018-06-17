package com.mcbc.shaktiman.common;

public class Card {
    private CardNumber cardNumber;
    private CardType cardType;

    public Card(CardNumber cardNumber, CardType cardType) {
        this.cardNumber = cardNumber;
        this.cardType = cardType;
    }

    public void setCardNumber(CardNumber cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    @Override
    public String toString() {
        return cardNumber + " of " + cardType;
    }
}
