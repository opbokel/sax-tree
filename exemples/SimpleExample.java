package worksheet;


import java.io.IOException;
import java.io.StringReader;
import java.util.Collection;
import java.util.LinkedList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.onestop.util.xml.sax.tree.SaxTreeHandlerBase;
import com.onestop.util.xml.sax.tree.SaxTreeNode;
import com.onestop.util.xml.sax.tree.SaxTreeNodeQNameImpl;


public class SimpleExample {

    public static class Person {
        public String name;
        public int age;
    }

    public static class ExempleSaxHandler extends
            SaxTreeHandlerBase<Collection<Person>> {

        private Person currentPerson;

        @Override
        public SaxTreeNode initTree() {
            // Adds the root node, it is the initial node of the tree and will
            // be returned. The SaxTreeNodeQNameImpl is the simplest kind of
            // node and it is identified only by the XML tag
            SaxTreeNode rootNode = new SaxTreeNodeQNameImpl("root");

            // A node representing the people list. The putChildren method
            // returns the last inserted node
            SaxTreeNode peopleNode = rootNode
                    .putChildren(new SaxTreeNodeQNameImpl("people"));


            // A node representing one Person
            // When a person tag is found a new person starts to be processed
            // When a person tag end, the person is added to the result
            SaxTreeNode personNode = peopleNode
                    .putChildren(new SaxTreeNodeQNameImpl("person") {
                        public void startElement(String uri, String localName,
                                String qName, Attributes attributes) {
                            currentPerson = new Person();
                        }

                        public void endElement(String uri, String localName,
                                String qName) {
                            result.add(currentPerson);
                        }

            });

            // Two nodes are added to the person, the name and the age. The read
            // value is passed to the method parse
            // You just need to add the nodes that you want to process
            personNode.putChildren(new SaxTreeNodeQNameImpl("name") {
                public void parseValue(String value) {
                    currentPerson.name = value;
                }
            }, new SaxTreeNodeQNameImpl("age") {
                public void parseValue(String value) {
                    currentPerson.age = Integer.parseInt(value);
                }
            });

            // Returns the initial node
            return rootNode;
        }

        // Prepares the parser for reuse, it is called also in the first
        // initialization
        @Override
        public void clear() {
            result = new LinkedList<>();
        }
    }

    static String EXAMPLE_XML = "<root>" + "<people>" + "<person>"
            + "<name>Mr. Nobody</name>" + "<age>33</age>" + "</person>"
            + "<person>" + "<name>John Doe</name>" + "<age>28</age>"
            + "</person>" + "</people>" + "</root>";

    public static void main(String[] args) throws ParserConfigurationException,
            SAXException, IOException {
        ExempleSaxHandler handler = new ExempleSaxHandler();
        handler.init();
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();

        SAXParser saxParser = saxParserFactory.newSAXParser();
        InputSource iSource = new InputSource(new StringReader(EXAMPLE_XML));
        saxParser.parse(iSource, handler);

        for (Person p : handler.getResult()) {
            System.out.println("name: " + p.name + " - age: " + p.age);
        }

    }

}
