package org.lucci.lmu.input;

import org.lucci.lmu.model.IModel;

/**
 * @author Marc Karassev
 */
public interface ModelBuilder {

    // Methods

    IModel createModel(String item) throws ParseError, ModelException;
}
