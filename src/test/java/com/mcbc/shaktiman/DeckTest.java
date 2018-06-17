package com.mcbc.shaktiman;

import com.mcbc.shaktiman.common.Deck;
import org.junit.Test;

public class DeckTest {

    @Test
    public void halfDeck() {
        System.out.println(Deck.getRandomShuffle(3, Deck.HALFDECK));
    }

    @Test
    public void fullDeck() {
        System.out.println(Deck.getRandomShuffle(4, Deck.FULLDECK));
    }

}
