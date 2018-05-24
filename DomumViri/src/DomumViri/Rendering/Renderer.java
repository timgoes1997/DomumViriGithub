package DomumViri.Rendering;

import DomumViri.Debugging.Debug;
import DomumViri.Tiles.Tile;
import DomumViri.Entities.Item;
import DomumViri.Entities.Bot;
import DomumViri.Entities.Character;
import DomumViri.Entities.Entity;
import DomumViri.Entities.NetworkedPlayer;
import DomumViri.Main.GameMode;
import DomumViri.User.Team;
import DomumViri.World.GameWorld;
import java.util.List;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import DomumViri.World.IGameWorld;

public class Renderer {

    private int width;
    private int height;
    private double scaleX;
    private double scaleY;

    private GraphicsContext gc;
    private String debugLine;
    private boolean debug;
    private boolean debugTilePosition;

    private int drawCallsSecond;
    private int drawCallsFrame;

    /**
     * De renderer klasse die de objecten van de game op het scherm laat
     * weergeven.
     *
     * @param gc De graphicscontext van het canvas
     * @param width De breedte van het scherm
     * @param height De hoogte van het scherm
     * @param requestedWidth De render breedte
     * @param requestedHeight De render hoogte
     */
    public Renderer(GraphicsContext gc, int width, int height, int requestedWidth, int requestedHeight) {
        this.width = width;
        this.height = height;
        this.scaleX = (double) width / (double) requestedWidth;
        this.scaleY = (double) height / (double) requestedHeight;
        this.gc = gc;
        this.debug = false;
        this.debugTilePosition = true;
        this.drawCallsSecond = 0;
        this.drawCallsFrame = 0;
    }

    /**
     * Hoeveel draw calls er per seconde zijn gemaakt.
     *
     * @return Hoeveel draw calls er in de huidige seconde zijn gemaakt.
     */
    public int getDrawCallsSecond() {
        return drawCallsSecond;
    }

    /**
     * Verkrijgt de draw calls per frame
     *
     * @return Hoeveel drawcalls er in het huidige fram hebben plaats gevonden.
     */
    public int getDrawCallsFrame() {
        return drawCallsFrame;
    }

    /**
     * Hoeveel draw calls er per seconde zijn gemaakt.
     *
     * @param drawCalls Het aantal drawCalls voor de huidige seconde.
     */
    public void setDrawCallsSecond(int drawCalls) {
        drawCallsSecond = drawCalls;
    }

    /**
     * Verkrijgt de draw calls per frame
     *
     * @param drawCalls Het aantal drawCalls voor de huidige frame.
     */
    public void setDrawCallsFrame(int drawCalls) {
        drawCallsFrame = drawCalls;
    }

    /**
     * Verkrijgt de schaal van de x-as waarop de game wordt gerendert.
     *
     * @return
     */
    public double getScaleX() {
        return scaleX;
    }

    /**
     * Verkrijgt de schaal van de y-as waarop de game wordt gerendert.
     *
     * @return
     */
    public double getScaleY() {
        return scaleY;
    }

    /**
     * Kijkt of het debugen voor de tilePositie aan staat
     *
     * @return of het debugen voor de tile positie aan staat.
     */
    public boolean isDebugTilePosition() {
        return debugTilePosition;
    }

    /**
     * Kijkt of debug mode aan staat.
     *
     * @return of er gedebugt wordt of niet
     */
    public boolean isDebug() {
        return debug;
    }

    /**
     * Zet de huidige hoofd debugline
     *
     * @param debugLine Zet de hoofd debugline
     */
    public void setDebugLine(String debugLine) {
        this.debugLine = debugLine;
    }

    /**
     * Zet debugmode mode aan of uit.
     *
     * @param debug Of debug mode aan of uit moet.
     */
    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    /**
     * Laat de tileposities in debugmode zien
     *
     * @param showTilePosition Of de tilePosities moeten worden weergeven of
     * niet.
     */
    public void showPositionTilesDebug(boolean showTilePosition) {
        this.debugTilePosition = showTilePosition;
    }

