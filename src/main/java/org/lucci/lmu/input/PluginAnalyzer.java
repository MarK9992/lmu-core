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
public class PluginAnalyzer extends ManifestAnalyzer {

    // Constants

    private static final Logger LOGGER = LogManager.getLogger();
    private static final String PLUGIN_PATH = "/plugin.xml", MANIFEST_PATH = "/META-INF/MANIFEST.MF";

    // Methods

    public IModel createModelFromPlugin(String pluginPath) throws IOException {
        File manifestFile = new File(pluginPath + MANIFEST_PATH);
        Set<String> dependencies = new HashSet<>();
        File fXmlFile = new File(pluginPath + PLUGIN_PATH);
        String rootName = deleteUnauthorizedToken(fXmlFile.getName());
        Entity root = new DeploymentUnit(), entity;

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
            } catch (SAXException | ParserConfigurationException e) {
                e.printStackTrace();
            }
        }
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

        return createModelFromManifest(new Manifest(new FileInputStream(manifestFile)), rootName);
    }
}
