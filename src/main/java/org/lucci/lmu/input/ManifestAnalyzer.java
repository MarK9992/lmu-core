package org.lucci.lmu.input;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lucci.lmu.LmuCore;
import org.lucci.lmu.model.*;
import org.lucci.lmu.output.ModelExporterImpl;
import sun.misc.Regexp;

import java.io.*;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.jar.*;
import java.util.jar.Attributes;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;

public class ManifestAnalyzer implements JarAnalyzer {

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
        populateModel(model, jarPath, 0);
        stream = Files.newDirectoryStream(Paths.get(LmuCore.DEFAULT_OUTPUT_PATH));
        for (Path filePath: stream) {
            if (filePath.toString().endsWith(".jar")) {
                Files.delete(filePath);
            }
        }
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
                    if (depth < 1) {
                        jis.close();
                        LOGGER.debug("looking for dependency: " + dependency);
                        populateModel(model, findAndExtractJarEntry(dependency, jarPath), depth + 1);
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