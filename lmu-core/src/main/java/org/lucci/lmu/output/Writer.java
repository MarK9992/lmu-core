package org.lucci.lmu.output;

import org.lucci.lmu.model.IModel;

/**
 * @author Marc Karassev
 */
public interface Writer {

    public abstract byte[] writeModel(IModel diagram) throws WriterException;
}
