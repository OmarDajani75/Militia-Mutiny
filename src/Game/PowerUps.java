package src.Game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.TimerTask;
import java.util.Timer;

public class PowerUps extends GameObject{
    private int width, height, random;
    private final int DELAY = 6000;
    Timer timer;
    private boolean visible;

    public PowerUps (float x, float y, BufferedImage image) {
        super(x, y, image);
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.visible = true;
    }

    public void applyPowerUp(Tank t1) {
    }

    public void DamageUp(Tank t1) {
        int oldDamage = t1.getDamage();
        t1.setDamage(-24);
        timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                t1.setDamage(oldDamage);
            }
        }, DELAY);
    }

    public void FireRateUP(Tank t1) {
        int oldROF = t1.getRateOfFire();
        t1.setRateOfFire(5);
        timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                t1.setRateOfFire(oldROF);
            }
        }, DELAY);
    }

    public void HealthRestore(Tank t1) {
        t1.setLives(t1.getLives() + 1);
    }

    public int getRandom() {
        return random;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    @Override
    public void collides(GameObject obj2, GameWorld gw) throws InterruptedException {
        if (obj2 instanceof PowerUps) {
            Resources.getSound("pickup").play();
            //((PowerUps) obj2).applyPowerUp();
        }
    }

    @Override
    public void drawImage(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.image, (int)x, (int) y, null);
    }
}