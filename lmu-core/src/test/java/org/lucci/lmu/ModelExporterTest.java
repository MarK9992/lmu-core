package org.lucci.lmu;

import org.junit.BeforeClass;
import org.junit.Test;
import org.lucci.lmu.input.JarFileAnalyser;
import org.lucci.lmu.input.ParseError;
import org.lucci.lmu.model.IModel;
import org.lucci.lmu.output.ModelExporter;
import org.lucci.lmu.output.ModelExporterImpl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Marc Karassev
 */
public class ModelExporterTest {

    // Variables

    private static IModel sampleOrgModel;
    private static ModelExporter modelExporter;

    // Set-ups and tear-downs

    @BeforeClass
    public static void setUp() throws IOException, ParseError {
        URL url = Thread.currentThread().getContextClassLoader().getResource("sample-org.jar");
        DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(LmuCore.DEFAULT_OUTPUT_PATH));

        sampleOrgModel = new JarFileAnalyser().createModel(url.getPath());
        modelExporter = new ModelExporterImpl();
        for (Path filePath: stream) {
            Files.delete(filePath);
        }
    }

    // Tests

    @Test
    public void exportToLmuTest() throws FileNotFoundException {
        modelExporter.exportToFile(sampleOrgModel, LmuCore.DEFAULT_OUTPUT_PATH + "sample-org", "lmu");
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "sample-org.lmu");
        // check the file content by yourself now
    }

    @Test
    public void exportToDotTest() throws FileNotFoundException {
        modelExporter.exportToFile(sampleOrgModel, LmuCore.DEFAULT_OUTPUT_PATH + "sample-org", "dot");
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "sample-org.dot");
        // check the file content by yourself now
    }

    @Test
    public void exportToJavaTest() throws FileNotFoundException {
        modelExporter.exportToFile(sampleOrgModel, LmuCore.DEFAULT_OUTPUT_PATH + "sample-org", "java");
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "sample-org.java");
        // check the file content by yourself now
    }

    @Test
    public void exportToPdfTest() throws FileNotFoundException {
        modelExporter.exportToFile(sampleOrgModel, LmuCore.DEFAULT_OUTPUT_PATH + "sample-org", "pdf");
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "sample-org.pdf");
        // check the file content by yourself now
    }

    @Test
    public void exportToPngTest() throws FileNotFoundException {
        modelExporter.exportToFile(sampleOrgModel, LmuCore.DEFAULT_OUTPUT_PATH + "sample-org", "png");
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "sample-org.png");
        // check the file content by yourself now
    }

    @Test
    public void exportToPsTest() throws FileNotFoundException {
        modelExporter.exportToFile(sampleOrgModel, LmuCore.DEFAULT_OUTPUT_PATH + "sample-org", "ps");
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "sample-org.ps");
        // check the file content by yourself now
    }

    @Test
    public void exportToFidTest() throws FileNotFoundException {
        modelExporter.exportToFile(sampleOrgModel, LmuCore.DEFAULT_OUTPUT_PATH + "sample-org", "fig");
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "sample-org.fig");
        // check the file content by yourself now
    }

    @Test
    public void exportToSvgTest() throws FileNotFoundException {
        modelExporter.exportToFile(sampleOrgModel, LmuCore.DEFAULT_OUTPUT_PATH + "sample-org", "svg");
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "sample-org.svg");
        // check the file content by yourself now
    }
}
