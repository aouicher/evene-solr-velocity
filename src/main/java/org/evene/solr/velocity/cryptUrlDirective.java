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

    // Simplification de la fonction strtr
    public String strtr(String str, String from, String to) {
        StringBuilder strb = null;
        for (int i = 0, length = str.length(); i < length; i++) {
            char lettre = str.charAt(i);
            int position = from.indexOf(lettre);
            if (position >= 0) {
                lettre = to.charAt(position);
                if (strb == null) {
                    strb = new StringBuilder(length);
                    strb.append(str, 0, i);
                }
            }
            if (strb != null) strb.append(lettre);
        }
        return strb != null ? strb.toString() : str;
    }
}