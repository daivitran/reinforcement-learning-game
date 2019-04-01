package byog.Core.Agents;

import byog.Core.Environment.GameState;
import byog.Core.Interactivity.Gun;
import byog.TileEngine.TETile;

import java.io.Serializable;
import java.util.Random;

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
        agent.x = this.x;
        agent.y = this.y;
        agent.health = this.health;
        agent.direction= this.direction;
        agent.health = this.health;
        agent.alternate = this.alternate;
        agent.agentIndex = this.agentIndex;
        agent.isAlive = this.isAlive;
        agent.r = this.r;
        return agent;
    }

    @Override
    public char nextAction(GameState state) {
        if(delay == 0) {
            delay = 20;
            if (isAlive == 0) {
                return '!';
            }
            char[] legalActs = getLegalActions();
            int actionIndex = r.nextInt(legalActs.length);
            return legalActs[actionIndex];
        }
        else {
            --delay;
            return '!';
        }
    }
}
