package byog.Core.Interactivity;

import byog.Core.Agents.*;

import byog.Core.Environment.GameState;
import byog.Core.WorldGenerator.MapGenerator;
import byog.TileEngine.TETile;

import edu.princeton.cs.introcs.StdDraw;
import edu.princeton.cs.introcs.StdAudio;

import java.awt.Color;
import java.awt.Font;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.File;

import java.lang.Character;

/* This class controls the menu of the game.
 */
public class Game {
    private static final int WIDTH = 80;
    private static final int HEIGHT = 30;
    private boolean render = true;
    private Agent [] agents;

    public Game() {
        StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 16);

        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);

        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);

        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
    }

    public Game(Agent [] agents) {
        this();
        this.agents = agents;
    }

    public void muteGraphics() {
        render = false;
    }

    public void playWithKeyboard() {
        String userOption = "";

//        StdAudio.loop("/byog/Soundtrack/menu.wav");

        while (!userOption.toLowerCase().equals("q")) {
            drawMenu("", "");

            boolean validOption = false;

            String fileName = "byog/Core/Interactivity/filer.ser";

            while (!validOption) {
                userOption = getOption().toLowerCase();

                File newFile = new File(fileName);

                if (userOption.equals("n") || (userOption.equals("l") && newFile.length() != 0)
                        || userOption.equals("q")) {
                    validOption = true;
                }
            }

            if (userOption.equals("n")) {
                Long seed = getInput();
                MapGenerator m = new MapGenerator(seed);

                TETile[][] map= new TETile[WIDTH][HEIGHT];
                m.generateWorld(map);
                HUD hud = new HUD(map);

                int numOfAgents = 4;
                Agent[] agents = new Agent[numOfAgents];
                agents[0] = new Player(map);

                if(this.agents == null) {
                    for (int i = 1; i < numOfAgents; ++i) {
                        agents[i] = new ApproximateQAgent(map, i);
                    }
                } else {
                    for(int i = 1; i < numOfAgents; ++i) {
                        agents[i] = this.agents[i];
                        agents[i].setMap(map);
                        ((ApproximateQAgent) agents[i]).interact();
                    }
                }

                this.agents = agents;
                for (int i = 0; i < numOfAgents; ++i) {
                    this.agents[i].initialPosition();
                }

                GameState state = new GameState(map, this.agents);

                for(int i = 1; i < this.agents.length; ++i) {
                    ((ApproximateQAgent) this.agents[i]).printWeights();
                }

                Controller c = new Controller(hud, state);

                boolean wantToSaved = c.render();
                resetScreen();

                if (wantToSaved) {
                    try {
                        FileOutputStream file = new FileOutputStream(fileName);
                        ObjectOutputStream out = new ObjectOutputStream(file);

                        out.writeObject(c);
                        out.close();
                        file.close();

                        drawPostGame("Go back to main menu !");
                    } catch (IOException ex) {
                        drawPostGame("IOException is caught ! Sorry for your inconvenience.");
                    }
                } else if (c.isVictory()){
                    drawPostGame("Victory !");
                } else {
                    drawPostGame("Game Over !");
                }
                StdDraw.pause(700);

            } else if (userOption.equals("l")) {
                Controller c = null;

                try {
                    FileInputStream file = new FileInputStream(fileName);
                    ObjectInputStream in = new ObjectInputStream(file);

                    c = (Controller) in.readObject();
                    in.close();
                    file.close();
                } catch (IOException ex) {
                    drawPostGame("IOException is caught ! Sorry for your inconvenience.");
                } catch (ClassNotFoundException ex) {
                    drawPostGame("ClassNotFoundException is caught ! Sorry for your inconvenience");
                }

                boolean wantToSaved = c.render();
                resetScreen();

                if (wantToSaved) {
                    try {
                        FileOutputStream file = new FileOutputStream(fileName);
                        ObjectOutputStream out = new ObjectOutputStream(file);

                        out.writeObject(c);
                        out.close();
                        file.close();

                        drawPostGame("Go back to main menu !");
                    } catch (IOException ex) {
                        drawPostGame("IOException is caught ! Sorry for your inconvenience.");
                    }
                } else if (c.isVictory()) {
                    drawPostGame("Victory !");
                } else {
                    drawPostGame("Game Over!");
                }
                StdDraw.pause(1000);
            }
        }

        drawPostGame("Good Bye !");
        StdDraw.pause(800);
        System.exit(0);
    }

    private String getOption() {
        String input = "";
        while (input.length() < 1) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            char key = StdDraw.nextKeyTyped();
            input += String.valueOf(key);
        }
        return input;
    }

    private Long getInput() {
        String input = "n";
        drawMenu("Enter Random Seed (Press S to finish)", input.substring(1));
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                if (Character.isDigit(key) || Character.toLowerCase(key) == 's') {
                    input += String.valueOf(key);
                }
                if (input.substring(input.length() - 1).toLowerCase().equals("s")) {
                    return process(input);
                } else {
                    drawMenu("Enter Random Seed (Press S to finish)", input.substring(1));
                }
            }
        }
    }

    private long process(String input) {
        if (input.equals("ns")) {
            return 0;
        }
        return Long.parseLong(input.substring(1, input.length() - 1));
    }

    private void drawMenu(String slogan, String input) {
        // draw background
        StdDraw.picture(WIDTH / 2, HEIGHT / 2, "byog/Images/background.png");

        int midWidth = WIDTH / 2;
        int midHeight = HEIGHT / 2;

        Font bigFont = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(bigFont);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(midWidth, midHeight + 8, "The Game");

        Font smallFont = new Font("Monaco", Font.BOLD, 15);
        StdDraw.setFont(smallFont);
        StdDraw.setPenColor(Color.red);
        StdDraw.text(midWidth, midHeight + 5, "New Game (N)");
        StdDraw.setPenColor(Color.green);
        StdDraw.text(midWidth, midHeight + 4, "Load Game (L)");
        StdDraw.setPenColor(Color.blue);
        StdDraw.text(midWidth, midHeight + 3, "Quit (Q)");

        Font creditFont = new Font("Monaco", Font.ITALIC, 12);
        StdDraw.setFont(creditFont);
        StdDraw.setPenColor(Color.white);
        StdDraw.textRight(WIDTH,  HEIGHT - 1, "Created by Vi Tran and Phuoc Do.");

        StdDraw.setPenColor(Color.red);
        StdDraw.text(midWidth, midHeight + 1, slogan);
        StdDraw.text(midWidth, midHeight, input);

        StdDraw.show();
    }

    private void drawPostGame(String input) {
        // draw background
        StdDraw.picture(WIDTH / 2, HEIGHT / 2, "byog/Images/background.png");

        int midWidth = WIDTH / 2;
        int midHeight = HEIGHT / 2;

        Font bigFont = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(bigFont);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(midWidth, midHeight + 8, "The Game");

        Font smallFont = new Font("Monaco", Font.BOLD, 15);
        StdDraw.setFont(smallFont);
        StdDraw.setPenColor(Color.red);
        StdDraw.text(midWidth, midHeight + 5, input);

        Font creditFont = new Font("Monaco", Font.ITALIC, 12);
        StdDraw.setFont(creditFont);
        StdDraw.setPenColor(Color.white);
        StdDraw.textRight(WIDTH,  HEIGHT - 1, "Created by Vi Tran and Phuoc Do.");

        StdDraw.show();
    }

    private void resetScreen() {
        StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 16);

        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);

        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);

        StdDraw.clear(Color.BLACK);
    }
}
