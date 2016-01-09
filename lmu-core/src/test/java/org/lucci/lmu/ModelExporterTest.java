package org.lucci.lmu;

import org.junit.BeforeClass;
import org.junit.Test;
import org.lucci.lmu.input.JarFileAnalyser;
import org.lucci.lmu.input.ParseError;
import org.lucci.lmu.model.IModel;
import org.lucci.lmu.output.ModelExporter;
import org.lucci.lmu.output.ModelExporterImpl;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * @author Marc Karassev
 */
public class ModelExporterTest {

    // Variables

    private static IModel sampleOrgModel;
    private static ModelExporter modelExporter;

    // Tests

    @BeforeClass
    public static void setUp() throws IOException, ParseError {
        URL url = Thread.currentThread().getContextClassLoader().getResource("sample-org.jar");
        File file = new File(url.getPath());

        sampleOrgModel = new JarFileAnalyser().createModel(file);
        modelExporter = new ModelExporterImpl();
    }

    @Test
    public void exportToPdfTest() {
        modelExporter.exportToFile(sampleOrgModel, LmuCore.DEFAULT_OUTPUT_PATH + "/sample-org.pdf");
        // check by yourself now
    }
}
