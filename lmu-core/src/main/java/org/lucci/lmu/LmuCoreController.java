package org.lucci.lmu;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lucci.lmu.input.JarFileAnalyser;
import org.lucci.lmu.model.IModel;
import org.lucci.lmu.output.ModelExporter;
import org.lucci.lmu.output.ModelExporterImpl;

import java.io.File;
import java.net.URL;

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
	public void analyzePackage(String packageName) {
		// TODO
		// modelExporter.exportToFile(model, DEFAULT_OUTPUT_PATH); // TODO add extension
	}

	@Override
	public void analyzePackage(String packageName, String outputPath) {
		// TODO
		// modelExporter.exportToFile(model, outputPath);
	}

	@Override
	public void analyzeJar(String jarPath) {
		String[] split = jarPath.split("/|\\\\"); // TODO does it works on every OS?
        String outputName = split[split.length - 1];

        outputName = outputName.replaceAll(".jar$", ".pdf");
        LOGGER.debug(outputName);
        analyzeJar(jarPath, DEFAULT_OUTPUT_PATH + outputName);
	}

	@Override
	public void analyzeJar(String jarPath, String outputPath) {
        try {
            URL url = new URL("file://" + jarPath);
            IModel model = new JarFileAnalyser().createModel(url.getPath());

            modelExporter.exportToFile(model, outputPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}
