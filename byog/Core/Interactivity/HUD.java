package byog.Core.Interactivity;

import byog.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;
import java.io.Serializable;

/* This class controls the information displayed when hovering the mouse
 * on anywhere in the map.
 */
public class HUD implements Serializable {

    private TETile[][] map;

    public HUD(TETile[][] map) {
        this.map = map;
    }

    public String startUHD() {
        // text to render
        String item = "";
        int x = (int) StdDraw.mouseX();
        int y = (int) StdDraw.mouseY();

        // Condition for the space of info and coordinates
        if (y >= map[0].length) {
            return "";
        }

        // Get character from Tile
        char c = map[x][y].character();

        // Print the according Tile to the info bar
        switch (c) {
            case ' ': {
                item = "NOTHING";
                break;
            }
            case '·': {
                item = "FLOOR";
                break;
            }
            case '#': {
                item = "WALL";
                break;
            }
            case '@': {
                item = "PLAYER";
                break;
            }
            case '▢': {
                item = "DOOR";
            }
        }
        return item;
    }
}