    /**
     *
     * @param gc De graphicscontext van het canvas
     * @param image De afbeelding die weergeven moet worden.
     * @param angle De rotatie waarop die afbeelding moet worden weergeven.
     * @param posX De x-positie waarop die afbeeling moet worden weergeven.
     * @param posY De y-positie waarop die afbeeling moet worden weergeven.
     * @param width De breedte van de afbeelding
     * @param height De hoogte van de afbeelding
     */
    public void drawImage(GraphicsContext gc, Image image, double angle, double posX, double posY, int width, int height) {
        gc.save(); // saves the current state on stack, including the current transform

        double imageXposition = posX + ((image.getWidth() * scaleX) / 2); //Bepaald het midden van een image zijn x positie.
        double imageYposition = posY + ((image.getHeight() * scaleY) / 2); //Bepaald het midden van een image zijn y positie.

        rotate(gc, angle, imageXposition, imageYposition); //Rotateerd het object op basis van het gegeven graden.
        gc.drawImage(image, posX, posY, width * scaleX, height * scaleY); //Drawt de image op het scherm.
        drawCallsSecond++; //voegt een drawcall toe
        drawCallsFrame++; //voegt een drawcall toe
        gc.restore(); // back to original state (before rotation)

        if (debugTilePosition) { //wanneer de bull debug voor tilepositionering aan staat worden er cirkels aan de linkerbovenkant en het midden getekent.
            debugCirlce(Color.RED, imageXposition, imageYposition);
            debugCirlce(Color.GREEN, posX, posY);
        }
    }

    /**
     * Rendert een image vanuit een specifiek pad
     *
     * @param gc De graphicscontext van het canvas
     * @param imagePath Locatie van het plaatje.
     * @param angle De rotatie waarop die afbeelding moet worden weergeven.
     * @param posX De x-positie waarop die afbeeling moet worden weergeven.
     * @param posY De y-positie waarop die afbeeling moet worden weergeven.
     * @param width De breedte van de afbeelding
     * @param height De hoogte van de afbeelding
     */
    public void drawImage(GraphicsContext gc, String imagePath, double angle, int posX, int posY, int width, int height) {
        gc.save(); // saves the current state on stack, including the current transform

        Image newImage = new Image(imagePath, (double) width, (double) height, false, false);

        double imageXposition = posX + newImage.getWidth() / 2;
        double imageYposition = posY + newImage.getHeight() / 2;

        rotate(gc, angle, imageXposition, imageYposition);
        gc.scale(scaleX, scaleY);
        gc.drawImage(newImage, posX, posY, width, height);
        drawCallsSecond++;
        drawCallsFrame++;
        gc.restore(); // back to original state (before rotation)

        debugLine(Color.YELLOW, debugLine, 10, 10);
        debugCirlce(Color.RED, imageXposition, imageYposition);
        debugCirlce(Color.GREEN, posX, posY);
    }

