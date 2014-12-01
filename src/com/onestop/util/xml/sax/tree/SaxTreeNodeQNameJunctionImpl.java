package com.onestop.util.xml.sax.tree;

import org.xml.sax.Attributes;

/**
 * A SaxTreeNodeQNameImpl that can have children of a different type
 * 
 * @author opbokel
 *
 */
public class SaxTreeNodeQNameJunctionImpl extends SaxTreeNodeQNameImpl {

    protected SaxTreeNode childTypeNode;

    public SaxTreeNodeQNameJunctionImpl(String qName,
            SaxTreeNode childTypeNode) {
        super(qName);
        this.childTypeNode = childTypeNode;
    }

    public String buildChildKey(String uri, String localName, String qName,
            Attributes attributes) {
        return childTypeNode.buildChildKey(uri, localName, qName, attributes);
    }

    public SaxTreeNode createChildNode(String uri, String localName,
            String qName, Attributes attributes) {
        return childTypeNode.createChildNode(uri, localName, qName, attributes);
    }
    
}
