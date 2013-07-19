package org.evene.solr.velocity;

/**
 * Created with IntelliJ IDEA.
 * User: aouicher
 * Date: 05/07/13
 * Time: 14:36
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class cryptUrlDirective extends Directive {

    public String getName() {
        return "crypturl";
    }

    public int getType() {
        return LINE;
    }

    public boolean render(InternalContextAdapter context, Writer writer, Node node)
            throws IOException, ResourceNotFoundException, ParseErrorException, MethodInvocationException {

        String cryptUrlMe = null;

        //reading params
        if (node.jjtGetChild(0) != null) {
            cryptUrlMe = String.valueOf(node.jjtGetChild(0).value(context));
        }

        writer.write(cryptUrl(cryptUrlMe));
        return true;
    }

    public String cryptUrl(String cryptUrlMe) {
        if (cryptUrlMe == null) {
            return null;
        }

        String maskInput = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890:.-/?&%+_=#(),@[]{};^~$";
        String maskOutput = "op8#0qr:stxyEFYZ.-/?7zaR)ST9UV_WXbcdeuvwfghijkl&456mnG+HIJKL%123=MNOPQA(BCD,@[]{};^~$";

        // First reverse the string.
        String reverse_url = strrev(cryptUrlMe);
        return '#' + strtr(reverse_url, maskInput, maskOutput);
    }

    public String strrev(String str) {
        return new StringBuffer(str).reverse().toString();
    }

    public String strtr(String str, String from, String to) {
        //String          fr;
        Integer i = 0;
        Integer j = 0;
        Integer lenStr = 0;
        Integer lenFrom = 0;
        Boolean tmpStrictForIn = false;
        Boolean fromTypeStr;
        Boolean toTypeStr;
        Character istr;
        ArrayList<String> tmpFrom = new ArrayList<String>();
        ArrayList<String> tmpTo = new ArrayList<String>();
        String ret;
        Boolean match = false;
        ret = "";

        // Received replace_pairs?
        // Convert to normal from->to chars

        //if (typeof from === 'object') {
        //tmpStrictForIn = this.ini_set('phpjs.strictForIn', false); // Not thread-safe; temporarily set to true
        from = new StringBuffer(from).reverse().toString();


        for (char fr : from.toCharArray()) {
            tmpFrom.add(Character.toString(fr));
            tmpTo.add(Character.toString(fr));
        }

        // Walk through subject and replace chars when needed
        lenStr = str.length();
        lenFrom = from.length();

        fromTypeStr = from instanceof String;
        toTypeStr = to instanceof String;


        for (i = 0; i < lenStr; i++) {
            match = false;
            if (fromTypeStr) {
                istr = str.charAt(i);
                for (j = 0; j < lenFrom; j++) {
                    if (istr == from.charAt(j)) {
                        match = true;
                        break;
                    }
                }
            } else {
                for (j = 0; j < lenFrom; j++) {
                    if (str.substring(i, tmpFrom.get(j).length()) == tmpFrom.get(j)) {
                        match = true;
                        // Fast forward
                        i = (i + tmpFrom.get(j).length()) - 1;
                        break;
                    }
                }
            }
            if (match) {
                ret += to.charAt(j);
            } else {
                ret += str.charAt(i);
            }
        }
        return ret;
    }
}