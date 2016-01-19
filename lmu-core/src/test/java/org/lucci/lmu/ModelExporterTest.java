package org.lucci.lmu;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lucci.lmu.input.Analyzer;
import org.lucci.lmu.input.JarFileAnalyser;
import org.lucci.lmu.input.ParseError;
import org.lucci.lmu.model.IModel;
import org.lucci.lmu.output.ModelExporter;
import org.lucci.lmu.output.ModelExporterImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
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
public class ModelExporterTest {

    // Constants

    private static final Logger LOGGER = LogManager.getLogger();
    private static final String JAR_NAME = "sample-org", PACKAGE_NAME = "input";

    // Variables

    private static IModel sampleOrgJarModel, inputPackageModel;
    private static ModelExporter modelExporter;

    // Set-ups and tear-downs

    @BeforeClass
    public static void setUp() throws IOException, ParseError {
        URL jarUrl = Thread.currentThread().getContextClassLoader().getResource(JAR_NAME + ".jar");
        File file = new File(LmuCore.DEFAULT_OUTPUT_PATH);
        DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(LmuCore.DEFAULT_OUTPUT_PATH));
        ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
        ArrayList<Class<?>> classes = new ArrayList<>();

        modelExporter = new ModelExporterImpl();

        LOGGER.info("cleaning out directory");
        file.mkdirs();
        assertTrue(file.exists());
        for (Path filePath: stream) {
            Files.delete(filePath);
        }

        LOGGER.info("creating class list");
        try {
            classes.add(currentClassLoader.loadClass("org.lucci.lmu.LmuCore"));
            classes.add(currentClassLoader.loadClass("org.lucci.lmu.LmuCoreController"));
            // TODO add input package
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        LOGGER.info("creating model from classes");
        inputPackageModel = new Analyzer().createModelFromClasses(classes);

        LOGGER.info("creating model from jar");
        assertNotNull(jarUrl);
        sampleOrgJarModel = new JarFileAnalyser().createModel(jarUrl.getPath());
    }

    // Tests

    @Test
    public void exportJarToLmuTest() throws FileNotFoundException {
        final String FORMAT = "lmu";

        modelExporter.exportToFile(sampleOrgJarModel, LmuCore.DEFAULT_OUTPUT_PATH + JAR_NAME, FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + JAR_NAME + "." + FORMAT);
        // check the files content by yourself now
    }

    @Test
    public void exportClassesToLmuTest() throws FileNotFoundException {
        final String FORMAT = "lmu";

        modelExporter.exportToFile(inputPackageModel, LmuCore.DEFAULT_OUTPUT_PATH + PACKAGE_NAME, FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + PACKAGE_NAME + "." + FORMAT);
        // check the files content by yourself now
    }

    @Test
    public void exportToDotTest() throws FileNotFoundException {
        final String FORMAT = "dot";

        modelExporter.exportToFile(sampleOrgJarModel, LmuCore.DEFAULT_OUTPUT_PATH + "sample-org", FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "sample-org.dot");
        //modelExporter.exportToFile(inputPackageModel, LmuCore.DEFAULT_OUTPUT_PATH + "input", FORMAT);
        //new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "input." + FORMAT);
        // check the files content by yourself now
    }

    @Test
    public void exportToJavaTest() throws FileNotFoundException {
        final String FORMAT = "java";

        modelExporter.exportToFile(sampleOrgJarModel, LmuCore.DEFAULT_OUTPUT_PATH + "sample-org", FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "sample-org.java");
        //modelExporter.exportToFile(inputPackageModel, LmuCore.DEFAULT_OUTPUT_PATH + "input", FORMAT);
        //new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "input." + FORMAT);
        // check the files content by yourself now
    }

    @Test
    public void exportToPdfTest() throws FileNotFoundException {
        final String FORMAT = "pdf";

        modelExporter.exportToFile(sampleOrgJarModel, LmuCore.DEFAULT_OUTPUT_PATH + "sample-org", FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "sample-org.pdf");
        //modelExporter.exportToFile(inputPackageModel, LmuCore.DEFAULT_OUTPUT_PATH + "input", FORMAT);
        //new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "input." + FORMAT);
        // check the files content by yourself now
    }

    @Test
    public void exportToPngTest() throws FileNotFoundException {
        final String FORMAT = "png";

        modelExporter.exportToFile(sampleOrgJarModel, LmuCore.DEFAULT_OUTPUT_PATH + "sample-org", FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "sample-org.png");
        //modelExporter.exportToFile(inputPackageModel, LmuCore.DEFAULT_OUTPUT_PATH + "input", FORMAT);
        //new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "input." + FORMAT);
        // check the files content by yourself now
    }

    @Test
    public void exportToPsTest() throws FileNotFoundException {
        final String FORMAT = "ps";

        modelExporter.exportToFile(sampleOrgJarModel, LmuCore.DEFAULT_OUTPUT_PATH + "sample-org", FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "sample-org.ps");
        //modelExporter.exportToFile(inputPackageModel, LmuCore.DEFAULT_OUTPUT_PATH + "input", FORMAT);
        //new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "input." + FORMAT);
        // check the files content by yourself now
    }

    @Test
    public void exportToFigTest() throws FileNotFoundException {
        final String FORMAT = "fig";

        modelExporter.exportToFile(sampleOrgJarModel, LmuCore.DEFAULT_OUTPUT_PATH + "sample-org", FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "sample-org.fig");
        //modelExporter.exportToFile(inputPackageModel, LmuCore.DEFAULT_OUTPUT_PATH + "input", FORMAT);
        //new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "input." + FORMAT);
        // check the files content by yourself now
    }

    @Test
    public void exportToSvgTest() throws FileNotFoundException {
        final String FORMAT = "svg";

        modelExporter.exportToFile(sampleOrgJarModel, LmuCore.DEFAULT_OUTPUT_PATH + "sample-org", FORMAT);
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "sample-org.svg");
        //modelExporter.exportToFile(inputPackageModel, LmuCore.DEFAULT_OUTPUT_PATH + "input", FORMAT);
        //new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "input." + FORMAT);
        // check the files content by yourself now
    }
}
