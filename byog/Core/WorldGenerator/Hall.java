package byog.Core.WorldGenerator;

/**
 * Our grid system
 *             (y) Height --------------------->
 *  (x) Width   -----------------------------
 *        |    |.............................|
 *        |    |.+     p1.y   p2.y         +.|
 *        |    |.+ p1.x  ___h___           +.|
 *        |    |.+      |_| _   |          +.|
 *        |    |.+     w|  |_| _| interior +.|
 *        |    |.+      |_____|_|          +.|
 *        |    |.+ p2.x                    +.|
 *        |    |.+++++++++++++++++++++++++++.|
 *        |    |.............................|
 *        v    |_____________________________|
 *
 */
public class Hall extends Block {

    private boolean isVertical;

    public Hall() {

    }

    public Hall(Position p, int length, boolean isVertical) {
        if (isVertical) {
            p1 = p;
            p2 = new Position(p.x, p.y + length - 1);
            width = 1;
            height = length;
            int y = (int) Math.floor(0.5 * (p1.y + p2.y));
            interiorPoint = new Position(p1.x, y);
        } else {
            p1 = p;
            p2 = new Position(p.x + length - 1, p.y);
            width = length;
            height = 1;
            int x = (int) Math.floor(0.5 * (p1.x + p2.x));
            interiorPoint = new Position(x, p1.y);
        }
    }
}
