package src.Game;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Tank extends GameObject {
    private float vx, vy, angle, screen_x, screen_y;
    private int width, height, rateOfFire;
    private Tank greenTank, blueTank;
    private float R = 4;
    private float ROTATIONSPEED = 2.0f;
    List<Bullet> ammo = new ArrayList<>();
    long bulletCooldown = 4000;
    Bullet currentChargeBullet = null;
    long timeSinceLastShot = 0L;
    private int lives = 3;
    private int health = 50;
    private boolean UpPressed, DownPressed, RightPressed, LeftPressed, shootPressed;

    public Tank(float x, float y, BufferedImage image) {
        super(x, y, image);
        this.vx = 0;
        this.vy = 0;
        this.angle = 0;
        this.width = image.getWidth();
        this.height = image.getHeight();
        ammo = new ArrayList<>();
        this.damage = -6;
        this.rateOfFire = 17;
        centerScreen();
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

    public float getScreen_x() {
        return screen_x;
    }

    public float getScreen_y() {
        return screen_y;
    }

    private int safeShootX() {
        double cx = 31 * Math.cos(Math.toRadians(this.angle));
        return (int) (x + this.image.getWidth() / 2f + cx - 4f);
    }

    private int safeShootY() {
        double cy = 31 * Math.sin(Math.toRadians(this.angle));
        return (int) (y + this.image.getWidth() / 2f + cy - 4f);
    }

    public void toggleShootPressed() {
        this.shootPressed = true;
    }

    public void untoggleShootPressed() {
        this.shootPressed = false;
    }

    public int getDamage() {
        return damage;
    }

    public int getRateOfFire() {
        return rateOfFire;
    }

    public void setRateOfFire(int rateOfFire) {
        this.rateOfFire = rateOfFire;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    void toggleUpPressed() {
        this.UpPressed = true;
    }

    void toggleDownPressed() {
        this.DownPressed = true;
    }

    void toggleRightPressed() {
        this.RightPressed = true;
    }

    void toggleLeftPressed() {
        this.LeftPressed = true;
    }

    void unToggleUpPressed() {
        this.UpPressed = false;
    }

    void unToggleDownPressed() {
        this.DownPressed = false;
    }

    void unToggleRightPressed() {
        this.RightPressed = false;
    }

    void unToggleLeftPressed() {
        this.LeftPressed = false;
    }

    private void rotateLeft() {
        this.angle -= this.ROTATIONSPEED;
    }

    private void rotateRight() {
        this.angle += this.ROTATIONSPEED;
    }

    private void moveBackwards() {
        vx = Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = Math.round(R * Math.sin(Math.toRadians(angle)));
        x -= vx;
        y -= vy;
        checkBorder();
        centerScreen();
    }

    private void moveForwards() {
        vx = Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = Math.round(R * Math.sin(Math.toRadians(angle)));
        x += vx;
        y += vy;
        checkBorder();
        centerScreen();
    }

    void update(GameWorld gw) {
        if (this.UpPressed) {
            this.moveForwards();
        }

        if (this.DownPressed) {
            this.moveBackwards();
        }

        if (this.LeftPressed) {
            this.rotateLeft();
        }

        if (this.RightPressed) {
            this.rotateRight();
        }

        if (this.shootPressed && (this.timeSinceLastShot + this.bulletCooldown) < System.currentTimeMillis()) {
            Resources.getSound("bullet").play();
            this.ammo.add(new Bullet(this.safeShootX(), this.safeShootY(), angle, Resources.getSprite("Bullet1")));
            Animation a = new Animation(Resources.getAnimation("explosion"), safeShootX(), safeShootY());
            Animation b = new Animation(Resources.getAnimation("nuke"), safeShootX(), safeShootY());
            gw.activeAnimations.add(a);
            gw.activeAnimations.add(b);
            for (Bullet bullet : this.ammo) {
                gw.gameObjects.add(bullet);
            }
            this.timeSinceLastShot = System.currentTimeMillis();
        }
        this.ammo.forEach(bullet -> bullet.update(gw));
        this.hitbox.setLocation((int) x, (int) y);
    }

    private void centerScreen() {
        int minX = GameConstants.GAME_SCREEN_WIDTH / 4;
        int maxX = GameConstants.GAME_WORLD_WIDTH - minX;
        int minY = GameConstants.GAME_SCREEN_HEIGHT / 2;
        int maxY = GameConstants.GAME_WORLD_HEIGHT - minY;

        if (getX() > minX && getX() < maxX) {
            this.screen_x = Math.abs(getX() - (GameConstants.GAME_SCREEN_WIDTH / 4));
        }
        if (getY() > minY && getY() < maxY) {
            this.screen_y = Math.abs(getY() - (GameConstants.GAME_SCREEN_HEIGHT / 2));
        }
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
        } else if (obj2 instanceof Wall) {
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
            //stop
        } else if (obj2 instanceof PowerUps) {
            Resources.getSound("pickup").play();
            ((PowerUps) obj2).applyPowerUp(this);
        }
    }

    public void pushFromWall() {
        if (DownPressed) {
            vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
            vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
            x += 4 * vx;
            y += 4 * vy;
        } else if (UpPressed) {
            vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
            vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
            x -= 4 * vx;
            y -= 4 * vy;
        }
    }

    public void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), width / 2.0, height / 2.0);
        //rotation.scale(6,6);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.image, rotation, null);
//        int distancep1 = 10;
//        int distancep2 = 10;
//        //Outputting the hearts
//        for (int i = 0; i < greenTank.getLives(); i++) {
//            g2d.drawImage(Resources.getSprite("Heart"), (int)greenTank.getX() + distancep1, (int)greenTank.getY() - 20, null);
//            distancep1 += 12;
//        }
//        for (int i = 0; i < blueTank.getLives(); i++) {
//            g2d.drawImage(Resources.getSprite("Heart"), (int)blueTank.getX() + distancep2, (int)blueTank.getY() - 20, null);
//            distancep2 += 12;
//        }
        this.ammo.forEach(b -> b.drawImage(g2d));
        if (this.currentChargeBullet != null) {
            this.currentChargeBullet.drawImage(g2d);
        }
        g2d.setColor(Color.GREEN);
        g2d.drawRect((int) x, (int) y - 20, 100, 15);
        long currentWidth = 100 - ((this.timeSinceLastShot + this.bulletCooldown) - System.currentTimeMillis()) / 40;
        if (currentWidth > 100) {
            currentWidth = 100;
        }
        g2d.fillRect((int) x, (int) y - 20, (int) currentWidth, 15);
    }
}
