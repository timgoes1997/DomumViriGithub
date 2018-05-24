/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testgame;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

/**
 *
 * @author Tim
 */
public class Level {

    private List<Tile> tileSoorten;
    private List<Tile> tileMap;
    private int levelHeight;
    private int levelWidth;

    private int tileHeight;
    private int tileWidth;

    public int getLevelHeight() {
        return levelHeight;
    }

    public int getLevelWidth() {
        return levelWidth;
    }

    public String path;
    private static final String userDir = System.getProperty("user.dir");

    public Level(String path, int levelHeight, int levelWidth) {
        this.path = path;
        this.levelHeight = levelHeight;
        this.levelWidth = levelWidth;
        tileHeight = 64;
        tileWidth = 64;
        loadTileInfo(path);
        loadLevel(path);
    }

    public List<Tile> getTileSoorten() {
        return tileSoorten;
    }

    public List<Tile> getTileMap() {
        return tileMap;
    }

    public boolean hasCollision(int x, int y) {

        int tileX = x / tileWidth;
        int tileY = y / tileHeight;

        if (tileX >= 0 && tileY >= 0 && tileX < levelWidth && tileY < levelHeight) {
            if (tileMap.get((tileX * tileY)).collidable) {
                return true;
            } 
        }
        return false;
    }

    public void loadTileInfo(String path) {
        tileSoorten = new ArrayList<Tile>();

        try {
            String XMLurl = userDir + path;
            File inputFile = new File(XMLurl);
            DocumentBuilderFactory dbFactory
                    = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            System.out.println("Root element :"
                    + doc.getDocumentElement().getNodeName());

            NodeList nList = doc.getElementsByTagName("tileInfo");
            System.out.println("----------------------------");
            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);
                System.out.println("\nCurrent Element :" + nNode.getNodeName());
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element e = (Element) nNode;
                    int id = Integer.parseInt(e.getAttribute("id"));
                    System.out.println(id);
                    String name = getElementString(e, "name");
                    System.out.println(name);
                    String url = userDir + getElementString(e, "image");
                    System.out.println(url);
                    Image image = getImage(url);
                    System.out.println(image.getWidth());
                    String collidable = getElementString(e, "collidable");
                    boolean result = collidable.equalsIgnoreCase("true") ? true : false;
                    System.out.println(result);
                    tileSoorten.add(new Tile(id, name, image, result));
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException | DOMException e) {
            e.printStackTrace();
        }
    }

    public void loadLevel(String path) {
        try {
            tileMap = new ArrayList<Tile>();
            String XMLurl = userDir + path;
            File inputFile = new File(XMLurl);
            DocumentBuilderFactory dbFactory
                    = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            System.out.println("Root element :"
                    + doc.getDocumentElement().getNodeName());

            NodeList tilesOnMap = doc.getElementsByTagName("tile");
            System.out.println("----------------------------");
            for (int i = 0; i < tilesOnMap.getLength(); i++) {
                Node nNode = tilesOnMap.item(i);
                System.out.println("\nCurrent Element :" + nNode.getNodeName());
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element e = (Element) nNode;
                    int id = Integer.parseInt(getElementString(e, "id"));
                    System.out.println(id);
                    int x = Integer.parseInt(getElementString(e, "x"));
                    System.out.println(x);
                    int y = Integer.parseInt(getElementString(e, "y"));
                    System.out.println(y);
                    int w = Integer.parseInt(getElementString(e, "w"));
                    System.out.println(w);
                    int h = Integer.parseInt(getElementString(e, "h"));
                    System.out.println(h);
                    Tile t = tileSoorten.get(id - 1);

                    tileMap.add(new Tile(id, t.naam, t.image, x, y, w, h, t.collidable));
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException | DOMException e) {
            e.printStackTrace();
        }
    }

    private Image getImage(String url) {
        File f = new File(url);
        Image i = new Image(f.toURI().toString());
        return i;
    }

    private String getElementString(Element e, String tag) {
        return e.getElementsByTagName(tag).item(0).getTextContent();
    }
}
