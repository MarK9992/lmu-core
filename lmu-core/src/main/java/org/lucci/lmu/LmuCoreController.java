package org.lucci.lmu;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lucci.lmu.input.JarFileAnalyser;
import org.lucci.lmu.input.JarFromPathsCreator;
import org.lucci.lmu.model.IModel;
import org.lucci.lmu.output.ModelExporter;
import org.lucci.lmu.output.ModelExporterImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author Benjamin Benni, Marc Karassev
 * 
 * This class is an implementation of the app's API.
 */
public class LmuCoreController implements LmuCore {

    // Constants

    private static final Logger LOGGER = LogManager.getLogger();
	
	// Attributes
	
	private ModelExporter modelExporter = new ModelExporterImpl();
			
	// Methods

	@Override
	public void analyzePaths(List<String> paths, String outputPath, String outputFormat) throws IOException {
        String jarPath = new JarFromPathsCreator().createJarFromPaths(paths);

        analyzeJar(jarPath, outputPath, outputFormat);
        Files.delete(Paths.get(jarPath));
    }

	@Override
	public void analyzeJar(String jarPath, String outputPath, String outputFormat) {
        try {
            IModel model = new JarFileAnalyser().createModel(jarPath);

            modelExporter.exportToFile(model, outputPath, outputFormat);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}
