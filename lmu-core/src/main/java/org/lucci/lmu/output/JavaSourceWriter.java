package org.lucci.lmu.output;

import org.lucci.lmu.model.*;

import java.util.Collection;
import java.util.Iterator;



/*
 * Created on Oct 8, 2004
 */

/**
 * @author luc.hogie
 */
class JavaSourceWriter implements Writer {

	/* (non-Javadoc)
	 * @see org.lucci.lmu.ViewFactory#createViewData(org.lucci.lmu.model.ClassDiagram)
	 */
	@Override
	public byte[] writeModel(IModel model) throws WriterException {
		String source = "";
		Collection<Entity> entities = ModelElement.findVisibleModelElements(model.getEntities());

		for (Entity entity : entities) {
			source += getJavaSource(entity) + "\n\n";
		}

		return source.getBytes();
	}

	public String getJavaSource(Entity entity)
			throws WriterException {
		String source = "";

		source += "\nclass " + entity.getName() + "\n{";
		Iterator attributeIterator = entity.getAttributes().iterator();

		while (attributeIterator.hasNext()) {
			Attribute attribute = (Attribute) attributeIterator.next();

			if (attribute.getType() == null) {
				throw new WriterException("type for attribute " + entity.getName() + "#" + attribute.getName() + " is undefined");
			} else {
				source += "\n\t" + getJavaVisibility(attribute.getVisibility())
						+ " " + getJavaType(attribute.getType().getName())
						+ " " + attribute.getName() + ";";
			}
		}

		Iterator operationIterator = entity.getOperations().iterator();

		while (operationIterator.hasNext()) {
			Operation operation = (Operation) operationIterator.next();

			if (operation.getType() == null) {
				throw new WriterException("type for operation " + entity.getName() + "#" + operation.getName() + " is undefined");
			} else {
				source += "\n\t " + getJavaVisibility(operation.getVisibility())
						+ " " + getJavaType(operation.getType().getName()) + " "
						+ operation.getName() + "(";

				Iterator<Entity> parameterIterator = operation.getParameterList().iterator();

				while (parameterIterator.hasNext()) {
					Entity parm = parameterIterator.next();
					source += parm.getName();

					if (parameterIterator.hasNext()) {
						source += ", ";
					}
				}

				source = ");";
			}
		}

		source += "\n}";
		return source;
	}

	private String getJavaVisibility(Visibility v) {
		if (v == Visibility.PUBLIC) {
			return "public";
		} else if (v == Visibility.PROTECTED) {
			return "protected";
		} else if (v == Visibility.PRIVATE) {
			return "private";
		} else {
			throw new IllegalArgumentException("unknow visilibity " + v);
		}
	}


	private String getJavaType(String t) {
		if (t.equals("string")) {
			return "String";
		} else if (t.equals("set")) {
			return "java.util.Set";
		} else if (t.equals("sequence")) {
			return "java.util.List";
		} else if (t.equals("class")) {
			return "Class";
		} else {
			return t;
		}
	}
}
