package org.lucci.lmu;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lucci.lmu.input.ParseError;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Marc Karassev
 */
public class LmuCoreTest {

    // Constants

    private static final URL SAMPLE_ORG_URL = Thread.currentThread().getContextClassLoader().getResource("sample-org.jar");
    private static final Map<String, String> PATHS_AND_PACKAGES = new HashMap<>();

    // Attributes

    private LmuCore controller;

    // Set-ups and tear-downs

    @BeforeClass
    public static void setUp() throws IOException, ParseError {
        File file = new File(LmuCore.DEFAULT_OUTPUT_PATH);

        file.mkdirs();
        assertTrue(file.exists());

        DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(LmuCore.DEFAULT_OUTPUT_PATH));

        for (Path filePath: stream) {
            Files.delete(filePath);
        }
        assertNotNull(SAMPLE_ORG_URL);
        PATHS_AND_PACKAGES.put("target/classes/org/lucci/lmu/LmuCore.class", "org.lucci.lmu");
        PATHS_AND_PACKAGES.put("target/classes/org/lucci/lmu/input", "org.lucci.lmu.input");
    }

    @Before
    public void init() {
        controller = new LmuCoreController();
    }

    @After
    public void tearDown() {
        controller = null;
    }

    // Tests

    @Test
    public void analyzeJarToPdfTest() throws IOException {
        final String FORMAT = "pdf";

        controller.analyzeJar(SAMPLE_ORG_URL.getPath(), LmuCore.DEFAULT_OUTPUT_PATH + "sample-org", FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "sample-org." + FORMAT);
        controller.analyzePaths(PATHS_AND_PACKAGES, LmuCore.DEFAULT_OUTPUT_PATH + "input", FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "input." + FORMAT);
        // check the files contents by yourself now
    }

    @Test
    public void analyzeJarToLmuTest() throws IOException {
        final String FORMAT = "lmu";

        controller.analyzeJar(SAMPLE_ORG_URL.getPath(), LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org", FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org.lmu");
        controller.analyzePaths(PATHS_AND_PACKAGES, LmuCore.DEFAULT_OUTPUT_PATH + "input", FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "input." + FORMAT);
        // check the files contents by yourself now
    }

    @Test
    public void analyzeJarToDotTest() throws IOException {
        final String FORMAT = "dot";

        controller.analyzeJar(SAMPLE_ORG_URL.getPath(), LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org", FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org.dot");
        controller.analyzePaths(PATHS_AND_PACKAGES, LmuCore.DEFAULT_OUTPUT_PATH + "input", FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "input." + FORMAT);
        // check the files contents by yourself now
    }

    @Test
    public void analyzeJarToJavaTest() throws IOException {
        final String FORMAT = "java";

        controller.analyzeJar(SAMPLE_ORG_URL.getPath(), LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org", FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org.java");
        controller.analyzePaths(PATHS_AND_PACKAGES, LmuCore.DEFAULT_OUTPUT_PATH + "input", FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "input." + FORMAT);
        // check the files contents by yourself now
    }

    @Test
    public void analyzeJarToPngTest() throws IOException {
        final String FORMAT = "png";

        controller.analyzeJar(SAMPLE_ORG_URL.getPath(), LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org", FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org.png");
        controller.analyzePaths(PATHS_AND_PACKAGES, LmuCore.DEFAULT_OUTPUT_PATH + "input", FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "input." + FORMAT);
        // check the files contents by yourself now
    }

    @Test
    public void analyzeJarToPsTest() throws IOException {
        final String FORMAT = "ps";

        controller.analyzeJar(SAMPLE_ORG_URL.getPath(), LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org", FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org.ps");
        controller.analyzePaths(PATHS_AND_PACKAGES, LmuCore.DEFAULT_OUTPUT_PATH + "input", FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "input." + FORMAT);
        // check the files contents by yourself now
    }

    @Test
    public void analyzeJarToSvgTest() throws IOException {
        final String FORMAT = "svg";

        controller.analyzeJar(SAMPLE_ORG_URL.getPath(), LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org", FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org.svg");
        controller.analyzePaths(PATHS_AND_PACKAGES, LmuCore.DEFAULT_OUTPUT_PATH + "input", FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "input." + FORMAT);
        // check the files contents by yourself now
    }

    @Test
    public void analyzeJarToFigTest() throws IOException {
        final String FORMAT = "fig";

        controller.analyzeJar(SAMPLE_ORG_URL.getPath(), LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org", FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org.fig");
        controller.analyzePaths(PATHS_AND_PACKAGES, LmuCore.DEFAULT_OUTPUT_PATH + "input", FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "input." + FORMAT);
        // check the files contents by yourself now
    }
}
