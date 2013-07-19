package org.evene.solr.velocity;
/**
 * Created with IntelliJ IDEA.
 * User: aouicher
 * Date: 03/07/13
 * Time: 11:05
 * To change this template use File | Settings | File Templates.
 */

import java.io.IOException;
import java.io.Writer;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;
import java.text.Normalizer;

public class cleanStringDirective extends Directive{

    public String getName() {
        return "cleanstring";
    }

    public int getType() {
        return LINE;
    }

    public boolean render(InternalContextAdapter context, Writer writer, Node node)
            throws IOException, ResourceNotFoundException, ParseErrorException, MethodInvocationException {

        String CleanStringMe = null;

        //reading params
        if (node.jjtGetChild(0) != null) {
            CleanStringMe = String.valueOf(node.jjtGetChild(0).value(context));
        }

        writer.write(cleanString(CleanStringMe));

        return true;
    }

    public String cleanString(String CleanStringMe) {
        if (CleanStringMe == null) {
            return null;
        }

        CleanStringMe = CleanStringMe.trim();
        CleanStringMe =  Normalizer.normalize(CleanStringMe, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        CleanStringMe = CleanStringMe.toLowerCase();
        CleanStringMe = CleanStringMe.replaceAll("[^A-Za-z0-9]", "-");
        return CleanStringMe;
    }
}
