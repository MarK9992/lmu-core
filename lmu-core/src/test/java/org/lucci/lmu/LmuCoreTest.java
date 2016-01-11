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

        assertNotNull(url);
        controller.analyzeJar(url.getPath());
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "sample-org.pdf");
        controller.analyzeJar(url.getPath(), LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org.pdf");
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org.pdf");
        // check the files contents by yourself now
    }

    @Test
    public void analyzeJarToLmuTest() throws FileNotFoundException{
        URL url = Thread.currentThread().getContextClassLoader().getResource("sample-org.jar");

        assertNotNull(url);
        controller.analyzeJar(url.getPath(), LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org.lmu");
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org.lmu");
        // check the files contents by yourself now
    }

    @Test
    public void analyzeJarToDotTest() throws FileNotFoundException{
        URL url = Thread.currentThread().getContextClassLoader().getResource("sample-org.jar");

        assertNotNull(url);
        controller.analyzeJar(url.getPath(), LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org.dot");
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org.dot");
        // check the files contents by yourself now
    }

    @Test
    public void analyzeJarToJavaTest() throws FileNotFoundException{
        URL url = Thread.currentThread().getContextClassLoader().getResource("sample-org.jar");

        assertNotNull(url);
        controller.analyzeJar(url.getPath(), LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org.java");
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org.java");
        // check the files contents by yourself now
    }

    @Test
    public void analyzeJarToPngTest() throws FileNotFoundException{
        URL url = Thread.currentThread().getContextClassLoader().getResource("sample-org.jar");

        assertNotNull(url);
        controller.analyzeJar(url.getPath(), LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org.png");
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org.png");
        // check the files contents by yourself now
    }

    @Test
    public void analyzeJarToPsTest() throws FileNotFoundException{
        URL url = Thread.currentThread().getContextClassLoader().getResource("sample-org.jar");

        assertNotNull(url);
        controller.analyzeJar(url.getPath(), LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org.ps");
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org.ps");
        // check the files contents by yourself now
    }

    @Test
    public void analyzeJarToSvgTest() throws FileNotFoundException{
        URL url = Thread.currentThread().getContextClassLoader().getResource("sample-org.jar");

        assertNotNull(url);
        controller.analyzeJar(url.getPath(), LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org.svg");
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org.svg");
        // check the files contents by yourself now
    }

    @Test
    public void analyzeJarToFigTest() throws FileNotFoundException{
        URL url = Thread.currentThread().getContextClassLoader().getResource("sample-org.jar");

        assertNotNull(url);
        controller.analyzeJar(url.getPath(), LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org.fig");
        new FileInputStream(LmuCore.DEFAULT_OUTPUT_PATH + "custom-sample-org.fig");
        // check the files contents by yourself now
    }
}
