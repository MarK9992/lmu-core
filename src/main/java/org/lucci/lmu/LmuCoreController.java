package org.lucci.lmu;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lucci.lmu.input.ClassesAnalyzer;
import org.lucci.lmu.input.JarFileAnalyser;
import org.lucci.lmu.model.IModel;
import org.lucci.lmu.output.ModelExporter;
import org.lucci.lmu.output.ModelExporterImpl;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

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
	public void analyzeClasses(List<Class<?>> classes, String outputPath, String outputFormat) {
		IModel model = new ClassesAnalyzer().createModelFromClasses(classes);

		modelExporter.exportToFile(model, outputPath, outputFormat);
	}

	@Override
	public void analyzeJar(String jarPath, String outputPath, String outputFormat) {
        try {
            IModel model = new JarFileAnalyser().createModelFromJar(jarPath);

            modelExporter.exportToFile(model, outputPath, outputFormat);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

	@Override
	public void analyzeJarDependencies(String jarPath, String outputPath, String outputFormat) throws IOException {
		File file = new File(jarPath);
        JarFile jarFile = new JarFile(file);
        Manifest manifest = jarFile.getManifest();
        Map<String, Attributes> entries = manifest.getEntries();

        LOGGER.debug(manifest.getMainAttributes().get(Attributes.Name.MANIFEST_VERSION));
        entries.forEach((name, attributes) -> LOGGER.debug(name + "\t" + attributes));
	}
}
