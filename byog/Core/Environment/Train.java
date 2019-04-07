package byog.Core.Environment;

import byog.Core.Agents.Agent;
import byog.Core.Agents.ApproximateQAgent;
import byog.Core.Agents.Player;
import byog.Core.Interactivity.Game;
import byog.Core.WorldGenerator.MapGenerator;
import byog.TileEngine.TETile;

import java.util.Random;

public class Train {
    private Agent [] agents;
    private Random r = new Random(21421);
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
        int numOfAgents = agents.length;

        TETile[][] map = new TETile[80][30];
        for(int j = 1; j < agents.length; ++j) {
            agents[j] = new ApproximateQAgent(map, j);
//                System.out.println("Place bot " + j);
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
//            if(i == 99) {
//              env.display();
//            }
            env.runEpisode();

            System.out.println("Finished episode " + i + ".\n");
            ++i;

            agents = env.getState().getAgents();
//
//            for(int j = 1; j < numOfAgents; ++j) {
//                ((ApproximateQAgent) agents[j]).terminated();
//            }
//
//            for(int j = 1; j < numOfAgents; ++j) {
//                ((ApproximateQAgent) agents[j]).printWeights();
//            }
        }
        this.agents = agents;

//        for(int j = 1; j < numOfAgents; ++j) {
//            ((ApproximateQAgent) this.agents[j]).printWeights();
//        }
    }

    public static void main(String args []) {
        Train t = new Train(100);
        t.train();
        System.out.println("Starting game with trained agents: ");
        Game g = new Game(t.agents);
        g.playWithKeyboard();
        System.exit(0);
    }
}
