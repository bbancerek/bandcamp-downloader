package bartibart.downloader.bandcamp.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map.Entry;
import java.util.Properties;

public class AppConfig {

    public static final String CONFIG_FILE = "app.properties";

    public static final String PROPERTY_DEFAULT_DIRECTORY = "default.directory";

    public static void main(String[] args) {
        boolean edit = false;
        boolean help = true;
        boolean showAll = false;
        if (args.length == 1 && args[0].equals("-all")) {
            help = false;
            showAll = true;
        } else if (args.length > 0) {
            if (args[0].equals("-edit")) {
                edit = true;
            }
            if (!edit || (edit && args.length > 1)) {
                help = !validateProperties(args, edit);
            }
        }
        if (help) {
            System.out.println(help());
            System.exit(0);
        }

        Properties properties = getProperties();
        if (edit) {
            editProperties(properties, args);
        } else if (showAll) {
            displayProperties(properties);
        } else {
            displayProperties(properties, args);
        }
    }

    private static String help() {
        return "Usage:\n"
             + "  EDIT:        AppConfig -edit <property=value> <property=value>...\n"
             + "  DISPLAY:     AppConfig <property> <property>...\n"
             + "  DISPLAY ALL: AppConfig -all"
             + "\n"
             + "Properties:\n"
             + "  " + PROPERTY_DEFAULT_DIRECTORY + " = default directory for downloaded albums/tracks";
    }

    private static boolean validateProperties(String[] args, boolean edit) {
        for (int i = edit ? 1 : 0; i < args.length; ++i) {
            String prop = edit ? args[i].split("=")[0] : args[i];
            if (!prop.equals(PROPERTY_DEFAULT_DIRECTORY)) {
                return false;
            }
        }
        return true;
    }

    private static void displayProperties(Properties properties) {
        for (Entry<Object, Object> entry : properties.entrySet()) {
            System.out.println(entry.getKey().toString() + "=" + entry.getValue().toString());
        }
    }

    private static void displayProperties(Properties properties, String[] args) {
        for (int i = 0; i < args.length; ++i) {
            System.out.println(args[i] + "=" + properties.getProperty(args[i]));
        }
    }

    private static void editProperties(Properties properties, String[] args) {
        // start from index 1 (index 0 points to "-edit")
        for (int i = 1; i < args.length; ++i) {
            int index = args[i].indexOf("=");
            System.out.println("Setting " + args[i]);
            properties.setProperty(args[i].substring(0, index), args[i].substring(index + 1));
        }
        // save properties file
        try (OutputStream out = new FileOutputStream(new File(getConfigFileURI()))) {
            properties.store(out, "Use forward slashes (/) in paths");
        } catch (Exception e) {
            System.out.println("Could not save properties!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static Properties getProperties() {
        Properties properties = new Properties();
        try (InputStream in = new FileInputStream(new File(getConfigFileURI()))) {
            properties.load(in);
        } catch (Exception e) {
            System.out.println("Could not read " + AppConfig.CONFIG_FILE + " file!");
        }
        return properties;
    }

    private static URI getConfigFileURI() throws URISyntaxException {
        URL url = AppConfig.class.getClassLoader().getResource(CONFIG_FILE);
        if (url == null) {
            try {
                url = new URL(AppConfig.class.getProtectionDomain().getCodeSource().getLocation(), CONFIG_FILE);
            } catch (MalformedURLException e) {
                System.out.println("Could not find " + AppConfig.CONFIG_FILE + " file!");
                url = null;
            }
        }
        return url.toURI();
    }

}
