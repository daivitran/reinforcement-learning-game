package byog.Core.Agents;

import byog.Core.Environment.GameState;
import byog.Core.Interactivity.Gun;
import byog.Core.WorldGenerator.Position;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serializable;
import java.util.Random;
import byog.Core.Interactivity.Gun.Bullet;

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
        if(!map[x][y + 1].equals(Tileset.WALL) && !map[x][y+1].equals(Tileset.UNLOCKED_DOOR) &&
        !map[x][y+1].equals(Tileset.PLAYER) && !map[x][y+1].equals(Tileset.MOUNTAIN)
        && !map[x][y+1].equals(Tileset.FLOWER)) {
            map[x][y] = Tileset.FLOOR;
            map[x][y+1] = tetile;
            updateImage('u');
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
        if(!map[x][y - 1].equals(Tileset.WALL) && !map[x][y-1].equals(Tileset.UNLOCKED_DOOR) &&
        !map[x][y-1].equals(Tileset.PLAYER) && !map[x][y-1].equals(Tileset.MOUNTAIN)
        && !map[x][y-1].equals(Tileset.FLOWER)) {
            map[x][y] = Tileset.FLOOR;
            map[x][y-1] = tetile;
            updateImage('d');
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
        if(!map[x-1][y].equals(Tileset.WALL) && !map[x-1][y].equals(Tileset.UNLOCKED_DOOR) &&
                !map[x-1][y].equals(Tileset.PLAYER) && !map[x-1][y].equals(Tileset.MOUNTAIN)
                    && !map[x-1][y].equals(Tileset.FLOWER)) {
            map[x][y] = Tileset.FLOOR;
            map[x-1][y] = tetile;
            updateImage('l');
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
        if(!map[x+1][y].equals(Tileset.WALL) && !map[x+1][y].equals(Tileset.UNLOCKED_DOOR) &&
                !map[x+1][y].equals(Tileset.PLAYER) && !map[x+1][y].equals(Tileset.MOUNTAIN)
                    && !map[x+1][y].equals(Tileset.FLOWER)) {
            map[x][y] = Tileset.FLOOR;
            map[x+1][y] = tetile;
            updateImage('r');
            ++x;
            gun.setPos(x,y);
            return "GOING UP";
        } else {
            return "CAN NOT MOVE";
        }
    }

    protected String shoot() {
        gun.shoot(direction);
        //StdAudio.play("/byog/Soundtrack/laser.wav");
        return "Pow Pow";
    }

    protected void updateAlternate() {
        alternate = (alternate + 1) % 3;
    }

    public void updateBullets() {
        gun.updateAll();
    }

    public int getHealth() {
        return health;
    }

    public void checkGotShot() {
        if(isAlive == 0) {
            return;
        }
        if(map[x][y] == Tileset.FLOWER) {
            --health;
            map[x][y] = tetile;
        }

        if(health <= 0) {
            isAlive = 0;
            map[x][y] = Tileset.FLOOR;
        }

        if(isAlive == 0) {
            return;
        } else  {
            map[x][y] = tetile;
        }
    }

    public Position[] getBulletsPos() {
        return gun.getBulletPosition();
    }


     public Bullet[] getBullets() { return gun.getBullets(); }

    public Position getPos() {
        return new Position(x,y);
    }

    public TETile[][] getMap() {
        return map;
    }

    public int checkAlive() { return isAlive; }

    public void initialPosition() {
        Random r = new Random(534213);

        int width = 0;
        int height = 0;
        while(map[width][height] != Tileset.FLOOR) {
            width = r.nextInt(map.length);
            height = r.nextInt(map[0].length);
        }
        x = width;
        y = height;
//        System.out.println(x + ", " + y);
        health = 5;
        alternate = 0;
        isAlive = 1;
        map[x][y] = this.tetile;
        gun = new Gun(x, y, this.map);
        direction = 2;
    }

    public int getDirection() {
        return direction;
    }

    public void setMap(TETile [][] map) {
        this.map = map;
    }

    public void setGun(Gun other) {
        this.gun = other;
    }

    public void updateImage(char action) {
        alternate = (alternate + 1) % 3;
        if (tetile.equals(Tileset.MOUNTAIN)) {
            String path = "byog/Images/bot/" + action + "" + alternate + ".png";
            Tileset.MOUNTAIN.changeImage(path);
        } else if (tetile.equals(Tileset.PLAYER)) {
            String path = "byog/Images/player/" + action + "" + alternate + ".png";
            Tileset.PLAYER.changeImage(path);
        }
    }
}
