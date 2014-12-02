package com.onestop.util.xml.sax.tree;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.xml.sax.helpers.DefaultHandler;

/**
 * A abstract class that should be sufficient to be the base to most SaxTreeNode
 * implementations. It has linear access time to the children nodes
 * 
 * @author opbokel
 *
 */
public abstract class SaxTreeNodeBase extends DefaultHandler implements
        SaxTreeNode, Cloneable {

    protected Map<String, SaxTreeNode> children = new HashMap<>();

    protected SaxTreeNode parent;

    protected String key;

    protected StringBuilder valueBuffer = new StringBuilder();

    public String getKey() {
        return this.key;
    }

    public SaxTreeNode getChild(String key) {
        return children.get(key);
    }

    public Collection<SaxTreeNode> getChildren(){
        return children.values();
    }

    public SaxTreeNode putChildren(SaxTreeNode... childs) {
        SaxTreeNode lastChild = null;
        for(SaxTreeNode child: childs){
            child.setParent(this);
            this.children.put(child.getKey(), child);
            lastChild = child;
        }
        return lastChild;
    }

    public SaxTreeNode removeChildren(SaxTreeNode... childs) {
        SaxTreeNode lastChild = null;
        for (SaxTreeNode child : childs) {
            this.children.remove(child.getKey());
            lastChild = child;
        }
        return lastChild;
    }

    public SaxTreeNode getParent() {
        return this.parent;
    }

    public void setParent(SaxTreeNode parent) {
        this.parent = parent;
    }

    public void clearChildren() {
        this.children = new HashMap<String, SaxTreeNode>();
    }

    public void removeNodeFromTree() {
        clearChildren();
        if (this.parent != null) {
            this.parent.removeChildren(this);
            this.parent = null;
        }
    }

    public SaxTreeNode cloneUnbonded(boolean deepCopy) {
        SaxTreeNodeBase result;
        try {
            result = (SaxTreeNodeBase) super.clone();
        } catch (CloneNotSupportedException e) {
            // Will never happen. Java made me do it(boiler plate code)
            return null;
        }
        result.removeNodeFromTree();
        if (deepCopy) {
            for (SaxTreeNode child : this.children.values()) {
                result.children.put(child.getKey(), child.cloneUnbonded(true));
            }
        }
        return result;
    }

    public void characters(char ch[], int start, int length) {
        valueBuffer.append(ch, start, length);
    }

    public String consumeValueBuffer() {
        String result = valueBuffer.toString();
        valueBuffer.setLength(0);
        return result;
    }

    public void parseValue(String value) {

    }

}
