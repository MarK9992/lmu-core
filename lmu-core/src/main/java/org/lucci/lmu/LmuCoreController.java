package org.lucci.lmu;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lucci.lmu.input.JarFileAnalyser;
import org.lucci.lmu.input.PackageAnalyzer;
import org.lucci.lmu.model.IModel;
import org.lucci.lmu.output.ModelExporter;
import org.lucci.lmu.output.ModelExporterImpl;

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
	public void analyzePackage(String packageName, String outputPath, String outputFormat) {
		IModel model = new PackageAnalyzer().createModel(packageName);

		modelExporter.exportToFile(model, outputPath, outputFormat);
	}

	@Override
	public void analyzeJar(String jarPath, String outputPath, String outputFormat) {
        try {
            URL url = new URL("file://" + jarPath);
            IModel model = new JarFileAnalyser().createModel(url.getPath());

            modelExporter.exportToFile(model, outputPath, outputFormat);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}
