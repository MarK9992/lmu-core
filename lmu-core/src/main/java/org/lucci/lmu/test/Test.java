package org.lucci.lmu.test;

import java.io.File;
import java.io.IOException;

import org.lucci.lmu.model.Entities;
import org.lucci.lmu.model.Entity;
import org.lucci.lmu.model.IModel;
import org.lucci.lmu.model.Model;
import org.lucci.lmu.input.JarFileAnalyser;
import org.lucci.lmu.input.ParseError;

public class Test
{
	public static void main(String... args) throws ParseError, IOException
	{
		 IModel model = new JarFileAnalyser().createModel(new File("/Users/lhogie/Downloads/fr.inria.aoste.timesquare.ccslkernel.solver_1.0.0.201403240946.jar"));
		 Entity e = Entities.findEntityByName(model, "WaitExpression");
		 System.out.println(e.getName());
		 System.out.println(e.getAttributes());
	}
}
