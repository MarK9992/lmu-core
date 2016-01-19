package org.lucci.lmu.input;

import java.util.HashMap;
import java.util.Map;

/*
 * Created on Oct 11, 2004
 */

/**
 * @author luc.hogie, Marc Karassev
 */
public abstract class ModelFactory {

	static private Map<String, ClassesAnalyzer> factoryMap = new HashMap<>();

	static {
		factoryMap.put(null, new ClassesAnalyzer());
		factoryMap.put("jar", new JarFileAnalyser());
	}

	public static ClassesAnalyzer getModelFactory(String type) {
		return factoryMap.get(type);
	}
}
