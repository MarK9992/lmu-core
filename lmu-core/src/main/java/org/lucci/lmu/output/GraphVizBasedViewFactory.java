package org.lucci.lmu.output;

import java.util.Arrays;
import java.util.Collection;

import org.lucci.lmu.model.IModel;
import org.lucci.lmu.model.Model;

import toools.extern.Proces;

/*
 * Created on Oct 3, 2004
 */

/**
 * @author luc.hogie
 */
public class GraphVizBasedViewFactory extends AbstractWriter
{
	private final String outputType;

	public GraphVizBasedViewFactory(String type)
	{
		this.outputType = type;
	}

	@Override
	public byte[] writeModel(IModel model)
		throws WriterException
	{
		DotWriter dotTextFactory = new DotWriter();
		byte[] dotText = dotTextFactory.writeModel(model);
		return Proces.exec("dot", dotText, "-T" + getOutputType());
	}

	public String getOutputType()
	{
		return outputType;
	}
}
