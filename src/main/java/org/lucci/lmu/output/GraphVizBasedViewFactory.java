package org.lucci.lmu.output;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lucci.lmu.model.IModel;

import org.lucci.lmu.output.dot.DotWriter;
import toools.extern.Proces;

/*
 * Created on Oct 3, 2004
 */

/**
 * @author luc.hogie, Marc Karassev
 */
public class GraphVizBasedViewFactory implements Writer {

    // Constants

    private static final Logger LOGGER = LogManager.getLogger();

    // Attributes

	private final String OUTPUT_TYPE;

    // Constructors

	public GraphVizBasedViewFactory(String type)
	{
		this.OUTPUT_TYPE = type;
	}

    // Methods

	@Override
	public byte[] writeModel(IModel model) throws WriterException {
		DotWriter dotTextFactory = new DotWriter();
		byte[] dotText = dotTextFactory.writeModel(model);

        LOGGER.debug(OUTPUT_TYPE);
		return Proces.exec("dot", dotText, "-T" + getOutputType());
	}

	public String getOutputType()
	{
		return OUTPUT_TYPE;
	}
}
