package src.Game;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Bullet extends GameObject {
    private float vx, vy, angle;
    private int width, height;
    private float R = 7;
    private boolean visible;

    Bullet(float x, float y, float angle, BufferedImage image) {
        super(x, y, image);
        this.vx = 0;
        this.vy = 0;
        this.angle = angle;
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.visible = true;
    }

    void setX(float x) {
        this.x = x;
    }

    void setY(float y) {
        this.y = y;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getDamage() {
        return damage;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    void update(GameWorld gw) {
        vx = Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = Math.round(R * Math.sin(Math.toRadians(angle)));
        x += 3 * vx;
        y += 3 * vy;
        checkBorder();
        this.hitbox.setLocation((int) x, (int) y);
    }

    private void checkBorder() {
        if (x < 30) {
            x = 30;
        }
        if (x >= GameConstants.GAME_WORLD_WIDTH - 88) {
            x = GameConstants.GAME_WORLD_WIDTH - 88;
        }
        if (y < 40) {
            y = 40;
        }
        if (y >= GameConstants.GAME_WORLD_HEIGHT - 80) {
            y = GameConstants.GAME_WORLD_HEIGHT - 80;
        }
    }

    @Override
    public void collides(GameObject obj2, GameWorld gw) throws InterruptedException {
        if (obj2 instanceof Bullet) {
            Animation bulletHit = new Animation(Resources.getAnimation("bullethit"), ((Bullet) obj2).getX(), ((Bullet) obj2).getY());
            gw.activeAnimations.add(bulletHit);
        }
    }

    public void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.image.getWidth() / 2.0, this.image.getHeight() / 2.0);
        rotation.scale(1, 1);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.image, rotation, null);
        g2d.setColor(Color.RED);
    }
}