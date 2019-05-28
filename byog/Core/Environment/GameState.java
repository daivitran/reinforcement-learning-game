package byog.Core.Environment;

import byog.Core.Agents.Agent;
import byog.Core.Agents.ApproximateQAgent;
import byog.Core.WorldGenerator.Position;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serializable;

import static byog.TileEngine.TETile.copyOf;
import byog.Core.Interactivity.Gun.Bullet;

public class GameState implements Serializable {

    private Agent[] agents;
    private TETile [][] map;
    private static int BULLETDELAY = 8;

    public GameState(GameState g) {
        this.map = copyOf(g.map);
        Agent [] newAgents = new Agent[g.agents.length];
        for(int i = 0; i < g.agents.length; ++i) {
            if(g.agents[i] == null) { continue; }
            Agent agent = g.agents[i].clone(this.map);
            newAgents[i] = agent;
        }
        this.agents = newAgents;
    }

    public GameState(TETile [][] map, Agent [] agents) {
        this.map = map;
        this.agents = agents;
    }

    public Agent getAgent(int agentIndex) {
        return agents[agentIndex];
    }

    public TETile[][] getMap() {
        return map;
    }

    public int getHealth(int agentIndex) {
        return agents[agentIndex].getHealth();
    }

    public Position [] getBulletPosition(int agentIndex) {
        return agents[agentIndex].getBulletsPos();
    }

    public Position [] getOtherBulletPosition(int agentIndex) {
        int count = getNumOfAgents() - 1;
        Position [] bullets = new Position[count * 20];
        int temp = 0;
        for(int i = 0; i < agents.length; ++i) {
            if(agents[i] == null | i == agentIndex) { continue; }
            Position [] otherBullets = agents[i].getBulletsPos();
            System.arraycopy(otherBullets, 0, bullets, temp,  otherBullets.length);
            temp += otherBullets.length;
        }

        return bullets;
    }

    public Agent [] getAgents() {
        return agents;
    }

    public Position getPosition(int agentIndex) {
        return agents[agentIndex].getPos();
    }

    public Position [] getOtherPosition(int agentIndex) {
        Position [] other = new Position[getNumOfAgents() - 1];
        int temp = 0;
        for(int i = 0; i < agents.length; ++i) {
            if(i == agentIndex || agents[i] == null || agents[i].checkAlive() == 0) { continue; }
            other[temp] = agents[i].getPos();
            ++temp;
        }

        return other;
    }

    public GameState getNextState(int agentIndex, char action) {
        GameState nextState = new GameState(this);
        nextState.agents[agentIndex].doAction(action);
        nextState.update();
        return nextState;
    }

    public boolean isAlive(int agentIndex) {
        return agents[agentIndex].checkAlive() == 1;
    }

    public char nextAction(int agentIndex) {
        return agents[agentIndex].nextAction(this);
    }

    public int getAliveAgents() {
        int result = 0;
        for(int i = 0; i < agents.length; ++i) {
            if(agents[i] == null) { continue; }
            result += agents[i].checkAlive();
        }
        return result;
    }

    public boolean isTerminal() {
        int result = 0;
        if(agents[0] == null) {
            for(int i = 1; i < agents.length; ++i) {
                result += agents[i].checkAlive();
            }
            return result == 1 || result == 0;
        }
        else {
            for(int i = 1; i < agents.length; ++i) {
                result += agents[i].checkAlive();
            }
            return agents[0].checkAlive() == 0 || result == 0;
        }
    }

    public int getNumOfAgents() {
        int count = (agents[0] == null) ? 0 : 1;
        count += agents.length - 1;
        return count;
    }

    public int getNumOfEnemies(int agentIndex) {
        int count = 0;
        for (int i = 0; i < agents.length; ++i) {
            if (agents[i] == null) {
                continue;
            }
            if (agents[i].team() != agents[agentIndex].team()) {
                ++count;
            }
        }
        return count;
    }

    public Agent getNearestAgent(int agentIndex) {
        Agent result = null;
        double minDistance = Double.MAX_VALUE;
        for(int i = 0; i < agents.length; ++i) {
            // if(agents[i] == null || i == agentIndex || agents[i].checkAlive() == 0) { continue; }
            if (agents[i] == null || i == agentIndex || agents[i].checkAlive() == 0 || agents[i].team() == agents[agentIndex].team()) { continue; }
            double distance = agents[agentIndex].getPos().distanceSquaredTo(agents[i].getPos());
            if(distance < minDistance) {
                minDistance = distance;
                result = agents[i];
            }
        }

        return result;
    }

