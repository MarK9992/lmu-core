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

/**
 * @author Marc Karassev
 */
public class LmuCoreTest {

    // Variables

    private LmuCore controller = new LmuCoreController();

    // Set-ups and tear-downs

    @BeforeClass
    public static void setUp() throws IOException, ParseError {
        DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(LmuCore.DEFAULT_OUTPUT_PATH));

        for (Path filePath: stream) {
            Files.delete(filePath);
        }
    }

    // Tests

    @Test
    public void analyzeJarToPdfTest() throws FileNotFoundException{
        URL url = Thread.currentThread().getContextClassLoader().getResource("sample-org.jar");

        controller.analyzeJar("file://" + url.getPath());
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "sample-org.pdf");
        controller.analyzeJar("file://" + url.getPath(), LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org.pdf");
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org.pdf");
        // check the files contents by yourself now
    }

    @Test
    public void analyzeJarToPngTest() throws FileNotFoundException{
        URL url = Thread.currentThread().getContextClassLoader().getResource("sample-org.jar");

        controller.analyzeJar("file://" + url.getPath(), LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org.png");
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org.png");
        // check the files contents by yourself now
    }
}
