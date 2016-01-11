package org.lucci.lmu.input;

import org.lucci.lmu.model.Entity;
import toools.io.file.RegularFile;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * @author Marc Karassev
 */
public abstract class Analyzer extends ModelBuilder {

    // Attributes

    protected Collection<RegularFile> knownJarFiles = new HashSet<RegularFile>();
    protected Map<Class<?>, Entity> primitiveMap = new HashMap<Class<?>, Entity>();
    protected Map<Entity, Class<?>> entity_class = new HashMap<Entity, Class<?>>();
}
