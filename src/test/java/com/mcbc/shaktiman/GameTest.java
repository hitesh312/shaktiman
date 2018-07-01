package com.mcbc.shaktiman;

import com.mcbc.shaktiman.common.User;
import com.mcbc.shaktiman.game.GameEngine;
import com.mcbc.shaktiman.game.teendopanch.GameImpl;
import com.mcbc.shaktiman.game.teendopanch.GameState;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class GameTest {

    @Test
    public void generateGameIDTest() {
        System.out.println(GameEngine.getGameEngine().newGame());
    }

    @Test
    public void GameImplTest() {
        GameImpl game = new GameImpl("test");
        User user1 = new User("userid1", "name1", "email1", "pass1");
        User user2 = new User("userid2", "name2", "email2", "pass2");
        User user3 = new User("userid3", "name3", "email3", "pass3");
        game.join(user1);
        game.join(user2);
        game.join(user3);
        Map<String, String> args = new HashMap<>();

        for (int i = 0; i < 4; i++) {
            System.out.println("********************** starting game number " + (i + 1) + " *********************");
            System.out.println("---------------------- setting trump ----------------------------------------");
            args.put("userid", "userid1");
            args.put("trump", "SPADE");
            game.play(args);
            args.put("userid", "userid2");
            args.put("trump", "DIAMOND");
            game.play(args);
            args.put("userid", "userid3");
            args.put("trump", "CLUB");
            game.play(args);
            System.out.println("---------------------- starting game ----------------------------------------");
            while (true) {
                if (game.getGameState() == GameState.FINISH) break;
                args.put("userid", "userid1");
                args.put("card", game.getCards().get(0).get(0).toString());
                game.play(args);
                if (game.getGameState() == GameState.FINISH) break;
                args.put("userid", "userid2");
                args.put("card", game.getCards().get(1).get(0).toString());
                game.play(args);
                if (game.getGameState() == GameState.FINISH) break;
                args.put("userid", "userid3");
                args.put("card", game.getCards().get(2).get(0).toString());
                game.play(args);
            }
            System.out.println("---------------------- ready for next game -----------------------------");
            args.put("userOnline", "true");
            args.remove("card");
            args.remove("trump");
            args.put("userid", "userid1");
            game.play(args);
            args.put("userid", "userid2");
            game.play(args);
            args.put("userid", "userid3");
            game.play(args);
            System.out.println("--------------------------------------------");
        }
    }
}
