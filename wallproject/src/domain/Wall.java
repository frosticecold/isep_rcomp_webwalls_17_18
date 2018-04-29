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

    private Map<Integer, String> map;

    private int currentMessageIndex;
    public static final int MESSAGE_SIZE = 180;

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
    
    public boolean addMessage(String message) {
        if (message.length() <= MESSAGE_SIZE) {
            currentMessageIndex++;
            map.put(currentMessageIndex, message);
            return true;
        }
        return false;
    }

    public boolean removeMessage(int idx) {
        if (map.containsKey(idx)) {
            map.remove(idx);
            return true;
        }
        return false;
    }
    
    
    public String wallContent(){
        StringBuilder sb = new StringBuilder();
        for(Entry<Integer,String> entry : map.entrySet()){
            sb.append("[").append(entry.getKey()).append("] ")
                    .append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }
    
    public byte[] wallContentBytes(){
        return wallContent().getBytes();
    }
}
