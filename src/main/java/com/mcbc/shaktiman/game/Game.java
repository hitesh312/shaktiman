package com.mcbc.shaktiman.game;

import com.mcbc.shaktiman.common.Card;
import com.mcbc.shaktiman.common.User;

import java.util.List;
import java.util.Map;

public abstract class Game {
    protected String gameID;
    protected List<User> players;
    protected List<List<Card>> cards;

    public String getGameID() {
        return gameID;
    }

    public List<List<Card>> getCards() {
        return cards;
    }

    public List<User> getPlayers() {
        return players;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    protected int numberOfPlayers;

    public synchronized boolean join(User user) {
        if (players.size() == numberOfPlayers) return false;
        players.add(user);
        return true;
    }

    public abstract void play(Map<String, String> args);

}
