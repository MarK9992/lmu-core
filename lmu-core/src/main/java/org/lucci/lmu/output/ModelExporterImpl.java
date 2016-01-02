package org.lucci.lmu.output;

import java.io.File;
import java.io.IOException;

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

	@Override
	public void exportToFile(Model model, String filePath) {
		RegularFile out = new RegularFile(filePath);
		String fileExtension = FileUtilities.getFileNameExtension(filePath);
		AbstractWriter factory = AbstractWriter.getTextFactory(fileExtension);

		if (factory == null) {
			System.err.println("Fatal error: Do not know how to generate '" + fileExtension + "' code");
		}
		else {
			try {
				byte[] outputBytes = factory.writeModel(model);
				
				System.out.println("Writing file " + filePath);
				out.setContent(outputBytes);
			}
			catch (WriterException ex) {
				System.err.println(ex.getMessage());
			}
			catch (IOException ex) {
				System.err.println("I/O error while writing file " + filePath);
			}
		}
	}
}
