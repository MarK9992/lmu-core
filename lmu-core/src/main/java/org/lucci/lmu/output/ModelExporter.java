package org.lucci.lmu.output;

import org.lucci.lmu.model.IModel;

/**
 * @author Marc Karassev
 *         <p>
 *         API for the org.lucci.lmu.output package.
 *         Specifies methods for exporting models to files.
 */
public interface ModelExporter {

	/**
	 * Exports a given LMU model to a file at the specified path.
	 *
	 * @param model    the model to export
	 * @param filePath the exported file location
	 * @param format   the ouput format to use
	 */
	void exportToFile(IModel model, String filePath, String format);
}
