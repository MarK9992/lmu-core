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
public class JarFileAnalyser extends ClassesAnalyzer implements JarAnalyzer {

    @Override
    public IModel createModelFromJar(String jarPath) throws IOException {
        URL url = new URL("file://" + jarPath);
        File file = new File(url.getPath());
        RegularFile jarFile = RegularFile.createTempFile("lmu-", ".jar");
        ClassLoader classLoader = new URLClassLoader(new URL[] { jarFile.toURL() });
        ClassPath classContainers = new ClassPath();

        jarFile.setContent(FileUtilities.getFileContent(file));
        classContainers.add(new ClassContainer(jarFile, classLoader));
        this.classes = classContainers.listAllClasses();
        jarFile.delete();

        return createModel();
    }
}
