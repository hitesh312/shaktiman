package com.mcbc.shaktiman.game.teendopanch;

import com.mcbc.shaktiman.common.Card;
import com.mcbc.shaktiman.common.CardType;
import com.mcbc.shaktiman.common.Deck;
import com.mcbc.shaktiman.common.User;
import com.mcbc.shaktiman.game.Game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GameImpl extends Game {

    private GameState gameState;
    private CardType trump = null;
    private CardType chance = null;
    private Card[] currentCard;
    private int startingUser;
    private int turn;
    private int[] userPoints;
    private boolean[] userOnline;

    public GameState getGameState() {
        return gameState;
    }

    public GameImpl(String gameID) {
        startingUser = 0;
        turn = 0;
        userPoints = new int[3];
        currentCard = new Card[3];
        userOnline = new boolean[3];
        players = new ArrayList<>();
        currentCard[0] = null;
        currentCard[1] = null;
        currentCard[2] = null;
        userPoints[0] = 0;
        userPoints[1] = 0;
        userPoints[2] = 0;
        userOnline[0] = false;
        userOnline[1] = false;
        userOnline[2] = false;
        numberOfPlayers = 3;
        this.gameID = gameID;
        cards = Deck.getRandomShuffle(3, Deck.HALFDECK);
        gameState = GameState.CREATED;
        publish();
    }

    @Override
    public synchronized boolean join(User user) {
        boolean res = super.join(user);
        if (res) {
            gameState = GameState.valueOf("USER" + (players.size() - 1) + "JOINED");
            userOnline[players.size() - 1] = true;
            publish();
            if (players.size() == 3) {
                gameState = GameState.NEWHAND;
                publish();
            }
        }
        return res;
    }


    private void publish() {
        System.out.println(this);
    }

    @Override
    public String toString() {
        StringBuilder cardStr = new StringBuilder();
        cardStr.append('[');
        for (List<Card> set : cards) {
            cardStr.append('[');
            for (Card c : set)
                cardStr.append('\'').append(c).append("',");
            if (cardStr.charAt(cardStr.length() - 1) == ',') cardStr.deleteCharAt(cardStr.length() - 1);
            cardStr.append("],");
        }
        if (cardStr.charAt(cardStr.length() - 1) == ',') cardStr.deleteCharAt(cardStr.length() - 1);
        cardStr.append(']');

        StringBuilder playerStr = new StringBuilder();
        playerStr.append('[');
        for (User user : players)
            playerStr.append("{'userid':'").append(user.getUserid())
                    .append("','name':'").append(user.getName()).append("'},");
        if (playerStr.charAt(playerStr.length() - 1) == ',') playerStr.deleteCharAt(playerStr.length() - 1);
        playerStr.append(']');

        StringBuilder currentCardStr = new StringBuilder();
        currentCardStr.append("['").append(currentCard[0]).append("','")
                .append(currentCard[1]).append("','").append(currentCard[2]).append("']");

        return "{" +
                "'gameState':'" + gameState +
                "', 'trump':'" + trump +
                "', 'chance':'" + chance +
                "', 'currentCard':" + currentCardStr +
                ", 'startingUser':" + startingUser +
                ", 'turn':" + turn +
                ", 'userPoints':" + Arrays.toString(userPoints) +
                ", 'userOnline':" + Arrays.toString(userOnline) +
                ", 'gameID':'" + gameID +
                "', 'players':" + playerStr.toString() +
                ", 'cards':" + cardStr.toString() +
                '}';
    }

    @Override
    public void play(Map<String, String> args) {
        if (args.containsKey("userOnline") && args.containsKey("userid")
                && args.get("userOnline").equals("false")) {
            int userNum = getUserNumber(args.get("userid"));
            userOnline[userNum] = false;
            gameState = GameState.ENDED;
            publish();
            return;
        }
        switch (gameState) {
            case NEWHAND: {
                if (args.containsKey("userid") && args.containsKey("trump")
                        && args.get("userid").equals(players.get(turn).getUserid())) {
                    trump = CardType.valueOf(args.get("trump"));
                    setExchangeState();
                    publish();
                }
                break;
            }
            case EXCHANGE01: {
                if (args.containsKey("userid") && args.containsKey("card")
                        && (args.get("userid").equals(players.get(0).getUserid())
                        || args.get("userid").equals(players.get(1).getUserid())
                )) {
                    if (exchange(0, 1, args)) {
                        publish();
                        resetCards();
                    }
                    setExchangeState();
                    publish();
                }
                break;
            }
            case EXCHANGE12: {
                if (args.containsKey("userid") && args.containsKey("card")
                        && (args.get("userid").equals(players.get(2).getUserid())
                        || args.get("userid").equals(players.get(1).getUserid())
                )) {
                    if (exchange(1, 2, args)) {
                        publish();
                        resetCards();
                    }
                    setExchangeState();
                    publish();
                }
                break;
            }
            case EXCHANGE02: {
                if (args.containsKey("userid") && args.containsKey("card")
                        && (args.get("userid").equals(players.get(0).getUserid())
                        || args.get("userid").equals(players.get(2).getUserid())
                )) {
                    if (exchange(0, 2, args)) {
                        publish();
                        resetCards();
                    }
                    setExchangeState();
                    publish();
                }
                break;
            }
            case HAND1:
            case HAND2:
            case HAND3:
            case HAND4:
            case HAND5:
            case HAND6:
            case HAND7:
            case HAND8:
            case HAND9:
            case HAND10: {
                if (args.containsKey("userid") && args.containsKey("card")
                        && args.get("userid").equals(players.get(turn).getUserid())) {
                    int res = playHand(args);
                    if (res != -1) {
                        userPoints[res]--;
                        turn = res;
                        publish();
                        resetCards();
                        chance = null;
                        gameState = GameState.values()[gameState.ordinal() + 1];
                    } else {
                        turn = (turn + 1) % 3;
                    }
                    publish();
                }
                break;
            }
            case FINISH: {
                if (args.containsKey("userOnline") && args.containsKey("userid") &&
                        args.get("userOnline").equals("true")) {
                    int userNum = getUserNumber(args.get("userid"));
                    userOnline[userNum] = true;
                    if (userOnline[0] && userOnline[1] && userOnline[2]) {
                        startingUser = (startingUser + 1) % 3;
                        turn = startingUser;
                        cards = Deck.getRandomShuffle(3, Deck.HALFDECK);
                        trump = null;
                        chance = null;
                        gameState = GameState.NEWHAND;
                        publish();
                    }
                }
                break;
            }
        }
    }

    private int playHand(Map<String, String> args) {
        int userNum = getUserNumber(args.get("userid"));
        currentCard[userNum] = Deck.getCard(args.get("card"));
        if (currentCard[(userNum + 1) % 3] == null
                && currentCard[(userNum + 2) % 3] == null) {
            chance = currentCard[userNum].getCardType();
        } else if (currentCard[(userNum + 1) % 3] != null
                && currentCard[(userNum + 2) % 3] != null) {
            cards.get(0).remove(currentCard[0]);
            cards.get(1).remove(currentCard[1]);
            cards.get(2).remove(currentCard[2]);
            return getWinner();
        }
        return -1;
    }

    private int getWinner() {
        int val0 = getCardValue(0);
        int val1 = getCardValue(1);
        int val2 = getCardValue(2);
        if (val0 > val1 && val0 > val2) return 0;
        if (val1 > val2) return 1;
        return 2;
    }

    private int getCardValue(int id) {
        Card tmp = currentCard[id];
        int val = tmp.getCardNumber().ordinal();
        if (tmp.getCardType().equals(trump)) val += 1000;
        if (tmp.getCardType().equals(chance)) val += 100;
        return val;
    }

    private int getUserNumber(String userid) {
        if (players.get(0).getUserid().equals(userid)) return 0;
        if (players.get(1).getUserid().equals(userid)) return 1;
        if (players.get(2).getUserid().equals(userid)) return 2;
        return -1;
    }

    private void setExchangeState() {
        if (userPoints[0] > 0 && userPoints[1] < 0 ||
                userPoints[0] < 0 && userPoints[1] > 0) gameState = GameState.EXCHANGE01;
        else if (userPoints[1] > 0 && userPoints[2] < 0 ||
                userPoints[1] < 0 && userPoints[2] > 0) gameState = GameState.EXCHANGE12;
        else if (userPoints[0] > 0 && userPoints[2] < 0 ||
                userPoints[0] < 0 && userPoints[2] > 0) gameState = GameState.EXCHANGE02;
        else {
            userPoints[startingUser] = 5;
            userPoints[(startingUser + 1) % 3] = 3;
            userPoints[(startingUser + 2) % 3] = 2;
            gameState = GameState.HAND1;
        }
    }

    private boolean exchange(int i, int j, Map<String, String> args) {
        if (args.get("userid").equals(players.get(i).getUserid())) {
            currentCard[i] = Deck.getCard(args.get("card"));
        }
        if (args.get("userid").equals(players.get(j).getUserid())) {
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
            return true;
        }
        return false;
    }

    private void resetCards() {
        currentCard[0] = null;
        currentCard[1] = null;
        currentCard[2] = null;
    }
}
