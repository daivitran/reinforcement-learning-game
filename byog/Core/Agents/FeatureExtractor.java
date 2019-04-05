package byog.Core.Agents;

import byog.Core.Environment.GameState;

import java.io.Serializable;
import byog.Core.WorldGenerator.Position;

public class FeatureExtractor implements Serializable {
    public static final int numOfFeatures = 4;

    public static double[] getFeatures(GameState state, char action, int agentIndex) {
        double[] features = new double[numOfFeatures];
        FeatureExtractor.populate(features, state, action, agentIndex);
        return features;
    }

    public static void populate(double[] features, GameState state, char action, int agentIndex) {
        GameState nextState = state.getNextState(agentIndex, action);

        // feature 1
        Agent nearest = state.getNearestAgent(agentIndex);
        features[0] = nearest.getPos().distanceSquaredTo(nextState.getAgent(agentIndex).getPos());
        //features[0] = 1;
        // feature 2
        features[1] = nextState.getHealth(agentIndex);

        // feature 3
        features[2] = (double) nextState.getTotalOtherHealth(agentIndex) / nextState.getNumOfAgents();

        // feature 4
        /*
        Position[] bullets = nextState.getOtherBulletPosition(agentIndex);
        Position botPos = nextState.getPosition(agentIndex);
        int minDistance = Integer.MAX_VALUE;
        for(int j = 0; j < bullets.length; ++j) {
            if(bullets[j] == null) { continue; }
            int distance = botPos.distanceSquaredTo(bullets[j]);
            if(distance < minDistance) {
                minDistance = distance;
            }
        }
        features[3] = minDistance;*/
        features[3] = 4;
    }
}
