package byog.Core.Agents;

import byog.Core.Environment.GameState;
import byog.Core.Interactivity.Gun;
import byog.TileEngine.TETile;

import java.io.Serializable;
import java.util.Random;

public class ApproximateQAgent extends Bot implements Serializable {
    private static final double LAMBDA = -0.000002;
    public static double DISCOUNT = 0.99;
    public static double ALPHA = 0.001;
    private int actionNum = 0;
    private double reward = 0;
    private boolean interact = false;
    private Random r;

    private double[] weights = new double[FeatureExtractor.numOfFeatures];
    public ApproximateQAgent(TETile[][] map, int agentIndex) {
        super(map, agentIndex);
        for(int i = 1; i <= FeatureExtractor.numOfFeatures; ++i) {
            weights[i-1] = 0.1 * i;
        }
        r = new Random(32431242);
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
        agent.actionNum = this.actionNum;
        int next = r.nextInt();
        agent.r = new Random(next);
        agent.interact = this.interact;
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
        char action = 'p';
        char[] legalActions = getLegalActions();
        double exploreProb = Math.exp(LAMBDA * actionNum);
        if (r.nextDouble() < exploreProb && !interact) {
            action = legalActions[r.nextInt(legalActions.length)];
        } else {
            action = getPolicy(state);
//            System.out.println("Agent " + agentIndex + " took learned action at " + actionNum);
//            System.out.println("Action: " + action + "\n");
        }
        ++actionNum;
        return action;
    }

    private char getPolicy(GameState state) {
        return computeActionFromQValues(state);
    }

    private char computeActionFromQValues(GameState state) {
        char[] legalActions = getLegalActions();
        char bestAction = 'p';
        double bestValue = Double.MIN_VALUE;
        for (char action : legalActions) {
            double currentQValue = getQValue(state, action);
            if (bestValue <= currentQValue) {
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
        double nextStateVal = getValue(nextState);
        double qVal = getQValue(state, action);
        double difference = reward + DISCOUNT * nextStateVal - qVal;
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
        double value = -Double.MAX_VALUE;
        for (char action : legalActions) {
            value = Math.max(value, getQValue(state, action));
        }
        return value;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }
    /****************************************************************************************
    This function is called by Environment to inform that the agent has observed a transition
     ****************************************************************************************/
    public void observeTransition(GameState lastState, char lastAction, GameState thisState, double deltaReward) {
//        System.out.println("Observed a transition");
        this.reward += deltaReward;
        update(lastState, lastAction, thisState, deltaReward);
    }

    public void terminated() {
        printReward();
    }

    public void interact() {
        DISCOUNT = 0;
        ALPHA = 0;
        interact = true;
    }

    public void printReward() {
        System.out.printf("Agent %d reward: %.2f\n", agentIndex, reward);
    }

    public void printWeights() {
        System.out.println("Agent " + agentIndex + ": ");
        for(int i = 0; i < weights.length; ++i) {
            System.out.printf("%.2f ", weights[i]);
        }
        System.out.println();
    }
}
