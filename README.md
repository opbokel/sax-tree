A light weight, open source and fast XML lib that helps using the Java SAX parser. The idea is to use inversion of control with a tree representation of the XML document. For each node the engine changes the handler responsible for processing that node and use it to execute the correspondent SAX actions, making it much more organized since each handler is self contained. A full example of a complex XML parsing and the Java code of this example is provided in the examples folder.


A very simple example of a simple XML parsing:
```xml
<root>
    <people>
	     <person>
		      <name>Mr. Nobody</name>
	      	<age>33</age>
	     </person>
	     <person>
		      <name>John Doe</name>
		      <age>28</age>
	     </person>
    </people>
</root>
```

```java
    public static class GiataLinkSaxHandler extends
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

            // Two nodes are added to the person, the name and the age. 
            // The read value is passed to the parse method
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
```
Using the Sax Parser:
```java
    public static void main(String[] args) throws ParserConfigurationException,
            SAXException, IOException {
        ExempleSaxHandler handler = new ExempleSaxHandler();
        handler.init();
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();

        SAXParser saxParser = saxParserFactory.newSAXParser();
        //EXAMPLE_XML = A String with the XML above 
        InputSource iSource = new InputSource(new StringReader(EXAMPLE_XML));
        saxParser.parse(iSource, handler);

        for (Person p : handler.getResult()) {
            System.out.println("name: " + p.name + " - age: " + p.age);
        }

    }
```
The printed result:<BR>
name: Mr. Nobody - age: 33 <BR>
name: John Doe - age: 28
	

Fell free to contact me at GitHub any time. I will be glad to receive code contributions or answer questions.
