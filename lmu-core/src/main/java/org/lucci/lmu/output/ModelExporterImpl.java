package org.lucci.lmu.output;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lucci.lmu.model.IModel;
import toools.io.file.RegularFile;

import java.io.IOException;

/**
 * 
 * @author Marc Karassev
 * 
 * Implementation of the {@link ModelExporter} interface.
 *
 */
public class ModelExporterImpl implements ModelExporter {
	
	// Constants
	
	private final static Logger LOGGER = LogManager.getLogger();
	
	// Methods

	@Override
	public void exportToFile(IModel model, String filePath, String format) {
		RegularFile out = new RegularFile(filePath + "." + format);
		WriterFactory factory = WriterFactory.getTextFactory(format);

        LOGGER.debug(filePath);
        LOGGER.debug(format);
		if (factory == null) {
			LOGGER.error("Fatal error: Do not know how to generate '" + format + "' code");
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
