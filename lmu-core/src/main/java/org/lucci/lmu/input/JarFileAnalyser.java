package org.lucci.lmu.input;

import org.lucci.lmu.model.*;
import org.lucci.lmu.test.DynamicCompiler;
import toools.ClassContainer;
import toools.ClassName;
import toools.ClassPath;
import toools.Clazz;
import toools.io.FileUtilities;
import toools.io.file.RegularFile;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

/*
 * Created on Oct 11, 2004
 */

/**
 * @author luc.hogie, Marc Karassev
 */
public class JarFileAnalyser extends Analyzer
{
	private Map<Class<?>, Entity> primitiveMap = new HashMap<Class<?>, Entity>();
	private Map<Entity, Class<?>> entity_class = new HashMap<Entity, Class<?>>();

	@Override
	public IModel createModel(byte[] data) throws ParseError
	{
		IModel model = new Model();

		fillPrimitiveMap(model);
		try {

			// create a jar file on the disk from the binary data
			RegularFile jarFile = RegularFile.createTempFile("lmu-", ".jar");
			jarFile.setContent(data);

			ClassLoader classLoader = new URLClassLoader(new URL[] { jarFile.toURL() });

			ClassPath classContainers = new ClassPath();
			
			classContainers.add(new ClassContainer(jarFile, classLoader));

			// take all the classes in the jar files and convert them to LMU
			// Entities
			for (Class<?> thisClass : classContainers.listAllClasses())
			{
				// if this is not an anonymous inner class (a.b$1)
				// we take it into account
				if (!thisClass.getName().matches(".+\\$[0-9]+"))
				{
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
			jarFile.delete();
		}
		catch (IOException ex)
		{
			throw new IllegalStateException();
		}

		return model;
	}

	private void fillModel(IModel model)
	{
		for (Entity entity : new HashSet<Entity>(model.getEntities()))
		{
			if (!entity.isPrimitive())
			{
				Class<?> clazz = entity_class.get(entity);
				initInheritance(clazz, entity, model);
				initAttributes(clazz, entity, model);
				initOperations(clazz, entity, model);
			}
		}
	}

	private void initAttributes(Class<?> clazz, Entity entity, IModel model)
	{
		for (Field field : clazz.getDeclaredFields())
		{
			// if the field is not static
			if ((field.getModifiers() & Modifier.STATIC) == 0)
			{
				// System.err.println(clazz.getName() + " " + field.getName());
				Type fieldType = field.getGenericType();

				if (fieldType instanceof ParameterizedType)
				{
					for (Type parameterType : ((ParameterizedType) fieldType).getActualTypeArguments())
					{
						if (parameterType instanceof Class<?>)
						{
							Class<?> parameterClass = (Class<?>) parameterType;
							Entity parameterEntity = getEntity(model, parameterClass);

							if (!parameterEntity.isPrimitive())
							{
								AssociationRelation rel = new AssociationRelation(parameterEntity, entity);
								rel.setType(AssociationRelation.TYPE.AGGREGATION);
								//
								// if
								// (!field.getName().equalsIgnoreCase(parameterEntity.getName()
								// + 's'))
								// {
								// rel.setLabel(field.getName());
								// }
								//
								rel.setLabel(field.getName());
								rel.setCardinality("0..n");
								model.addRelation(rel);
							}
						}
					}
				}
				else
				{
					Entity fieldTypeEntity = getEntity(model, field.getType());

					if (fieldTypeEntity != null)
					{
						if (fieldTypeEntity.isPrimitive())
						{
							Attribute att = new Attribute();
							att.setName(field.getName());
							att.setVisibility(getVisibility(field));
							att.setType(fieldTypeEntity);
							entity.getAttributes().add(att);
						}
						else
						{
							AssociationRelation rel = new AssociationRelation(fieldTypeEntity, entity);
							rel.setType(AssociationRelation.TYPE.AGGREGATION);

							// if (fieldTypeEntity.getName().contains("$"))
							// System.out.println("inner class: " +
							// fieldTypeEntity.getName());

							if (fieldTypeEntity.getName().toUpperCase().indexOf(field.getName().toUpperCase()) < 0)
							{
								rel.setLabel(field.getName());
							}

							rel.setCardinality("1");
							model.addRelation(rel);
						}
					}
				}
			}
		}
	}

	private void initOperations(Class<?> clazz, Entity entity, IModel model)
	{
		try
		{
			for (Method method : clazz.getDeclaredMethods())
			{
				Entity typeEntity = getEntity(model, method.getReturnType());

				if (typeEntity != null)
				{
					Operation op = new Operation();
					op.setClassStatic((method.getModifiers() & Modifier.STATIC) != 0);
					op.setName(method.getName());
					op.setVisibility(getVisibility(method));
					op.setType(typeEntity);

					Class<?>[] parms = method.getParameterTypes();

					for (int j = 0; j < parms.length; ++j)
					{
						Entity parmEntity = getEntity(model, parms[j]);

						if (parmEntity == null)
						{
							return;
						}
						else
						{
							op.getParameterList().add(parmEntity);
						}

					}

					entity.getOperations().add(op);

					// for (Class<?> exceptionClass :
					// method.getExceptionTypes())
					// {
					// Entity exceptionEntity = Entities.findEntity(model,
					// exceptionClass.getName());
					//
					// if (exceptionEntity == null)
					// {
					// exceptionEntity = new Entity();
					// exceptionEntity.setName(exceptionClass.getName());
					// model.getEntities().add(exceptionEntity);
					// }
					//
					// AssociationRelation relation = new
					// AssociationRelation(entity, exceptionEntity);
					// relation.setLabel("throws");
					// model.getRelations().add(relation);
					// }

				}
			}
		}
		catch (NoClassDefFoundError ex)
		{
			// ex.printStackTrace();

		}
	}

	private Visibility getVisibility(Member m)
	{
		if ((m.getModifiers() & Modifier.PUBLIC) != 0)
		{
			return Visibility.PUBLIC;
		}
		else if ((m.getModifiers() & Modifier.PROTECTED) != 0)
		{
			return Visibility.PROTECTED;
		}
		else if ((m.getModifiers() & Modifier.PRIVATE) != 0)
		{
			return Visibility.PRIVATE;
		}
		else
		{
			return Visibility.PRIVATE;
		}
	}

	public IModel createModel(File file) throws ParseError, IOException
	{
		byte[] data = FileUtilities.getFileContent(file);
		return createModel(data);
	}
}
