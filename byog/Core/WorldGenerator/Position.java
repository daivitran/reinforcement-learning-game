package byog.Core.WorldGenerator;

/**
 * Our grid system
 *             (y) Height --------------------->
 *  (x) Width   -----------------------------
 *        |    |.............................|
 *        |    |.+++++++++++++++++++++++++++.|
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
public class Position {

    public int x, y;

    public Position() {

    }

    public Position(int xPos, int yPos) {
        x = xPos;
        y = yPos;
    }

    public double distanceSquaredTo(Position otherPosition) {
        return Math.sqrt((otherPosition.x - x) * (otherPosition.x - x)
                + (otherPosition.y - y) * (otherPosition.y - y));
    }

    public boolean equals(Position otherPosition) {
        return x == otherPosition.x && y == otherPosition.y;
    }


}
