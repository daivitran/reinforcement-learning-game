package byog.Core.Environment;

import byog.Core.Agents.Agent;
import byog.Core.Agents.ApproximateQAgent;
import byog.Core.WorldGenerator.MapGenerator;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.util.Random;

public class Environment {

    private GameState thisState;
    private GameState lastState;
    private boolean render = false;
    private int width;
    private int height;
    private static final int TILE_SIZE = 16;

    public Environment(TETile [][] map, Agent[] agents) {
        width = map.length;
        height = map[0].length;
        this.thisState = new GameState(map, agents);
    }

    public GameState getState() {
        return thisState;
    }


    public void runEpisode() {
        int numOfAgents = thisState.getNumOfAgents();
        while(!thisState.isTerminal()) {
            for (int i = 1; i < numOfAgents + 1; ++i) {
                if (!thisState.isAlive(i)) { continue; }
                char action = thisState.nextAction(i);
                lastState = thisState;
                thisState = thisState.getNextState(i, action);
                Agent agent = thisState.getAgent(i);
                if(!(action == '!')) {
                    if(thisState.isTerminal()) {
                        break;
                    }
                    double reward = thisState.getThisStateReward(i);
                    ((ApproximateQAgent) agent).observeTransition(lastState, action, thisState, reward);
                }
            }
            if(render) {
                render(thisState);
            }
        }
    }

    public void display() {
        render = true;

        // Draw the canvas and set scales
        StdDraw.setCanvasSize(width * TILE_SIZE, height * TILE_SIZE);
        StdDraw.clear(Color.BLACK);
        StdDraw.setXscale(0, width);
        StdDraw.setYscale(0, height);

        // Show the image after all have been drawn
        StdDraw.enableDoubleBuffering();
    }

    public void muteDisplay() {
        render = false;
    }

    public void render(GameState state) {
        StdDraw.clear(Color.BLACK);
        TETile [][] map = state.getMap();

        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                if (map[x][y] == null) {
                    throw new IllegalArgumentException("Tile at position x=" + x + ", y=" + y
                            + " is null.");
                }
                if (map[x][y].equals(Tileset.PLAYER)) {
                    Tileset.FLOOR.draw(x, y);
                    map[x][y].draw(x, y);
                } else if (map[x][y].equals(Tileset.FLOWER)) {
                    Tileset.FLOOR.draw(x, y);
                    map[x][y].draw(x, y);
                } else if (!map[x][y].equals(Tileset.NOTHING)) {
                    map[x][y].draw(x, y);
                }
            }
        }

        StdDraw.show();
    }

}
