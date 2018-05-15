/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author Raúl Correia
 */
public class Wall {

    /**
     * Map that contains the number of message and its associated message
     */
    private Map<Integer, String> map;

    /**
     * Current message Index
     */
    private int currentMessageIndex;

    /**
     * Max Message Size(chars)
     */
    public static final int MESSAGE_SIZE = 180;

    /**
     * Empty constructor of a wall
     */
    public Wall() {
        map = new LinkedHashMap<>();
        currentMessageIndex = 0;
    }

    /*
    public String getMessage(int idx) {
        if (map.containsKey(idx)) {
            return map.get(idx);
        }
        return null;
    }
     */
    /**
     * Adds a message to a wall, if its within limits
     *
     * @param message Message to add
     * @return true or false
     */
    public boolean addMessage(String message) {
        if (message.length() <= MESSAGE_SIZE) {
            currentMessageIndex++;
            map.put(currentMessageIndex, message);
            return true;
        }
        return false;
    }

    /**
     * Removes a message from a wall by it's index
     *
     * @param idx Index of a message to remove
     * @return true or false
     */
    public boolean removeMessage(int idx) {
        if (map.containsKey(idx)) {
            map.remove(idx);
            return true;
        }
        return false;
    }

    /**
     * Returns all the wall content in a single string
     *
     * @return String with all the wall content
     */
    public String wallContent() {
        StringBuilder sb = new StringBuilder();
        for (Entry<Integer, String> entry : map.entrySet()) {
            sb.append("[").append(entry.getKey()).append("] ")
                    .append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }

    /**
     * Returns the wall content in a single string with HTML new lines
     *
     * @return String with all the content to display in HTML
     */
    public String wallContentToHTML() {
        StringBuilder sb = new StringBuilder();
        for (Entry<Integer, String> entry : map.entrySet()) {
            sb.append("[").append(entry.getKey()).append("] ")
                    .append(entry.getValue()).append("<p></p>");
        }
        return sb.toString();
    }

    /**
     * Returns the wall content in bytes
     *
     * @return wall content in bytes
     */
    public byte[] wallContentBytes() {
        return wallContent().getBytes();
    }

    /**
     * Returns the wall content formated in html in bytes
     *
     * @return wall content formated in html in bytes
     */
    public byte[] wallContentBytesToHTML() {
        return wallContentToHTML().getBytes();
    }

}
