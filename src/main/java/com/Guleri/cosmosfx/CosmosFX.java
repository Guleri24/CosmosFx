package com.Guleri.cosmosfx;

import com.jpro.webapi.WebAPI;
import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

public class CosmosFX<Player> extends Application {
    // Switches to switch on/off different features

    private static final boolean PLAY_SOUND = true;
    private static final boolean PLAY_MUSIC = true;
    private static final boolean SHOW_BACKGROUND = true;
    private static final boolean SHOW_STARS = true;
    private static final boolean SHOW_ENEMIES = true;
    private static final boolean SHOW_ASTEROIDS = true;
    private static final int NO_OF_STARS = SHOW_STARS ? 200 : 0;
    private static final int NO_OF_ASTEROIDS = SHOW_ASTEROIDS ? 15 : 0;
    private static final int NO_OF_ENEMIES = SHOW_ENEMIES ? 5 : 0;

    private static final int LIFE = 5;
    private static final int SHIELDS = 10;
    private static final int DEFLECTOR_SHIELD_TIME = 5000;
    private static final int MAX_NO_OF_ROCKETS = 3;
    private static final double TORPEDO_SPEED = 6;
    private static final double ROCKET_SPEED = 4;
    private static final double ENEMY_TORPEDO_SPEED = 5;
    private static final double ENEMY_BOSS_TORPEDO_SPEED = 6;
    private static final int ENEMY_FIRE_SENSITIVITY = 10;
    private static final long ENEMY_BOSS_ATTACK_INTERVAL = 20_000_000_000L;
    private static final long CRYSTAL_SPAWN_INTERVAL = 25_000_000_000L;
    private static final Random RND = new Random();
    private static final double WIDTH = 700;
    private static final double HEIGHT = 900;
    private static final double FIRST_QUARTER_WIDTH = WIDTH * 0.25;
    private static final double LAST_QUARTER_WIDTH = WIDTH * 0.75;
    private static final double SHIELD_INDICATOR_X = WIDTH * 0.73;
    private static final double SHIELD_INDICATOR_Y = HEIGHT * 0.26;
    private static final long FPS_60 = 3894710L;
    private static final long FPS_30 = 7190235L;
    private static final long FPS_10 = 16777216L;
    private static final long FPS_2 = 83886080L;
    private static final double VELOCITY_FACTOR_X = 1.0;
    private static final double VELOCITY_FACTOR_Y = 1.0;
    private static final double VELOCITY_FACTOR_R = 1.0;
    private static final Color SCORE_COLOR = Color.rgb(51, 210, 206);
    private static final String SPACE_BOY = null;
    private static final boolean IS_BROWSER = WebAPI.isBrowser();
    private static String spaceBoyName;


    private final Image[] asteroidImages = {
            new Image(getClass().getResourceAsStream("asteroid1.png"), 140, 140, true, false),
            new Image(getClass().getResourceAsStream("asteroid2.png"), 140, 140, true, false),
            new Image(getClass().getResourceAsStream("asteroid3.png"), 140, 140, true, false),
            new Image(getClass().getResourceAsStream("asteroid4.png"), 110, 110, true, false),
            new Image(getClass().getResourceAsStream("asteroid5.png"), 100, 100, true, false),
            new Image(getClass().getResourceAsStream("asteroid6.png"), 120, 120, true, false),
            new Image(getClass().getResourceAsStream("asteroid7.png"), 110, 110, true, false),
            new Image(getClass().getResourceAsStream("asteroid8.png"), 100, 100, true, false),
            new Image(getClass().getResourceAsStream("asteroid9.png"), 130, 130, true, false),
            new Image(getClass().getResourceAsStream("asteroid10.png"), 120, 120, true, false),
            new Image(getClass().getResourceAsStream("asteroid11.png"), 140, 140, true, false)
    };

