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
 * @author luc.hogie
 */
public class JarFileAnalyser extends ModelFactory
{
	private Map<Class<?>, Entity> primitiveMap = new HashMap<Class<?>, Entity>();
	private Map<Entity, Class<?>> entity_class = new HashMap<Entity, Class<?>>();

	@Override
	public IModel createModel(byte[] data) throws ParseError
	{
		IModel model = new Model();
		primitiveMap.put(void.class, Entities.findEntityByName(model, "void"));
		primitiveMap.put(int.class, Entities.findEntityByName(model, "int"));
		primitiveMap.put(long.class, Entities.findEntityByName(model, "long"));
		primitiveMap.put(char.class, Entities.findEntityByName(model, "char"));
		primitiveMap.put(float.class, Entities.findEntityByName(model, "float"));
		primitiveMap.put(double.class, Entities.findEntityByName(model, "double"));
		primitiveMap.put(String.class, Entities.findEntityByName(model, "string"));
		primitiveMap.put(Class.class, Entities.findEntityByName(model, "class"));
		primitiveMap.put(boolean.class, Entities.findEntityByName(model, "boolean"));
		primitiveMap.put(Collection.class, Entities.findEntityByName(model, "set"));
		primitiveMap.put(List.class, Entities.findEntityByName(model, "sequence"));
		primitiveMap.put(Map.class, Entities.findEntityByName(model, "map"));
		primitiveMap.put(Object.class, Entities.findEntityByName(model, "object"));
		primitiveMap.put(java.util.Date.class, Entities.findEntityByName(model, "date"));
		primitiveMap.put(java.sql.Date.class, Entities.findEntityByName(model, "date"));

		try
		{

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

	protected static Class<?> createClassNamed(String fullName)
	{
		ClassName cn = Clazz.getClassName(fullName);
		String src = "";

		if (cn.pkg != null)
		{
			src += "package " + cn.pkg + ";";
		}

		src += "public class " + cn.name + " {}";

		// System.out.println(src);
		return DynamicCompiler.compile(fullName, src);
	}

	/*
	 * public static void main(String[] args) {
	 * System.out.println(createClassNamed("lucci.Coucou"));
	 * System.out.println(createClassNamed("Coucou")); }
	 */

	public String computeEntityName(Class<?> c)
	{
		return c.getName().substring(c.getName().lastIndexOf('.') + 1);
	}

	public String computeEntityNamespace(Class<?> c)
	{
		return c.getPackage() == null ? Entity.DEFAULT_NAMESPACE : c.getPackage().getName();
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

	private void initInheritance(Class<?> clazz, Entity entity, IModel model)
	{
		// this collection will store the super class and super interfaces for
		// the given class
		Set<Class<?>> supers = new HashSet<Class<?>>();

		// first get the superclass, if any
		if (clazz.getSuperclass() != null && clazz.getSuperclass() != Object.class && clazz.getSuperclass() != Enum.class)
		{
			supers.add(clazz.getSuperclass());
		}

		// then find all super interfaces
		supers.addAll(Arrays.asList(clazz.getInterfaces()));

		for (Class<?> c : supers)
		{
			Entity superentity = getEntity(model, c);

			// if the superentity exists in the model
			if (superentity != null)
			{
				// define the corresponding relation
				model.addRelation(new InheritanceRelation(entity, superentity));
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

	private Entity getEntity(IModel model, Class<?> c)
	{
		Entity e = (Entity) primitiveMap.get(c);

		if (e == null)
		{
			e = Entities.findEntityByName(model, computeEntityName(c));

			if (e == null && c != Object.class && Entities.isValidEntityName(computeEntityName(c)))
			{
				e = new Entity();
				e.setPrimitive(true);
				e.setName(computeEntityName(c));
				model.addEntity(e);
			}
		}

		return e;
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
