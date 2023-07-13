package src.Game;

import src.Menus.Launcher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author anthony-pc
 */
public class GameWorld extends JPanel implements Runnable {
    private Tank greenTank, blueTank;
    private BufferedImage world;
    private final Launcher lf;
    private long tick = 0;
    List<GameObject> gameObjects;
    List<Animation> activeAnimations = new ArrayList<>(15);
    Sound bg, ts;

    /**
     *
     */
    public GameWorld(Launcher lf) {
        ts = Resources.getSound("title");
        ts.setLooping();
        ts.setVolume(0.2f);
        ts.play();
        this.lf = lf;
    }

    @Override
    public void run() {
        try {
            ts.stop();
            bg = Resources.getSound("background");
            bg.setLooping();
            bg.setVolume(0.2f);
            bg.play();
            while (true) {
                this.tick++;
                this.greenTank.update(this);
                this.blueTank.update(this);
                this.checkCollision();
                this.activeAnimations.forEach(a -> a.update());
                this.repaint();
                /*
                 * Sleep for 1000/144 ms (~6.9ms). This is done to have our
                 * loop run at a fixed rate per/sec.
                 */
                Thread.sleep(1000 / 144);
                this.activeAnimations.removeIf(a -> !a.isRunning());
            }
        } catch (InterruptedException ignored) {
            System.out.println(ignored);
        }
    }

    private void checkCollision() throws InterruptedException {
        for (int i = 0; i < this.gameObjects.size(); i++) {
            GameObject obj1 = this.gameObjects.get(i);
            if (obj1 instanceof Wall || obj1 instanceof PowerUps) {
                continue;
            }
            for (int j = 0; j < this.gameObjects.size(); j++) {
                if (i == j) continue;
                GameObject obj2 = this.gameObjects.get(j);
                if (obj2 instanceof Tank) continue;
                if (obj1.getHitBox().intersects(obj2.getHitBox())) {
                    obj1.collides(obj2, this);
                }
            }
        }
    }

//    private void updateWalls() {
//
//        for (int i = 0; i < BreakableWall.size(); i++) {
//
//            BreakableWall bwall = bWalls.get(i);
//            if (!bwall.isVisible()) {
//
//                bWalls.remove(i);
//            }
//        }
//    }

    /**
     * Reset game to its initial state.
     */
    public void resetGame() {
        this.tick = 0;
        this.greenTank.setX(300);
        this.greenTank.setY(300);
    }

    /**
     * Load all resources for Tank Wars Game. Set all Game Objects to their
     * initial state as well.
     */
    public void InitializeGame() {
        this.world = new BufferedImage(GameConstants.GAME_WORLD_WIDTH,
                GameConstants.GAME_WORLD_HEIGHT,
                BufferedImage.TYPE_INT_RGB);
        gameObjects = new ArrayList<>(720);
        InputStreamReader isr = new InputStreamReader(GameWorld.class.getClassLoader().getResourceAsStream("Map/map1.csv"));
        try (BufferedReader mapReader = new BufferedReader(isr)) {
            for (int i = 0; mapReader.ready(); i++) {
                String[] items = mapReader.readLine().split(",");
                for (int j = 0; j < items.length; j++) {
                    String objectType = items[j];
                    if ("0".equals(objectType)) continue;
                    gameObjects.add(GameObject.getNewInstance(objectType, j * 30, i * 30));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading map");
            System.exit(-2);
        }

        greenTank = new Tank(300, 300, Resources.getSprite("tank1"));
        TankControl playerTank1 = new TankControl(greenTank, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_SPACE);
        this.lf.getJf().addKeyListener(playerTank1);

        blueTank = new Tank(800, 800, Resources.getSprite("tank2"));
        TankControl playerTank2 = new TankControl(blueTank, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_F);
        this.lf.getJf().addKeyListener(playerTank2);

        this.gameObjects.add(greenTank);
        this.gameObjects.add(blueTank);
    }

    private void drawFloor(Graphics2D buffer) {
        BufferedImage floor = Resources.getSprite("Floor");
        for (int i = 0; i < GameConstants.GAME_WORLD_WIDTH; i += 320) {
            for (int j = 0; j < GameConstants.GAME_WORLD_HEIGHT; j += 240) {
                buffer.drawImage(floor, i, j, null);
            }
        }
    }

    private void renderMiniMap(Graphics2D g2, BufferedImage world) {
        BufferedImage mm = world.getSubimage(0,
                0,
                GameConstants.GAME_WORLD_WIDTH,
                GameConstants.GAME_WORLD_HEIGHT);
        g2.scale(.2, .2);
        g2.drawImage(mm,
                (GameConstants.GAME_SCREEN_WIDTH * 5) / 2 - (GameConstants.GAME_WORLD_WIDTH / 2),
                (GameConstants.GAME_SCREEN_HEIGHT * 5) - (GameConstants.GAME_WORLD_HEIGHT) - 190, null);
    }

    private void renderSplitScreen(Graphics2D g2, BufferedImage world) {
        BufferedImage lh = world.getSubimage((int) this.greenTank.getScreen_x(),
                (int) this.greenTank.getScreen_y(),
                GameConstants.GAME_SCREEN_WIDTH / 2,
                GameConstants.GAME_SCREEN_HEIGHT);
        BufferedImage rh = world.getSubimage((int) this.blueTank.getScreen_x(),
                (int) this.blueTank.getScreen_y(),
                GameConstants.GAME_SCREEN_WIDTH / 2,
                GameConstants.GAME_SCREEN_HEIGHT);
        g2.drawImage(lh, 0, 0, null);
        g2.drawImage(rh, (GameConstants.GAME_SCREEN_WIDTH / 2) + 4, 0, null);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Graphics2D buffer = world.createGraphics();
        this.drawFloor(buffer);
        this.gameObjects.forEach(w -> w.drawImage(buffer));
        this.greenTank.drawImage(buffer);
        this.blueTank.drawImage(buffer);
        this.activeAnimations.forEach(a -> a.drawImage(buffer));
        renderSplitScreen(g2, world);
        renderMiniMap(g2, world);
    }
}