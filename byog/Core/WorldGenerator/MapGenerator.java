package byog.Core.WorldGenerator;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.Random;

/* Generate the world, inside (.) is rooms and hall
 *
 *           Height --------------------->
 *   Width   -----------------------------
 *     |    |.............................|
 *     |    |.............................|
 *     |    |..                         ..|
 *     |    |..                         ..|
 *     |    |..                         ..|
 *     |    |..                         ..|
 *     |    |..                         ..|
 *     |    |..                         ..|
 *     |    |.............................|
 *     |    |.............................|
 *     v    |_____________________________|
 *
 */
public class MapGenerator {
    private static final int WIDTH = 80;
    private static final int HEIGHT = 30;
    private static final int WINDOW_WIDTH = 80;
    private static final int WINDOW_HEIGHT = 30;

    // The percentage of floor in the map.
    private static final double FLOOR_PERCENTAGE = 0.3;

    // The maximum length of a side of room.
    private static final int MIN_ROOM_SIZE = 2;
    private static final int MAX_ROOM_SIZE = 5;

    // The maximum length of a hall.
    private static final int MIN_LENGTH = 4;
    private static final int MAX_LENGTH = 8;

    // Keeps track of the added room and the area usage of rooms.
    private ArrayList<Block> blocks = new ArrayList<>();
    private int area = 0;
    private Random RANDOM;

    // Keeps track of the added walls that are possible to generate exit door.
    private ArrayList<Position> wallPositions = new ArrayList<>();

    public MapGenerator() {

    }

    public MapGenerator(long seed) {
        RANDOM = new Random(seed);
    }

    /* Fill our world with NOTHING, FLOOR, and WALL.
     * @param 2D array world.
     */
    public void generateWorld(TETile[][] world) {
        fillNothing(world);
        fillFloor(world);
        fillWall(world);
        pickExitDoor(world);
    }

