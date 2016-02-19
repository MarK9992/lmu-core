package org.lucci.lmu.input;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lucci.lmu.LmuCore;
import org.lucci.lmu.model.*;
import org.lucci.lmu.output.ModelExporterImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
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
        IModel model = new Model();

        populateModel(model, jarPath, 0);
        return model;
    }

    private void populateModel(IModel model, String jarPath, int depth) throws IOException {
        File jarFile = new File(jarPath);
        JarInputStream jis = new JarInputStream(new FileInputStream(jarFile));
        Manifest manifest = jis.getManifest();

        if (manifest == null) {
            LOGGER.debug("no manifest in " + jarPath);
        }
        else {
            Attributes mainAttribs = manifest.getMainAttributes();
            ArrayList<String> dependencies = new ArrayList<>();
            Entity root = new DeploymentUnit(), entity;
            String rootName = deleteUnauthorizedToken(jarFile.getName());
            String entityName;
            //String[] splitPath;

            for(Attributes.Name currentTargetKey : targetKeys) {
                if(mainAttribs.get(currentTargetKey) != null) {
                    dependencies.addAll(Arrays.asList(((String) mainAttribs.get(currentTargetKey)).split(",")));
                }
            }
            //LOGGER.debug("iterating on dependencies and adding entities to model");
            root.setName(rootName);
            root.setNamespace(rootName);
            model.addEntity(root);
            for (String dependency: dependencies) {
                LOGGER.debug("dependency: " + dependency);
                if (!dependency.equals(".")) {
                    entityName = deleteUnauthorizedToken(dependency);
                    entity = new DeploymentUnit();
                    entity.setName(entityName);
                    entity.setNamespace(entityName);
                    LOGGER.debug("Creating new entity : " + entity.getName());
                    model.addEntity(entity);
                    model.addRelation(new DependencyRelation(root, entity));
                    if (depth < 1) {
                        //splitPath = dependency.split("/");
                        //dependency = splitPath[splitPath.length - 1];
                        LOGGER.debug("looking for dependency: " + dependency);

//                        URL dependencyUrl = Thread.currentThread().getContextClassLoader().getResource(dependency);
//                        if (dependencyUrl == null) {
//                            LOGGER.warn("could not find dependency: " + dependency);
//                        }
//                        else {
//                            LOGGER.debug("found dependency at: " + dependencyUrl.getPath());
//                            populateModel(model, dependencyUrl.getPath(), depth + 1);
//                        }
                    }
                }
            }
            jis.close();
        }
        //LOGGER.debug("end of dependencies iteration");
    }

    private void findJarEntry(JarInputStream jis, String entry) {

    }

    private static String deleteUnauthorizedToken(String str) {
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