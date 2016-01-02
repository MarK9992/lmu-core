package org.lucci.lmu;

import org.lucci.lmu.output.ModelExporter;
import org.lucci.lmu.output.ModelExporterImpl;

/**
 * @author Benjamin Benni, Marc Karassev
 * 
 * This class is an implementation of the app's API.
 */
public class LmuCoreController implements LmuCore {
	
	// Attributes
	
	private ModelExporter modelExporter = new ModelExporterImpl();
			
	// Methods

	@Override
	public void analyzePackage(String packageName) {
		// TODO
		// modelExporter.exportToFile(model, DEFAULT_OUTPUT_PATH);
	}

	@Override
	public void analyzePackageAndExport(String packageName, String outputPath) {
		// TODO
		// modelExporter.exportToFile(model, outputPath);
	}
}