    private final Image deflectorShieldImg      = new Image(getClass().getResourceAsStream("deflectorshield.png"), 100, 100, true, false);
    private final Image miniDeflectorShieldImg  = new Image(getClass().getResourceAsStream("deflectorshield.png"), 16, 16, true, false);
    private final Image torpedoImg              = new Image(getClass().getResourceAsStream("torpedo.png"), 17, 20, true, false);

    private final Image[] enemyImages = {
            new Image(getClass().getResourceAsStream("enemy1.png"), 56, 56, true, false),
            new Image(getClass().getResourceAsStream("enemy2.png"), 50, 50, true, false),
            new Image(getClass().getResourceAsStream("enemy3.png"), 68, 68, true, false)
    };


    private final Image enemyBossImg0            = new Image(getClass().getResourceAsStream("enemyBoss0.png"), 100, 100, true, false);
    private final Image enemyBossImg1            = new Image(getClass().getResourceAsStream("enemyBoss1.png"), 100, 100, true, false);
    private final Image enemyBossImg2            = new Image(getClass().getResourceAsStream("enemyBoss2.png"), 100, 100, true, false);
    private final Image enemyBossImg3            = new Image(getClass().getResourceAsStream("enemyBoss3.png"), 100, 100, true, false);
    private final Image enemyBossImg4            = new Image(getClass().getResourceAsStream("enemyBoss4.png"), 100, 100, true, false);
    private final Image spaceshipImg             = new Image(getClass().getResourceAsStream("fighter.png"), 48, 48, true, false);
    private final Image spaceshipThrustImg       = new Image(getClass().getResourceAsStream("fighterThrust.png"), 48, 48, true, false);
    private final Image miniSpaceshipImg         = new Image(getClass().getResourceAsStream("fighter.png"), 16, 16, true, false);
    private final Image enemyTorpedoImg          = new Image(getClass().getResourceAsStream("enemyTorpedo.png"), 21, 21, true, false);
    private final Image enemyBossTorpedoImg      = new Image(getClass().getResourceAsStream("enemyBossTorpedo.png"), 26, 26, true, false);
    private final Image explosionImg             = new Image(getClass().getResourceAsStream("explosion.png"), 960, 768, true, false);
    private final Image asteroidExplosionImg     = new Image(getClass().getResourceAsStream("asteroidExplosion.png"), 2048, 1792, true, false);
    private final Image spaceShipExplosionImg    = new Image(getClass().getResourceAsStream("spaceshipexplosion.png"), 800, 600, true, false);
    private final Image hitImg                   = new Image(getClass().getResourceAsStream("torpedoHit2.png"), 400, 160, true, false);
    private final Image enemyBossHitImg          = new Image(getClass().getResourceAsStream("torpedoHit.png"), 400, 160, true, false);
    private final Image enemyBossExplosionImg    = new Image(getClass().getResourceAsStream("enemyBossExplosion.png"), 800, 1400, true, false);
    private final Image crystalImg               = new Image(getClass().getResourceAsStream("crystal.png"), 100, 100, true, false);
    private final Image crystalExplosionImg      = new Image(getClass().getResourceAsStream("crystalExplosion.png"), 400, 700, true, false);
    private final Image rocketImg                = new Image(getClass().getResourceAsStream("rocket.png"), 17, 50, true, false);
    private final Image rocketExplosionImg       = new Image(getClass().getResourceAsStream("rocketExplosion.png"), 512, 896, true, false);

    private boolean running;
    private boolean gameOverScreen;
    private boolean hallOfFameScreen;
    private List<Player> hallOfFame;
    private boolean inputAllowed;
    private Text userName;
/*    private Image startImg = new Image(getClass().getResourcesAsStream("startscreen.png"));
    private Image gameOverImg = new Image(getClass().getResourcesAsStream("gameover.png"));
    private Image backgroundImg = new Image(getClass().getResourcesAsStream("background.png"));*/

    @Override
    public void start(Stage stage) throws Exception {

    }
}
