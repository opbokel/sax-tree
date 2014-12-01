package com.onestop.util.xml.sax.tree;

import java.util.Collection;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;

/**
 * A Sax content handler that represents a node in the tree, each nodes
 * represents a XML element. The nodes represents the tree itself.
 * 
 * @author opbokel
 */
public interface SaxTreeNode extends ContentHandler {
    
    /**
     * Must return the key that will be used to find the next child node; The
     * parameters received are the same of the method {@link #startElement
     * startElement}
     * 
     * @return The key to the child node
     */
    public String buildChildKey(String uri, String localName, String qName,
            Attributes attributes);

    /**
     * The key must be unique among the child nodes
     * 
     * @return This node Key
     */
    public String getKey();

    /**
     * Returns the child node represented by this key
     * 
     * @param key
     *            The Key
     * @return The child node represented by this key
     */
    public SaxTreeNode getChild(String key);

    /**
     * Get all the child nodes bellow this node
     * 
     * @return all the child nodes
     */
    public Collection<SaxTreeNode> getChildren();

    /**
     * Put one or more child nodes, if the node is represented by the same key,
     * replaces it. Make this node the parent node of the inserted children.
     * 
     * @param children
     *            The child nodes to put
     * @return The last inserted child Node or null if none is inserted
     */
    public SaxTreeNode putChildren(SaxTreeNode... children);

    /**
     * Remove the passed child nodes.
     * 
     * @param children
     *            The child nodes to remove.
     * @return The last removed child Node or null if none is removed
     */
    public SaxTreeNode removeChildren(SaxTreeNode... children);

    /**
     * @return the parent node or null if this is the first node
     */
    public SaxTreeNode getParent();

    /**
     * Sets the parent node
     * 
     * @param parent
     *            The parent Node
     */
    public void setParent(SaxTreeNode parent);

    /**
     * Clone a node removing any reference to the original tree so it can be
     * allocated to another part of the tree or a new tree
     * 
     * @param deepCopy
     *            Clone all the nodes bellow this node too recursively
     * @return
     */
    public SaxTreeNode cloneUnbonded(boolean deepCopy);

    /**
     * Remove this node from the tree, removing the children and parent
     * references
     */
    public void removeNodeFromTree();

    /**
     * remove all the child nodes from the tree
     */
    public void clearChildren();

    /**
     * Must return the buffer contents and make it empty
     * 
     * @return The buffer contents
     */
    public String consumeValueBuffer();

    /**
     * Parse the received value, should used to consume the buffer after it is
     * full
     * 
     * @param value
     *            The string to parse
     */
    public void parseValue(String value);

    /**
     * The parameters received are the same of the method {@link #startElement
     * startElement} It must be able to create a child node that the
     * {@link #startElement startElement} would go to after receiving the same
     * parameters. It only returns the node and does not add it to the tree
     * 
     * @return The created child node
     */
    public SaxTreeNode createChildNode(String uri, String localName,
            String qName, Attributes attributes);

}
