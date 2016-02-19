package org.lucci.lmu.input;

/**
 * @author Marc Karassev
 */
public abstract class Analyzer {

    public static String deleteUnauthorizedToken(String str) {
        String[] dependenciesPath = str.split("/");  //  Retrieve only file name
        String nameOfFileDependency = dependenciesPath[dependenciesPath.length - 1];

        //  Delete unauthorized token (for dotWriter)
        nameOfFileDependency = nameOfFileDependency.replace(".", "_");
        nameOfFileDependency = nameOfFileDependency.replace("-", "_");

        return  nameOfFileDependency;
    }
}
