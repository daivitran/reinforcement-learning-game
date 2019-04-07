package byog.Core.Interactivity;

import byog.Core.WorldGenerator.Position;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serializable;

import static byog.TileEngine.TETile.copyOf;

public class Gun implements Serializable {
    public class Bullet implements Serializable {
        int x;
        int y;
        int direction;
        boolean hitWall;
        private TETile[][] map;

        private Bullet(int x, int y, int direction, TETile[][] map) {
            this.x = x;
            this.y = y;
            this.direction = direction;
            hitWall = false;
            this.map = map;
        }

        private Bullet(Bullet other, TETile [][] map) {
            this.x = other.x;
            this.y = other.y;
            this.direction = other.direction;
            this.hitWall = other.hitWall;
            this.map = map;
        }

        public int getDirection() {
            return direction;
        }

        public Position getPos() {
            return new Position(x, y);
        }

        private void update() {
            if (!hitWall) {
                map[x][y] = map[x][y].equals(Tileset.PLAYER) || map[x][y].equals(Tileset.MOUNTAIN)
                        ? map[x][y] : Tileset.FLOOR;
                switch (direction) {
                    case 0: {
                        ++y;
                        break;
                    }
                    case 1: {
                        ++x;
                        break;
                    }
                    case 2: {
                        --y;
                        break;
                    }
                    case 3: {
                        --x;
                        break;
                    }
                }
                hitWall = map[x][y].equals(Tileset.WALL)
                        || map[x][y].equals(Tileset.LOCKED_DOOR)
                        || map[x][y].equals(Tileset.UNLOCKED_DOOR);
                if(!hitWall) {
                    map[x][y] = Tileset.FLOWER;
                }
            }
        }

        private boolean finished() {
            return hitWall;
        }

    }
    private TETile[][] map;
    private Bullet [] ammo;
    private int x;
    private int y;
    private int track; // Keep track of index to put in the next available place
    private int count;

    public Gun(int x, int y, TETile[][] map) {
        ammo = new Bullet [20];
        this.x = x;
        this.y = y;
        this.map = map;
        track = 0;
        this.count = 0;
    }


    public Gun(Gun g, TETile[][] newMap) {
        this.x = g.x;
        this.y = g.y;
        this.map = newMap;
        this.track = g.track;
        this.count = g.count;
        this.ammo = new Bullet[20];
        for(int i = 0; i < 20; ++i) {
            if(g.ammo[i] == null) { continue; }
            this.ammo[i] = new Bullet(g.ammo[i], this.map);
        }
    }

    /* Handling the shoot action
     * @param the direction of the player
     */
    public void shoot(int direction) {
        if(count < 20) {
            Bullet bull = new Bullet(x, y, direction, map);
            if(count == 0) {
                ammo[track] = bull;
            }
            else {
                track = (track + 1) % 20;
                ammo[track] = bull;
            }
            ++count;
        }
    }

    public void updateAll() {
        for(int i = 0; i < 20; ++i) {
            if(ammo[i] == null) {
                continue;
            }
            ammo[i].update();
            if(ammo[i].finished()) {
                --count;
                ammo[i] = null;
            }
        }
    }

    /* For moving the gun according to Player's position
     * @param the position x, y of Player
     */
    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position [] getBulletPosition() {
        int temp = 0;
        Position [] bullets = new Position [count];
        for(int i = 0; i < 20; ++i) {
            if(ammo[i] == null || ammo[i].finished()) { continue; }
            bullets[temp] = ammo[i].getPos();
            ++temp;
        }

        return bullets;
    }

     public Bullet[] getBullets() {
        int temp = 0;
        Bullet [] bullets = new Bullet[count];
        for (int i = 0; i < 20; ++i) {
            if (ammo[i] == null || ammo[i].finished()) {
                continue;
            }
            bullets[temp] = new Bullet(ammo[i], copyOf(map));
            ++temp;
        }
        return bullets;
     }
}
