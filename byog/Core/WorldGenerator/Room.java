package byog.Core.WorldGenerator;

/**
 *  Our grid system
 *             (y) Height --------------------->
 *  (x) Width   -----------------------------
 *        |    |.............................|
 *        |    |.+++++++++++++++++++++++++++.|
 *        |    |.+     p1.y   p2.y         +.|
 *        |    |.+ p1.x  ___h___           +.|
 *        |    |.+      |_|     |          +.|
 *        |    |.+     w|      _|          +.|
 *        |    |.+      |_____|_|          +.|
 *        |    |.+ p2.x                    +.|
 *        |    |.+++++++++++++++++++++++++++.|
 *        |    |.............................|
 *        v    |_____________________________|
 *
 */
public class Room extends Block {

    /**
     * Empty default constructor for room since we do not need it.
     */
    public Room() {

    }

    /**
     * Full constructor for Room object.
     * @param p bottom left position of room.
     * @param w width of room.
     * @param h height of room.
     */
    public Room(Position p, int w, int h) {
        p1 = p;
        p2 = new Position(p.x + w - 1, p.y + h - 1);
        width = w;
        height = h;
        int x = (int) Math.floor(0.5 * (p1.x + p2.x));
        int y = (int) Math.floor(0.5 * (p1.y + p2.y));
        interiorPoint = new Position(x, y);
    }
}
