package org.lucci.lmu.test;

import org.lucci.lmu.input.JarFileAnalyser;
import org.lucci.lmu.input.ParseError;
import org.lucci.lmu.model.Entities;
import org.lucci.lmu.model.Entity;
import org.lucci.lmu.model.IModel;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Test {
	public static void main(String... args) throws ParseError, IOException {

		URL url = Thread.currentThread().getContextClassLoader().getResource("sample-org.jar");

		IModel model = new JarFileAnalyser().createModel(url.getPath());
		Entity e = Entities.findEntityByName(model, "LmuException");

		System.out.println(e.getName());
		System.out.println(e.getAttributes());
	}
}
