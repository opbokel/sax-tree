package com.onestop.util.xml.sax.tree;

import java.util.Collection;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;

/**
 * Represents the Sax Handler. It is responsible to walk in the tree and use the
 * correct SaxTreeNode to process the XML content
 * 
 * @author opbokel
 *
 * @param <R>
 *            The result type
 */
public interface SaxTreeHandler<R> extends ContentHandler {

    /**
     * @return the current node to process the XML element
     */
    public SaxTreeNode getCurrentNode();

    /**
     * Must be able to change the current node to one of its children
     * represented by the {@link SaxTreeNode#buildChildKey buildChildKey}. It
     * must create a node (possibly using({@link SaxTreeNode#createChildNode
     * createChildNode}) if a child node is not found The parameters received
     * are the same of the method {@link SaxTreeNode#buildChildKey startElement}
     */
    public void goToChild(String uri, String localName, String qName,
            Attributes attributes);

    /**
     * Change the current node to the parent node
     */
    public void backToParent();

    /**
     * Initialize the tree node configuration
     * 
     * @return The first tree node
     */
    public SaxTreeNode initTree();

    /**
     * Must implements all the initialization of the parser and must be called
     * before using it. It is expected to call the initTree and to set the
     * initial node
     */
    public void init();

    /**
     * Sets the first tree node
     * 
     * @param firstNode
     *            The first tree node
     */
    public void setFirstNode(SaxTreeNode firstNode);

    /**
     * @return The first tree Node
     */
    public SaxTreeNode getFirstNode();

    /**
     * @return The result after running the Sax parser
     */
    public R getResult();

    /**
     * Must reset the handler and make it usable to process another XML
     */
    public void clear();

    /**
     * Adds a exception to a collection in order to be retrieved latter and keep
     * the handler compatible with the Sax interface
     * 
     * @param ex
     *            The Exception
     */
    public void addException(Exception ex);

    /**
     * @return The exceptions generated and captured by the addException method
     */
    public Collection<Exception> getExceptions();

    /**
     * Read the attributes values
     * 
     * @param attributes
     *            XML attributes, passed in the start node method
     * @param attQNames
     *            The q name of the attributes
     * @return A array of the same size and order of the attQNames with null
     *         when it is not possible to read the property
     */
    public String[] getAttributeValues(Attributes attributes,
            String... attQNames);

}
