package org.lucci.lmu;

/**
 * @author Benjamin Benni, Marc Karassev
 * 
 * This interface represents the app's API.
 */
public interface LmuCore {
	
	// Constants
	
	String DEFAULT_OUTPUT_PATH = "./out/";
	
	// Methods
	
	void analyzePackage(String packageName);
	
	void analyzePackage(String packageName, String outputPath);

	void analyzeJar(String jarPath);

    void analyzeJar(String jarPath, String outputPath);
}
