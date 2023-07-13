package src.Game;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class GameObject {
    protected float x,y;
    private int width, height;
    protected int damage = 25;
    protected BufferedImage image;
    protected Rectangle hitbox;

    GameObject(float x, float y, BufferedImage image) {
        this.x = x;
        this.y = y;
        this.image = image;
        this.hitbox = new Rectangle((int)x, (int)y, width, height);
    }

    public static GameObject getNewInstance(String type, float x, float y) {
        return switch (type) {
            case "9", "3" -> new Wall(x, y, Resources.getSprite("Wall1"));
            case "2" -> new BreakableWall(x, y, Resources.getSprite("Wall2"));
            case "75" -> new PowerUps(x, y, Resources.getSprite("Heart"));
            case "16" -> new PowerUps(x, y, Resources.getSprite("FireRateUP"));
            case "32" -> new PowerUps(x, y, Resources.getSprite("DamageUp"));
            default -> throw new IllegalArgumentException("type not supported");
        };
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public Rectangle getHitBox() {
        return this.hitbox.getBounds();
    }

    public abstract void collides(GameObject obj2, GameWorld gw) throws InterruptedException;

    public abstract void drawImage(Graphics g);
}