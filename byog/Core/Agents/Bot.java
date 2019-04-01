package byog.Core.Agents;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serializable;

public class Bot extends Agent implements Serializable {
    protected int delay = 20;

    public Bot(TETile[][] map, int agentIndex) {
        this.map = map;
        this.agentIndex = agentIndex;
        this.tetile = Tileset.MOUNTAIN;
        initialPosition(agentIndex);
    }

    public Bot() {

    }

    @Override
    public Bot clone(TETile [][] map) {
        return new Bot();
    }

    public char [] getLegalActions() {
        int count = 2;
        if(!map[x][y + 1].equals(Tileset.WALL) && !map[x][y+1].equals(Tileset.UNLOCKED_DOOR)) {
            ++count;
        }
        if(!map[x][y - 1].equals(Tileset.WALL) && !map[x][y-1].equals(Tileset.UNLOCKED_DOOR)) {
            ++count;
        }
        if(!map[x-1][y].equals(Tileset.WALL) && !map[x-1][y].equals(Tileset.UNLOCKED_DOOR)) {
            ++count;
        }
        if(!map[x+1][y].equals(Tileset.WALL) && !map[x+1][y].equals(Tileset.UNLOCKED_DOOR)) {
            ++count;
        }

        char [] legalActs = new char [count];
        --count;

        if(!map[x][y + 1].equals(Tileset.WALL) && !map[x][y+1].equals(Tileset.UNLOCKED_DOOR)) {
            legalActs[count] = 'w';
            --count;
        }
        if(!map[x][y - 1].equals(Tileset.WALL) && !map[x][y-1].equals(Tileset.UNLOCKED_DOOR)) {
            legalActs[count] = 's';
            --count;
        }
        if(!map[x-1][y].equals(Tileset.WALL) && !map[x-1][y].equals(Tileset.UNLOCKED_DOOR)) {
            legalActs[count] = 'a';
            --count;
        }
        if(!map[x+1][y].equals(Tileset.WALL) && !map[x+1][y].equals(Tileset.UNLOCKED_DOOR)) {
            legalActs[count] = 'd';
            --count;
        }
        legalActs[count] = 'k';
        --count;
        legalActs[count] = 'p';

        return legalActs;
    }

    /**********************************
     Override this method for bot only.
     **********************************/
    public char nextAction(GameState state) {
        return 'd';
    }
}
