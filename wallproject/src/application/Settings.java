package application;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author Raúl Correia
 */
public class Settings {

    private static final String CONFIG_PROPERTIES = "config.properties";

    public static final String[] PROPERTIES_KEYWORDS = {"udp_port", "tcp_port"};

    private static final Map<String, String> settings_map = new HashMap<>();

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
        for (String s : PROPERTIES_KEYWORDS) {
            String input = prop.getProperty(s);
            settings_map.put(s, input);
        }
    }

    public final String getProperty(final String prop) {
        return settings_map.get(prop);
    }

}