    /**
     * Roteerd de meegegeven image.
     *
     * @param gc De graphicscontext van het canvas.
     * @param angle De angle waarop het object moet worden getekent.
     * @param px De x coord van het object.
     * @param py De y coord van het object.
     */
    public void rotate(GraphicsContext gc, double angle, double px, double py) {
        Rotate r = new Rotate(angle, px, py);
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy()); //Zet het transform van de image in de gegeven rotaties.
    }

    /**
     * Tekent een debug cirkel op het scherm
     *
     * @param c De kleur van de debug cirkel
     * @param x X coord waar de lijn moet worden gedrawt
     * @param y Y coord waar de lijn moet worden gedrawt
     */
    public void debugCirlce(Color c, double x, double y) {
        if (debug) {
            gc.save();
            gc.setFill(c);
            gc.fillOval(x, y, 10, 10);
            drawCallsSecond++;
            drawCallsFrame++;
            gc.restore();
        }
    }

    /**
     * Tekent een debug lijn op het scherm
     *
     * @param c Kleur van de lijn
     * @param s De string van de lijn
     * @param x X coord waar de lijn moet worden gedrawt
     * @param y Y coord waar de lijn moet worden gedrawt
     */
    public void debugLine(Color c, String s, double x, double y) {
        if (debug) {
            gc.save();
            gc.setFill(c);
            gc.fillText("Debug line:" + s, x, y);
            gc.restore();
        }
    }

    /**
     * Tekent een lijn
     *
     * @param c Kleur van de lijn
     * @param s String die in de lijn moet komen te staan
     * @param x X coord waar de lijn moet worden gedrawt
     * @param y Y coord waar de lijn moet worden gedrawt
     * @param scaleX schaal van de lijn
     * @param scaleY schaal van de lijn
     */
    public void drawLine(Color c, String s, double x, double y) {
        gc.save();
        gc.setFill(c);
        gc.fillText(s, x, y);
        gc.restore();
    }

    /**
     * Rendert de gamewereld.
     *
     * @param world De gamewereld die gerendert moet worden
     */
    public void update(IGameWorld iWorld) {
        GameWorld world = (GameWorld) iWorld;
        clear(); //leegt het huidig canvas.
        gc.save(); //slaat de huidige staat van het canvas op.
        Tile playerTile = world.getPlayer().getTile();
        int midX = (width / 2) - (int) (world.getMap().getEntityImage(playerTile.getImageID()).getWidth() * scaleX / 2); //Bepaald het midden van het scherm (x-coord van de speler dus)
        int midY = (height / 2) - (int) (world.getMap().getEntityImage(playerTile.getImageID()).getHeight() * scaleY / 2); //Bepaald het midden van het scherm (y-coord van de speler dus)

        drawWorldTiles(world, playerTile, midX, midY);
        drawWorldEntities(world, playerTile, midX, midY);
        drawDebug();
        hud(world);

        if (world.getMode() == GameMode.MPTeams) {
            if (world.getPlayer().getAccount().getTeam() == Team.Red) {
                Color c = Color.rgb(255, 0, 0, 0.3);
                System.out.println(world.getPlayer().getAccount().getTeam());
                gc.setFill(c);
            } else {
                Color c = Color.rgb(0, 0, 255, 0.3);
                System.out.println(world.getPlayer().getAccount().getTeam());
                gc.setFill(c);
            }
            int outline = 5;
            gc.fillOval(midX - outline, midY - outline, (world.getPlayer().getTile().getWidth() * scaleX) + (outline * 2), (world.getPlayer().getTile().getHeight() * scaleY) + (outline * 2));
        } else if (world.getMode() == GameMode.MPFFA) {
            Color c = Color.rgb(0, 255, 0, 0.3);
            gc.setFill(c);
            int outline = 5;
            gc.fillOval(midX - outline, midY - outline, (world.getPlayer().getTile().getWidth() * scaleX) + (outline * 2), (world.getPlayer().getTile().getHeight() * scaleY) + (outline * 2));
        }

        //(playerTile.rect.xProperty().doubleValue() - playerTile.width) tekent het in de null hoek
        if (world.getPlayer().isFacingLeft()) {
            gc.drawImage(world.getMap().getEntityImage(playerTile.getImageID()), midX, midY, //indien de player over de tilemap moet lopen verwijder dan -playerTile.rect.getX() boven en voeg die aan midX toe zelfde geld voor midY
                    world.getMap().getEntityImage(playerTile.getImageID()).getWidth() * scaleX, world.getMap().getEntityImage(playerTile.getImageID()).getHeight() * scaleY); //Tekent de speler.
        } else {
            gc.drawImage(world.getMap().getEntityImage(playerTile.getImageID()), midX + (playerTile.getWidth() * scaleX), midY, //indien de player over de tilemap moet lopen verwijder dan -playerTile.rect.getX() boven en voeg die aan midX toe zelfde geld voor midY
                    world.getMap().getEntityImage(playerTile.getImageID()).getWidth() * -scaleX, world.getMap().getEntityImage(playerTile.getImageID()).getHeight() * scaleY); //Tekent de speler.
        }
        gc.restore(); //restored de staat van het canvas.
    }

    /**
     * Tekent alle tiles uit de map op het scherm.
     *
     * @param world De wereld waarin de tiles zich bevinden
     * @param playerTile De speler zijn tile
     * @param midX Het midden van het scherm zijn x-axis waar de speler later
     * wordt getekent.
     * @param midY Het midden van het scherm zijn y-axis waar de speler later
     * wordt getekent.
     */
    private void drawWorldTiles(GameWorld world, Tile playerTile, int midX, int midY) {
        for (Tile t : world.getMap().getTileMap()) {
            int h = (world.getMap().getHeight() - t.getY()) + 1; //De hoogte van de map en bepaald de positie van de tile (Java drawt normaal andersom dus word dit ook andersom berekent om het er normaal te laten uitzien.)
            double x = ((t.getX() * t.getWidth() * scaleX) - (playerTile.getLocation().getX() * scaleX) + midX) - (t.getWidth() * scaleX); //De x-positie van de tile
            int margin = (int) ((t.getHeight() * scaleY) / 4); //margin fout die optreed door het berekening van de scaling
            double y = ((h * t.getHeight() * scaleY) + (playerTile.getLocation().getY()) * scaleY) - midY - (t.getHeight() * scaleY * 2) + margin; //y-positie van de tile.

            if (x >= (0 - (t.getWidth() * scaleX)) && y >= ((0 - t.getHeight()) * scaleY) && x <= width && y <= height) { //Kijkt of de tile zich binnen het scherm bevind.
                drawImage(gc, world.getMap().getTileImage(t.getImageID()), 0, x, y, t.getWidth(), t.getHeight()); //tekent de tile.
            }
        }
    }

    /**
     * Tekent alle entities uit de wereld op het scherm.
     *
     * @param world De wereld waarin de entities zich bevinden
     * @param playerTile De speler zijn tile
     * @param midX Het midden van het scherm zijn x-axis waar de speler later
     * wordt getekent.
     * @param midY Het midden van het scherm zijn y-axis waar de speler later
     * wordt getekent.
     */
    private void drawWorldEntities(GameWorld world, Tile playerTile, int midX, int midY) {
        for (Entity e : world.getEntities()) {
            if (e instanceof Bot && world.getMode() == GameMode.SinglePlayer) {
                drawWorldBot(world, (Bot) e, playerTile, midX, midY);
            }
            if (e instanceof Item) {
                drawWorldItem(world, (Item) e, playerTile, midX, midY);
            }
            if (e instanceof Character) {
                drawCharacterAnimation(world, (Character) e, playerTile, midX, midY);
            }
            if (e instanceof NetworkedPlayer) {
                drawNetworkedPlayers(world, (NetworkedPlayer) e, playerTile, midX, midY);
            }
        }
    }

    /**
     * Tekent een bot op het scherm
     *
     * @param b
     * @param world De wereld waarin de entities zich bevinden
     * @param playerTile De speler zijn tile
     * @param midX Het midden van het scherm zijn x-axis waar de speler later
     * wordt getekent.
     * @param midY Het midden van het scherm zijn y-axis waar de speler later
     * wordt getekent.
     */
    private void drawNetworkedPlayers(GameWorld world, NetworkedPlayer player, Tile playerTile, int midX, int midY) {
        if (player.getAccount().getUserName() != world.getPlayer().getNaam()) {
            double x = ((player.getTile().getLocation().getX() - playerTile.getLocation().getX()) * scaleX) + midX;
            double y = height - ((player.getTile().getLocation().getY() - playerTile.getLocation().getY() + player.getTile().getHeight()) * scaleY) - midY;

            if (world.getMode() == GameMode.MPTeams) {
                if (player.getAccount().getTeam() == Team.Red) {
                    Color c = Color.rgb(255, 0, 0, 0.3);
                    gc.setFill(c);
                } else {
                    Color c = Color.rgb(0, 0, 255, 0.3);
                    gc.setFill(c);
                }
                int outline = 5;
                gc.fillOval(x - outline, y - outline, (player.getTile().getWidth() * scaleX) + (outline * 2), (player.getTile().getHeight() * scaleY) + (outline * 2));
            }

            if (player.isFacingLeft()) {
                String debugLn = "| " + " X: " + player.getTile().getLocation().getX() + " | Y:" + player.getTile().getLocation().getY() + " | draw X:" + x + " | draw Y:" + y;
                debugLine(Color.RED, debugLn, 10, 48);
                if (x >= (0 - (player.getTile().getWidth() * scaleX)) && y >= ((0 - player.getTile().getHeight()) * scaleY) && x <= width && y <= height) { //Kijkt of de tile zich binnen het scherm bevind.
                    gc.drawImage(world.getMap().getEntityImage(playerTile.getImageID()), x, y, //indien de player over de tilemap moet lopen verwijder dan -playerTile.rect.getX() boven en voeg die aan midX toe zelfde geld voor midY
                            world.getMap().getEntityImage(playerTile.getImageID()).getWidth() * scaleX, world.getMap().getEntityImage(playerTile.getImageID()).getHeight() * scaleY); //Tekent de speler.               
                }
            } else {
                String debugLn = "| " + " X: " + player.getTile().getLocation().getX() + " | Y:" + player.getTile().getLocation().getY() + " | draw X:" + x + " | draw Y:" + y;
                debugLine(Color.RED, debugLn, 10, 48);
                if (x >= (0 - (player.getTile().getWidth() * scaleX)) && y >= ((0 - player.getTile().getHeight()) * scaleY) && x <= width && y <= height) { //Kijkt of de tile zich binnen het scherm bevind.
                    //drawImage(gc, world.getMap().getEntityImage(player.getTile().getImageID()), 0, x, y, player.getTile().getWidth(), player.getTile().getHeight()); //tekent de tile.
                    gc.drawImage(world.getMap().getEntityImage(playerTile.getImageID()), x + (playerTile.getWidth() * scaleX), y, //indien de player over de tilemap moet lopen verwijder dan -playerTile.rect.getX() boven en voeg die aan midX toe zelfde geld voor midY
                            world.getMap().getEntityImage(playerTile.getImageID()).getWidth() * -scaleX, world.getMap().getEntityImage(playerTile.getImageID()).getHeight() * scaleY); //Tekent de speler.                    
                }
            }
        }
    }

    /**
     * Tekent een bot op het scherm
     *
     * @param b
     * @param world De wereld waarin de entities zich bevinden
     * @param playerTile De speler zijn tile
     * @param midX Het midden van het scherm zijn x-axis waar de speler later
     * wordt getekent.
     * @param midY Het midden van het scherm zijn y-axis waar de speler later
     * wordt getekent.
     */
    private void drawWorldBot(GameWorld world, Bot b, Tile playerTile, int midX, int midY) {
        double x = ((b.getTile().getLocation().getX() - playerTile.getLocation().getX()) * scaleX) + midX;
        double y = height - ((b.getTile().getLocation().getY() - playerTile.getLocation().getY() + b.getTile().getHeight()) * scaleY) - midY;
        String debugLn = "| " + " X: " + b.getTile().getLocation().getX() + " | Y:" + b.getTile().getLocation().getY() + " | draw X:" + x + " | draw Y:" + y;
        debugLine(Color.RED, debugLn, 10, 48);
        if (x >= (0 - (b.getTile().getWidth() * scaleX)) && y >= ((0 - b.getTile().getHeight()) * scaleY) && x <= width && y <= height) { //Kijkt of de tile zich binnen het scherm bevind.
            drawImage(gc, world.getMap().getEntityImage(b.getTile().getImageID()), 0, x, y, b.getTile().getWidth(), b.getTile().getHeight()); //tekent de tile.
        }
    }

    /**
     * Tekent een item op het scherm
     *
     * @param i
     * @param world De wereld waarin de entities zich bevinden
     * @param playerTile De speler zijn tile
     * @param midX Het midden van het scherm zijn x-axis waar de speler later
     * wordt getekent.
     * @param midY Het midden van het scherm zijn y-axis waar de speler later
     * wordt getekent.
     */
    private void drawWorldItem(GameWorld world, Item i, Tile playerTile, int midX, int midY) {
        double x = ((i.getTile().getLocation().getX() - playerTile.getLocation().getX()) * scaleX) + midX;
        double y = height - ((i.getTile().getLocation().getY() - playerTile.getLocation().getY() + i.getTile().getHeight()) * scaleY) - midY;
        if (x >= (0 - (i.getTile().getWidth() * scaleX)) && y >= ((0 - i.getTile().getHeight()) * scaleY) && x <= width && y <= height) { //Kijkt of de tile zich binnen het scherm bevind.
            drawImage(gc, world.getMap().getEntityImage(i.getTile().getImageID()), 0, x, y, i.getTile().getWidth(), i.getTile().getHeight()); //tekent de tile.
        }
    }

    /**
     * Tekent een animatie van een character op het scherm
     *
     * @param c Het character waarvan de animatie moet worden getekent.
     * @param world De wereld waarin de entities zich bevinden
     * @param playerTile De speler zijn tile
     * @param midX Het midden van het scherm zijn x-axis waar de speler later
     * wordt getekent.
     * @param midY Het midden van het scherm zijn y-axis waar de speler later
     * wordt getekent.
     */
    private void drawCharacterAnimation(GameWorld world, Character c, Tile playerTile, int midX, int midY) {
        if (c.isAttackingLeft()) {
            double x = ((c.getTile().getLocation().getX() - playerTile.getLocation().getX()) * scaleX) + midX;
            double y = height - ((c.getTile().getLocation().getY() - playerTile.getLocation().getY() + c.getTile().getHeight()) * scaleY) - midY;
        } else if (c.isAttackingRight()) {
            double x = ((c.getTile().getLocation().getX() - playerTile.getLocation().getX()) * scaleX) + midX;
            double y = height - ((c.getTile().getLocation().getY() - playerTile.getLocation().getY() + c.getTile().getHeight()) * scaleY) - midY;
        }
    }

    /**
     * Tekent alle debug messages van de debug klasse op het scherm.
     */
    private void drawDebug() {
        if (debug && Debug.getLog() != null) {
            int start = (height / 3) * 2;
            for (String s : Debug.getLog()) {
                debugLine(Debug.color, s, 10, start);
                start += 15;
            }
        }
    }

    /**
     * Tekent de hud van de wereld op het scherm.
     *
     * @param world De wereld waarvan de hud zijn informatie moet laten
     * weergeven
     */
    private void hud(IGameWorld iWorld) {
        GameWorld world = (GameWorld) iWorld;
        drawPlayerInfo(world);
        drawPlayerItem(world);
        if (world.getMode() == GameMode.SinglePlayer) {
            drawBotInfo(world);
        }
    }

    /**
     * Drawt de info van de speler.
     *
     * @param world De wereld waarvan de hud zijn informatie moet laten
     * weergeven
     */
    private void drawPlayerInfo(GameWorld world) {
        int hudX = 40;
        int hudY = 50;
        int hudWidth = 100;
        int hudHeight = 110;
        gc.save();
        gc.setFill(Color.BLACK.deriveColor(Color.BLACK.getHue(), Color.BLACK.getBrightness(), Color.BLACK.getBrightness(), 0.3));
        gc.fillRect((double) hudX, (double) hudY, (double) hudWidth, (double) hudHeight);
        int substringLength = world.getPlayer().getNaam().length();
        if (substringLength > 16) {
            substringLength = 16;
        }
        drawLine(Color.YELLOW, world.getPlayer().getNaam().substring(0, substringLength), (double) (hudX + 10), (double) (hudY + 15));
        drawLine(Color.YELLOW, "Health: " + world.getPlayer().getHealth(), (double) (hudX + 10), (double) (hudY + 30));
        drawLine(Color.YELLOW, "Armor: " + world.getPlayer().getArmor(), (double) (hudX + 10), (double) (hudY + 45));
        drawLine(Color.YELLOW, "Speed: " + world.getPlayer().getMovementSpeedModifier(), (double) (hudX + 10), (double) (hudY + 60));
        drawLine(Color.YELLOW, "Damage: " + world.getPlayer().getDamageModifier(), (double) (hudX + 10), (double) (hudY + 75));
        drawLine(Color.YELLOW, "Kills: " + world.getPlayer().getKills(), (double) (hudX + 10), (double) (hudY + 90));
        drawLine(Color.YELLOW, "Deaths: " + world.getPlayer().getDeaths(), (double) (hudX + 10), (double) (hudY + 105));
        gc.restore();
    }

    /**
     * Drawt de info van de bot in de wereld.
     *
     * @param world De wereld waarvan de hud zijn informatie moet laten
     * weergeven
     */
    private void drawBotInfo(GameWorld world) {
        int hudX = 150;
        int hudY = 50;
        int hudWidth = 100;
        int hudHeight = 50;
        gc.save();
        gc.setFill(Color.BLACK.deriveColor(Color.BLACK.getHue(), Color.BLACK.getBrightness(), Color.BLACK.getBrightness(), 0.3));
        gc.fillRect(hudX, hudY, hudWidth, hudHeight);
        List<Entity> entiteiten = world.getEntities();
        for (Entity e : entiteiten) {
            if (e instanceof Bot) {
                drawLine(Color.YELLOW, "Bot: " + ((Bot) e).getHealth() + "%", (double) (hudX + 10), (double) (hudY + 15));
                drawLine(Color.YELLOW, "Kills: " + ((Bot) e).getKills(), (double) (hudX + 10), (double) (hudY + 30));
                drawLine(Color.YELLOW, "Deaths: " + ((Bot) e).getDeaths(), (double) (hudX + 10), (double) (hudY + 45));
            }
        }
        gc.restore();
    }

    /**
     * Drawt het player item op het scherm.
     *
     * @param world De wereld waarvan de hud zijn informatie moet laten
     * weergeven
     */
    private void drawPlayerItem(GameWorld world) {
        if (world.getPlayer().getItem() != null) {
            int hudWidth = 70;
            int hudHeight = 50;
            int hudX = width - hudWidth - 50;
            int hudY = 50;
            gc.save();
            gc.setFill(Color.BLACK.deriveColor(Color.BLACK.getHue(), Color.BLACK.getBrightness(), Color.BLACK.getBrightness(), 0.3));
            gc.fillRect(hudX, hudY, hudWidth, hudHeight);
            drawImage(gc, world.getMap().getEntityImage(world.getPlayer().getItem().getTile().getImageID()), 0, hudX + world.getPlayer().getItem().getTile().getWidth() - (int) (50 / scaleX), hudY, (int) (50 / scaleX), (int) (50 / scaleY));
            if (world.getPlayer().isUsingItem()) {
                gc.setFill(Color.CYAN);
                int size = 10;
                gc.fillOval(hudX + hudWidth - size, hudY + (hudHeight / 2) - (size / 2), size * scaleX, size * scaleY);
            }
            gc.restore();
        } else {
            int hudWidth = 70;
            int hudHeight = 50;
            int hudX = width - hudWidth - 50;
            int hudY = 50;
            gc.save();
            gc.setFill(Color.BLACK.deriveColor(Color.BLACK.getHue(), Color.BLACK.getBrightness(), Color.BLACK.getBrightness(), 0.3));
            gc.fillRect(hudX, hudY, hudWidth, hudHeight);
            gc.restore();
        }
    }

    /**
     * Leegt het scherm.
     */
    public void clear() {
        gc.clearRect(0.0, 0.0, width, height);
        gc.setFill(Color.BLACK);
        drawCallsSecond++;
        drawCallsFrame++;
        gc.fillRect(0.0, 0.0, width, height);
    }
}
