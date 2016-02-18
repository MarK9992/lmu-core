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

    public static final Logger LOGGER = LogManager.getLogger();

    // Methods

    @Override
    public IModel createModelFromJar(String jarPath) throws IOException {
        return getManifestInfo(jarPath);
    }

    public static IModel getManifestInfo(String pathToJarFile) throws IOException {
        File file = new File(pathToJarFile);
        return getManifestInfo(file);
    }

    public static IModel getManifestInfo(File jarFile) throws IOException {
        try{
            Manifest manifest;
            FileInputStream fis = new FileInputStream(jarFile);
            JarInputStream jis = new JarInputStream(fis);
            manifest = jis.getManifest();
            Attributes mainAttribs = manifest.getMainAttributes();

            List<Attributes.Name> targetKeys = Arrays.asList(new Attributes.Name("Bundle-ClassPath"));
            //new Attributes.Name("Require-Bundle"),new Attributes.Name("Import-Package"));

            IModel model = new Model();
            ArrayList<String> dependencies = new ArrayList<>();
            LOGGER.debug("iterating on dependencies");
            for(Attributes.Name currentTargetKey : targetKeys) {
                System.out.println("\n===========================");
                System.out.println("Target Key : " + currentTargetKey);
                System.out.println("Result : \n" + mainAttribs.get(currentTargetKey));
                dependencies.addAll(Arrays.asList(((String) mainAttribs.get(currentTargetKey)).split(",")));
                // at this only the name of entities is known
                // neither members nor relation are known
                // let's find them
                //fillModel(model);
            }
            Entity root = new DeploymentUnit();
            String rootName = deleteUnauthorizedToken(jarFile.getName());
            root.setName(rootName);
            root.setNamespace(rootName);
            model.addEntity(root);
            for (String dependency: dependencies) {
                LOGGER.debug(dependency);
                if (!dependency.equals(".")) {

                    String nameOfFileDependency = deleteUnauthorizedToken(dependency);

                    Entity entity = new DeploymentUnit();
                    entity.setName(nameOfFileDependency);
                    entity.setNamespace(nameOfFileDependency);

                    LOGGER.debug("Creating new entity : " + entity.getName());

                    model.addEntity(entity);
                }
            }
            for (Entity entity: model.getEntities()) {
                if (!entity.getName().equals(rootName)) {
                      model.addRelation(new DependencyRelation(root, entity));
                }
            }
            LOGGER.debug("end of dependencies iteration");

            //new ModelExporterImpl().exportToFile(model, "/home/mark/bureau", "png");
            //String dependencies = mainAttribs.getValue("Import-Package");
            jis.close();
            return model;
        }catch(Throwable t){
            t.printStackTrace();
            return null;
        }
    }

    static String deleteUnauthorizedToken(String str) {
        String[] dependenciesPath = str.split("/");  //  Retrieve only file name
        String nameOfFileDependency = dependenciesPath[dependenciesPath.length-1];

        //  Delete unauthorized token (for dotWriter)
        nameOfFileDependency = nameOfFileDependency.replace(".", "_");
        nameOfFileDependency = nameOfFileDependency.replace("-", "_");

        return  nameOfFileDependency;
    }
}