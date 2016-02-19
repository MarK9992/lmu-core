package org.lucci.lmu.input;

import org.lucci.lmu.model.IModel;
import org.lucci.lmu.model.Model;

/**
 * @author Marc Karassev
 */
public abstract class Analyzer {

    // Attributes

    protected IModel model = new Model();

    // Methods

    public static String deleteUnauthorizedToken(String str) {
        String[] dependenciesPath = str.split("/");  //  Retrieve only file name
        String nameOfFileDependency = dependenciesPath[dependenciesPath.length - 1];

        //  Delete unauthorized token (for dotWriter)
        nameOfFileDependency = nameOfFileDependency.replace(".", "_");
        nameOfFileDependency = nameOfFileDependency.replace("-", "_");
        nameOfFileDependency = nameOfFileDependency.replace("\"", "'");

        return  nameOfFileDependency;
    }

    // Getters and setters

    public IModel getModel() {
        return model;
    }

    public void setModel(IModel model) {
        this.model = model;
    }
}
