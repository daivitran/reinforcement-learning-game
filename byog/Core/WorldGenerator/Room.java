package byog.Core.WorldGenerator;

/* Our grid system
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
    public Room() {

    }

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
