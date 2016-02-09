package org.lucci.lmu.input;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

public class ManifestAnalyzer {

    // Constants

    public static final Logger LOGGER = LogManager.getLogger();

    public static void main(String[] args) throws IOException {
        System.out.println(ManifestAnalyzer.getManifestInfo("/home/mark/Downloads/lmu-eclipse-plugin-bkp.jar"));
    }

    public static List<String> getManifestInfo(String pathToJarFile) throws IOException {
        File file = new File(pathToJarFile);
        return getManifestInfo(file);
    }

    public static List<String> getManifestInfo(File jarFile) throws IOException {
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
            root.setName("this");
            root.setNamespace("this");
            model.addEntity(root);
            for (String dependency: dependencies) {
                LOGGER.debug(dependency);
                if (!dependency.equals(".")) {
                    Entity entity = new DeploymentUnit();
                    entity.setName(dependency);
                    entity.setNamespace(dependency);
                    model.addEntity(entity);
                }
            }
            for (Entity entity: model.getEntities()) {
                if (!entity.getName().equals("this")) {
                    model.addRelation(new AssociationRelation(root, entity));
                }
            }
            LOGGER.debug("end of dependencies iteration");

            new ModelExporterImpl().exportToFile(model, "/home/mark/downloads", "pdf");
            //String dependencies = mainAttribs.getValue("Import-Package");
            jis.close();
            return dependencies;
        }catch(Throwable t){
            t.printStackTrace();
            return new ArrayList<String>();
        }
    }
}