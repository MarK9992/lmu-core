package org.lucci.lmu.input;

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
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Marc Karassev
 */
public class PluginAnalyzer extends Analyzer {

    public IModel createModelFromPlugin(String pluginPath) throws ParserConfigurationException, IOException, SAXException {
        IModel model = new Model();
        Set<String> dependencies = new HashSet<>();

        File fXmlFile = new File(pluginPath + "/plugin.xml");
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
        Entity root = new DeploymentUnit();
        root.setNamespace(rootName);
        root.setName(rootName);
        model.addEntity(root);

        for(String depencyName : dependencies) {
            Entity entity = new DeploymentUnit();
            entity.setName(depencyName);
            entity.setNamespace(depencyName);
            model.addEntity(entity);
            model.addRelation(new DependencyRelation(root, entity));
        }

        return model;
    }
}
