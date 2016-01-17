package org.lucci.lmu;

import org.junit.Before;
import org.junit.Test;
import org.lucci.lmu.input.JarFromPathsCreator;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Marc Karassev
 */
public class JarFromPathsCreatorTest {

    private JarFromPathsCreator jarCreator;
    private Map<String, String> pathsAndPackages;

    @Before
    public void init() {
        jarCreator = new JarFromPathsCreator();
        pathsAndPackages = new HashMap<>();
    }

    @Test
    public void emptyJarTest() throws IOException {
        pathsAndPackages.put("target/generated-sources/annotations", "");
        new FileInputStream(jarCreator.createJarFromPaths(pathsAndPackages, ""));
        // check the files contents by yourself now
    }

    @Test
    public void noDirectoryJarTest() throws IOException {
        pathsAndPackages.put("target/classes/org/lucci/lmu/input", "org.lucci.lmu.input");
        new FileInputStream(jarCreator.createJarFromPaths(pathsAndPackages, ""));
        // check the files contents by yourself now
    }

    @Test
    public void jarWithDirectoriesTest() throws IOException {
        pathsAndPackages.put("target/classes/org", "org");
        new FileInputStream(jarCreator.createJarFromPaths(pathsAndPackages, ""));
        // check the files contents by yourself now
    }

    @Test
    public void createJarFromSeveralPaths() throws IOException {
        pathsAndPackages.put("target/classes/org/lucci/lmu/input", "org.lucci.lmu.input");
        pathsAndPackages.put("target/classes/org/lucci/lmu/output", "org.lucci.lmu.output");
        new FileInputStream(jarCreator.createJarFromPaths(pathsAndPackages, ""));
        // check the files contents by yourself now
    }

    @Test
    public void createJarFromClasses() throws IOException {
        pathsAndPackages.put("target/classes/org/lucci/lmu/LmuCore.class", "org.lucci.lmu");
        pathsAndPackages.put("target/classes/org/lucci/lmu/input/ModelFactory.class", "org.lucci.lmu.input");
        new FileInputStream(jarCreator.createJarFromPaths(pathsAndPackages, ""));
        // check the files contents by yourself now
    }

    @Test
    public void createJarFromClassesAndPaths() throws IOException {
        pathsAndPackages.put("target/classes/org/lucci/lmu/LmuCore.class", "org.lucci.lmu");
        pathsAndPackages.put("target/classes/org/lucci/lmu/input", "org.lucci.lmu.input");
        pathsAndPackages.put("target/classes/org/lucci/lmu/output", "org.lucci.lmu.output");
        new FileInputStream(jarCreator.createJarFromPaths(pathsAndPackages, ""));
        // check the files contents by yourself now
    }
}
