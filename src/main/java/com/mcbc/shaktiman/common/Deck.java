package com.mcbc.shaktiman.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Deck {
    public static int FULLDECK = 0;
    public static int HALFDECK = 1;
    private static HashMap<String, Card> cards;
    private static List<Card> fullDeck;
    private static List<Card> halfDeck;

    static {
        halfDeck = new ArrayList<>();
        fullDeck = new ArrayList<>();
        cards = new HashMap<>();
        for (CardType type : CardType.values()) {
            for (CardNumber number : CardNumber.values()) {
                Card card = new Card(number, type);
                fullDeck.add(card);
                cards.put(card.toString(), card);
                if (number.compareTo(CardNumber.SEVEN) > 0 ||
                        (number == CardNumber.SEVEN &&
                                (type == CardType.SPADE || type == CardType.HEART)
                        )) {
                    halfDeck.add(card);
                }
            }
        }
    }

    public static List<Card> getFullDeck() {
        return fullDeck;
    }

    public static List<Card> getHalfDeck() {
        return halfDeck;
    }

    public static synchronized List<List<Card>> getRandomShuffle(int n, int deckType) {
        List<Card> deck = null;
        if (deckType == HALFDECK) deck = halfDeck;
        else if (deckType == FULLDECK) deck = fullDeck;
        if (deck.size() % n != 0) throw new RuntimeException("Cannot divide deck among " + n + " players");
        Collections.shuffle(deck);
        int handSize = deck.size() / n;
        List<List<Card>> result = new ArrayList<>();
        for (int i = 0; i < deck.size(); i += handSize) {
            result.add(new ArrayList<>(deck.subList(i, i + handSize)));
        }
        return result;
    }

    public static Card getCard(String card) {
        return cards.get(card);
    }
}
