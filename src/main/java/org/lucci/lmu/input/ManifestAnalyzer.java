package org.lucci.lmu.input;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lucci.lmu.LmuCore;
import org.lucci.lmu.model.*;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.jar.*;
import java.util.jar.Attributes;

public class ManifestAnalyzer extends Analyzer implements JarAnalyzer {

    // Constants

    private static final Logger LOGGER = LogManager.getLogger();
    private static final List<Attributes.Name> targetKeys = Collections.singletonList(new Attributes.Name("Bundle-ClassPath"));

    // Methods

    @Override
    public IModel createModelFromJar(String jarPath) throws IOException {
        IModel model = new Model();
        File file = new File(LmuCore.DEFAULT_OUTPUT_PATH);
        DirectoryStream<Path> stream;

        file.mkdirs();
        populateModel(model, jarPath);
        stream = Files.newDirectoryStream(Paths.get(LmuCore.DEFAULT_OUTPUT_PATH));
        for (Path filePath: stream) {
            if (filePath.toString().endsWith(".jar")) {
                Files.delete(filePath);
            }
        }
        return model;
    }

    private void populateModel(IModel model, String jarPath) throws IOException {
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
            String entityName, tmpPath;

            for(Attributes.Name currentTargetKey : targetKeys) {
                if(mainAttribs.get(currentTargetKey) != null) {
                    dependencies.addAll(Arrays.asList(((String) mainAttribs.get(currentTargetKey)).split(",")));
                }
                else {
                    LOGGER.debug("no dependencies in " + jarPath + " manifest");
                }
            }
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
                    jis.close();
                    tmpPath = findAndExtractJarEntry(dependency, jarPath);
                    if (tmpPath !=  null) {
                        populateModel(model, tmpPath);
                    }
                }
            }
        }
    }

    private String findAndExtractJarEntry(String entry, String jarFile) throws IOException {
        JarFile jar = new JarFile(jarFile);
        Enumeration enumEntries = jar.entries();
        JarEntry jarEntry;
        File file;
        InputStream is;
        FileOutputStream fos;
        String[] splitPath;

        LOGGER.debug("looking for dependency: " + entry);
        while (enumEntries.hasMoreElements()) {
            jarEntry = (JarEntry) enumEntries.nextElement();
            if (jarEntry.getName().equals(entry)) {
                LOGGER.debug("found dependency: " + entry);
                splitPath = entry.split("[/\\\\]");
                file = new File(LmuCore.DEFAULT_OUTPUT_PATH + splitPath[splitPath.length - 1]);
                is = jar.getInputStream(jarEntry);
                fos = new FileOutputStream(file);
                while (is.available() > 0) {
                    fos.write(is.read());
                }
                fos.close();
                is.close();
                return LmuCore.DEFAULT_OUTPUT_PATH + splitPath[splitPath.length - 1];
            }
        }
        return null;
    }
}