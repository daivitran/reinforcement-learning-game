package byog.Core.Agents;

import byog.Core.Interactivity.Gun;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serializable;

public class Player extends Agent implements Serializable {
    public Player(TETile[][] map) {
        this.map = map;
        this.agentIndex = 0;
        this.tetile = Tileset.PLAYER;
        initialPosition(0);
    }

    public char [] getLegalActions() {
        return null;
    }
}
