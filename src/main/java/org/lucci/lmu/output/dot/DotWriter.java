package org.lucci.lmu.output.dot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lucci.lmu.model.*;
import org.lucci.lmu.output.Writer;
import org.lucci.lmu.output.WriterException;

import java.util.Collection;

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
        DotWriterRelations relationsWriter = new DotWriterRelations();
        DotWriterSuperEntities superEntitiesWriter = new DotWriterSuperEntities();

        StringBuffer buf = new StringBuffer();
        drawHeader(buf, model);

        superEntitiesWriter.drawEntities(buf, model);
        relationsWriter.drawRelations(buf, model);

        drawGroups(model, buf);

        buf.append("\n}");

        String dotText = buf.toString();
        return dotText.getBytes();
    }

    private void drawGroups(IModel model, StringBuffer buf) {
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
    }

    private StringBuffer drawHeader(StringBuffer buf, IModel model) {
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
        return buf;
    }

    private String quoteNodeNameIfNecessary(String s) {
        if (!s.matches("[0-9a-zA-Z]+")) {
            return '"' + s + '"';
        } else {
            return s;
        }
    }
}
