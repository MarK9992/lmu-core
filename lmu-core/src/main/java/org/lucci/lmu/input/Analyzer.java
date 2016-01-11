package org.lucci.lmu.input;

import org.lucci.lmu.model.Entities;
import org.lucci.lmu.model.Entity;
import org.lucci.lmu.model.IModel;
import org.lucci.lmu.model.InheritanceRelation;
import toools.io.file.RegularFile;

import java.util.*;

/**
 * @author Marc Karassev
 */
public abstract class Analyzer extends ModelBuilder {

    // Attributes

    protected Map<Class<?>, Entity> primitiveMap = new HashMap<Class<?>, Entity>();
    protected Map<Entity, Class<?>> entity_class = new HashMap<Entity, Class<?>>();

    // Methods

    protected void fillPrimitiveMap(IModel model) {
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

    protected void initInheritance(Class<?> clazz, Entity entity, IModel model) {
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
            Entity superentity = getEntity(model, c);

            // if the superentity exists in the model
            if (superentity != null) {
                // define the corresponding relation
                model.addRelation(new InheritanceRelation(entity, superentity));
            }
        }
    }

    protected Entity getEntity(IModel model, Class<?> c) {
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
}
