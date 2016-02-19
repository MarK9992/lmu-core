package org.lucci.lmu.input;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lucci.lmu.model.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.jar.*;
import java.util.jar.Attributes;

/**
 * @author Marc Karassev
 */
public class PluginAnalyzer extends Analyzer {

    // Constants

    private static final Logger LOGGER = LogManager.getLogger();
    private static final List<java.util.jar.Attributes.Name> targetKeys = Collections.singletonList(new java.util.jar.Attributes.Name("Bundle-ClassPath"));
    private static final String PLUGIN_PATH = "/plugin.xml", MANIFEST_PATH = "/META-INF/MANIFEST.MF";

    // Attributes

    private ManifestAnalyzer manifestAnalyzer = new ManifestAnalyzer();

    // Methods

    public IModel createModelFromPlugin(String pluginPath) throws ParserConfigurationException, IOException, SAXException {
        IModel model = new Model();
        Set<String> dependencies = new HashSet<>();

        File fXmlFile = new File(pluginPath + PLUGIN_PATH);
        if (fXmlFile.exists()) {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            try {
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(fXmlFile);
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getElementsByTagName("extension");
                for (int temp = 0; temp < nList.getLength(); temp++) {
                    Node nNode = nList.item(temp);
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;
                        String s = eElement.getAttribute("point");
                        dependencies.add(s);
                    }
                }
            } catch (SAXException | IOException | ParserConfigurationException e) {
                e.printStackTrace();
            }
        }

        String rootName = deleteUnauthorizedToken(fXmlFile.getName());
        Entity root = new DeploymentUnit(), entity;
        root.setNamespace(rootName);
        root.setName(rootName);
        model.addEntity(root);

        for(String dependencyName : dependencies) {
            entity = new DeploymentUnit();
            entity.setName(dependencyName);
            entity.setNamespace(dependencyName);
            model.addEntity(entity);
            model.addRelation(new DependencyRelation(root, entity));
        }

        File manifestFile = new File(pluginPath + MANIFEST_PATH);

        if (!manifestFile.exists()) {
            LOGGER.debug("no manifest at " + pluginPath + MANIFEST_PATH);
        }
        else {
            Manifest manifest = new Manifest(new FileInputStream(manifestFile));
            Attributes mainAttribs = manifest.getMainAttributes();
            ArrayList<String> manifestDependencies = new ArrayList<>();
            String entityName, tmpPath;

            for(java.util.jar.Attributes.Name currentTargetKey : targetKeys) {
                if(mainAttribs.get(currentTargetKey) != null) {
                    manifestDependencies.addAll(Arrays.asList(((String) mainAttribs.get(currentTargetKey)).split(",")));
                }
                else {
                    LOGGER.debug("no dependencies in " + pluginPath + MANIFEST_PATH + " manifest");
                }
            }
            for (String dependency: manifestDependencies) {
                LOGGER.debug("dependency: " + dependency);
                if (!dependency.equals(".")) {
                    entityName = deleteUnauthorizedToken(dependency);
                    entity = new DeploymentUnit();
                    entity.setName(entityName);
                    entity.setNamespace(entityName);
                    LOGGER.debug("Creating new entity : " + entity.getName());
                    model.addEntity(entity);
                    model.addRelation(new DependencyRelation(root, entity));
//                    jis.close();
//                    tmpPath = findAndExtractJarEntry(dependency, jarPath);
//                    if (tmpPath !=  null) {
//                        populateModel(model, tmpPath);
//                    }
                }
            }
        }
        return model;
    }
}
