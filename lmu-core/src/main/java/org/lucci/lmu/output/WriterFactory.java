package org.lucci.lmu.output;

import org.lucci.lmu.model.IModel;

import java.util.HashMap;
import java.util.Map;

/*
 * Created on Oct 3, 2004
 */

/**
 * @author luc.hogie
 */
public abstract class WriterFactory
{
	public abstract byte[] writeModel(IModel diagram)
		throws WriterException;
    
	static Map<String, WriterFactory> factoryMap = new HashMap<String, WriterFactory>();
	
	static
	{
        factoryMap.put(null, new LmuWriter());
		factoryMap.put("lmu", new LmuWriter());
		factoryMap.put("dot", new DotWriter());
		factoryMap.put("java", new JavaSourceWriter());
		factoryMap.put("ps", new GraphVizBasedViewFactory("ps"));
		factoryMap.put("png", new GraphVizBasedViewFactory("png"));
		factoryMap.put("fig", new GraphVizBasedViewFactory("fig"));
		factoryMap.put("svg", new GraphVizBasedViewFactory("svg"));
	}

	public static WriterFactory getTextFactory(String type)
	{
	    WriterFactory f = factoryMap.get(type);
	    
	    if (f == null)
	    {
            return new GraphVizBasedViewFactory(type);
	    }
	    else
	    {
	        return f;
	    }
	}
}