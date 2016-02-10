package org.lucci.lmu.output;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lucci.lmu.model.*;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/*
 * Created on Oct 2, 2004
 */

/**
 * @author luc.hogie
 */
public class DotWriter implements Writer {
    public static final Logger LOGGER = LogManager.getLogger();

    private String fontName = "Times";

    @Override
    public byte[] writeModel(IModel model) throws WriterException {
        StringBuffer buf = new StringBuffer();
        buf.append("digraph ClassDiagram\n{");
        buf.append("\n\tgraph [rankdir=TD,ranksep=0.75];\n\tedge [fontname=\"" + fontName
                + "\", fontsize=10,labelfontname=\"" + fontName + "\", labelfontsize=10];\n\tnode [fontname=\""
                + fontName + "\", fontsize=10];\n");

        buf.append("\n");

        for (Collection<Entity> align : model.getAlignments()) {
            buf.append("\t{rank=same");

            for (Entity e : align) {
                buf.append(";\"" + e.getName().hashCode() + "\"");
            }

            buf.append("}\n");
        }

        Collection<Entity> visibleEntities = ModelElement.findVisibleModelElements(model.getEntities());

        for (Entity entity : visibleEntities) {
            drawEntity(buf, entity);
        }

        for (Relation relation : model.getRelations()) {
            drawRelation(buf, relation);
        }

        int gid = 0;

        for (Group group : model.getGroups()) {
            buf.append("\n\n\tsubgraph cluster_" + gid++ + " {");
            buf.append("\n\t\tcolor = " + group.getColorName() + ";");
            buf.append("\n\t\tlabel = \"" + group.getLabel() + "\";");

            for (Entity e : group) {
                buf.append("\n\t\t" + quoteNodeNameIfNecessary(String.valueOf(e.getName().hashCode())) + ";");
            }

            buf.append("\n\t}");
        }

        buf.append("\n}");
        String dotText = buf.toString();

        //  Debug if dotwriter throw runtime exception
        //  System.out.println("DO TEXT : " + dotText);

        return dotText.getBytes();
    }

    private void drawRelation(StringBuffer buf, Relation relation) {
        // c0 -> c1 [taillabel="1", label="come from", headlabel="1",
        // fontname="Helvetica", fontcolor="black", fontsize=10.0,
        // color="black", , arrowtail=ediamond];
        // System.out.println(relation);

        if (relation.getTailEntity().isVisible() && relation.getHeadEntity().isVisible()) {
            if (relation instanceof AssociationRelation) {
                LOGGER.debug("Drawing an association relation");
                AssociationRelation assoc = (AssociationRelation) relation;
                buf.append("\n\t");
                buf.append(quoteNodeNameIfNecessary(String.valueOf(assoc.getContainedEntity().getName().hashCode())));
                buf.append(" -> ");
                buf.append(quoteNodeNameIfNecessary(String.valueOf(assoc.getContainerEntity().getName().hashCode())));

                if (assoc.getType() == AssociationRelation.TYPE.ASSOCIATION) {
                    buf.append(" [arrowhead=none");
                } else if (assoc.getType() == AssociationRelation.TYPE.AGGREGATION) {
                    buf.append(" [arrowhead=odiamond");
                } else if (assoc.getType() == AssociationRelation.TYPE.COMPOSITION) {
                    buf.append(" [arrowhead=diamond");
                } else if (assoc.getType() == AssociationRelation.TYPE.DIRECTION) {
                    buf.append(" [arrowtail=vee");
                } else {
                    throw new IllegalStateException("unknow relation type");
                }

                if (assoc.getCardinality() != null && !assoc.getCardinality().equals("1")) {
                    buf.append(", taillabel=\"" + assoc.getCardinality() + "\"");
                }

                if (assoc.getLabel() != null) {
                    buf.append(", label=\"" + assoc.getLabel() + "\"");
                }

                buf.append("];");
            }
            else if(relation instanceof DependencyRelation) {
                LOGGER.debug("Drawing an dependency relation");

                DependencyRelation dependencyRelation = (DependencyRelation) relation;

                buf.append("\n\t");
                buf.append(quoteNodeNameIfNecessary(String.valueOf(dependencyRelation.getTailEntity().getName().hashCode())));
                buf.append(" -> ");
                buf.append(quoteNodeNameIfNecessary(String.valueOf(dependencyRelation.getHeadEntity().getName().hashCode())));

                buf.append(" [arrowtail=vee");
                buf.append(",style=dashed");
                buf.append(", label=uses");

                buf.append("];");

            }
            else {
                InheritanceRelation heritage = (InheritanceRelation) relation;
                buf.append("\n\t");
                buf.append(quoteNodeNameIfNecessary(String.valueOf(heritage.getSubEntity().getName().hashCode())));
                buf.append(" -> ");
                buf.append(quoteNodeNameIfNecessary(String.valueOf(heritage.getSuperEntity().getName().hashCode())));
                buf.append(" [arrowhead=onormal");

                if (heritage.getSuperEntity().isInterface()) {
                    buf.append(",style=dashed");
                }

                buf.append("];");
            }
        }
    }

    private void drawEntity(StringBuffer buf, Entity entity) {

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

        if(entity instanceof DeploymentUnit) {
            isRecord = false;
        }

        LOGGER.debug("isRecord:"+isRecord);

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
            if(entity.isAbsract()) {
                label = "abstract";
            }
            if(entity.isDeploymentUnit()) {
                label = "Unité de deploiement";
            }

            buf.append(", label=\"&lt;&lt; "+label+"&gt;&gt; \\n"  + entity.getName());

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
