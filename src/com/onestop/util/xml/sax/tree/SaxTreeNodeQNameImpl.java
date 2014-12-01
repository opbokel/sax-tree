package com.onestop.util.xml.sax.tree;

import org.xml.sax.Attributes;

/**
 * A Simple implementation that uses only the qName {@link #startElement
 * startElement} to identify itself and the children nodes
 * 
 * @author opbokel
 */
public class SaxTreeNodeQNameImpl extends SaxTreeNodeBase {

    public String buildChildKey(String uri, String localName, String qName,
            Attributes attributes) {
        return qName;
    }

    public SaxTreeNodeQNameImpl(String qName) {
        this.key = qName;
    }

    public SaxTreeNode createChildNode(String uri, String localName, String qName,
            Attributes attributes) {
        return new SaxTreeNodeQNameImpl(qName);
    }

}
