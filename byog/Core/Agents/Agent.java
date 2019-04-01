package byog.Core.Agents;

import byog.Core.Interactivity.Gun;
import byog.Core.WorldGenerator.Position;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdAudio;

import java.io.Serializable;

public class Agent implements Serializable {

    protected TETile[][] map;
    protected TETile tetile;
    protected int x;
    protected int y;
    protected Gun gun;
    protected int direction;
    protected int health = 5;
    protected int alternate = 0;
    protected int agentIndex;
    protected int isAlive = 1;


    public Agent() {

    }

    public Agent clone(TETile [][] map) {
        return new Agent();
    }

    public Agent(Agent other, TETile [][] map) {
        this.map = map;
        this.gun = new Gun(other.gun, this.map);
        this.x = other.x;
        this.y = other.y;
        this.direction = other.direction;
        this.health = other.health;
        this.alternate = other.alternate;
        this.agentIndex = other.agentIndex;
        this.tetile = other.tetile;
    }

    public String doAction(char k) {
        switch (k) {
            case 'k': {
                return shoot();
            }
            case 'w': {
                direction = 0;
                return moveUp();
            }
            case 's': {
                direction = 2;
                return moveDown();
            }
            case 'a': {
                direction = 3;
                return moveLeft();
            }
            case 'd': {
                direction = 1;
                return moveRight();
            }
            case 'p' : {
                return "DO NOTHING";
            }
            default: {
                return "";
            }
        }
    }

    /**********************************
     Override this method for bot only.
     **********************************/
    public char nextAction(GameState state) {
        return 'w';
    }

    protected String moveUp() {
        if(!map[x][y + 1].equals(Tileset.WALL) && !map[x][y+1].equals(Tileset.UNLOCKED_DOOR)) {
            map[x][y] = Tileset.FLOOR;
            map[x][y+1] = tetile;
            ++y;
            gun.setPos(x,y);
            return "GOING UP";
        } else {
            return "CAN NOT MOVE";
        }
    }

    /* Change the position of our bot on the map.
     * @return a String signalling that the bot is moving down.
     */
    protected String moveDown() {
        if(!map[x][y - 1].equals(Tileset.WALL) && !map[x][y-1].equals(Tileset.UNLOCKED_DOOR)) {
            map[x][y] = Tileset.FLOOR;
            map[x][y-1] = tetile;
            --y;
            gun.setPos(x,y);
            return "GOING UP";
        } else {
            return "CAN NOT MOVE";
        }
    }

    /* Change the position of our bot on the map.
     * @return a String signalling that the bot is moving left.
     */
    protected String moveLeft() {
        if(!map[x-1][y].equals(Tileset.WALL) && !map[x-1][y].equals(Tileset.UNLOCKED_DOOR)) {
            map[x][y] = Tileset.FLOOR;
            map[x-1][y] = tetile;
            --x;
            gun.setPos(x,y);
            return "GOING UP";
        } else {
            return "CAN NOT MOVE";
        }
    }

    /* Change the position of our bot on the map.
     * @return a String signalling that the bot is moving right.
     */
    protected String moveRight() {
        if(!map[x+1][y].equals(Tileset.WALL) && !map[x+1][y].equals(Tileset.UNLOCKED_DOOR)) {
            map[x][y] = Tileset.FLOOR;
            map[x+1][y] = tetile;
            ++x;
            gun.setPos(x,y);
            return "GOING UP";
        } else {
            return "CAN NOT MOVE";
        }
    }

    protected String shoot() {
        gun.shoot(direction);
        StdAudio.play("/byog/Soundtrack/laser.wav");
        updateBullets();
        return "Pow Pow";
    }

    protected void updateAlternate() {
        alternate = (alternate + 1) % 3;
    }

    protected void updateBullets() {
        gun.updateAll();
    }

    public int getHealth() {
        return health;
    }

    public void checkGotShot() {
        if(map[x][y] == Tileset.FLOWER) {
            --health;
        }
        if(health == 0) {
            isAlive = 0;
            map[x][y] = Tileset.FLOOR;
        }
    }

    public Position[] getBulletsPos() {
        return gun.getBulletPosition();
    }

    public Position getPos() {
        return new Position(x,y);
    }

    public TETile[][] getMap() {
        return map;
    }

    public int checkAlive() { return isAlive; }

    public void initialPosition(int agentIndex) {
        int width = 0;
        int height = 0;
        if (agentIndex == 0) {
            width = 0;
            height = 0;
            while(map[width][height] != Tileset.FLOOR) {
                ++width;
                ++height;
                if (width > map.length || height > map[0].length) {
                    break;
                }
            }
        } else if (agentIndex == 1) {
            width = 0;
            height = map[0].length - 1;
            while(map[width][height] != Tileset.FLOOR) {
                ++width;
                --height;
                if (width > map.length || height < 0) {
                    break;
                }
            }
        } else if (agentIndex == 2) {
            width = map.length - 1;
            height = map[0].length - 1;
            while(map[width][height] != Tileset.FLOOR) {
                --width;
                --height;
                if (width < 0 || height < 0) {
                    break;
                }
            }

        } else if (agentIndex == 3) {
            width = map.length - 1;
            height = 0;
            while(map[width][height] != Tileset.FLOOR) {
                --width;
                ++height;
                if (width < 0 || height > map[0].length) {
                    break;
                }
            }
        }
        x = width;
        y = height;
        System.out.println(x + ", " + y);
        map[x][y] = this.tetile;
        this.gun = new Gun(x, y, this.map);
        direction = 2;
    }

    public void setMap(TETile [][] map) {
        this.map = map;
    }

    public void setGun(Gun other) {
        this.gun = other;
    }
}
