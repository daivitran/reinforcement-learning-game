package byog.Core.Environment;

import byog.Core.Agents.Agent;
import byog.Core.Agents.ApproximateQAgent;
import byog.Core.WorldGenerator.MapGenerator;
import byog.TileEngine.TETile;

import java.util.Random;

public class Environment {
    private GameState thisState;
    private GameState lastState;

    public Environment(TETile [][] map, Agent[] agents) {
        this.thisState = new GameState(map, agents);
    }


    public void runEpisode() {
        int numOfAgents = thisState.getNumOfAgents();
        while(!thisState.isTerminal()) {
            for (int i = 1; i < numOfAgents; ++i) {
                char action = thisState.nextAction(i);
                lastState = thisState;
                thisState = thisState.getNextState(i, action);
                int reward = thisState.getThisStateReward(i);
                Agent agent = thisState.getAgent(i);
                ((ApproximateQAgent) agent).observeTransition(lastState, action, thisState, reward);
            }
        }
    }


}
