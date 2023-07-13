package src.Game;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

public class Resources {
    private static final Map<String, BufferedImage> sprites = new HashMap<>();
    private static final Map<String, List<BufferedImage>> animations = new HashMap<>();
    private static final Map<String, Integer> animationInfo = new HashMap<>() {{
        put("explosion", 32);
        put("nuke", 24);
        put("bullethit", 7);
    }};
    private static final Map<String, Sound> sounds = new HashMap<>();

    private static BufferedImage loadSprite(String path) throws IOException {
        return ImageIO.read(Objects.requireNonNull(Resources.class.
                getClassLoader().getResource(path), "Resource %s is not found".formatted(path)));
    }

    private static Sound loadSound(String path) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        AudioInputStream ais = AudioSystem.getAudioInputStream(Objects.requireNonNull(Resources.class.
                getClassLoader().getResource(path), "Resource %s is not found".formatted(path)));
        Clip c = AudioSystem.getClip();
        c.open(ais);
        Sound s = new Sound(c);
        s.setVolume(2f);
        return s;
    }

//    private static List<BufferedImage> loadAnimations(String path) throws IOException {
//        return ImageIO.read(Objects.requireNonNull(Resources.class.
//                getClassLoader().getResource(path), "Resource %s is not found".formatted(path)));
//    }

    private static void initSprites() {
        try {
            Resources.sprites.put("tank1", loadSprite("Images/tank1.png"));
            Resources.sprites.put("tank2", loadSprite("Images/tank2.png"));
            Resources.sprites.put("menu", loadSprite("Images/title.png"));
            Resources.sprites.put("Heart", loadSprite("Images/HealthHeart.png"));
            Resources.sprites.put("DamageUp", loadSprite("Images/Bomb.png"));
            Resources.sprites.put("Wall1", loadSprite("Images/Wall1.png"));
            Resources.sprites.put("Wall2", loadSprite("Images/Wall2.png"));
            Resources.sprites.put("Bullet1", loadSprite("Images/Bullet.png"));
            Resources.sprites.put("FireRateUP", loadSprite("Images/PrismaticShard.png"));
            Resources.sprites.put("Floor", loadSprite("Images/bg.bmp"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void initAnimations() {
        String animationBasePath = "Animations/%s/%s_%04d.png";
        Resources.animationInfo.forEach((animationName, frameCount) -> {
            List<BufferedImage> temp = new ArrayList<>(frameCount);
            try {
                for (int i = 0; i < frameCount; i++) {
                    String framePath = animationBasePath.formatted(animationName, animationName, i);
                    temp.add(loadSprite(framePath));
                }
                Resources.animations.put(animationName, temp);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static void initSounds() {
        try {
            Resources.sounds.put("bullet", Resources.loadSound("Sound/bullet.wav"));
            Resources.sounds.put("pickup", Resources.loadSound("Sound/pickup.wav"));
            Resources.sounds.put("hit", Resources.loadSound("Sound/shotexplosion.wav"));
            Resources.sounds.put("fire", Resources.loadSound("Sound/shotfiring.wav"));
            Resources.sounds.put("background", Resources.loadSound("Sound/filming.wav"));
            Resources.sounds.put("title", Resources.loadSound("Sound/freezer.wav"));
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    public static BufferedImage getSprite(String type) {
        if (!Resources.sprites.containsKey(type)) {
            throw new RuntimeException("%s is missing from resource map".formatted(type));
        }
        return Resources.sprites.get(type);
    }

    public static List<BufferedImage> getAnimation(String type) {
        if (!Resources.animations.containsKey(type)) {
            throw new RuntimeException("%s is missing from resource map".formatted(type));
        }
        return Resources.animations.get(type);
    }

    public static Sound getSound(String type) {
        if (!Resources.sounds.containsKey(type)) {
            throw new RuntimeException("%s is missing from resource map".formatted(type));
        }
        return Resources.sounds.get(type);
    }

    public static void loadAssets() {
        initSprites();
        initAnimations();
        initSounds();
    }
}