package com.mcbc.shaktiman.game;

import com.mcbc.shaktiman.common.Constant;
import com.mcbc.shaktiman.game.teendopanch.GameImpl;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GameEngine {

    private static GameEngine gameEngine = null;
    private Map<String, String> userSessions;
    private Map<String, Game> activeGames;
    private Random rand;

    private GameEngine() {
        userSessions = new HashMap<>();
        activeGames = new HashMap<>();
        rand = new Random();
    }

    public String newGame() {
        String key;
        do {
            key = Constant.wordList.get(rand.nextInt(Constant.wordCount)) + "_" + Constant.wordList.get(rand.nextInt(Constant.wordCount));
        } while (activeGames.containsKey(key));
        activeGames.put(key, new GameImpl(key));
        return key;
    }

    public void joinGame(String gameID) {
        return;
    }

    public String joinRandomGame() {
        return "";
    }

    public static GameEngine getGameEngine() {
        if (gameEngine == null) {
            gameEngine = new GameEngine();
        }
        return gameEngine;
    }

    public String createSession(String userid) {
        String sessionKey = RandomStringUtils.random(30, true, true);
        userSessions.put(userid, sessionKey);
        return sessionKey;
    }

    public void verifySession(String userid, String sessionKey) {
        if (!sessionKey.equals(userSessions.getOrDefault(userid, null)))
            throw new RuntimeException("Session expired, please login again");
    }
}
