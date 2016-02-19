package org.lucci.lmu.input;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lucci.lmu.model.*;

import java.lang.reflect.*;
import java.util.*;

/**
 * @author Marc Karassev
 */
public class ClassesAnalyzer extends Analyzer {

    // Constants

    public static final Logger LOGGER = LogManager.getLogger();

    // Attributes

    protected Map<Class<?>, Entity> primitiveMap = new HashMap<>();
    protected Map<Entity, Class<?>> entity_class = new HashMap<>();
    protected List<Class<?>> classes;

    // Methods

    protected IModel createModel() {
        fillPrimitiveMap();
        // take all the classes in the jar files and convert them to LMU
        // Entities
        LOGGER.debug("iterating on classes");
        for (Class<?> thisClass: classes) {
            LOGGER.debug(thisClass);
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
        LOGGER.debug("end of class iteration");

        // at this only the name of entities is known
        // neither members nor relation are known
        // let's find them
        fillModel();

        return model;
    }

    public IModel createModelFromClasses(List<Class<?>> classes) {
        this.classes = classes;
        return createModel();
    }

    protected void fillPrimitiveMap() {
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
    }

    public String computeEntityName(Class<?> c) {
        return c.getName().substring(c.getName().lastIndexOf('.') + 1);
    }

    public String computeEntityNamespace(Class<?> c) {
        return c.getPackage() == null ? Entity.DEFAULT_NAMESPACE : c.getPackage().getName();
    }

    protected void initInheritance(Class<?> clazz, Entity entity) {
        // this collection will store the super class and super interfaces for
        // the given class
        Set<Class<?>> supers = new HashSet<Class<?>>();

        // first get the superclass, if any
        if (clazz.getSuperclass() != null && clazz.getSuperclass() != Object.class
                && clazz.getSuperclass() != Enum.class) {
            supers.add(clazz.getSuperclass());
        }

        // then find all super interfaces
        supers.addAll(Arrays.asList(clazz.getInterfaces()));

        for (Class<?> c : supers) {
            Entity superentity = getEntity(c);

            // if the superentity exists in the model
            if (superentity != null) {
                // define the corresponding relation
                model.addRelation(new InheritanceRelation(entity, superentity));
            }
        }
    }

    protected Entity getEntity(Class<?> c) {
        Entity e = (Entity) primitiveMap.get(c);

        if (e == null) {
            e = Entities.findEntityByName(model, computeEntityName(c));

            if (e == null && c != Object.class && Entities.isValidEntityName(computeEntityName(c))) {
                e = new Entity();
                e.setPrimitive(true);
                e.setName(computeEntityName(c));
                model.addEntity(e);
            }
        }

        return e;
    }

    protected void initAttributes(Class<?> clazz, Entity entity) {
        for (Field field : clazz.getDeclaredFields()) {
            // if the field is not static
            if ((field.getModifiers() & Modifier.STATIC) == 0) {
                // System.err.println(clazz.getName() + " " + field.getName());
                Type fieldType = field.getGenericType();

                if (fieldType instanceof ParameterizedType) {
                    for (Type parameterType : ((ParameterizedType) fieldType).getActualTypeArguments()) {
                        if (parameterType instanceof Class<?>) {
                            Class<?> parameterClass = (Class<?>) parameterType;
                            Entity parameterEntity = getEntity(parameterClass);

                            if (!parameterEntity.isPrimitive()) {
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
                } else {
                    Entity fieldTypeEntity = getEntity(field.getType());

                    if (fieldTypeEntity != null) {
                        if (fieldTypeEntity.isPrimitive()) {
                            Attribute att = new Attribute();
                            att.setName(field.getName());
                            att.setVisibility(getVisibility(field));
                            att.setType(fieldTypeEntity);
                            entity.getAttributes().add(att);
                        } else {
                            AssociationRelation rel = new AssociationRelation(fieldTypeEntity, entity);
                            rel.setType(AssociationRelation.TYPE.AGGREGATION);

                            // if (fieldTypeEntity.getName().contains("$"))
                            // System.out.println("inner class: " +
                            // fieldTypeEntity.getName());

                            if (fieldTypeEntity.getName().toUpperCase().indexOf(field.getName().toUpperCase()) < 0) {
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

    protected void initOperations(Class<?> clazz, Entity entity) {
        try {
            for (Method method : clazz.getDeclaredMethods()) {
                Entity typeEntity = getEntity(method.getReturnType());

                if (typeEntity != null) {
                    Operation op = new Operation();
                    op.setClassStatic((method.getModifiers() & Modifier.STATIC) != 0);
                    op.setName(method.getName());
                    op.setVisibility(getVisibility(method));
                    op.setType(typeEntity);

                    Class<?>[] parms = method.getParameterTypes();

                    for (int j = 0; j < parms.length; ++j) {
                        Entity parmEntity = getEntity(parms[j]);

                        if (parmEntity == null) {
                            return;
                        } else {
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
        } catch (NoClassDefFoundError ex) {
            // ex.printStackTrace();

        }
    }

    protected Visibility getVisibility(Member m) {
        if ((m.getModifiers() & Modifier.PUBLIC) != 0) {
            return Visibility.PUBLIC;
        } else if ((m.getModifiers() & Modifier.PROTECTED) != 0) {
            return Visibility.PROTECTED;
        } else if ((m.getModifiers() & Modifier.PRIVATE) != 0) {
            return Visibility.PRIVATE;
        } else {
            return Visibility.PRIVATE;
        }
    }

    protected void fillModel() {
        for (Entity entity : new HashSet<Entity>(model.getEntities())) {
            if (!entity.isPrimitive()) {
                Class<?> clazz = entity_class.get(entity);
                LOGGER.debug(clazz);
                initInheritance(clazz, entity);
                initAttributes(clazz, entity);
                initOperations(clazz, entity);
            }
        }
    }
}
