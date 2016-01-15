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

	static
	{
		factoryMap.put(null, LmuParser.getParser());
		factoryMap.put("lmu", LmuParser.getParser());
		factoryMap.put("jar", new JarFileAnalyser());
		factoryMap.put("classes", null);    // TODO: 23/12/15 implements ClassesAnalyzer

	}

	public static ModelBuilder getModelFactory(String type)
	{
		return factoryMap.get(type);
	}
}
