package com.onestop.util.xml.sax.tree;

import java.util.ArrayList;
import java.util.Collection;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public abstract class SaxTreeHandlerBase<T> extends DefaultHandler
        implements SaxTreeHandler<T> {

    protected SaxTreeNode currentNode;

    protected SaxTreeNode firstNode;

    protected T result;

    protected Collection<Exception> exceptions = new ArrayList<>();

    public SaxTreeNode getFirstNode() {
        return firstNode;
    }

    public void setFirstNode(SaxTreeNode firstNode) {
        this.firstNode = firstNode;
    }

    public SaxTreeNode getCurrentNode() {
        return this.currentNode;
    }

    public void goToChild(String uri, String localName, String qName,
            Attributes attributes) { 
        String key = this.currentNode.buildChildKey(uri, localName, qName,
                attributes);
        SaxTreeNode childNode = currentNode.getChild(key);
        if (childNode == null) {
            childNode = currentNode.createChildNode(uri, localName, qName,
                    attributes);
            this.currentNode.putChildren(childNode);
        }
        this.currentNode = childNode;
    }

    public void backToParent() {
        this.currentNode = currentNode.getParent();
    }

    public void init() {
        clear();
        setFirstNode(initTree());
    }

    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {
        if (currentNode == null) {
            this.currentNode = this.firstNode;
        } else {
            goToChild(uri, localName, qName, attributes);
        }
        this.currentNode.startElement(uri, localName, qName, attributes);
    }

    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        String value = this.currentNode.consumeValueBuffer();
        this.currentNode.parseValue(value);
        this.currentNode.endElement(uri, localName, qName);
        backToParent();
    }

    public void characters(char ch[], int start, int length)
            throws SAXException {
        this.currentNode.characters(ch, start, length);
    }
    
    public void addException(Exception ex){
        this.exceptions.add(ex);
    }

    public Collection<Exception> getExceptions() {
        return this.exceptions;
    }

    public T getResult() {
        return this.result;
    }

}
