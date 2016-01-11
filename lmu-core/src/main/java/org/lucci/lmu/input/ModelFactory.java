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

	static private Map<String, Analyzer> factoryMap = new HashMap<>();

	static
	{
		factoryMap.put(null, LmuParser.getParser());
		factoryMap.put("lmu", LmuParser.getParser());
		factoryMap.put("jar", new JarFileAnalyser());
		factoryMap.put("pckg", new PackageAnalyzer());
		factoryMap.put("classes", null);    // TODO: 23/12/15 implements ClassesAnalyzer

	}

	public static Analyzer getModelFactory(String type)
	{
		return factoryMap.get(type);
	}
}
