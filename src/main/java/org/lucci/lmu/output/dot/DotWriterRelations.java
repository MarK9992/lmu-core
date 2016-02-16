package org.lucci.lmu.output.dot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lucci.lmu.model.*;

/**
 * Created by benjamin on 16/02/16.
 */
public class DotWriterRelations {

    public static final Logger LOGGER = LogManager.getLogger();


    public void drawRelations(StringBuffer buf, IModel model) {
        for (Relation relation : model.getRelations()) {
            drawRelation(buf, relation);
        }
    }

    private void drawRelation(StringBuffer buf, Relation relation) {
        if (relation.getTailEntity().isVisible() && relation.getHeadEntity().isVisible()) {
            if (relation instanceof AssociationRelation) {
                drawAssociationRelation(buf, (AssociationRelation) relation);
            } else if (relation instanceof DependencyRelation) {
                drawDependencyRelation(buf, (DependencyRelation) relation);

            } else {
                drawInheritanceRelation(buf, (InheritanceRelation) relation);
            }
        }
    }

    private void drawInheritanceRelation(StringBuffer buf, InheritanceRelation relation) {
        InheritanceRelation heritage = relation;
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

    private void drawDependencyRelation(StringBuffer buf, DependencyRelation relation) {
        LOGGER.debug("Drawing an dependency relation");

        DependencyRelation dependencyRelation = relation;

        buf.append("\n\t");
        buf.append(quoteNodeNameIfNecessary(String.valueOf(dependencyRelation.getTailEntity().getName().hashCode())));
        buf.append(" -> ");
        buf.append(quoteNodeNameIfNecessary(String.valueOf(dependencyRelation.getHeadEntity().getName().hashCode())));

        buf.append(" [arrowtail=vee");
        buf.append(",style=dashed");
        buf.append(", label=uses");

        buf.append("];");
    }

    private void drawAssociationRelation(StringBuffer buf, AssociationRelation relation) {
        LOGGER.debug("Drawing an association relation");
        AssociationRelation assoc = relation;
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

    private String quoteNodeNameIfNecessary(String s) {
        if (!s.matches("[0-9a-zA-Z]+")) {
            return '"' + s + '"';
        } else {
            return s;
        }
    }
}
