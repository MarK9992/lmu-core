package org.lucci.lmu;

import java.util.List;

/**
 * @author Benjamin Benni, Marc Karassev
 * 
 * This interface represents the app's API.
 */
public interface LmuCore {
	
	// Constants
	
	String DEFAULT_OUTPUT_PATH = "./out/";
	
	// Methods
	
	void analyzeClasses(List<Class<?>> classes, String outputPath, String outputFormat);

    void analyzeJar(String jarPath, String outputPath, String outputFormat);
}
