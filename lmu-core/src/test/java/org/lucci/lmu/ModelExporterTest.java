package org.lucci.lmu;

import org.junit.BeforeClass;
import org.junit.Test;
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
import java.nio.file.*;

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
        File file = new File(url.getPath());
        DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(LmuCore.DEFAULT_OUTPUT_PATH));

        sampleOrgModel = new JarFileAnalyser().createModel(file);
        modelExporter = new ModelExporterImpl();
        for (Path filePath: stream) {
            Files.delete(filePath);
        }
    }

    // Tests

    @Test
    public void exportToPdfTest() throws FileNotFoundException {
        FileInputStream fileInputStream;

        modelExporter.exportToFile(sampleOrgModel, LmuCore.DEFAULT_OUTPUT_PATH + "sample-org.pdf");
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "sample-org.pdf");
        // check the file content by yourself now
    }
}
