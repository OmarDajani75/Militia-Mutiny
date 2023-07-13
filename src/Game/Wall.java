package src.Game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Wall extends GameObject {
    private int width, height;

    public Wall(float x, float y, BufferedImage image) {
        super(x, y, image);
        this.width = image.getWidth();
        this.height = image.getHeight();
    }

    @Override
    public void collides(GameObject obj2, GameWorld gw) throws InterruptedException {
        if (obj2 instanceof Wall) {
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
    }
}