package play.ant;

import org.apache.tools.ant.BuildException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

/**
 * @author huljas@gmail.com
 */
public class PlayConfUtils {

    public static Map<String, String> loadAndResolve(File srcFile, String playId) {
        try {
            Map<String,String> defaults = new HashMap<String,String>();
            Map<String,String> idSpecific = new HashMap<String,String>();
            BufferedReader reader = new BufferedReader(new FileReader(srcFile));
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("#")) {
                    continue;
                }
                if (line.startsWith("%")) {
                    if (playId.length() > 0 && line.startsWith(playId + ".")) {
                        line = line.substring((playId + ".").length());
                        String[] sa = splitLine(line);
                        if (sa != null) {
                            idSpecific.put(sa[0], sa[1]);
                        }
                    } else {
                        continue;
                    }
                } else {
                    String[] sa = splitLine(line);
                    if (sa != null) {
                        defaults.put(sa[0], sa[1]);
                    }
                }
            }
            defaults.putAll(idSpecific);
            return defaults;
        } catch (IOException e) {
            throw new BuildException("Failed to load configuration file: " + srcFile.getAbsolutePath(), e);
        }
    }

    private static String[] splitLine(String line) {
        int i = line.indexOf("=");
        if (i > 0) {
            String key = line.substring(0, i);
            String value = "";
            if (i < line.length()) {
                value = line.substring(i+1);
            }
            return new String[]{key.trim(), value.trim()};
        }
        return null;
    }
}