    public int getTotalOtherHealth(int agentIndex) {
        int total = 0;
        for (int i = 0; i < agents.length; ++i) {
            if (agentIndex == i || agents[i] == null || agents[i].checkAlive() == 0 || agents[i].team() == agents[agentIndex].team()) {
                continue;
            }
            total += agents[i].getHealth();
        }
        return total;
    }

    private void update() {
        for (int i = 0; i < agents.length; ++i) {
            if (agents[i] == null) {
                continue;
            } else {
                agents[i].checkGotShot();
            }
        }

        if(BULLETDELAY == 0) {
            BULLETDELAY = 8;
            for(int i = 0; i < agents.length; ++i) {
                if(agents[i] == null) { continue; }
                agents[i].updateBullets();
            }
        } else {
            --BULLETDELAY;
        }
    }

    public int getDirection(int agentIndex) {
        return agents[agentIndex].getDirection();
    }

    public double getThisStateReward(int agentIndex) {
        double result = -0.1;
        Position agentPos = getPosition(agentIndex);
        Position [] bulletsPos = getBulletPosition(agentIndex);
        for(int i = 0; i < agents.length; ++i) {
            if (i == agentIndex || agents[i] == null || agents[agentIndex].team() == agents[i].team()) { continue;}
            for(int j = 0; j < bulletsPos.length; ++j) {
                if(bulletsPos[j] == null) { continue; }
                if(agents[i].getPos().equals(bulletsPos[j])) {
                    switch (getHealth(i)) {
                        case 5: {
                            result += 100;
                            break;
                        }
                        case 4: {
                            result += 150;
                            break;
                        }
                        case 3: {
                            result += 200;
                            break;
                        }
                        case 2: {
                            result += 250;
                            break;
                        }
                        case 1: {
                            result += 750;
                            break;
                        }
                    }
                }
            }
        }
        if(map[agentPos.x][agentPos.y] == Tileset.FLOWER) {
            switch (getHealth(agentIndex)) {
                case 5: {
                    result -= 10;
                    break;
                }
                case 4: {
                    result -= 20;
                    break;
                }
                case 3: {
                    result -= 30;
                    break;
                }
                case 2: {
                    result -= 40;
                    break;
                }
                case 1: {
                    result -= 500;
                    break;
                }
            }
        }
        return result;
    }

     public boolean isBulletComingToBot(int agentIndex) {
         Bullet[] bullets = getOtherBullets(agentIndex);
         for (Bullet bullet : bullets) {
             if (bullet == null) {
                 continue;
             }
             int bulletDirection = bullet.getDirection();
             int i = 0;
             int numOfNext = 5;
             int bX = bullet.getPos().x;
             int bY = bullet.getPos().y;
             int aX = getPosition(agentIndex).x;
             int aY = getPosition(agentIndex).y;
             switch (bulletDirection) {
                 case 0 :
                     while (map[bX][bY] != Tileset.WALL && i < numOfNext) {
                         if (bX == aX && bY == aY) {
                             return true;
                         }
                         ++bY;
                         ++i;
                         if (bY >= 30) {
                             break;
                         }
                     }
                     break;
                 case 1 :
                     while (map[bX][bY] != Tileset.WALL && i < numOfNext) {
                         if (bX == aX && bY == aY) {
                             return true;
                         }
                         ++bX;
                         ++i;
                         if (bX >= 80) {
                             break;
                         }
                     }
                     break;
                 case 2 :
                     while (map[bX][bY] != Tileset.WALL && i < numOfNext) {
                         if (bX == aX && bY == aY) {
                             return true;
                         }
                         --bY;
                         ++i;
                         if (bY < 0) {
                             break;
                         }
                     }
                     break;
                 case 3 :
                     while (map[bX][bY] != Tileset.WALL && i < numOfNext) {
                         if (bX == aX && bY == aY) {
                             return true;
                         }
                         --bX;
                         ++i;
                         if (bX < 0) {
                             break;
                         }
                     }
                     break;
             }
         }
         return false;
    }

     public Bullet[] getOtherBullets(int agentIndex) {
        int count = getNumOfAgents() - 1;
        Bullet[] bullets = new Bullet[count * 20];
        int temp = 0;
        for(int i = 0; i < agents.length; ++i) {
            if(agents[i] == null | i == agentIndex) { continue; }
            Bullet [] otherBullets = agents[i].getBullets();
            System.arraycopy(otherBullets, 0, bullets, temp,  otherBullets.length);
            temp += otherBullets.length;
        }
        return bullets;
     }

}
