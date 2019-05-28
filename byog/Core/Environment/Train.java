package byog.Core.Environment;

import byog.Core.Agents.Agent;
import byog.Core.Agents.ApproximateQAgent;
import byog.Core.Agents.FeatureExtractor;
import byog.Core.Agents.Player;
import byog.Core.Interactivity.Game;
import byog.Core.WorldGenerator.MapGenerator;
import byog.TileEngine.TETile;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.util.Random;

public class Train {
    private Agent [] agents;
    private Random r = new Random(21421);
    private int numOfEpisode;

    public Train(int numOfEpisode) {
        this.numOfEpisode = numOfEpisode;
    }

    public double[] train() {
        return train(numOfEpisode);
    }

    public double[] train(int numOfEpisode) {
        int i = 0;
        System.out.println("Start training");
        Agent[] agents = new Agent[5];
        int numOfAgents = agents.length;
        TETile[][] map = new TETile[80][30];
        for(int j = 1; j < agents.length; ++j) {
            agents[j] = new ApproximateQAgent(map, j, j);
        }

        while(i < numOfEpisode)
        {
            System.out.println("\nStart episode " + i + ":");

            long seed = r.nextLong();

            MapGenerator mg = new MapGenerator(seed);
            mg.generateWorld(map);
            System.out.println(TETile.toString(map));


            for (int j = 1; j < agents.length; ++j) {
                agents[j].setMap(map);
                agents[j].initialPosition();
                ((ApproximateQAgent) agents[j]).setReward(0);
            }

            for(int j = 1; j < numOfAgents; ++j) {
                ((ApproximateQAgent) agents[j]).printReward();
            }

            Environment env = new Environment(map, agents);

            env.runEpisode();

            System.out.println("Finished episode " + i + ".\n");
            ++i;

            agents = env.getState().getAgents();
        }

        Agent maxAgent = agents[1];
        for (int z = 2; z < agents.length; ++z) {
            if (((ApproximateQAgent) agents[z]).getReward() > ((ApproximateQAgent) maxAgent).getReward()) {
                maxAgent = agents[z];
            }
        }

        return ((ApproximateQAgent) maxAgent).getWeights();
    }

    public static void main(String args []) throws Exception {
        Train t = new Train(100);
        double[] weights = new double[FeatureExtractor.numOfFeatures];
        weights = t.train();
        FileOutputStream fos = new FileOutputStream("weights.txt");
        DataOutputStream dos = new DataOutputStream(fos);
        for (int i = 0; i < weights.length; ++i) {
            dos.writeDouble(weights[i]);
        }
        dos.close();
        System.out.println("Training done !!! Saved !!");
    }
}
