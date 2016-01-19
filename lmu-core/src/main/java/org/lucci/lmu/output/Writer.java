package org.lucci.lmu.output;

import org.lucci.lmu.model.IModel;

/**
 * This interface allows you to generate a model description into a file
 * depending on the Writer implementation chosen
 *
 * @author Marc Karassev, Benjamin Benni
 */
interface Writer {

	/**
	 * This method takes a IModel implementation and produces bytes array<br/>
	 * See ModelExporter to find out how to use this bytes array
	 *
	 * @param diagram The IModel implementation, representing a state of the model
	 * @return A bytes array representing the model in the specified Writer implementation
	 * @throws WriterException
	 */
	byte[] writeModel(IModel diagram) throws WriterException;
}
