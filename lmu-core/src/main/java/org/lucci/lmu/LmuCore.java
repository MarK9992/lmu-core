package org.lucci.lmu;

import java.io.IOException;
import java.util.Map;

/**
 * @author Benjamin Benni, Marc Karassev
 * 
 * This interface represents the app's API.
 */
public interface LmuCore {
	
	// Constants
	
	String DEFAULT_OUTPUT_PATH = "./out/";
	
	// Methods
	
	void analyzePaths(Map<String, String> pathsAndPackages, String outputPath, String outputFormat) throws IOException;

    void analyzeJar(String jarPath, String outputPath, String outputFormat);
}
