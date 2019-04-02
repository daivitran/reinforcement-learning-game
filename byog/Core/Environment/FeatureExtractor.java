package byog.Core.Environment;

import byog.Core.Agents.Agent;
import byog.Core.WorldGenerator.Position;

import java.io.Serializable;

public class FeatureExtractor implements Serializable {
    public static final int numOfFeatures =  5;

    public static double[] getFeatures(GameState state, char action, int agentIndex) {
        double[] features = new double[numOfFeatures];
        FeatureExtractor.populate(features, state, action, agentIndex);
        return features;
    }

    public static void populate(double[] features, GameState state, char action, int agentIndex) {
        Agent nearest = null;
        for (int i = 0; i < numOfFeatures; ++i) {
//            System.out.println("Start: " + i);
//            double feature = 0.0;
//
//            GameState nextState = state.getNextState(agentIndex, action);
//
//            if (i == 0) {
//                // manhattan distance from bot the other
//                nearest = nextState.getNearestAgent(agentIndex);
//                feature = nearest.getPos().distanceSquaredTo(nextState.getAgent(agentIndex).getPos());
//            } else if (i == 1) {
//                // this agent's health
//                feature = nextState.getHealth(agentIndex);
//            } else if (i == 2) {
//                // nearest agent's health;
//                feature = nearest.getHealth();
//            } else if (i == 3) {
//                // manhattan distance to nearest bullet
//                Position[] bullets = nextState.getOtherBulletPosition(agentIndex);
//                Position botPos = nextState.getPosition(agentIndex);
//                int minDistance = Integer.MAX_VALUE;
//                for(int j = 0; j < bullets.length; ++j) {
//                    if(bullets[j] == null) { continue; }
//                    int distance = botPos.distanceSquaredTo(bullets[j]);
//                    if( distance < minDistance) {
//                        minDistance = distance;
//                    }
//                }
//                feature = minDistance;
//
//            } else if (i == 4) {
//                // literally
////                System.out.println("Here");
//                feature = nextState.isBulletComingToBot(agentIndex);
//            }

//            features[i] = feature;
            features[i] = i;
//            System.out.println("End: " + i);
        }
    }
}
