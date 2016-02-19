package org.lucci.lmu.input;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lucci.lmu.LmuCore;
import org.lucci.lmu.model.*;
import org.lucci.lmu.output.ModelExporterImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;

public class ManifestAnalyzer implements JarAnalyzer {

    // Constants

    private static final Logger LOGGER = LogManager.getLogger();
    private static final List<Attributes.Name> targetKeys = Collections.singletonList(new Attributes.Name("Bundle-ClassPath"));

    // Methods

    @Override
    public IModel createModelFromJar(String jarPath) throws IOException {
        File jarFile = new File(jarPath);
        JarInputStream jis = new JarInputStream(new FileInputStream(jarFile));
        Manifest manifest = jis.getManifest();
        Attributes mainAttribs = manifest.getMainAttributes();
        IModel model = new Model();
        ArrayList<String> dependencies = new ArrayList<>();
        Entity root = new DeploymentUnit(), entity;
        String rootName = deleteUnauthorizedToken(jarFile.getName());

        for(Attributes.Name currentTargetKey : targetKeys) {
            if(mainAttribs.get(currentTargetKey) != null) {
                dependencies.addAll(Arrays.asList(((String) mainAttribs.get(currentTargetKey)).split(",")));
            }
        }

        LOGGER.debug("iterating on dependencies and adding entities to model");
        root.setName(rootName);
        root.setNamespace(rootName);
        model.addEntity(root);
        for (String dependency: dependencies) {
            LOGGER.debug("dependency: " + dependency);
            if (!dependency.equals(".")) {
                dependency = deleteUnauthorizedToken(dependency);
                entity = new DeploymentUnit();
                entity.setName(dependency);
                entity.setNamespace(dependency);
                LOGGER.debug("Creating new entity : " + entity.getName());
                model.addEntity(entity);
            }
        }
        for (Entity e: model.getEntities()) {
            if (!e.getName().equals(rootName)) {
                model.addRelation(new DependencyRelation(root, e));
            }
        }
        LOGGER.debug("end of dependencies iteration");
        jis.close();
        return model;
    }

    static String deleteUnauthorizedToken(String str) {
        String[] dependenciesPath = str.split("/");  //  Retrieve only file name
        String nameOfFileDependency = dependenciesPath[dependenciesPath.length - 1];

        //  Delete unauthorized token (for dotWriter)
        nameOfFileDependency = nameOfFileDependency.replace(".", "_");
        nameOfFileDependency = nameOfFileDependency.replace("-", "_");

        return  nameOfFileDependency;
    }

    public IModel createModelFromPlugin(String pluginPath) {

        // TODO

        return null;

    }
}