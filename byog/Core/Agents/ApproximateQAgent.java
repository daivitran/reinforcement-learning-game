package byog.Core.Agents;

import byog.Core.Environment.FeatureExtractor;
import byog.Core.Environment.GameState;
import byog.Core.Interactivity.Gun;
import byog.TileEngine.TETile;

import java.io.Serializable;

public class ApproximateQAgent extends Bot implements Serializable {
    public static final double DISCOUNT = 0.1;
    public static final double ALPHA = 0.1;
    private int reward;

    private double[] weights = new double[FeatureExtractor.numOfFeatures];
    public ApproximateQAgent(TETile[][] map, int agentIndex) {
        super(map, agentIndex);
        for(int i = 1; i <= 5; ++i) {
            weights[i-1] = 0.1 * i;
        }
        reward = 0;
    }

    public ApproximateQAgent(ApproximateQAgent other) {
        super(other.map, other.agentIndex);
        for(int i = 0; i < weights.length; ++i) {
            this.weights[i] = other.weights[i];
        }
        this.delay = other.delay;
    }

    @Override
    public ApproximateQAgent clone(TETile [][] map) {
        ApproximateQAgent agent = new ApproximateQAgent(map, this.agentIndex);
        for (int i = 0; i < agent.weights.length; ++i) {
            agent.weights[i] = this.weights[i];
        }
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
        agent.reward = this.reward;
        return agent;
    }

    @Override
    public char nextAction(GameState state) {
        if(delay == 0) {
            delay = 20;
            char action = getAction(state);
            return action;
        }
        else {
            --delay;
            return '!';
        }
    }

    private char getAction(GameState state) {
        // May handle exploration over here !
        if(isAlive == 0) {
            return '!';
        }
        char action = getPolicy(state);
        return action;
    }

    private char getPolicy(GameState state) {
        return computeActionFromQValues(state);
    }

    private char computeActionFromQValues(GameState state) {
        char[] legalActions = getLegalActions();
        char bestAction = 'p';
        double bestValue = getQValue(state, legalActions[0]);
        for (char action : legalActions) {
            double currentQValue = getQValue(state, action);
            if (bestValue < currentQValue) {
                bestValue = currentQValue;
                bestAction = action;
            }
        }
        return bestAction;
    }

    private double getQValue(GameState state, char action) {
        double QValue = 0;
        double[] features = FeatureExtractor.getFeatures(state, action, agentIndex);
        for (int i = 0; i < FeatureExtractor.numOfFeatures; ++i) {
            QValue += weights[i] * features[i];
        }
        return QValue;
    }

    public void update(GameState state, char action, GameState nextState, double reward) {
        double difference = reward + DISCOUNT * getValue(nextState) - getQValue(state, action);
        double[] features = FeatureExtractor.getFeatures(state, action, agentIndex);
        for (int i = 0; i < FeatureExtractor.numOfFeatures; ++i) {
            weights[i] += ALPHA * difference * features[i];
        }
    }

    private double getValue(GameState state) {
        return computeValueFromQValue(state);
    }

    private double computeValueFromQValue(GameState state) {
        char[] legalActions = getLegalActions();
        if (legalActions.length == 0) {
            return 0.0;
        }
        double value = getQValue(state, legalActions[0]);
        for (char action : legalActions) {
            value = Math.max(value, getQValue(state, action));
        }
        return value;
    }

    /****************************************************************************************
    This function is called by Environment to inform that the agent has observed a transition
     ****************************************************************************************/
    public void observeTransition(GameState lastState, char lastAction, GameState thisState, int deltaReward) {
        System.out.println("Observed a transition");
        reward += deltaReward;
        update(lastState, lastAction, thisState, deltaReward);
    }

    public void terminated() {
        System.out.println("Agent " + agentIndex + " has finished the episode");
        System.out.println("This episode reward is " + reward);
    }
}