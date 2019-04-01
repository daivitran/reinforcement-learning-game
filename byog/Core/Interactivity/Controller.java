package byog.Core.Interactivity;

import byog.Core.Environment.GameState;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.Serializable;

public class Controller implements Serializable {
    private static final int TILE_SIZE = 16;
    private static final int WIDTH = 80;
    private static final int HEIGHT = 30;
    private HUD hud;
    private GameState state;

    public Controller(HUD hud, GameState state) {
        this.hud = hud;
        this.state = state;
        int width = WIDTH;
        // Plus two pixels for the line and the info
        int height = HEIGHT + 2;

        // Draw the canvas and set scales
        StdDraw.setCanvasSize(width * TILE_SIZE, height * TILE_SIZE);
        StdDraw.clear(Color.BLACK);
        StdDraw.setXscale(0, width);
        StdDraw.setYscale(0, height);

        // Show the image after all have been drawn
        StdDraw.enableDoubleBuffering();
    }

    /* Received w, a, s, d input and mouse input. Then update.
     * @return true if the user wants to save the game, false if the user
     * won the game and therefore, the game will not be saved.
     */
    public boolean render() {
        // keep re-rendering until the program is terminated
        String input = "";
        boolean wantToSaved = false;
        String hudText;
        int delay = 2;
        while(!state.isTerminal() && !wantToSaved) {
            // Check if user quits and saves the gameText
            if (input.length() >= 2) {
                char last = Character.toLowerCase(input.charAt(input.length() - 1));
                char secondToLast = input.charAt(input.length() - 2);
                if (last == 'q' && secondToLast == ':') {
                    wantToSaved = true;
                    continue;
                }
            }

            // Render HUD
            hudText = hud.startUHD();

            // Render player's move if a key got stroke
            if(StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                input += key;
                state = state.getNextState(0, key);
            }

            for (int i = 1; i < state.getNumOfAgents(); ++i) {
                char action = state.nextAction(i);
                state = state.getNextState(i, action);
            }

            render(state.getHealth(0), hudText);
        }

        return wantToSaved;
    }

    /* Draw the whole map and display given text.
     * @param text on the top left, text on the top middle, text on the top right.
     */
    private void render(int playerHealth, String middleText) {
        StdDraw.clear(Color.BLACK);
        // raw background
        StdDraw.picture(WIDTH / 2, HEIGHT / 2, "byog/Images/background.png");

        TETile[][] map = state.getMap();
        // Draw the map
        int numXTiles = WIDTH;
        int numYTiles = HEIGHT;

        for (int x = 0; x < numXTiles; x += 1) {
            for (int y = 0; y < numYTiles; y += 1) {
                if (map[x][y] == null) {
                    throw new IllegalArgumentException("Tile at position x=" + x + ", y=" + y
                            + " is null.");
                }
                if (map[x][y].equals(Tileset.PLAYER)) {
                    Tileset.FLOOR.draw(x, y);
                    map[x][y].draw(x, y);
                } else if (map[x][y].equals(Tileset.FLOWER)) {
                    Tileset.FLOOR.draw(x, y);
                    map[x][y].draw(x, y);
                } else if (!map[x][y].equals(Tileset.NOTHING)) {
                    map[x][y].draw(x, y);
                }
            }
        }

        // Draw the text and the line
        Font font = new Font("Monaco", Font.BOLD, TILE_SIZE - 1);
        StdDraw.setPenColor(Color.white);
        StdDraw.setFont(font);
        // Draw player health
        StdDraw.textLeft(1, HEIGHT + 1, "Player");
        int i = 0;
        for (; i < playerHealth; ++i) {
            StdDraw.picture(6 + i, HEIGHT + 1, "byog/Images/healthFull.png");
        }
        for (; i < 5; ++i) {
            StdDraw.picture(6 + i, HEIGHT + 1, "byog/Images/healthEmpty.png");
        }
        // Draw middle Text
        StdDraw.setPenColor(Color.white);
        StdDraw.text(WIDTH / 2, HEIGHT + 1, middleText);
        StdDraw.show();
    }

    public boolean isVictory() {
        return state.getHealth(0) > 0;
    }
}
