package DomumViri.Level;

import DomumViri.Level.SpawnPoint;
import DomumViri.Tiles.Tile;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Map {

    private MapInfo infoPath;
    private static final String USERDIR = System.getProperty("user.dir");

    private List<Tile> tileSoorten;
    private List<Tile> tileMap;
    private List<SpawnPoint> spawnPoints;

    private List<Image> tileImages;
    private List<Image> entityImages;

    private int mapHeight;
    private int mapWidth;
    private int tileHeight;
    private int tileWidth;

    /**
     * Maakt een map aan.
     *
     * @param info De lebel info.
     * @param level De maplayout.
     */
    public Map(MapName level, MapInfo info) {
        this.infoPath = info;
        this.mapHeight = 12;
        this.mapWidth = 20;
        tileHeight = 64;
        tileWidth = 64;
        loadMapInfo(info);
        loadMap(level);
    }
    
        /**
     * Maakt een map aan.
     *
     * @param info De lebel info.
     * @param level De maplayout.
     */
    public Map(MapInfo info) {
        this.infoPath = info;
        this.mapHeight = 12;
        this.mapWidth = 20;
        tileHeight = 64;
        tileWidth = 64;
        loadMapInfo(info);
    }

    /**
     * Verkrijgt een lijst aan willekeurige spawnpoints van het type player.
     *
     * @return
     */
    public List<SpawnPoint> getPlayerSpawnPoints() {
        List<SpawnPoint> player = new ArrayList<>();
        for (SpawnPoint s : spawnPoints) {
            if (s.getType() == SpawnPointType.Player) { //Controleert of het spanwpoint van het type player is.
                player.add(s);
            }
        }
        return player;
    }

    /**
     * Verkrijgt een lijst met willekeurige item spawnpoints.
     *
     * @return
     */
    public List<SpawnPoint> getItemSpawnPoints() {
        List<SpawnPoint> spawnPointsItems = new ArrayList<>();
        for (SpawnPoint s : spawnPoints) {
            if (s.getType() == SpawnPointType.Item) { //Controleert of het spanwpoint van het type item is.
                spawnPointsItems.add(s);
            }
        }
        return spawnPointsItems;
    }

    /**
     * Het breedte van dit level Als je dit omdraait werkt t niet meer
     *
     * @return
     */
    public int getWidth() {
        return mapWidth;
    }

    /**
     * De hoogte van dit level Als je dit omdraait werkt t niet meer
     *
     * @return
     */
    public int getHeight() {
        return mapHeight;
    }

    /**
     * Retourneert alle soorten tiles die deze map heeft.
     *
     * @return
     */
    public List<Tile> getTileSoorten() {
        return tileSoorten;
    }

    /**
     * Verkrijgt de tileMap.
     *
     * @return
     */
    public List<Tile> getTileMap() {
        return tileMap;
    }

    /**
     * Verkrijgt een soort tile op basis van ID.
     *
     * @param id
     * @return
     */
    public Tile getTileSoortenTile(int id) {
        for (Tile t : tileSoorten) {
            if (id == t.getId()) {
                return t;
            }
        }
        return null;
    }

    /**
     * Laad de map in vanuit een tekstbestand.
     *
     * @param name Het pad van het tekstbestand waaruit het level moet worden
     * geladen.
     * @return Of de map is ingeladen of niet.
     */
    public boolean loadMap(MapName name) {
        try {
            if (tileSoorten == null && !loadMapInfo(infoPath)) { //Kijkt of de tileSoorten niet null zijn.
                throw new NullPointerException(); //Geeft een exception af aangezien er geen tiles zijn gevonden.
            }

            tileMap = new ArrayList<>();
            String levelPath = USERDIR + name.toString(); //Het pad naar het tekstbestand met de levellayout.
            FileReader fr = new FileReader(levelPath); //We maken gebruik van de filereader om het tekstbestand uit te lezen.
            BufferedReader br = new BufferedReader(fr); //We maken een bufferedReader aan die het bestand uit de filereader pakt.

            List<String> lines = new ArrayList<>(); //Een lijst met het aantal lijnen uit het tekstbestand.
            String readedLine; //De huidige lijn.
            while ((readedLine = br.readLine()) != null) { //Leest het bestand uit tot dat er geen lijnen meer zijn.
                System.out.println(readedLine); //Print de huidige lijn.
                lines.add(readedLine); //Voegt de huidige lijn toe aan de lijst met lijnen.
            }

            int x = 0; //Huidige x coord van de huidige gelezen tile.
            int y = 0; //Huidige
            for (int i = lines.size(); i > 0; i--) { //Leest de lijst met de lijnen van het tekstbestand uit.
                String t = lines.get(i - 1); //Pakt de huidige lijn uit het tekstbestand.
                if (name.toString().indexOf(".csv") > 0) {
                    for (String s : t.split(",")) { //Split de huidige lijn met spaties in meerdere strings met de getallen die daar tussen zitten. 
                        int tileID = Integer.parseInt(s); //Probeert de huidige substring te parsen naar een int.
                        Tile tileSoort = getTileSoortenTile(tileID); //Haalt een tile op met het gegeven id.
                        //int id, String naam, Image image, int x, int y, int width, int height, boolean collidable
                        Tile tile = new Tile(tileSoort.getId(), tileSoort.getNaam(), tileSoort.getImageID(), x, y, tileSoort.getWidth(), tileSoort.getHeight(), tileSoort.isCollidable(), tileSoort.isDeath()); //Maakt een nieuwe tile aan.
                        tileMap.add(tile); //Voegt de tile toe aan de tilemap.
                        x++; //Verhoogt de huidige x coord.
                    }
                } else {
                    for (String s : t.split("\\s")) { //Split de huidige lijn met spaties in meerdere strings met de getallen die daar tussen zitten. 
                        int tileID = Integer.parseInt(s); //Probeert de huidige substring te parsen naar een int.
                        Tile tileSoort = getTileSoortenTile(tileID); //Haalt een tile op met het gegeven id.
                        //int id, String naam, Image image, int x, int y, int width, int height, boolean collidable
                        Tile tile = new Tile(tileSoort.getId(), tileSoort.getNaam(), tileSoort.getImageID(), x, y, tileSoort.getWidth(), tileSoort.getHeight(), tileSoort.isCollidable(), tileSoort.isDeath()); //Maakt een nieuwe tile aan.
                        tileMap.add(tile); //Voegt de tile toe aan de tilemap.
                        x++; //Verhoogt de huidige x coord.
                    }
                }
                y++; //Verhoogt de y cooord.
                x = 0;
            }
            fr.close();
            br.close();
            return true;
        } catch (IOException ex) {
            Logger.getLogger(Map.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     * Deze methode kijkt naar collision in de map.
     *
     * @param x De x coord in het level
     * @param y De y coord in het level
     * @return Of die collision heeft of niet.
     */
    public boolean hasCollision(int x, int y) {

        int tileX = x / tileWidth;
        int tileY = y / tileHeight;

        if ((collisionX(tileX) && collisionY(tileY)) && tileMap.get(tileX * tileY).isCollidable()) {
            return true;
        }
        return false;
    }

    /**
     * Kijkt voor collision op de x-axis in de map
     *
     * @return Of het binnen de x-waarde valt.
     */
    private boolean collisionX(int tileX) {
        return tileX > 0 && tileX < mapWidth;
    }

    /**
     * Kijkt voor collision op de y-axis in de map
     *
     * @return Of het binnen de y-waarde valt.
     */
    private boolean collisionY(int tileY) {
        return tileY > 0 && tileY < mapHeight;
    }

    /**
     * Laad de map info in. (Spawnpoints en tiles)
     *
     * @param info Het pad waar de mapinfo is opgeslagen
     * @return Of het inladen is gelukt of niet
     */
    public boolean loadMapInfo(MapInfo info) { //Laad alle info van de map in (Spawnpoints, items en tilesoorten.)
        tileSoorten = new ArrayList<>();
        spawnPoints = new ArrayList<>();
        try {
            String xmlUrl = USERDIR + info.toString(); //Pad naar het xml bestand/
            File inputFile = new File(xmlUrl); //De file van het xml bestand.
            DocumentBuilderFactory dbFactory
                    = DocumentBuilderFactory.newInstance(); //De documentbuilder library waarmee wij het xml bestand gaan uitlezen.
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            System.out.println("Root element :"
                    + doc.getDocumentElement().getNodeName());

            NodeList nList = doc.getElementsByTagName("tileInfo"); //Verkrijgt een nodelijst van alle xmlElementen met de naam tileInfo.
            System.out.println("----------------------------");
            for (int i = 0; i < nList.getLength(); i++) { //Doorloopt de nodelist met alle tilesoorten.
                Node nNode = nList.item(i);
                System.out.println("\nCurrent Element :" + nNode.getNodeName());
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element e = (Element) nNode;
                    int id = Integer.parseInt(e.getAttribute("id")); //Parsed het xmlelement id naar een int.
                    System.out.println(id);
                    String name = getElementString(e, "name"); //Verkrijgt de string van het xmlelement naam.
                    System.out.println(name);
                    String url = USERDIR + getElementString(e, "image");
                    System.out.println(url);
                    Image image = getImage(url);
                    int tileID = setTileImageID(image);
                    System.out.println(image.getWidth());
                    String collidable = getElementString(e, "collidable");
                    int w = Integer.parseInt(getElementString(e, "width"));
                    System.out.println(w);
                    int h = Integer.parseInt(getElementString(e, "height"));
                    System.out.println(h);
                    String death = getElementString(e, "death");
                    System.out.println(death);
                    String equalsIgnoreTrue = "true";
                    boolean col = collidable.equalsIgnoreCase(equalsIgnoreTrue) ? true : false; //Kijkt of de string collidable gelijk is aan true en maakt daar een bool van.
                    boolean dea = death.equalsIgnoreCase(equalsIgnoreTrue) ? true : false;
                    System.out.println(col);
                    tileSoorten.add(new Tile(id, name, tileID, w, h, col, dea)); //Voegt de tilesoort toe aan een lijst met tilesoorten.
                }
            }

            NodeList spawns = doc.getElementsByTagName("spawnpoint");
            System.out.println("----------------------------");
            for (int i = 0; i < spawns.getLength(); i++) { //Verkrijgt alle spawnpoints uit de nodelist met spawnpoint elementen.
                Node nNode = spawns.item(i);
                System.out.println("\nCurrent Element :" + nNode.getNodeName());
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element e = (Element) nNode;
                    int sp = Integer.parseInt(getElementString(e, "type"));
                    SpawnPointType spt = SpawnPointType.fromInteger(sp);
                    System.out.println(spt.toString());
                    int x = Integer.parseInt(getElementString(e, "x"));
                    System.out.println(x);
                    int y = Integer.parseInt(getElementString(e, "y"));
                    System.out.println(y);

                    SpawnPoint s = new SpawnPoint(x - 1, y - 1, spt);
                    spawnPoints.add(s);
                }
            }

            NodeList entityImages = doc.getElementsByTagName("entity");
            System.out.println("----------------------------");
            for (int i = 0; i < entityImages.getLength(); i++) {
                Node nNode = entityImages.item(i);
                System.out.println("\nCurrent Element :" + nNode.getNodeName());
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element e = (Element) nNode;
                    String eImagePath = getElementString(e, "image");
                    File f = new File(USERDIR + eImagePath); //De imageFile
                    Image image = new Image(f.toURI().toString()); //Het ophalen van de image.
                    addEntityImage(image);
                }
            }

            return true;
        } catch (ParserConfigurationException | SAXException | IOException | DOMException e) {
            Logger.getLogger(Map.class.getName()).log(Level.SEVERE, null, e);
        }
        return false;
    }

    /**
     * Returns a image from a list
     *
     * @param id the ID starting from 1
     * @return The image of a entity
     */
    public Image getEntityImage(int id) {
        return entityImages.get(id - 1);
    }

    /**
     * Returns a image from a list
     *
     * @param id the ID starting from 1
     * @return The image of a tile
     */
    public Image getTileImage(int id) {
        return tileImages.get(id - 1);
    }

    /**
     * Set the tiles image and retreives the ID;
     *
     * @param image The image you wan't to add.
     * @return The ID of the image.
     */
    public int setTileImageID(Image image) {
        if (tileImages != null) {
            tileImages.add(image);
            return tileImages.size();
        } else {
            tileImages = new ArrayList<Image>();
            tileImages.add(image);
            return tileImages.size();
        }
    }

    /**
     * adds a new entity image
     *
     * @param image the image you want to add to the entity imagelist
     */
    public void addEntityImage(Image image) {
        if (entityImages != null) {
            entityImages.add(image);
        } else {
            entityImages = new ArrayList<Image>();
            entityImages.add(image);
        }
    }

    /**
     * Verkrijgt een image vanuit een gegeven url
     *
     * @param url Pad van het imagebestand dat je wilt verkrijgen.
     * @return De gevonden image.
     */
    private Image getImage(String url) {
        File f = new File(url);
        return new Image(f.toURI().toString());
    }

    /**
     * afkorting voor het verkrijgen van een elementenString;
     *
     * @param e Het xml element
     * @param tag De tag van het xml element
     * @return De String die in het xml element is gevonden.
     */
    private String getElementString(Element e, String tag) {
        return e.getElementsByTagName(tag).item(0).getTextContent();
    }
}
