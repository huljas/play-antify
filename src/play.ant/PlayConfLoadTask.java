package play.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;

import java.io.File;
import java.util.Map;

/**
 * Ant task which loads the properties from the application.conf file to ant properties.
 *
 * @author huljas@gmail.com
 */
public class PlayConfLoadTask {

    private Project project;
    /** Play id */
    private String playId = "";
    /** Prefix to use for the properties loaded from the configuration file */
    private String prefix = "application.conf.";
    /** Source file to read */
    private File srcFile;

    public void setProject(Project project) {
        this.project = project;
    }

    public void setPlayId(String playId) {
        this.playId = playId;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setSrcFile(File srcFile) {
        this.srcFile = srcFile;
    }

    public void execute() {
        if (srcFile == null) {
            throw new BuildException("No srcFile set!");
        }
        Map<String,String> map = PlayConfUtils.loadAndResolve(srcFile, playId);
        for (Map.Entry<String,String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = project.replaceProperties(entry.getValue());
            project.setProperty(prefix + key, value);
            project.log("Loaded property '" + prefix + key + "'='" + value + "'");
        }
    }
}
