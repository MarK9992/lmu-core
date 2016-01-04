package org.lucci.lmu.output;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lucci.lmu.model.IModel;
import org.lucci.lmu.model.Model;

import toools.io.FileUtilities;
import toools.io.file.RegularFile;

/**
 * 
 * @author Marc Karassev
 * 
 * Implementation of the {@link ModelExporter} interface.
 *
 */
public class ModelExporterImpl implements ModelExporter {
	
	// Constants
	
	private final static Logger LOGGER = LogManager.getLogger("ModelExporter");
	
	// Methods

	@Override
	public void exportToFile(IModel model, String filePath) {
		RegularFile out = new RegularFile(filePath);
		String fileExtension = FileUtilities.getFileNameExtension(filePath);
		AbstractWriter factory = AbstractWriter.getTextFactory(fileExtension);

		if (factory == null) {
			LOGGER.error("Fatal error: Do not know how to generate '" + fileExtension + "' code");
		}
		else {
			try {
				byte[] outputBytes = factory.writeModel(model);
				
				LOGGER.info("Writing file " + filePath);
				out.setContent(outputBytes);
			}
			catch (WriterException ex) {
				LOGGER.error(ex.getMessage());
			}
			catch (IOException ex) {
				LOGGER.error("I/O error while writing file " + filePath);
			}
		}
	}
}
