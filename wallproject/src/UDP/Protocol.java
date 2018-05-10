/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UDP;

import java.net.DatagramPacket;

/**
 *
 * @author Raúl Correia <1090657@isep.ipp.pt>
 */
public class Protocol {

    public static final char STARTING_COMMAND = '@';
    public static final char MSG_INDEX = '#';
    public static final char HEADER_FILLER = '?';
    public static final String MSG_TOTAL_CONTENT = "#!";

    public static final String KEYWORDS[] = {"@hello", "@getwall", "@confirm",
        "@error@resend", "@success", "@checksum",
        "@checksum_client", "@checksum_server", "@wall;%wall;%msgsize;@msg;%content",
        "@wall", "@message", "@failed"};
    public static final String NUMBER_SPLITTER = "/";
    public static final String MSG_SPLITTER = ";";

    public static final int HELLO_COMMAND = 0;
    public static final int GETWALL_COMMAND = 1;
    public static final int CONFIRM_COMMAND = 2;
    public static final int ERROR_RESEND_COMMAND = 3;
    public static final int SUCCESS_COMMAND = 4;
    public static final int CHECKSUM_COMMAND = 5;
    public static final int CHECKSUM_CLIENT_COMMAND = 6;
    public static final int CHECKSUM_SERVER_COMMAND = 7;
    public static final int WALL_MSG_COMMAND = 8;
    public static final int WALL_COMMAND = 9;
    public static final int MESSAGE_COMMAND = 10;
    public static final int FAILED_COMMAND = 11;

    public Protocol() {
    }

    public DatagramPacket[] interpret(DatagramPacket source) {

        return null;
    }

    public static String buildGetWallCommand(final String wallname) {
        return Protocol.KEYWORDS[Protocol.GETWALL_COMMAND] + MSG_SPLITTER + wallname;
    }

    public static String getCommand(final int indexOfCommand) {
        return KEYWORDS[indexOfCommand];

    }

    public static String buildErrorResendWall(final String wallname) {

        return KEYWORDS[ERROR_RESEND_COMMAND] + KEYWORDS[WALL_COMMAND] + MSG_SPLITTER + wallname;

    }

    public static String buildTotalChecksumPacket(final byte data[]) {

        return Protocol.MSG_TOTAL_CONTENT + MSG_SPLITTER + data.length + MSG_SPLITTER;
    }

    public static String buildWallAndMessageCommand(final String wallname, final String message) {

        String content = KEYWORDS[WALL_MSG_COMMAND].replaceFirst("%wall", wallname);
        content = content.replaceFirst("%msgsize", message.length() + "");
        content = content.replaceFirst("%content", message);

        return content.trim();
    }

    public static String buildErrorFailed(final String wallname) {
        String msg = KEYWORDS[FAILED_COMMAND];
        return msg;
    }
}
