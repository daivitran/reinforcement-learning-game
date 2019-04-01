package byog.Core.Environment;

import byog.Core.Agents.Agent;
import byog.Core.Agents.ApproximateQAgent;
import byog.Core.Interactivity.Game;
import byog.Core.WorldGenerator.MapGenerator;
import byog.TileEngine.TETile;

import java.util.Random;

public class Train {
    private Agent [] agents;
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
        System.out.println("Start training");
        Agent[] agents = new Agent[5];

        TETile[][] map = new TETile[40][20];
        for(int j = 1; j < agents.length; ++j) {
            agents[j] = new ApproximateQAgent(map, j);
//                System.out.println("Place bot " + j);
        }

        while(i < numOfEpisode)
        {
            System.out.println("Start episode " + i + ":");
            long seed = r.nextLong();

            MapGenerator mg = new MapGenerator(seed);
            mg.generateWorld(map);
            System.out.println(TETile.toString(map));


            for (int j = 1; j < agents.length; ++j) {
                agents[j].initialPosition(j);
            }

            Environment env = new Environment(map, agents);
//            env.display();
            env.runEpisode();

            for(int j = 1; j < agents.length; ++j) {
                ((ApproximateQAgent) agents[j]).terminated();
            }

            System.out.println("Finished episode " + i + ".\n");
            ++i;
        }

        this.agents = agents;
    }

    public static void main(String args []) {
        Train t = new Train(3);
        t.train();

        Game g = new Game(t.agents);
        g.playWithKeyboard();
    }
}
