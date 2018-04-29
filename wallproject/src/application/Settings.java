package application;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author Raúl Correia
 */
public class Settings {

    private static final String CONFIG_PROPERTIES = "config.properties";

    public static final String[] PROPERTIES_KEYWORDS = {"udp_port", "tcp_port"};

    public static int UDP_PORT;
    public static int TCP_PORT;
    public static final int DATAGRAM_SIZE = 256;

    public Settings() throws IOException {
        loadSettings();
    }

    private final void loadSettings() throws IOException {
        InputStream inputStream;
        Properties prop = new Properties();

        inputStream = getClass().getClassLoader().getResourceAsStream(CONFIG_PROPERTIES);
        if (inputStream != null) {
            prop.load(inputStream);
        } else {
            throw new FileNotFoundException("Config property file not found.");
        }
        UDP_PORT = Integer.parseInt(prop.getProperty(PROPERTIES_KEYWORDS[0]));
        TCP_PORT = Integer.parseInt(prop.getProperty(PROPERTIES_KEYWORDS[1]));
    }

}
