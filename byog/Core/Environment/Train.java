package byog.Core.Environment;

import byog.Core.Agents.Agent;
import byog.Core.Agents.ApproximateQAgent;
import byog.Core.WorldGenerator.MapGenerator;
import byog.TileEngine.TETile;

import java.util.Random;

public class Train {
    private Random r = new Random(431234321);
    private int numOfEpisode;

    public Train(int numOfEpisode) {
        this.numOfEpisode = numOfEpisode;
    }

    public void train() {
        train(numOfEpisode);
    }

    private void train(int numOfEpisode) {
        int i = 0;
        while(i < numOfEpisode)
        {
            long seed = r.nextLong();
            TETile[][] map = new TETile[80][30];
            MapGenerator m = new MapGenerator(seed);
            m.generateWorld(map);

            Agent[] agents = new Agent[5];
            for(int j = 1; j < agents.length; ++i) {
                agents[j] = new ApproximateQAgent(map, j);
            }

            Environment env = new Environment(map, agents);
            env.runEpisode();

            for(int j = 1; j < agents.length; ++i) {
                ((ApproximateQAgent) agents[j]).terminated();
            }

            System.out.println("\n Finished episode " + i + ".");
        }
    }

    public static void main(String args []) {
        Train t = new Train(3);
        t.train();
    }
}
