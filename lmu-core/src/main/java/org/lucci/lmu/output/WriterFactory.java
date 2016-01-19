package org.lucci.lmu.output;

import java.util.HashMap;
import java.util.Map;

/*
 * Created on Oct 3, 2004
 */

/**
 * This factory allows you to retrieve a specific 'writer' from a given string description<br/>
 * It allows you to generates specific output file.
 *
 * @author luc.hogie, Benjamin Benni and Marc Karassev
 */
abstract class WriterFactory {

	static Map<String, Writer> factoryMap = new HashMap<String, Writer>();

	static {
		factoryMap.put(null, new LmuWriter());
		factoryMap.put("lmu", new LmuWriter());
		factoryMap.put("dot", new DotWriter());
		factoryMap.put("java", new JavaSourceWriter());
		factoryMap.put("ps", new GraphVizBasedViewFactory("ps"));
		factoryMap.put("png", new GraphVizBasedViewFactory("png"));
		factoryMap.put("fig", new GraphVizBasedViewFactory("fig"));
		factoryMap.put("svg", new GraphVizBasedViewFactory("svg"));
	}

	/**
	 * This method will return a specific 'Writer' implementation from a S	tring description
	 *
	 * @param type The type of writer you want
	 *             - Must be 'null', 'lmu', 'dot', 'java','ps', 'png', 'fig' or 'svg'
	 * @return The specific writer implementation from the given String description<br/>
	 * If the given String is not contained in the list above, it will return
	 * a default GraphizBaseViewFactory writer implementation
	 */
	public static Writer getTextFactory(String type) {
		//	Retrieve writer implement from given string
		Writer f = factoryMap.get(type);

		//	If not found, return default implementation
		if (f == null) {
			return new GraphVizBasedViewFactory(type);
		} else {
			return f;
		}
	}
}
