package org.lucci.lmu.input;

import org.lucci.lmu.model.*;
import toools.ClassContainer;
import toools.ClassPath;
import toools.io.FileUtilities;
import toools.io.file.RegularFile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

/*
 * Created on Oct 11, 2004
 */

/**
 * @author luc.hogie, Marc Karassev
 */
public class JarFileAnalyser extends Analyzer {

    @Override
    public IModel createModel(String jarPath) {
        IModel model = new Model();

        fillPrimitiveMap(model);
        try {
            File file = new File(jarPath);
            RegularFile jarFile = RegularFile.createTempFile("lmu-", ".jar");
            jarFile.setContent(FileUtilities.getFileContent(file));

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
        catch (IOException ex) {
            ex.printStackTrace();
            throw new IllegalStateException();
        }

        return model;
    }
}
