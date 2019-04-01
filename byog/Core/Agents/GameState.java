package byog.Core.Agents;

import byog.Core.Interactivity.Gun;
import byog.Core.WorldGenerator.Position;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serializable;

import static byog.TileEngine.TETile.copyOf;

public class GameState implements Serializable {
    private Agent [] agents;
    private TETile [][] map;
    private static int BULLETDELAY = 8;


    public GameState(GameState g) {
        this.map = copyOf(g.map);
        Agent [] newAgents = new Agent[g.agents.length];
        for(int i = 0; i < g.agents.length; ++i) {
            Agent agent = g.agents[i].clone(this.map);
            newAgents[i] = agent;
        }
        this.agents = newAgents;
    }

    public GameState(TETile [][] map, Agent [] agents) {
        this.map = map;
        this.agents = agents;
    }

    public TETile[][] getMap() {
        return map;
    }

    public int getHealth(int agentIndex) {
        return agents[agentIndex].getHealth();
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

    public int isBulletComingToBot(int agentIndex) {
        Position botPos = agents[agentIndex].getPos();
        int x = botPos.x;
        int y = botPos.y;
        System.out.println("StartBullet");
        while(map[x][y] != Tileset.WALL) {
            ++x;
            if(map[x][y] == Tileset.FLOWER) {
                return 1;
            }
        }

        x = botPos.x;
        while(map[x][y] != Tileset.WALL) {
            --x;
            if(map[x][y] == Tileset.FLOWER) {
                return 1;
            }
        }

        while(map[x][y] != Tileset.WALL) {
            ++y;
            if(map[x][y] == Tileset.FLOWER) {
                return 1;
            }
        }

        y = botPos.y;
        while(map[x][y] != Tileset.WALL) {
            --y;
            if(map[x][y] == Tileset.FLOWER) {
                return 1;
            }
        }
        System.out.println("EndBullet");
        return 0;
    }

    public GameState getNextState(int agentIndex, char action) {
        GameState nextState = new GameState(this);
        nextState.agents[agentIndex].doAction(action);
        nextState.update();
        return nextState;
    }


    public char nextAction(int agentIndex) {
        return agents[agentIndex].nextAction(this);
    }


    public boolean isTerminal() {
        int result = 0;
        if(agents[0] == null) {
            for(int i = 1; i < agents.length; ++i) {
                result += agents[i].checkAlive();
            }
            return result == 1;
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

    private void update() {
        if(BULLETDELAY == 0) {
            BULLETDELAY = 8;
            for(int i = 0; i < agents.length; ++i) {
                if(agents[i] == null) { continue; }
                agents[i].updateBullets();
                agents[i].checkGotShot();
            }
        }
        else {
            --BULLETDELAY;
        }

    }
}
