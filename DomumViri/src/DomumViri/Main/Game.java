package DomumViri.Main;

import DomumViri.Menu;
import DomumViri.Rendering.Renderer;
import DomumViri.Managers.InputManager;
import DomumViri.Managers.AudioManager;
import DomumViri.Entities.Bot;
import DomumViri.Entities.Entity;
import DomumViri.Level.MapInfo;
import DomumViri.Level.MapName;
import DomumViri.Time.Time;
import DomumViri.Time.TimeStatus;
import DomumViri.User.Account;
import DomumViri.World.GameWorld;
import DomumViri.World.MultiPlayer.GameWorldMultiPlayer;
import DomumViri.World.SinglePlayer.GameWorldSinglePlayer;
import java.util.List;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.net.InetAddress;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Game {

    private AnimationTimer timer;
    private int width;
    private int height;
    private int gameTime;
    public static final int SECONDINNANOS = 1000000000;

    private int fps;
    private long lastFrame;
    private long lastSecond;
    private long lastFrameMS;
    private Stage currentStage;
    private GraphicsContext gc;
    private InputManager im;
    private Renderer renderer;
    private boolean fullscreen;
    private Menu menu;

    private GameWorld world;
    private Account account;

    private boolean playing;
    private boolean multiplayer;

    private InetAddress ip;
    private int port;

    private boolean debugPressed;
    private boolean debugTilePositionPressed;
    private boolean escapePressed;
    private boolean isMoveUpPressed;

    public boolean isPlaying() {
        return playing;
    }

    public Game(Stage currentStage, int gameTime, int sceneWidth, int sceneHeight, boolean fullscreen, Account account) {
        this.gameTime = gameTime;
        this.currentStage = currentStage;
        this.width = sceneWidth;
        this.height = sceneHeight;
        this.fullscreen = fullscreen;
        this.account = account;
        this.multiplayer = false;
    }

    public Game(Stage currentStage, int gameTime, int sceneWidth, int sceneHeight, boolean fullscreen, Account account, InetAddress ip, int port) {
        this.gameTime = gameTime;
        this.currentStage = currentStage;
        this.width = sceneWidth;
        this.height = sceneHeight;
        this.fullscreen = fullscreen;
        this.account = account;
        this.ip = ip;
        this.port = port;
        this.multiplayer = true;
    }

    /**
     * Genereert een compleet nieuwe gamescene met een nieuwe gamewereld.
     *
     * @param renderedWidth Op welk resolutie deze scene moet worden weergeven.
     * @param renderedHeight Op welk resolutie deze scene moet worden weergeven.
     * @param fullscreen Of deze scene in fullscreen modus moet worden weergeven
     * of niet.
     * @return
     */
    public Scene generateScene(int renderedWidth, int renderedHeight, boolean fullscreen) {
        this.fullscreen = fullscreen;

        Canvas canvas = new Canvas(width, height); //Maakt een nieuw canvas aan met de bepaalde hoogte en breedte waarop het level wordt gerendert (Dus niet weergeven).
        gc = canvas.getGraphicsContext2D();

        Group root = new Group();
        root.getChildren().add(canvas);

        Scene gameScene = new Scene(root);
        gameScene.setFill(Color.BLACK);

        renderer = new Renderer(gc, width, height, renderedWidth, renderedHeight);
        im = new InputManager(gameScene);

        if (multiplayer) {
            try {
                world = new GameWorldMultiPlayer(im, account, ip, port);
            } catch (Exception ex) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Couldn't connect to the given server!");
                alert.setHeaderText("A error has been catched with the following information!");
                alert.setContentText(ex.getMessage());
                alert.showAndWait();
                forceStop();
            }
        } else {
            world = new GameWorldSinglePlayer(MapName.Default, MapInfo.Default, im, GameMode.SinglePlayer, account, new Time(System.nanoTime(), gameTime, TimeStatus.Ingame));
        }
        lastSecond = System.nanoTime(); //De huidige seconde in nanosecondes.

        return gameScene;
    }

    /**
     * Hervat een level dat al begonnen is of op pauze is gezet.
     *
     * @param renderedWidth Op welk resolutie deze scene moet worden weergeven.
     * @param renderedHeight Op welk resolutie deze scene moet worden weergeven.
     * @param fullscreen Of deze scene in fullscreen modus moet worden weergeven
     * of niet.
     * @return
     */
    public Scene continueScene(int renderedWidth, int renderedHeight, boolean fullscreen) {
        this.fullscreen = fullscreen;

        Canvas canvas = new Canvas(width, height);
        gc = canvas.getGraphicsContext2D();

        Group root = new Group();
        root.getChildren().add(canvas);

        Scene gameScene = new Scene(root);
        gameScene.setFill(Color.BLACK);

        renderer = new Renderer(gc, width, height, renderedWidth, renderedHeight);
        im = new InputManager(gameScene);

        resetInputManager();
        lastSecond = System.nanoTime();

        return gameScene;
    }

    /**
     * Voegt een nieuwe inputmanager toe aan de gameworld.
     */
    public void resetInputManager() {
        world.resetInputManager(im);
    }

    /**
     * Maakt een nieuwe animatietimer aan (In dit geval de gameloop)
     */
    private void setAnimationTimer() {
        timer = new AnimationTimer() {

            @Override
            public void handle(long now) { //now is de huidige tick in nanoseconden.
                lastFrameMS = now - lastFrame; //Hoelang de frametime is tussen het huidige frame en de laatste frame.
                fps = (int) (SECONDINNANOS / lastFrameMS); //Hoeveel frames er per seconde worden weergeven op het scherm.
                currentStage.setTitle("FPS:" + fps + " | DomumViri |");
                lastFrame = now;

                update(); //Update de gameworld en renderer.
                checkFramesAndSeconds(now);
            }

            @Override
            public void start() {
                lastFrame = System.nanoTime();
                super.start();
            }
        };
    }

    /**
     * Checkt het frame en stelt de game time in.
     */
    private void checkFramesAndSeconds(long now) {
        renderer.setDrawCallsFrame(0); //reset de drawcalls van dit frame.
        if ((lastSecond + SECONDINNANOS) <= now) { //Kijkt of er een seconde voorbij is.
            lastSecond += SECONDINNANOS; //Verhoogt de laatste seconde.
            renderer.setDrawCallsSecond(0); //Reset de drawcalls per seconde.
            gameTime = world.getTime().getRemainingTime(now);
        }
    }

    /**
     * Update de game.
     */
    public void update() {
        renderer.clear(); //Cleart het scherm van alle drawings.
        checkInput(); //Kijkt naar de huidige input.
        world.update(lastFrameMS / (float) SECONDINNANOS); //Update de huidige wereld met alle objecten die daar in zitten.
        renderer.update(world); //, 0, (height / 2) + 72

        String debugLine = " | ScaleX:" + renderer.getScaleX() + " | ScaleY: " + renderer.getScaleY() + " | tileSoorten: "
                + world.getMap().getTileSoorten().size() + " | tiles: " + world.getMap().getTileMap().size()
                + " | drawCallsSecond: " + renderer.getDrawCallsSecond() + " | drawCallsFrame: " + renderer.getDrawCallsFrame() + "| remaining Time: " + gameTime
                + " | status: " + world.getTime().getStatus().toString() + " | falling: " + world.getPlayer().isFalling(); //String die een gedeelte van de huidige gegevens over de game weergeeft.
        String posX = " | currentX:" + (world.getPlayer().getTile().getLocation().getX());
        String posY = " | currentY:" + (world.getPlayer().getTile().getLocation().getY());
        renderer.debugLine(Color.ORANGE, debugLine, 10, 10);
        renderer.debugLine(Color.CYAN, posX, 10, 23);
        renderer.debugLine(Color.CYAN, posY, 10, 36);

        if (gameTime <= 10) { //end game timer voor als er nog minder dan 10 seconden zijn.
            //(world.player().getTile().getImage().getWidth() / 4)
            if (world.getTime().getStatus() == TimeStatus.Waiting) {
                renderer.drawLine(Color.YELLOW, Integer.toString(gameTime), (width / 2), 50);
            } else if (world.getTime().getStatus() == TimeStatus.Warmup) {
                renderer.drawLine(Color.GREEN, Integer.toString(gameTime), (width / 2), 50);
            } else if (world.getTime().getStatus() == TimeStatus.Ingame) {
                renderer.drawLine(Color.ORANGERED, Integer.toString(gameTime), (width / 2), 50);
            }
        }
        if (world.getTime().getStatus() == TimeStatus.Finished || world.isStopped()) { //stopt de game.
            endGame();
            playing = false;
        }
        if (world.endGame()) {
            endGame();
        }
    }

    /**
     * Zorgt er voor dat de game stopt.
     */
    public void stop() {
        timer.stop(); //Stopt de game timer.
        //world.shutdown();
        menu.changeMenuToPaused(false);
        currentStage.show(); //Laat het menu zien.
    }

    public void forceStop() {
        if (timer != null) {
            timer.stop();
        }
        world.shutdown();
        menu.changeMenuToPaused(true);

    }

    /**
     * Eindigt het spel
     */
    public void endGame() {
        world.shutdown();
        timer.stop(); //Stopt de game timer.
        gameTime = 0;
        List<Entity> entiteiten = world.getEntities();
        Bot b = null;
        for (Entity e : entiteiten) {
            if (e instanceof Bot) {
                b = (Bot) e;
            }
        }

        playing = false;
        menu.gameOver(b, world.getPlayer()); //Laat de pauze knop zien.
        currentStage.show(); //Laat het menu zien.

    }

    /**
     * Start de game
     *
     * @param menu het menu
     * @param gameScene de gameScene
     */
    public void start(Menu menu, Scene gameScene) {
        this.menu = menu;
        setAnimationTimer();
        currentStage.setScene(gameScene);
        currentStage.setFullScreen(fullscreen);
        currentStage.show();
        timer.start();
        playing = true;
    }

    /**
     * Stelt de grootte van de game in
     *
     * @param width de breedte
     * @param height de hoogte
     */
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Checkt de input van de knop o, p en escape voor debuging en terug gaan
     * naar het menu.
     */
    public void checkInput() {
        if (im.isDebug() && !debugPressed) {
            renderer.setDebug(!renderer.isDebug());
            debugPressed = true;
        }
        if (!im.isDebug() && debugPressed) {
            debugPressed = false;
        }

        if (im.isDebugTilePosition() && !debugTilePositionPressed) {
            renderer.showPositionTilesDebug(!renderer.isDebugTilePosition());
            debugTilePositionPressed = true;
        }
        if (!im.isDebugTilePosition() && debugTilePositionPressed) {
            debugTilePositionPressed = false;
        }

        if (im.isMenu() && !escapePressed) {
            stop();
            escapePressed = true;
        }
        if (!im.isMenu() && escapePressed) {
            escapePressed = false;
        }

        if (im.isMoveUp() && !isMoveUpPressed) {
            if (!world.getPlayer().isFalling()) {
                AudioManager.playJump(menu.getDecibel() * 5);
            } else if (!world.getPlayer().hasDoublejumped()) {
                AudioManager.playJump(menu.getDecibel() * 5);
            }
            isMoveUpPressed = true;
        }
        if (!im.isMoveUp() && isMoveUpPressed) {
            isMoveUpPressed = false;
        }
    }
}
