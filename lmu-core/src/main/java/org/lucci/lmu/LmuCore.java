package org.lucci.lmu;

import java.util.List;

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
	 * This method allows you to analyze all given classes<br/>
	 * It will generates the file into outputPath folder with given outputFormat.
	 * @param classes
	 * 		The absolute path of classes to analyze
	 * @param outputPath
	 * 		The path where to produce the file
	 * @param outputFormat
	 * 		The format of the generated file<br/>
	 * 		It can be : 'pdf', 'jpg', 'png', 'fig', 'svg', 'lmu', 'ps' or 'dot'
	 */
	void analyzeClasses(List<Class<?>> classes, String outputPath, String outputFormat);

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
