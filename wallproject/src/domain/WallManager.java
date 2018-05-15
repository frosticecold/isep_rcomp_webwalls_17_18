/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Raúl Correia
 */
public class WallManager {

    /**
     * This class is a singleton
     * <p>
     * It only exists one in the entire application
     */
    private Map<String, Wall> mapOfWalls;

    /**
     * Only instance of WallManager
     *
     */
    private static WallManager instance;

    /**
     * Number of walls exist
     *
     */
    private static int numberOfWalls = 0;

    /**
     * Empty constructor for singleton
     */
    protected WallManager() {
        mapOfWalls = new HashMap<>();
    }

    /**
     * Returns or creates a WallManager instance
     *
     * @return
     */
    public static WallManager getInstance() {
        if (instance == null) {
            instance = new WallManager();
        }

        return instance;
    }

    /**
     * Removes a Wall by a given name
     *
     * @param name Name of a said wall
     * @return True or false
     */
    public synchronized boolean removeWallByName(final String name) {
        if (mapOfWalls.containsKey(name)) {
            numberOfWalls--;
            return mapOfWalls.remove(name) != null;
        }
        return false;
    }

    /**
     * Adds a message to Wall
     * <p>
     * By design we don't create a new wall if it doesn't exist
     *
     * @param nameOfWall
     * @param message
     * @return True or False
     */
    public synchronized boolean addMessageToWall(final String nameOfWall, final String message) {
        Wall wall = findOrCreateWall(nameOfWall);
        return wall.addMessage(message);
    }

    /**
     * Removes a message from a Wall by Wall name and message number
     *
     * @param nameOfWall
     * @param indexOfMessage
     * @return True or False
     */
    public synchronized boolean removeMessageFromWall(final String nameOfWall, final int indexOfMessage) {
        if (mapOfWalls.containsKey(nameOfWall)) {
            return mapOfWalls.get(nameOfWall).removeMessage(indexOfMessage);
        }

        return false;
    }

    /**
     * Method that if a wall doesnt exist, creates a new one with that name
     *
     * @param nameOfWall
     * @return
     */
    public synchronized Wall findOrCreateWall(final String nameOfWall) {
        Wall w;
        if (!mapOfWalls.containsKey(nameOfWall)) {
            w = new Wall();
            mapOfWalls.put(nameOfWall, w);
            numberOfWalls++;
        } else {
            w = mapOfWalls.get(nameOfWall);
        }

        return w;
    }

    /**
     * Gets a wall information in a preformatted String
     *
     * @param nameOfWall
     * @return
     */
    public String getWallInformation(final String nameOfWall) {
        Wall w = findOrCreateWall(nameOfWall);
        return w.wallContent();
    }

    /**
     * Returns a wall information in bytes
     *
     * @param nameOfWall
     * @return
     */
    public byte[] getWallInformationBytes(final String nameOfWall) {
        if (mapOfWalls.containsKey(nameOfWall)) {
            return mapOfWalls.get(nameOfWall).wallContentBytes();
        }
        return new byte[0];
    }

    /**
     * Gets a wall information in a preformatted html string
     *
     * @param nameOfWall
     * @return
     */
    public String getWallInformationHTML(final String nameOfWall) {
        Wall w = findOrCreateWall(nameOfWall);
        return w.wallContentToHTML();
    }

    /**
     * Returns a wall information in bytes to html format
     *
     * @param nameOfWall
     * @return
     */
    public byte[] getWallInformationBytesHTML(final String nameOfWall) {
        if (mapOfWalls.containsKey(nameOfWall)) {
            return mapOfWalls.get(nameOfWall).wallContentBytesToHTML();
        }
        return new byte[0];
    }

    /**
     * Retrns the number of Walls
     *
     * @return
     */
    public int howManyWallsExist() {
        return numberOfWalls;
    }

}
