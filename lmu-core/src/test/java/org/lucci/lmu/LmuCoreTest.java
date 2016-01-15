package org.lucci.lmu;

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
import java.util.ArrayList;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Marc Karassev
 */
public class LmuCoreTest {

    // Constants

    private static final LmuCore CONTROLLER = new LmuCoreController();
    private static final URL SAMPLE_ORG_URL = Thread.currentThread().getContextClassLoader().getResource("sample-org.jar");
    private static final ArrayList<String> PATHS = new ArrayList<>();

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
        PATHS.add("target/classes/org/lucci/lmu/LmuCore.class");
        PATHS.add("target/classes/org/lucci/lmu/input");
    }

    // Tests

    @Test
    public void analyzeJarToPdfTest() throws IOException {
        final String FORMAT = "pdf";

        CONTROLLER.analyzeJar(SAMPLE_ORG_URL.getPath(), LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org", FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org.pdf");
        CONTROLLER.analyzePaths(PATHS, LmuCore.DEFAULT_OUTPUT_PATH + "input", FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "input." + FORMAT);
        // check the files contents by yourself now
    }

    @Test
    public void analyzeJarToLmuTest() throws IOException {
        final String FORMAT = "lmu";

        CONTROLLER.analyzeJar(SAMPLE_ORG_URL.getPath(), LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org", FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org.lmu");
        CONTROLLER.analyzePaths(PATHS, LmuCore.DEFAULT_OUTPUT_PATH + "input", FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "input." + FORMAT);
        // check the files contents by yourself now
    }

    @Test
    public void analyzeJarToDotTest() throws IOException {
        final String FORMAT = "dot";

        CONTROLLER.analyzeJar(SAMPLE_ORG_URL.getPath(), LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org", FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org.dot");
        CONTROLLER.analyzePaths(PATHS, LmuCore.DEFAULT_OUTPUT_PATH + "input", FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "input." + FORMAT);
        // check the files contents by yourself now
    }

    @Test
    public void analyzeJarToJavaTest() throws IOException {
        final String FORMAT = "java";

        CONTROLLER.analyzeJar(SAMPLE_ORG_URL.getPath(), LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org", FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org.java");
        CONTROLLER.analyzePaths(PATHS, LmuCore.DEFAULT_OUTPUT_PATH + "input", FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "input." + FORMAT);
        // check the files contents by yourself now
    }

    @Test
    public void analyzeJarToPngTest() throws IOException {
        final String FORMAT = "png";

        CONTROLLER.analyzeJar(SAMPLE_ORG_URL.getPath(), LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org", FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org.png");
        CONTROLLER.analyzePaths(PATHS, LmuCore.DEFAULT_OUTPUT_PATH + "input", FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "input." + FORMAT);
        // check the files contents by yourself now
    }

    @Test
    public void analyzeJarToPsTest() throws IOException {
        final String FORMAT = "ps";

        CONTROLLER.analyzeJar(SAMPLE_ORG_URL.getPath(), LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org", FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org.ps");
        CONTROLLER.analyzePaths(PATHS, LmuCore.DEFAULT_OUTPUT_PATH + "input", FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "input." + FORMAT);
        // check the files contents by yourself now
    }

    @Test
    public void analyzeJarToSvgTest() throws IOException {
        final String FORMAT = "svg";

        CONTROLLER.analyzeJar(SAMPLE_ORG_URL.getPath(), LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org", FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org.svg");
        CONTROLLER.analyzePaths(PATHS, LmuCore.DEFAULT_OUTPUT_PATH + "input", FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "input." + FORMAT);
        // check the files contents by yourself now
    }

    @Test
    public void analyzeJarToFigTest() throws IOException {
        final String FORMAT = "fig";

        CONTROLLER.analyzeJar(SAMPLE_ORG_URL.getPath(), LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org", FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org.fig");
        CONTROLLER.analyzePaths(PATHS, LmuCore.DEFAULT_OUTPUT_PATH + "input", FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "input." + FORMAT);
        // check the files contents by yourself now
    }
}
