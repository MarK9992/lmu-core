package org.lucci.lmu.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lucci.lmu.LmuCore;
import org.lucci.lmu.LmuCoreController;
import org.lucci.lmu.input.ParseError;
import org.lucci.lmu.test.util.ClassFinder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

    private static final Logger LOGGER = LogManager.getLogger();
    private static final LmuCore CONTROLLER = new LmuCoreController();
    private static final String PACKAGE_FULL = "org.lucci.lmu.input", JAR_NAME = "sample-org", CLASSES_NAME = "inputAndLmuCore",
        CLASS1_FULL = "org.lucci.lmu.LmuCore", CLASS2_FULL = "org.lucci.lmu.LmuCoreController", DEPENDENCIES_NAME = "bkp-dependencies",
        PLUGIN_NAME = "lmu-eclipse-plugin";
    private static final URL SAMPLE_ORG_URL = Thread.currentThread().getContextClassLoader().getResource(JAR_NAME + ".jar"),
        BKP_URL = Thread.currentThread().getContextClassLoader().getResource("lmu-eclipse-plugin-bkp.jar"),
        PLUGIN_URL = Thread.currentThread().getContextClassLoader().getResource("lmu-eclipse-plugin-bkp");
    private static final ArrayList<Class<?>> CLASSES = new ArrayList<>();

    // Set-ups and tear-downs

    @BeforeClass
    public static void setUp() throws IOException, ParseError {
        File file = new File(LmuCore.DEFAULT_OUTPUT_PATH);
        DirectoryStream<Path> stream;
        ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();

        LOGGER.info("cleaning out directory");
        file.mkdirs();
        assertTrue(file.exists());
        stream = Files.newDirectoryStream(Paths.get(LmuCore.DEFAULT_OUTPUT_PATH));
        for (Path filePath: stream) {
            Files.delete(filePath);
        }

        LOGGER.info("creating class list");
        try {
            CLASSES.add(currentClassLoader.loadClass(CLASS1_FULL));
            CLASSES.add(currentClassLoader.loadClass(CLASS2_FULL));
            CLASSES.addAll(ClassFinder.find(PACKAGE_FULL));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        assertNotNull(SAMPLE_ORG_URL);
        assertNotNull(BKP_URL);
        assertNotNull(PLUGIN_URL);
    }

    // Tests

    @Test
    public void analyzerPluginDependenciesToPdfTest() throws FileNotFoundException {
        final String FORMAT = "pdf";

        CONTROLLER.analyzePluginDependencies(PLUGIN_URL.getPath(), LmuCore.DEFAULT_OUTPUT_PATH + PLUGIN_NAME, FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + PLUGIN_NAME + "." + FORMAT);
        // check the files contents by yourself now
    }

    @Test
    public void analyzerPluginDependenciesToPngTest() throws FileNotFoundException {
        final String FORMAT = "png";

        CONTROLLER.analyzePluginDependencies(PLUGIN_URL.getPath(), LmuCore.DEFAULT_OUTPUT_PATH + PLUGIN_NAME, FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + PLUGIN_NAME + "." + FORMAT);
        // check the files contents by yourself now
    }

    @Test
    public void analyzeJarDependenciesToPngTest() throws FileNotFoundException {
        final String FORMAT = "png";

        CONTROLLER.analyzeJarDependencies(BKP_URL.getPath(), LmuCore.DEFAULT_OUTPUT_PATH + DEPENDENCIES_NAME, FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + DEPENDENCIES_NAME + "." + FORMAT);
        // check the files contents by yourself now
    }

    @Test
    public void analyzeJarToPdfTest() throws FileNotFoundException {
        final String FORMAT = "pdf";

        CONTROLLER.analyzeJar(SAMPLE_ORG_URL.getPath(), LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org", FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org.pdf");
        // check the files contents by yourself now
    }

    @Test
    public void analyzeClassesToPdfTest() throws FileNotFoundException {
        final String FORMAT = "pdf";

        CONTROLLER.analyzeClasses(CLASSES, LmuCore.DEFAULT_OUTPUT_PATH + CLASSES_NAME, FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + CLASSES_NAME + "." + FORMAT);
        // check the files contents by yourself now
    }

    @Test
    public void analyzeJarToLmuTest() throws FileNotFoundException {
        final String FORMAT = "lmu";

        CONTROLLER.analyzeJar(SAMPLE_ORG_URL.getPath(), LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org", FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org.lmu");
        // check the files contents by yourself now
    }

    @Test
    public void analyzeClassesToLmuTest() throws FileNotFoundException {
        final String FORMAT = "lmu";

        CONTROLLER.analyzeClasses(CLASSES, LmuCore.DEFAULT_OUTPUT_PATH + CLASSES_NAME, FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + CLASSES_NAME + "." + FORMAT);
        // check the files contents by yourself now
    }

    @Test
    public void analyzeJarToDotTest() throws FileNotFoundException {
        final String FORMAT = "dot";

        CONTROLLER.analyzeJar(SAMPLE_ORG_URL.getPath(), LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org", FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org.dot");
        // check the files contents by yourself now
    }

    @Test
    public void analyzeClassesToDotTest() throws FileNotFoundException {
        final String FORMAT = "dot";

        CONTROLLER.analyzeClasses(CLASSES, LmuCore.DEFAULT_OUTPUT_PATH + CLASSES_NAME, FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + CLASSES_NAME + "." + FORMAT);
        // check the files contents by yourself now
    }

    @Test
    public void analyzeJarToJavaTest() throws FileNotFoundException {
        final String FORMAT = "java";

        CONTROLLER.analyzeJar(SAMPLE_ORG_URL.getPath(), LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org", FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org.java");
        // check the files contents by yourself now
    }

    @Test
    public void analyzeClassesToJavaTest() throws FileNotFoundException {
        final String FORMAT = "java";

        CONTROLLER.analyzeClasses(CLASSES, LmuCore.DEFAULT_OUTPUT_PATH + CLASSES_NAME, FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + CLASSES_NAME + "." + FORMAT);
        // check the files contents by yourself now
    }

    @Test
    public void analyzeJarToPngTest() throws FileNotFoundException {
        final String FORMAT = "png";

        CONTROLLER.analyzeJar(SAMPLE_ORG_URL.getPath(), LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org", FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org.png");
        // check the files contents by yourself now
    }

    @Test
    public void analyzeClassesToPngTest() throws FileNotFoundException {
        final String FORMAT = "png";

        CONTROLLER.analyzeClasses(CLASSES, LmuCore.DEFAULT_OUTPUT_PATH + CLASSES_NAME, FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + CLASSES_NAME + "." + FORMAT);
        // check the files contents by yourself now
    }

    @Test
    public void analyzeJarToPsTest() throws FileNotFoundException {
        final String FORMAT = "ps";

        CONTROLLER.analyzeJar(SAMPLE_ORG_URL.getPath(), LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org", FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org.ps");
        // check the files contents by yourself now
    }

    @Test
    public void analyzeClassesToPsTest() throws FileNotFoundException {
        final String FORMAT = "ps";

        CONTROLLER.analyzeClasses(CLASSES, LmuCore.DEFAULT_OUTPUT_PATH + CLASSES_NAME, FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + CLASSES_NAME + "." + FORMAT);
        // check the files contents by yourself now
    }

    @Test
    public void analyzeJarToSvgTest() throws FileNotFoundException {
        final String FORMAT = "svg";

        CONTROLLER.analyzeJar(SAMPLE_ORG_URL.getPath(), LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org", FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org.svg");
        // check the files contents by yourself now
    }

    @Test
    public void analyzeClassesToSvgTest() throws FileNotFoundException {
        final String FORMAT = "svg";

        CONTROLLER.analyzeClasses(CLASSES, LmuCore.DEFAULT_OUTPUT_PATH + CLASSES_NAME, FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + CLASSES_NAME + "." + FORMAT);
        // check the files contents by yourself now
    }

    @Test
    public void analyzeJarToFigTest() throws FileNotFoundException {
        final String FORMAT = "fig";

        CONTROLLER.analyzeJar(SAMPLE_ORG_URL.getPath(), LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org", FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org.fig");
        // check the files contents by yourself now
    }

    @Test
    public void analyzeClassesToFigTest() throws FileNotFoundException {
        final String FORMAT = "fig";

        CONTROLLER.analyzeClasses(CLASSES, LmuCore.DEFAULT_OUTPUT_PATH + CLASSES_NAME, FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + CLASSES_NAME + "." + FORMAT);
        // check the files contents by yourself now
    }
}
