package byog.Core.Agents;

import byog.Core.Environment.GameState;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serializable;

public class Bot extends Agent implements Serializable {

    protected int delay = 20;

    public Bot() {

    }

    public Bot(TETile[][] map, int agentIndex, int team) {

        this.map = map;
        this.agentIndex = agentIndex;

        switch (agentIndex) {
            case 1:
                this.tetile = Tileset.BOT1;
                break;
            case 2:
                this.tetile = Tileset.BOT2;
                break;
            case 3:
                this.tetile = Tileset.BOT3;
                break;
            case 4:
                this.tetile = Tileset.BOT4;
                break;
        }
        this.team = team;
    }

    @Override
    public Bot clone(TETile [][] map) {
        return new Bot();
    }

    public char [] getLegalActions() {
        int count = 1;
        boolean canShoot = false;
        if(!map[x][y + 1].equals(Tileset.WALL) && !map[x][y+1].equals(Tileset.UNLOCKED_DOOR)) {
            ++count;
            canShoot = direction == 0;
        }
        if(!map[x][y - 1].equals(Tileset.WALL) && !map[x][y-1].equals(Tileset.UNLOCKED_DOOR)) {
            ++count;
            if(!canShoot) {
                canShoot = direction == 2;
            }
        }
        if(!map[x-1][y].equals(Tileset.WALL) && !map[x-1][y].equals(Tileset.UNLOCKED_DOOR)) {
            ++count;
            if(!canShoot) {
                canShoot = direction == 3;
            }
        }
        if(!map[x+1][y].equals(Tileset.WALL) && !map[x+1][y].equals(Tileset.UNLOCKED_DOOR)) {
            ++count;
            if(!canShoot) {
                canShoot = direction == 1;
            }
        }
        if(canShoot) { ++count; }

        char [] legalActs = new char [count];
        --count;
        if(canShoot) {
            legalActs[count] = 'k';
            --count;
        }
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
