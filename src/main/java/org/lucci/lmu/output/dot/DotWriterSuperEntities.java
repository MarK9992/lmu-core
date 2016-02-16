package org.lucci.lmu.output.dot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lucci.lmu.model.*;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by benjamin on 16/02/16.
 */
public class DotWriterSuperEntities {
    public static final Logger LOGGER = LogManager.getLogger();

    public void drawEntities(StringBuffer buf, IModel model) {
        Collection<Entity> visibleEntities = ModelElement.findVisibleModelElements(model.getEntities());

        for (Entity entity : visibleEntities) {
            drawEntity(buf, entity);
        }
    }

    private void drawEntity(StringBuffer buf, Entity entity) {

        if (entity instanceof DeploymentUnit) {
            drawDeploymentUnit(buf, entity);
        } else if (entity instanceof Entity) {
            drawEntities(buf, entity);
        }
    }

    private void drawDeploymentUnit(StringBuffer buf, Entity superEntity) {

        buf.append("\n\t");
        buf.append(quoteNodeNameIfNecessary(String.valueOf(superEntity.getName().hashCode())));
        buf.append(" [");
        buf.append("shape=\"" + "component" + "\"");

        if (superEntity.getColorName() != null) {
            buf.append(", fillcolor=" + superEntity.getColorName());
            buf.append(", style=filled");
        }

        buf.append(", fontcolor=black");
        buf.append(", fontsize=10.0");

        String label = "Unité de deploiement";


        buf.append(", label=\"&lt;&lt; " + label + "&gt;&gt; \\n" + superEntity.getName());

        buf.append("\"];");
    }

    private void drawEntities(StringBuffer buf, Entity entity) {
        boolean isRecord = true;// visibleAttributes.size() +
        // visibleOperations.size() > 0;

        buf.append("\n\t");
        buf.append(quoteNodeNameIfNecessary(String.valueOf(entity.getName().hashCode())));
        buf.append(" [");
        buf.append("shape=\"" + (isRecord ? "record" : "box") + "\"");

        if (entity.getColorName() != null) {
            buf.append(", fillcolor=" + entity.getColorName());
            buf.append(", style=filled");
        }

        buf.append(", fontcolor=black");
        buf.append(", fontsize=10.0");

        LOGGER.debug("isRecord:" + isRecord);

        if (isRecord) {
            buf.append(", label=\"" + "{");

            for (String st : entity.getStereoTypeList()) {
                buf.append("&lt;&lt;" + st + "&gt;&gt;\\n");
            }

            if (!entity.getStereoTypeList().isEmpty()) {
                buf.append("\\n");
            }

            buf.append(entity.getName());

            Collection<Attribute> visibleAttributes = ModelElement.findVisibleModelElements(entity.getAttributes());
            drawVisibleAttributesIfRecord(buf, visibleAttributes);

            Collection<Operation> visibleOperations = ModelElement.findVisibleModelElements(entity.getOperations());
            drawVisibleOperationsIfRecord(buf, visibleOperations);

            buf.append((isRecord ? "}" : "") + "\"];");
        }

        //  UNREACHABLE CODE ??! isRecord was _always_ true
        else {
            String label = "";
            if (entity.isAbstract()) {
                label = "abstract";
            }

            buf.append(", label=\"&lt;&lt; " + label + "&gt;&gt; \\n" + entity.getName());

            buf.append("\"];");
        }
    }

    private void drawVisibleOperationsIfRecord(StringBuffer buf, Collection<Operation> visibleOperations) {
        if (!visibleOperations.isEmpty()) {
            buf.append("|");

            for (Operation operation : visibleOperations) {
                if (operation.isVisible()) {
                    if (operation.getVisibility() != null) {
                        buf.append(getUMLVisibility(operation.getVisibility()) + " ");
                    }

                    buf.append(operation.getName() + "(");
                    Iterator<Entity> parameterIterator = operation.getParameterList().iterator();

                    while (parameterIterator.hasNext()) {
                        Entity parameterType = parameterIterator.next();
                        buf.append(escapeStringIfNecessary(parameterType.getName()));

                        if (parameterIterator.hasNext()) {
                            buf.append(", ");
                        }
                    }

                    buf.append(")");

                    if (operation.getType() != null) {
                        buf.append(" : " + escapeStringIfNecessary(operation.getType().getName()));
                    }

                    buf.append("\\l");
                }
            }
        }
    }

    private void drawVisibleAttributesIfRecord(StringBuffer buf, Collection<Attribute> visibleAttributes) {
        if (!visibleAttributes.isEmpty()) {
            buf.append("|");

            for (Attribute attribute : visibleAttributes) {
                if (attribute.getVisibility() != null) {
                    buf.append(getUMLVisibility(attribute.getVisibility()) + " ");
                }

                buf.append(attribute.getName());

                if (attribute.getType() != null) {
                    buf.append(" : " + escapeStringIfNecessary(attribute.getType().getName()));
                }

                buf.append("\\l");
            }
        }
    }



    private String getUMLVisibility(Visibility v) {
        if (v == Visibility.PUBLIC) {
            return "+";
        } else if (v == Visibility.PROTECTED) {
            return "#";
        } else if (v == Visibility.PRIVATE) {
            return "-";
        } else {
            throw new IllegalArgumentException("unknow visilibity " + v);
        }
    }

    private String quoteNodeNameIfNecessary(String s) {
        if (!s.matches("[0-9a-zA-Z]+")) {
            return '"' + s + '"';
        } else {
            return s;
        }
    }

    private String escapeStringIfNecessary(String s) {
        return s.replaceAll("\\.", "\\.");
    }

}
