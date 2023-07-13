package src.Game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BreakableWall extends GameObject {
    private int width, height;
    private int health = 20;
    private boolean visible;

    public BreakableWall(float x, float y, BufferedImage image) {
        super(x, y, image);
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.visible = true;
    }

    public void setHealth(int health) {
        this.health += health;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public void collides(GameObject obj2, GameWorld gw) throws InterruptedException {
        if (obj2 instanceof BreakableWall) {
            Resources.getSound("hit").play();
            if (this.x < obj2.x) {
                this.x = obj2.x - 45;
            }
            if (this.x > obj2.x) {
                this.x = obj2.x + 45;
            }
            if (this.y < obj2.y) {
                this.y = obj2.y - 45;
            }
            if (this.y > obj2.y) {
                this.y = obj2.y + 45;
            }
        }
    }

    public void drawImage(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.image, (int) x, (int) y, null);
        if (this.health == 0) {
            this.visible = false;
        }
    }
}