    /* Fill the world with nothing.
     * @param 2D array world.
     */
    private void fillNothing(TETile[][] world) {
        int worldHeight = world[0].length;
        int worldWidth = world.length;
        for (int x = 0; x < worldWidth; ++x) {
            for (int y = 0; y < worldHeight; ++y) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }

    /* Fill the world with floor 2 unit inside the 4 edges.
     * @param 2D array world.
     */
    private void fillFloor(TETile[][] world) {
        int worldHeight = world[0].length;
        int worldWidth = world.length;

        TETile[][] floor = createRoomAndHall(worldWidth - 4, worldHeight - 4);

        for (int x = 2; x < worldWidth - 2; ++x) {
            for (int y = 2; y < worldHeight - 2; ++y) {
                if (floor[x - 2][y - 2] == Tileset.FLOOR) {
                    world[x][y] = floor[x - 2][y - 2];
                }
            }
        }
    }
    /* Surrounds the floor in the world with wall.
     * @param 2D array world.
     */
    private void fillWall(TETile[][] world) {
        int worldHeight = world[0].length;
        int worldWidth = world.length;
        for (int x = 0; x < worldWidth; ++x) {
            for (int y = 0; y < worldHeight; ++y) {
                if (world[x][y] == Tileset.FLOOR) {
                    for (int i = -1; i <= 1; ++i) {
                        for (int j = -1; j <= 1; ++j) {
                            if (world[x + i][y + j] == Tileset.NOTHING) {
                                world[x + i][y + j] = Tileset.WALL;
                            }
                        }
                    }
                }
            }
        }
    }

    /* Randomly pick a valid wall to be our exit door.
     * @param 2D array world.
     */
    private void pickExitDoor(TETile[][] world) {
        int worldHeight = world[0].length;
        int worldWidth = world.length;
        for (int x = 0; x < worldWidth; ++x) {
            for (int y = 0; y < worldHeight; ++y) {
                if (world[x][y] == Tileset.WALL) {
                    Position p = new Position(x, y);
                    if (canBeDoor(world, p)) {
                        wallPositions.add(p);
                    }
                }
            }
        }
        Position doorPosition = wallPositions.get(RANDOM.nextInt(wallPositions.size()));
        world[doorPosition.x][doorPosition.y] = Tileset.UNLOCKED_DOOR;
    }

    /* Check if the wall is possible to be a door.
     * @param 2D array of world.
     * @param position of considered wall.
     * @return true if the wall can make a valid door
     */
    private boolean canBeDoor(TETile[][] world, Position wallPosition) {
        int x = wallPosition.x;
        int y = wallPosition.y;
        return world[x + 1][y].equals(Tileset.FLOOR)
                || world[x - 1][y].equals(Tileset.FLOOR)
                  || world[x][y + 1].equals(Tileset.FLOOR)
                    || world[x][y - 1].equals(Tileset.FLOOR);
    }

    /* Create a 2D array with rooms and halls.
     * @param width (2 unit less than map width).
     * @param height (2 unit less than map).
     * @return a 2D array with room and halls.
     */
    private TETile[][] createRoomAndHall(int width, int height) {
        // Create a roomAndHall variable that store our world.
        TETile[][] roomAndHall = new TETile[width][height];

        // maxArea is the maximum area that floor can cover.
        int maxArea = (int) (FLOOR_PERCENTAGE * width * height);

        // Draw random halls and rooms until the total area exceeds the maxArea.
        while (area < maxArea) {
            // This block should be either room or hall.
            Block block = new Room();

            // Randomly decide if a room (0) or hall (1) should be created.
            int num = RANDOM.nextInt(2);

            // Check if a valid room or hall is created.
            boolean isCreated = false;

            // Keeps repeating the process until a valid room or hall is produced.
            while (!isCreated) {
                if (num == 0) {
                    // Randomly shape the room.
                    int range = MAX_ROOM_SIZE - MIN_ROOM_SIZE + 1;
                    int roomWidth = MIN_ROOM_SIZE + RANDOM.nextInt(range);
                    int roomHeight = MIN_ROOM_SIZE + RANDOM.nextInt(range);
                    int x = RANDOM.nextInt(width - roomWidth - 1) + 1;
                    int y = RANDOM.nextInt(height - roomHeight - 1) + 1;
                    Position position = new Position(x, y);

                    // Create the room.
                    block = new Room(position, roomWidth, roomHeight);
                } else {
                    // Randomly shape the hall.
                    int lengthRange = MAX_LENGTH - MIN_LENGTH + 1;
                    int length = MIN_LENGTH + RANDOM.nextInt(lengthRange);
                    int hallWidth = 0;
                    int hallHeight = 0;

                    // Choose randomly between vertical and horizontal hall.
                    boolean isVertical = false;
                    if (RANDOM.nextInt(2) == 0) {
                        isVertical = true;
                        hallWidth = 1;
                        hallHeight = length;
                    } else {
                        isVertical = false;
                        hallWidth = length;
                        hallHeight = 1;
                    }

                    int x = RANDOM.nextInt(width - hallWidth - 1) + 1;
                    int y = RANDOM.nextInt(height - hallHeight - 1) + 1;
                    Position position = new Position(x, y);

                    // Create the hall.
                    block = new Hall(position, length, isVertical);
                }
                // Make sure the room or hall created valid.
                isCreated = isValidBlock(block);
            }
            // Draw a room or hall.
            drawBlock(roomAndHall, block);

            // Connect it to a previous  in our collection by drawing hall.
            connectBlock(roomAndHall, block);

            // Add it to our collection.
            addBlock(block);
        }
        return roomAndHall;
    }

    /* Make sure this room or hall does not overlap with other rooms or halls.
     * @param a room.
     * @return true if it does not overlap, false otherwise.
     */
    private boolean isValidBlock(Block block) {
        for (Block otherBlock : blocks) {
            if (block.intersect(otherBlock)) {
                return false;
            }
        }
        return true;
    }

    /* Draw a block or hall on the world.
     * @param 2D array world.
     * @param a block.
     */
    private void drawBlock(TETile[][] world, Block block) {
        for (int x = block.p1.x; x <= block.p2.x; ++x) {
            for (int y = block.p1.y; y <= block.p2.y; ++y) {
                world[x][y] = Tileset.FLOOR;
            }
        }
    }

    /* Connect a room to the previous  in our collection through their
     * special interior points.
     * @param 2D array world.
     * @param a room.
     */
    private void connectBlock(TETile[][] world, Block block) {
        if (blocks.size() != 0) {
            Block previousBlock = blocks.get(blocks.size() - 1);
            Position p1 = block.interiorPoint;
            Position p2 = previousBlock.interiorPoint;
            specialConnect(world, p1, p2);
        }
    }

    /* This special method draws the vertical hall and horizontal hall to
     * connect the randomPoint 's of two room. However, unlike the drawRoom() method,
     * this method can be called when the halls that are about to be drawn overlaps with
     * other existed rooms or hall. As a result, in addition to drawing and adding the hall
     * into our collection, we going to subtract the overlapped area from the total area after
     * adding the hall with the help of specialDraw() method.
     * @param 2D array world.
     * @param room.
     * @param room.
     */
    private void specialConnect(TETile[][] world, Position p1, Position p2) {
        // Set up p1 and p2 for personal preferences, p1 is on the left of p2.
        if (p1.x > p2.x) {
            Position temp = p1;
            p1 = p2;
            p2 = temp;
        }

        // Initialize variables needed for constructing halls.
        Position verticalHallPosition = new Position();
        Position horizontalHallPosition = new Position();
        int verticalHallLength = 0;
        int horizontalHallLength = 0;

        // Connect by drawing vertical hall first, horizontal second and vice versa.
        int rand = RANDOM.nextInt(2);

        if (rand == 0) {
            if (p2.y > p1.y) {
                verticalHallPosition = p1;
                horizontalHallPosition = new Position(p1.x, p2.y);
                verticalHallLength = p2.y - p1.y + 1;
                horizontalHallLength = p2.x - p1.x + 1;
            } else {
                verticalHallPosition = new Position(p1.x, p2.y);
                horizontalHallPosition = verticalHallPosition;
                verticalHallLength = p1.y - p2.y + 1;
                horizontalHallLength = p2.x - p1.x + 1;
            }
        } else {
            if (p2.y > p1.y) {
                horizontalHallPosition = p1;
                verticalHallPosition = new Position(p2.x, p1.y);
                horizontalHallLength = p2.x - p1.x + 1;
                verticalHallLength = p2.y - p1.y + 1;
            } else {
                horizontalHallPosition = p1;
                verticalHallPosition = p2;
                horizontalHallLength = p2.x - p1.x + 1;
                verticalHallLength = p1.y - p2.y + 1;
            }
        }

        // Add and Draw.
        Block verticalHall = new Hall(verticalHallPosition, verticalHallLength, true);
        Block horizontalHall = new Hall(horizontalHallPosition, horizontalHallLength, false);
        addBlock(verticalHall);
        addBlock(horizontalHall);
        specialDraw(world, verticalHall);
        specialDraw(world, horizontalHall);
    }

    /* This special method draws and subtracts overlap area.
     * @param 2D array world.
     * @param room.
     */
    private void specialDraw(TETile[][] world, Block block) {
        for (int x = block.p1.x; x <= block.p2.x; ++x) {
            for (int y = block.p1.y; y <= block.p2.y; ++y) {
                if (world[x][y] == Tileset.FLOOR) {
                    area -= 1;
                } else {
                    world[x][y] = Tileset.FLOOR;
                }
            }
        }
    }

    /* Adds a block to our collection and updates area usage.
     * @param a room.
     */
    private void addBlock(Block block) {
        blocks.add(block);
        area += block.width * block.height;
    }
}
