package byog.Core.WorldGenerator;

import java.util.Random;

/*
 * Block is a generalization of Room and Hall in the map. We treat
 * both of these objects as Block with common fields.
 *
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
abstract class Block {
    static final int SEED = 50;
    static final Random RANDOM = new Random(SEED);

    Position p1, p2, interiorPoint;
    int width, height;

    boolean intersect(Block block) {
        return p1.x < block.p2.x
                && p2.x > block.p1.x
                && p2.y > block.p1.y
                && p1.y < block.p2.y;
    }
}
