package org.lucci.lmu.input;

import java.util.HashMap;
import java.util.Map;

import org.lucci.lmu.model.IModel;

/*
 * Created on Oct 11, 2004
 */

/**
 * @author luc.hogie
 */
public abstract class ModelFactory
{

	static private Map<String, ModelFactory> factoryMap = new HashMap<String, ModelFactory>();

	static
	{
		factoryMap.put(null, LmuParser.getParser());
		factoryMap.put("lmu", LmuParser.getParser());
		factoryMap.put("jar", new JarFileAnalyser());
		factoryMap.put("pckg", new PackageAnalyzer());
		factoryMap.put("classes", null);    // TODO: 23/12/15 implements ClassesAnalyzer

	}

	public static ModelFactory getModelFactory(String type)
	{
		return factoryMap.get(type);
	}

	public abstract IModel createModel(byte[] data) throws ParseError, ModelException;
}
