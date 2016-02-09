package org.lucci.lmu.input;

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

    public static void main(String[] args) throws IOException {
        System.out.println(ManifestAnalyzer.getManifestInfo("/home/benjamin/Téléchargements/lmu-manifest.jar"));
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

            List<Attributes.Name> targetKeys = Arrays.asList(new Attributes.Name("Bundle-ClassPath"), new Attributes.Name("Require-Bundle"),new Attributes.Name("Import-Package"));

            for(Attributes.Name currentTargetKey : targetKeys) {
                System.out.println("\n===========================");
                System.out.println("Target Key : " + currentTargetKey);
                System.out.println("Result : \n" + mainAttribs.get(currentTargetKey));
            }

            String dependencies = mainAttribs.getValue("Import-Package");
            jis.close();
            return Arrays.asList(dependencies.split(","));
        }catch(Throwable t){
            t.printStackTrace();
            return new ArrayList<String>();
        }
    }

}