package org.lucci.lmu;

/**
 * @author Benjamin Benni, Marc Karassev
 * 
 * This interface represents the app's API.
 */
public interface LmuCore {
	
	// Constants
	
	String DEFAULT_OUTPUT_PATH = "./out";
	
	// Methods
	
	void analyzePackage(String packageName);
	
	void analyzePackageAndExport(String packageName, String outputPath);
}
