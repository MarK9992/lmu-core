package org.lucci.lmu;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lucci.lmu.input.ClassesAnalyzer;
import org.lucci.lmu.input.JarClassesAnalyser;
import org.lucci.lmu.input.ManifestAnalyzer;
import org.lucci.lmu.input.PluginAnalyzer;
import org.lucci.lmu.model.IModel;
import org.lucci.lmu.output.ModelExporter;
import org.lucci.lmu.output.ModelExporterImpl;

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
	public void analyzeClasses(List<Class<?>> classes, String outputPath, String outputFormat) {
		IModel model = new ClassesAnalyzer().createModelFromClasses(classes);

		modelExporter.exportToFile(model, outputPath, outputFormat);
	}

	@Override
	public void analyzeJar(String jarPath, String outputPath, String outputFormat) {
        try {
            IModel model = new JarClassesAnalyser().createModelFromJar(jarPath);

            modelExporter.exportToFile(model, outputPath, outputFormat);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

	@Override
	public void analyzeJarDependencies(String jarPath, String outputPath, String outputFormat) {
        try {
            IModel model = new ManifestAnalyzer().createModelFromJar(jarPath);

            modelExporter.exportToFile(model, outputPath, outputFormat);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void analyzePluginDependencies(String pluginPath, String outPath, String outputFormat) {
        try{
            IModel model = new PluginAnalyzer().createModelFromPlugin(pluginPath);

            modelExporter.exportToFile(model, outPath, outputFormat);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
