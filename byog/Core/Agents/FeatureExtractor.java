package byog.Core.Agents;

import byog.Core.Environment.GameState;

import java.io.Serializable;
import byog.Core.WorldGenerator.Position;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class FeatureExtractor implements Serializable {
    public static final int numOfFeatures = 9;

    public static double[] getFeatures(GameState state, char action, int agentIndex) {
        double[] features = new double[numOfFeatures];
        FeatureExtractor.populate(features, state, action, agentIndex);
        normalized(features);
        return features;
    }

    public static void populate(double[] features, GameState state, char action, int agentIndex) {
        GameState nextState = state.getNextState(agentIndex, action);
        TETile [][] map = state.getMap();

        // feature 0
        Agent nearest = state.getNearestAgent(agentIndex);
        features[0] = nearest.getPos().distanceSquaredTo(nextState.getAgent(agentIndex).getPos());
        //features[0] = 1;

        // feature 1
        features[1] = nextState.getHealth(agentIndex);

        // feature 2
        features[2] = (double) nextState.getTotalOtherHealth(agentIndex) / nextState.getNumOfAgents();

        // feature 3
        Position[] bullets = nextState.getOtherBulletPosition(agentIndex);
        Position botPos = nextState.getPosition(agentIndex);
        double minDistance = Double.MAX_VALUE;
        for(int j = 0; j < bullets.length; ++j) {
            if(bullets[j] == null) { continue; }
            double distance = botPos.distanceSquaredTo(bullets[j]);
            if(distance < minDistance) {
                minDistance = distance;
            }
        }
        if(minDistance == Double.MAX_VALUE) {
            features[3] = 0;
        } else {
            features[3] = minDistance / 2;
        }

        features[4] = -nearest.getHealth() * 10;

        features[5] = 0;
        Position [] agentBullets = state.getBulletPosition(agentIndex);
        for(int i = 0; i < agentBullets.length; ++i) {
            if(agentBullets[i] == null) { continue; }
            Position bullet = agentBullets[i];
            if(map[bullet.x][bullet.y] == Tileset.MOUNTAIN || map[bullet.x][bullet.y] == Tileset.PLAYER) {
                features[5] += 5;
            }
        }

        Position agent = state.getPosition(agentIndex);
        features[6] = 0;
        features[7] = 0;
        int x = agent.x;
        int y = agent.y;
        int agentDir = state.getDirection(agentIndex);
        for(int i = 0; i < 4; ++i) {
            int xxTemp = x;
            int yyTemp = y;
            switch(i) {
                case 0: {
                    while(map[xxTemp][yyTemp] != Tileset.WALL && map[xxTemp][yyTemp] != Tileset.PLAYER
                            && map[xxTemp][yyTemp] != Tileset.MOUNTAIN) {
                        ++yyTemp;
                    }
                    if(map[xxTemp][yyTemp] != Tileset.WALL) {
                        features[6] += 3;
                        if(agentDir == 0) {
                            features[7] += 1;
                        }
                    }
                    break;
                }
                case 1: {
                    while(map[xxTemp][yyTemp] != Tileset.WALL && map[xxTemp][yyTemp] != Tileset.PLAYER
                            && map[xxTemp][yyTemp] != Tileset.MOUNTAIN) {
                        --yyTemp;
                    }
                    if(map[xxTemp][yyTemp] != Tileset.WALL) {
                        features[6] += 3;
                        if(agentDir == 2) {
                            features[7] += 1;
                        }
                    }
                    break;
                }
                case 2: {
                    while(map[xxTemp][yyTemp] != Tileset.WALL && map[xxTemp][yyTemp] != Tileset.PLAYER
                            && map[xxTemp][yyTemp] != Tileset.MOUNTAIN) {
                        ++xxTemp;
                    }
                    if(map[xxTemp][yyTemp] != Tileset.WALL) {
                        features[6] += 3;
                        if(agentDir == 1) {
                            features[7] += 1;
                        }
                    }
                    break;
                }
                case 3: {
                    while(map[xxTemp][yyTemp] != Tileset.WALL && map[xxTemp][yyTemp] != Tileset.PLAYER
                            && map[xxTemp][yyTemp] != Tileset.MOUNTAIN) {
                        --xxTemp;
                    }
                    if(map[xxTemp][yyTemp] != Tileset.WALL) {
                        features[6] += 3;
                        if(agentDir == 3) {
                            features[7] += 1;
                        }
                    }
                    break;
                }
            }
        }
    }

    public static void normalized(double [] features) {
        double sqrt = 0;
        for(int i = 0; i < features.length; ++i) {
            sqrt += features[i] * features[i];
        }
        sqrt = Math.sqrt(sqrt);
        for(int i = 0; i < features.length; ++i) {
            features[i] /= sqrt;
        }
        features[features.length -1] = 1;
    }
}
