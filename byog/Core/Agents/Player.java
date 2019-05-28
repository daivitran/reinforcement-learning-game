package byog.Core.Agents;

import byog.Core.Interactivity.Gun;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serializable;

public class Player extends Agent implements Serializable {

    public Player(TETile[][] map, int team) {
        this.map = map;
        this.agentIndex = 0;
        this.tetile = Tileset.PLAYER;
        this.team = team;
    }

    @Override
    public Player clone(TETile[][] map) {
        Player player = new Player(map, this.team);
        player.setGun(new Gun(this.gun, map));
        player.x = this.x;
        player.y = this.y;
        player.health = this.health;
        player.direction= this.direction;
        player.health = this.health;
        player.alternate = this.alternate;
        player.agentIndex = this.agentIndex;
        player.isAlive = this.isAlive;
        return player;
    }

    public char [] getLegalActions() {
        return null;
    }
}
