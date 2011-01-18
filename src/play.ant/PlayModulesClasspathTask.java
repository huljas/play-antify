package play.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.selectors.ContainsSelector;
import org.apache.tools.ant.types.selectors.FilenameSelector;
import org.apache.tools.ant.util.FileUtils;

import java.io.File;
import java.util.Map;

/**
 * Ant task which creates classpath element containing the libraries from the modules defined in the application
 * configuration file.
 *
 * @author huljas@gmail.com
 */
public class PlayModulesClasspathTask {

    private Project project;
    /** Play id */
    private String playId = "";
    /** Name to use for the modules classpath */
    private String modulesClasspath = "modules.classpath";
    /** Source file to read */
    private File srcFile;

    public void setProject(Project project) {
        this.project = project;
    }

    public void setPlayId(String playId) {
        this.playId = playId;
    }

    public void setModulesClasspath(String modulesClasspath) {
        this.modulesClasspath = modulesClasspath;
    }

    public void setSrcFile(File srcFile) {
        this.srcFile = srcFile;
    }

    public void execute() {
        if (srcFile == null) {
            throw new BuildException("No srcFile set!");
        }
        if (project.getProperty("play.path") == null) {
            throw new BuildException("Property play.path is not set!");
        }
        Map<String,String> map = PlayConfUtils.loadAndResolve(srcFile, playId);

        File applicationDir = srcFile.getParentFile().getParentFile();

        System.out.println(map);

        Path path = new Path(project);

        FilenameSelector endsToJar = new FilenameSelector();
        endsToJar.setName("*.jar");

        for (Map.Entry<String,String> entry : map.entrySet()) {
            if (entry.getKey().startsWith("module.")) {
                String s = entry.getValue();
                s = project.replaceProperties(s);
                File moduleDir;
                if (!FileUtils.isAbsolutePath(s)) {
                    moduleDir = new File(applicationDir, s);
                } else {
                    moduleDir = new File(s);
                }
                if (!moduleDir.exists()) {
                    project.log("Failed add non existing module to classpath! " + moduleDir.getAbsolutePath(), Project.MSG_WARN);
                    continue;
                }
                File moduleLib = new File(moduleDir, "lib");
                if (moduleLib.exists()) {
                    FileSet fileSet = new FileSet();
                    fileSet.setDir(moduleLib);
                    fileSet.addFilename(endsToJar);
                    path.addFileset(fileSet);
                    project.log("Added fileSet to path: " + fileSet, Project.MSG_DEBUG);
                } else {
                    project.log("Ignoring non existing lib dir: " + moduleLib.getAbsolutePath());
                }
            }
        }
        project.addReference(modulesClasspath, path);
        project.log("Generated classpath '" + modulesClasspath + "':" + project.getReference(modulesClasspath));
    }
}
