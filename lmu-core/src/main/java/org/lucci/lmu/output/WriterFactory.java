package org.lucci.lmu.output;

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
    
	static Map<String, Writer> factoryMap = new HashMap<String, Writer>();
	
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

	public static Writer getTextFactory(String type)
	{
	    Writer f = factoryMap.get(type);
	    
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
