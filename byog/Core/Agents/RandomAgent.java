package byog.Core.Agents;

import byog.Core.Interactivity.Gun;
import byog.Core.RandomUtils;
import byog.TileEngine.TETile;

import java.io.Serializable;
import java.util.Random;

import static byog.TileEngine.TETile.copyOf;

public class RandomAgent extends Bot implements Serializable {
    private Random r;
    public RandomAgent(TETile[][] map, int agentIndex) {
        super(map, agentIndex);
        r = new Random(1234567);
    }

    @Override
    public RandomAgent clone(TETile [][] map) {
        RandomAgent agent = new RandomAgent(map, agentIndex);
        agent.delay = this.delay;
        agent.setGun(new Gun(this.gun, map));
        return agent;
    }

    @Override
    public char nextAction(GameState state) {
        if(delay == 0) {
            delay = 10;
            if (isAlive == 0) {
                return '!';
            }
            char[] legalActs = getLegalActions();
            System.out.println("Actions: ");
            for(int i = 0; i < legalActs.length; ++i) {
                System.out.print(legalActs[i] + " ");
            }
            System.out.println();
            int actionIndex = RandomUtils.uniform(r, 0, legalActs.length);
            return legalActs[actionIndex];
        }
        else {
            --delay;
            return '!';
        }
    }
}
