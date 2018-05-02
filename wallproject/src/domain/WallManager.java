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

    private static WallManager instance;

    protected WallManager() {
        mapOfWalls = new HashMap<>();
    }

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
        if (mapOfWalls.containsKey(nameOfWall)) {
            Wall w = mapOfWalls.get(nameOfWall);
            return w.addMessage(message);
        }
        return false;
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
    private synchronized Wall findOrCreateWall(final String nameOfWall) {
        Wall w;
        if (!mapOfWalls.containsKey(nameOfWall)) {
            w = new Wall();
            mapOfWalls.put(nameOfWall, w);
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
    public synchronized String getWallInformation(final String nameOfWall) {
        Wall w =findOrCreateWall(nameOfWall);
        return w.wallContent();
    }

    /**
     * Returns a wall information in bytes
     *
     * @param nameOfWall
     * @return
     */
    public synchronized byte[] getWallInformationBytes(final String nameOfWall) {
        if (mapOfWalls.containsKey(nameOfWall)) {
            return mapOfWalls.get(nameOfWall).wallContentBytes();
        }
        return new byte[0];
    }
}
