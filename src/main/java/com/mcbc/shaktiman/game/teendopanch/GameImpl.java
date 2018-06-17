package com.mcbc.shaktiman.game.teendopanch;

import com.mcbc.shaktiman.common.Card;
import com.mcbc.shaktiman.common.CardType;
import com.mcbc.shaktiman.common.Deck;
import com.mcbc.shaktiman.common.User;
import com.mcbc.shaktiman.game.Game;

import java.util.ArrayList;
import java.util.Map;

public class GameImpl extends Game {

    private GameState gameState;
    private CardType trump = null;
    private Card[] currentCard;
    private int startingUser = 0;
    private int turn = 0;
    private int[] userPoints;

    public GameImpl(String gameID) {
        startingUser = 0;
        turn = 0;
        userPoints = new int[3];
        currentCard = new Card[3];
        currentCard[0] = null;
        currentCard[1] = null;
        currentCard[2] = null;
        userPoints[0] = 0;
        userPoints[1] = 0;
        userPoints[2] = 0;
        numberOfPlayers = 3;
        players = new ArrayList<>();
        this.gameID = gameID;
        cards = Deck.getRandomShuffle(3, Deck.HALFDECK);
        gameState = GameState.CREATED;
        publish();
    }

    @Override
    public synchronized boolean join(User user) {
        boolean res = super.join(user);
        if (res) {
            gameState = GameState.valueOf("USER" + players.size() + "JOINED");
            publish();
            if (players.size() == 3) {
                gameState = GameState.STARTED;
                publish();
                gameState = GameState.NEWHAND;
                publish();
            }
        }
        return res;
    }


    private void publish() {
    }

    @Override
    public void play(Map<String, String> args) {
        switch (gameState) {
            case NEWHAND: {
                if (args.containsKey("userid") && args.containsKey("trump") && args.get("userid") == players.get(turn).getUserid()) {
                    trump = CardType.valueOf(args.get("trump"));
                    setExchangeState();
                    publish();
                }
                break;
            }
            case EXCHANGE01: {
                if (args.containsKey("userid")) {
                    exchange(0, 1, args);
                    setExchangeState();
                    publish();
                }
                break;
            }
            case EXCHANGE12: {
                if (args.containsKey("userid")) {
                    exchange(1, 2, args);
                    setExchangeState();
                    publish();
                }
                break;
            }
            case EXCHANGE02: {
                if (args.containsKey("userid")) {
                    exchange(0, 2, args);
                    setExchangeState();
                    publish();
                }
                break;
            }
        }
    }

    private void setExchangeState() {
        if (userPoints[0] != 0 && userPoints[1] != 0) gameState = GameState.EXCHANGE01;
        else if (userPoints[1] != 0 && userPoints[2] != 0) gameState = GameState.EXCHANGE12;
        else if (userPoints[0] != 0 && userPoints[2] != 0) gameState = GameState.EXCHANGE02;
        else {
            userPoints[startingUser] = 5;
            userPoints[(startingUser + 1) % 3] = 3;
            userPoints[(startingUser + 2) % 3] = 2;
            gameState = GameState.HAND1;
        }
    }

    private void exchange(int i, int j, Map<String, String> args) {
        if (args.get("userid") == players.get(i).getUserid() && args.containsKey("card")) {
            currentCard[i] = Deck.getCard(args.get("card"));
        }
        if (args.get("userid") == players.get(j).getUserid() && args.containsKey("card")) {
            currentCard[j] = Deck.getCard(args.get("card"));
        }
        if (currentCard[i] != null && currentCard[j] != null) {
            cards.get(i).remove(currentCard[i]);
            cards.get(j).remove(currentCard[j]);
            cards.get(i).add(currentCard[j]);
            cards.get(j).add(currentCard[i]);
            if (userPoints[i] > 0) {
                userPoints[i] -= 1;
                userPoints[j] += 1;
            } else {
                userPoints[i] += 1;
                userPoints[j] -= 1;
            }
            currentCard[0] = null;
            currentCard[1] = null;
            currentCard[2] = null;
        }
    }
}
