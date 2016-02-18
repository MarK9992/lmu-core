package org.lucci.lmu.input;

import org.lucci.lmu.model.IModel;

import java.io.IOException;

/**
 * @author Marc Karassev
 */
public interface JarAnalyzer {

    IModel createModelFromJar(String jarPath) throws IOException;
}
