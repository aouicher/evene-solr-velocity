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

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class autoLinkCitationDirective extends Directive{

    public String getName() {
        return "autolinkcitation";
    }

    public int getType() {
        return LINE;
    }

    public boolean render(InternalContextAdapter context, Writer writer, Node node)
            throws IOException, ResourceNotFoundException, ParseErrorException, MethodInvocationException {

        String CitationMe   = null;
        String KeyWords     = null;
        String SchemaUrl    = null;

        //params
        if (node.jjtGetChild(0) != null) {
            CitationMe = String.valueOf(node.jjtGetChild(0).value(context));
        }
        if (node.jjtGetChild(1) != null) {
            KeyWords = String.valueOf(node.jjtGetChild(1).value(context));
        }

        //schema url
        if (node.jjtGetChild(2) != null) {
            SchemaUrl = String.valueOf(node.jjtGetChild(2).value(context));
        }

        writer.write(autoLinkCitation(CitationMe, KeyWords, SchemaUrl));

        return true;
    }

    public String autoLinkCitation(String CitationMe, String KeyWords, String SchemaUrl) {
        if (CitationMe == null) {
            return null;
        }

        List<String> tokens = new ArrayList<String>();
        StringTokenizer stkKeyWords = new StringTokenizer(KeyWords, "#");
        while ( stkKeyWords.hasMoreTokens() ) {
            tokens.add(stkKeyWords.nextToken());
        }


        String patternString = "\\b(" + StringUtils.join(tokens, "|") + ")\\b";
        Pattern pattern = Pattern.compile(patternString);

        String strippedHtml = CitationMe.replaceAll("<(.|\n)*?>", "");
        StringTokenizer st = new StringTokenizer(strippedHtml, ".,! ()[]");

        while (st.hasMoreTokens())
        {
            String token = st.nextToken().trim();
            if (token.length() > 3)
            {
                Matcher matcher = pattern.matcher(cleanString(token));
                while (matcher.find()) {
                    if(CitationMe.indexOf( SchemaUrl + cleanString(token) + "'") == -1)
                    {
                        String tmpStringreplacement = "<a href='" + SchemaUrl + cleanString(token) + "'>"+token+"</a>";
                        CitationMe = CitationMe.replaceAll("\\b"+token+"\\b(?!/)",tmpStringreplacement);
                    }
                }
            }
        }

        return CitationMe;
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
