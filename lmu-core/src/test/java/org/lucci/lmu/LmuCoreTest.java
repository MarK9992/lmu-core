package org.lucci.lmu;

import org.junit.BeforeClass;
import org.junit.Test;
import org.lucci.lmu.input.ParseError;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertNotNull;

/**
 * @author Marc Karassev
 */
public class LmuCoreTest {

    // Constants

    private static final LmuCore CONTROLLER = new LmuCoreController();
    private static final URL SAMPLE_ORG_URL = Thread.currentThread().getContextClassLoader().getResource("sample-org.jar");
    private static final String INPUT_PACKAGE = "org.lucci.lmu.input";

    // Set-ups and tear-downs

    @BeforeClass
    public static void setUp() throws IOException, ParseError {
        DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(LmuCore.DEFAULT_OUTPUT_PATH));

        for (Path filePath: stream) {
            Files.delete(filePath);
        }
        assertNotNull(SAMPLE_ORG_URL);
    }

    // Tests

    @Test
    public void analyzeJarToPdfTest() throws FileNotFoundException {
        final String FORMAT = "pdf";

        CONTROLLER.analyzeJar(SAMPLE_ORG_URL.getPath(), LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org", FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org.pdf");
        CONTROLLER.analyzePackage(INPUT_PACKAGE, LmuCore.DEFAULT_OUTPUT_PATH + "input", FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "input." + FORMAT);
        // check the files contents by yourself now
    }

    @Test
    public void analyzeJarToLmuTest() throws FileNotFoundException {
        final String FORMAT = "lmu";

        CONTROLLER.analyzeJar(SAMPLE_ORG_URL.getPath(), LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org", FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org.lmu");
        CONTROLLER.analyzePackage(INPUT_PACKAGE, LmuCore.DEFAULT_OUTPUT_PATH + "input", FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "input." + FORMAT);
        // check the files contents by yourself now
    }

    @Test
    public void analyzeJarToDotTest() throws FileNotFoundException {
        final String FORMAT = "dot";

        CONTROLLER.analyzeJar(SAMPLE_ORG_URL.getPath(), LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org", FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org.dot");
        CONTROLLER.analyzePackage(INPUT_PACKAGE, LmuCore.DEFAULT_OUTPUT_PATH + "input", FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "input." + FORMAT);
        // check the files contents by yourself now
    }

    @Test
    public void analyzeJarToJavaTest() throws FileNotFoundException {
        final String FORMAT = "java";

        CONTROLLER.analyzeJar(SAMPLE_ORG_URL.getPath(), LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org", FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org.java");
        CONTROLLER.analyzePackage(INPUT_PACKAGE, LmuCore.DEFAULT_OUTPUT_PATH + "input", FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "input." + FORMAT);
        // check the files contents by yourself now
    }

    @Test
    public void analyzeJarToPngTest() throws FileNotFoundException {
        final String FORMAT = "png";

        CONTROLLER.analyzeJar(SAMPLE_ORG_URL.getPath(), LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org", FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org.png");
        CONTROLLER.analyzePackage(INPUT_PACKAGE, LmuCore.DEFAULT_OUTPUT_PATH + "input", FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "input." + FORMAT);
        // check the files contents by yourself now
    }

    @Test
    public void analyzeJarToPsTest() throws FileNotFoundException {
        final String FORMAT = "ps";

        CONTROLLER.analyzeJar(SAMPLE_ORG_URL.getPath(), LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org", FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org.ps");
        CONTROLLER.analyzePackage(INPUT_PACKAGE, LmuCore.DEFAULT_OUTPUT_PATH + "input", FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "input." + FORMAT);
        // check the files contents by yourself now
    }

    @Test
    public void analyzeJarToSvgTest() throws FileNotFoundException {
        final String FORMAT = "svg";

        CONTROLLER.analyzeJar(SAMPLE_ORG_URL.getPath(), LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org", FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org.svg");
        CONTROLLER.analyzePackage(INPUT_PACKAGE, LmuCore.DEFAULT_OUTPUT_PATH + "input", FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "input." + FORMAT);
        // check the files contents by yourself now
    }

    @Test
    public void analyzeJarToFigTest() throws FileNotFoundException {
        final String FORMAT = "fig";

        CONTROLLER.analyzeJar(SAMPLE_ORG_URL.getPath(), LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org", FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org.fig");
        CONTROLLER.analyzePackage(INPUT_PACKAGE, LmuCore.DEFAULT_OUTPUT_PATH + "input", FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "input." + FORMAT);
        // check the files contents by yourself now
    }
}
