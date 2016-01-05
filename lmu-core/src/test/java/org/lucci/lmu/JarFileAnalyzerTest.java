package org.lucci.lmu;

import org.junit.Test;
import org.lucci.lmu.input.JarFileAnalyser;
import org.lucci.lmu.input.ParseError;
import org.lucci.lmu.model.Entities;
import org.lucci.lmu.model.Entity;
import org.lucci.lmu.model.IModel;
import org.lucci.lmu.output.AbstractWriter;
import org.lucci.lmu.output.DotWriter;
import org.lucci.lmu.output.WriterException;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Benjamin on 05/01/2016.
 */
public class JarFileAnalyzerTest {

	@Test
	public void highLevelTest() throws IOException, ParseError {
		URL url = Thread.currentThread().getContextClassLoader().getResource("sample-org.jar");
		File file = new File(url.getPath());

		IModel model = new JarFileAnalyser().createModel(file);
		Entity e = Entities.findEntityByName(model, "LmuException");

		assertEquals("LmuException", e.getName());
	}

	@Test
	public void endToEndTest() throws IOException, ParseError, WriterException {
		URL url = Thread.currentThread().getContextClassLoader().getResource("sample-org.jar");
		File file = new File(url.getPath());

		IModel model = new JarFileAnalyser().createModel(file);

		DotWriter dotTextFactory = new DotWriter();
		byte[] dotText = dotTextFactory.writeModel(model);

		assertNotNull(dotText);
	}
}
