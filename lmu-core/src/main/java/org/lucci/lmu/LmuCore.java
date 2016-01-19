package org.lucci.lmu;

import java.io.IOException;
import java.util.Map;

/**
 * @author Benjamin Benni, Marc Karassev
 * 
 * This interface represents the app's API</br>
 * It allows you to analyze Jar file, or classes found in a given path
 */
public interface LmuCore {
	
	// Constants
	
	String DEFAULT_OUTPUT_PATH = "./out/";
	
	// Methods

	/**
	 * This method allows you to analyze all classes found in given path<br/>
	 * It will generates the file into outputPath folder with given outputFormat.
	 * @param pathsAndPackages
	 * 		The absolute path of classes to analyze
	 * @param outputPath
	 * 		The path where to produce the file
	 * @param outputFormat
	 * 		The format of the generated file<br/>
	 * 		It can be : 'pdf', 'jpg', 'png', 'fig', 'svg', 'lmu', 'ps' or 'dot'
	 * @throws IOException
	 */
	void analyzePaths(Map<String, String> pathsAndPackages, String outputPath, String outputFormat) throws IOException;

	/**
	 * This method allows you to analyze all classes contained in a jar file<br/>
	 * It will generates the file into outputPath folder with given outputFormat<br/>
	 * Be aware that given jar files must embed all its external dependencies.
	 * @param jarPath
	 * 		The absolute path of jar file to analyze
	 * @param outputPath
	 * 		The path where to produce the file
	 * @param outputFormat
	 * 		The format of the generated file<br/>
	 * 		It can be : 'pdf', 'jpg', 'png', 'fig', 'svg', 'lmu', 'ps' or 'dot'
	 */
    void analyzeJar(String jarPath, String outputPath, String outputFormat);
}
