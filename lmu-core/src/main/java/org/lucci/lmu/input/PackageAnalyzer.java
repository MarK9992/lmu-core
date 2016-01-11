package org.lucci.lmu.input;

import org.lucci.lmu.model.*;
import java.util.*;

/**
 * @author Benjamin Benni, Marc Karassev
 */
public class PackageAnalyzer extends Analyzer {

	@Override
	public IModel createModel(byte[] data) throws ParseError {
		IModel model = new Model();

		List<Class<?>> classes = ClassFinder.find("org.lucci.lmu.input");

		fillPrimitiveMap(model);
		// take all the classes in the jar files and convert them to LMU
		// Entities
		for (Class<?> thisClass : classes) {
			// if this is not an anonymous inner class (a.b$1)
			// we take it into account
			if (!thisClass.getName().matches(".+\\$[0-9]+")) {
				Entity entity = new Entity();
				entity.setName(computeEntityName(thisClass));
				entity.setNamespace(computeEntityNamespace(thisClass));
				entity_class.put(entity, thisClass);
				model.addEntity(entity);
			}
		}

		// at this only the name of entities is known
		// neither members nor relation are known
		// let's find them
		fillModel(model);

		return model;
	}
}
