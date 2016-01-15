package org.lucci.lmu.input;

import java.util.HashMap;
import java.util.Map;

/*
 * Created on Oct 11, 2004
 */

/**
 * @author luc.hogie
 */
public abstract class ModelFactory {

	static private Map<String, ModelBuilder> factoryMap = new HashMap<>();

	static {
		factoryMap.put("jar", new JarFileAnalyser());
	}

	public static ModelBuilder getModelFactory(String type)
	{
		return factoryMap.get(type);
	}
}
