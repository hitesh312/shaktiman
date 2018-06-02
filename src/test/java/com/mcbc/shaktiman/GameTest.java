package com.mcbc.shaktiman;

import com.mcbc.shaktiman.game.GameEngine;
import org.junit.Test;

public class GameTest {

    @Test
    public void generateGameIDTest() {
        System.out.println(GameEngine.getGameEngine().newGame());
    }
}
