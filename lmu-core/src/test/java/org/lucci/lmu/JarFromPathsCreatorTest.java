package org.lucci.lmu;

import org.junit.Before;
import org.junit.Test;
import org.lucci.lmu.input.JarFromPathsCreator;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Marc Karassev
 */
public class JarFromPathsCreatorTest {

    private JarFromPathsCreator jarCreator;
    private ArrayList<String> paths;

    @Before
    public void init() {
        jarCreator = new JarFromPathsCreator();
        paths = new ArrayList<>();
    }

    @Test
    public void emptyJarTest() throws IOException {
        paths.add("target/generated-sources/annotations");
        new FileInputStream(jarCreator.createJarFromPaths(paths));
        // check the files contents by yourself now
    }

    @Test
    public void noDirectoryJarTest() throws IOException {
        paths.add("target/classes/org/lucci/lmu/input");
        new FileInputStream(jarCreator.createJarFromPaths(paths));
        // check the files contents by yourself now
    }

    @Test
    public void jarWithDirectoriesTest() throws IOException {
        paths.add("target/classes");
        new FileInputStream(jarCreator.createJarFromPaths(paths));
        // check the files contents by yourself now
    }

    @Test
    public void createJarFromSeveralPaths() throws IOException {
        paths.add("target/classes/org/lucci/lmu/input");
        paths.add("target/classes/org/lucci/lmu/output");
        new FileInputStream(jarCreator.createJarFromPaths(paths));
        // check the files contents by yourself now
    }

    @Test
    public void createJarFromClasses() throws IOException {
        paths.add("target/classes/org/lucci/lmu/LmuCore.class");
        paths.add("target/classes/org/lucci/lmu/input/ModelFactory.class");
        new FileInputStream(jarCreator.createJarFromPaths(paths));
        // check the files contents by yourself now
    }
}
