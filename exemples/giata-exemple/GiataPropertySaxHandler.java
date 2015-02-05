package com.onestop.batch.loaders.giata;

import javax.xml.bind.DatatypeConverter;

import org.xml.sax.Attributes;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.onestop.util.xml.sax.tree.SaxTreeHandlerBase;
import com.onestop.util.xml.sax.tree.SaxTreeNode;
import com.onestop.util.xml.sax.tree.SaxTreeNodeQNameImpl;

public class GiataPropertySaxHandler extends SaxTreeHandlerBase<BasicDBObject> {

    private BasicDBObject cityObject;

    private BasicDBObject currentAddressObject;

    private BasicDBList currentAddressLines;

    private BasicDBList currentAdditionalAddressInformation;

    private BasicDBList addressList;

    private BasicDBList phoneList;

    private BasicDBObject currentPhoneObject;

    private int lastAddressLineIndex;

    private BasicDBList emailList;

    private BasicDBObject currentEmailObject;

    private BasicDBList urlList;

    private BasicDBList geoCodeList;

    private BasicDBObject currentGeoCodeObject;

    private BasicDBList propertyCodeList;

    private BasicDBObject currentProviderObject;

    private BasicDBList currentProviderCodeList;

    private BasicDBList currentProviderCodeValueList;

    private BasicDBObject currentProviderCodeValueObject;

    private BasicDBList chainList;

    @Override
    public SaxTreeNode initTree() {
        SaxTreeNode rootNode = new SaxTreeNodeQNameImpl("properties");

        // Property is the result object
        SaxTreeNode propertyNode = rootNode.putChildren(buildPropertyNode());

        propertyNode.putChildren(buildShallowPropertyNodes());
        propertyNode.putChildren(buildCityNode());
        propertyNode.putChildren(buildAddressesNode().putChildren(
                buildAddressNode()).getParent());
        propertyNode.putChildren(buildPhonesNode()
                .putChildren(buildPhoneNode()).getParent());
        propertyNode.putChildren(buildEmailsNode()
                .putChildren(buildEmailNode()).getParent());
        propertyNode.putChildren(buildUrlsNode().putChildren(buildUrlNode())
                .getParent());
        propertyNode.putChildren(buildGeoCodesNode().putChildren(
                buildGeoCodeNode()).getParent());
        propertyNode.putChildren(buildPropertyCodesNode().putChildren(
                buildProviderNode()).getParent());
        propertyNode.putChildren(buildChainsNode());

        return rootNode;
    }

    private SaxTreeNode buildPropertyNode() {
        return new SaxTreeNodeQNameImpl("property") {
            public void startElement(String uri, String localName,
                    String qName, Attributes attributes) {
                result = new BasicDBObject();

                String[] attValues = getAttributeValues(attributes, "giataId",
                        "lastUpdate");

                result.put("giataId", Integer.parseInt(attValues[0]));

                result.put("_id", Integer.parseInt(attValues[0]));

                if (attValues[1] != null) {
                    result.put("lastUpdate",
                            DatatypeConverter.parseDateTime(attValues[1])
                                    .getTime());
                }
            }
        };
    }

    public void copyAttributeValuesAsString(Attributes attributes,
            BasicDBObject copyIntoObject, String... attQNames) {
        String[] attValues = getAttributeValues(attributes, attQNames);
        for (int n = 0; n < attQNames.length; n++) {
            if (attValues[n] != null) {
                copyIntoObject.put(attQNames[n], attValues[n]);
            }
        }
    }

    private SaxTreeNode[] buildShallowPropertyNodes() {
        SaxTreeNode[] arrayResult = { new SaxTreeNodeQNameImpl("name") {
            public void parseValue(String value) {
                result.append("name", value);
            }
        }, new SaxTreeNodeQNameImpl("alternativeName") {
            public void parseValue(String value) {
                result.append("alternativeName", value);
            }
        }, new SaxTreeNodeQNameImpl("country") {
            public void parseValue(String value) {
                result.append("country", value);
            }
        }, new SaxTreeNodeQNameImpl("ghgml") {
            public void startElement(String uri, String localName,
                    String qName, Attributes attributes) {
                String[] attValues = getAttributeValues(attributes,
                        "lastUpdate", "xlink:href");
                BasicDBObject ghgmlObject = new BasicDBObject();
                if (attValues[0] != null) {
                    ghgmlObject
                            .put("lastUpdate",
                                    DatatypeConverter.parseDate(attValues[0])
                                            .getTime());
                }
                ghgmlObject.put("xlink:href", attValues[1]);
                result.put("ghgml", ghgmlObject);
            }
        } };
        return arrayResult;
    }

    private SaxTreeNode[] buildAddressShallowNodes() {
        SaxTreeNode[] arrayResult = { new SaxTreeNodeQNameImpl("street") {
            public void parseValue(String value) {
                currentAddressObject.append("street", value);
            }
        }, new SaxTreeNodeQNameImpl("streetNumber") {
            public void parseValue(String value) {
                currentAddressObject.append("streetNumber", value);
            }
        }, new SaxTreeNodeQNameImpl("cityName") {
            public void parseValue(String value) {
                currentAddressObject.append("cityName", value);
            }
        }, new SaxTreeNodeQNameImpl("locality") {
            public void parseValue(String value) {
                currentAddressObject.append("locality", value);
            }
        }, new SaxTreeNodeQNameImpl("postalCode") {
            public void parseValue(String value) {
                currentAddressObject.append("postalCode", value);
            }
        }, new SaxTreeNodeQNameImpl("stateProv") {
            public void parseValue(String value) {
                currentAddressObject.append("stateProv", value);
            }
        }, new SaxTreeNodeQNameImpl("poBox") {
            public void parseValue(String value) {
                currentAddressObject.append("poBox", value);
            }
        }, new SaxTreeNodeQNameImpl("country") {
            public void parseValue(String value) {
                currentAddressObject.append("country", value);
            }
        } };
        return arrayResult;
    }

    private SaxTreeNode buildAddressesNode() {
        return new SaxTreeNodeQNameImpl("addresses") {
            public void startElement(String uri, String localName,
                    String qName, Attributes attributes) {
                addressList = new BasicDBList();
            }

            public void endElement(String uri, String localName, String qName) {
                if (!addressList.isEmpty()) {
                    result.put("addresses", addressList);
                }
            }
        };
    }

    private SaxTreeNode buildAddressNode() {
        final SaxTreeNode addressNode = new SaxTreeNodeQNameImpl("address") {
            public void startElement(String uri, String localName,
                    String qName, Attributes attributes) {
                currentAddressObject = new BasicDBObject();
                currentAddressLines = new BasicDBList();
                currentAdditionalAddressInformation = new BasicDBList();
            }

            public void endElement(String uri, String localName, String qName) {
                if (!currentAddressLines.isEmpty()) {
                    currentAddressObject.append("addressLines",
                            currentAddressLines);
                }
                if (!currentAdditionalAddressInformation.isEmpty()) {
                    currentAddressObject.append(
                            "additionalAddressInformations",
                            currentAdditionalAddressInformation);
                }
                addressList.add(currentAddressObject);
            }
        };
        addressNode.putChildren(buildAddressShallowNodes());

        addressNode.putChildren(new SaxTreeNodeQNameImpl("addressLine") {
            public void startElement(String uri, String localName,
                    String qName, Attributes attributes) {
                String[] attValues = getAttributeValues(attributes,
                        "addressLineNumber");
                if (attValues[0] == null) {
                    lastAddressLineIndex = 0;
                } else {
                    lastAddressLineIndex = Integer.parseInt(attValues[0]) - 1;
                    if (lastAddressLineIndex < 0) {
                        lastAddressLineIndex = 0;
                    }
                }
            }

            public void parseValue(String value) {
                currentAddressLines.put(lastAddressLineIndex, value);
            }
        });

        addressNode.putChildren(new SaxTreeNodeQNameImpl(
                "additionalAddressInformation") {
            public void parseValue(String value) {
                currentAdditionalAddressInformation.add(value);
            }
        });

        return addressNode;
    }

    private SaxTreeNode buildPhonesNode() {
        return new SaxTreeNodeQNameImpl("phones") {
            public void startElement(String uri, String localName,
                    String qName, Attributes attributes) {
                phoneList = new BasicDBList();
            }

            public void endElement(String uri, String localName, String qName) {
                if (!phoneList.isEmpty()) {
                    result.put("phones", phoneList);
                }
            }
        };
    }

    private SaxTreeNode buildPhoneNode() {
        return new SaxTreeNodeQNameImpl("phone") {
            public void startElement(String uri, String localName,
                    String qName, Attributes attributes) {
                currentPhoneObject = new BasicDBObject();
                String[] attValues = getAttributeValues(attributes, "isDefault");
                if (attValues[0] != null) {
                    currentPhoneObject
                            .put("isDefault", toBoolean(attValues[0]));
                }
                copyAttributeValuesAsString(attributes, currentPhoneObject,
                        "countryAccessCode", "areaCityCode", "phoneNumber",
                        "extension", "tech", "use");

            }

            public void parseValue(String value) {
                currentPhoneObject.put("phone", value);
            }

            public void endElement(String uri, String localName, String qName) {
                phoneList.add(currentPhoneObject);
            }
        };
    }

    private SaxTreeNode buildCityNode() {
        return new SaxTreeNodeQNameImpl("city") {
            public void startElement(String uri, String localName,
                    String qName, Attributes attributes) {
                cityObject = new BasicDBObject();
                String[] attValues = getAttributeValues(attributes, "cityId");
                if (attValues[0] != null) {
                    cityObject.append("cityId", Integer.parseInt(attValues[0]));
                }
            }

            public void parseValue(String value) {
                cityObject.append("city", value);
            }
        };
    }

    private SaxTreeNode buildEmailsNode() {
        return new SaxTreeNodeQNameImpl("emails") {
            public void startElement(String uri, String localName,
                    String qName, Attributes attributes) {
                emailList = new BasicDBList();
            }

            public void endElement(String uri, String localName, String qName) {
                if (!phoneList.isEmpty()) {
                    result.put("emails", emailList);
                }
            }
        };
    }

    private SaxTreeNode buildEmailNode() {
        return new SaxTreeNodeQNameImpl("email") {
            public void startElement(String uri, String localName,
                    String qName, Attributes attributes) {
                currentEmailObject = new BasicDBObject();
                String[] attValues = getAttributeValues(attributes, "isDefault");
                if (attValues[0] != null) {
                    currentEmailObject.append("isDefault",
                            toBoolean(attValues[0]));
                }
            }

            public void parseValue(String value) {
                currentEmailObject.append("email", value);
            }

            public void endElement(String uri, String localName, String qName) {
                emailList.add(currentEmailObject);
            }
        };
    }

    private SaxTreeNode buildUrlsNode() {
        return new SaxTreeNodeQNameImpl("urls") {
            public void startElement(String uri, String localName,
                    String qName, Attributes attributes) {
                urlList = new BasicDBList();
            }

            public void endElement(String uri, String localName, String qName) {
                if (!urlList.isEmpty()) {
                    result.put("urls", urlList);
                }
            }
        };
    }

    private SaxTreeNode buildUrlNode() {
        return new SaxTreeNodeQNameImpl("url") {
            public void parseValue(String value) {
                urlList.add(value);
            }
        };
    }

    private SaxTreeNode buildGeoCodesNode() {
        return new SaxTreeNodeQNameImpl("geoCodes") {
            public void startElement(String uri, String localName,
                    String qName, Attributes attributes) {
                geoCodeList = new BasicDBList();
            }

            public void endElement(String uri, String localName, String qName) {
                if (!geoCodeList.isEmpty()) {
                    result.put("geoCodes", geoCodeList);
                }
            }
        };
    }

    private SaxTreeNode buildGeoCodeNode() {
        final SaxTreeNodeQNameImpl geoCodeNode = new SaxTreeNodeQNameImpl(
                "geoCode") {
            public void startElement(String uri, String localName,
                    String qName, Attributes attributes) {
                currentGeoCodeObject = new BasicDBObject();
                copyAttributeValuesAsString(attributes, currentGeoCodeObject,
                        "accuracy");
            }

            public void endElement(String uri, String localName, String qName) {
                geoCodeList.add(currentGeoCodeObject);
            }
        };

        geoCodeNode.putChildren(new SaxTreeNodeQNameImpl("latitude") {
            public void parseValue(String value) {
                currentGeoCodeObject.put("latitude",
                        DatatypeConverter.parseFloat(value));
            }
        });
        geoCodeNode.putChildren(new SaxTreeNodeQNameImpl("longitude") {
            public void parseValue(String value) {
                currentGeoCodeObject.put("longitude",
                        DatatypeConverter.parseFloat(value));
            }
        });

        return geoCodeNode;
    }

    private SaxTreeNode buildPropertyCodesNode() {
        return new SaxTreeNodeQNameImpl("propertyCodes") {
            public void startElement(String uri, String localName,
                    String qName, Attributes attributes) {
                propertyCodeList = new BasicDBList();
            }

            public void endElement(String uri, String localName, String qName) {
                if (!propertyCodeList.isEmpty()) {
                    result.put("propertyCodes", propertyCodeList);
                }
            }
        };
    }

    private SaxTreeNode buildProviderNode() {
        final SaxTreeNode providerNode = new SaxTreeNodeQNameImpl("provider") {
            public void startElement(String uri, String localName,
                    String qName, Attributes attributes) {
                currentProviderObject = new BasicDBObject();
                currentProviderCodeList = new BasicDBList();
                copyAttributeValuesAsString(attributes, currentProviderObject,
                        "providerCode", "providerId", "providerName",
                        "providerType");
            }

            public void endElement(String uri, String localName, String qName) {
                currentProviderObject.put("codes", currentProviderCodeList);
                propertyCodeList.add(currentProviderObject);
            }
        };
        providerNode.putChildren(new SaxTreeNodeQNameImpl("code") {
            public void startElement(String uri, String localName,
                    String qName, Attributes attributes) {
                currentProviderCodeValueList = new BasicDBList();
            }

            public void endElement(String uri, String localName, String qName) {
                currentProviderCodeList.add(currentProviderCodeValueList);
            }
        }.putChildren(new SaxTreeNodeQNameImpl("value") {
            public void startElement(String uri, String localName,
                    String qName, Attributes attributes) {
                currentProviderCodeValueObject = new BasicDBObject();
                copyAttributeValuesAsString(attributes,
                        currentProviderCodeValueObject, "name");
            }

            public void parseValue(String value) {
                currentProviderCodeValueObject.put("value", value);
            }

            public void endElement(String uri, String localName, String qName) {
                currentProviderCodeValueList
                        .add(currentProviderCodeValueObject);
            }
        }).getParent());

        return providerNode;
    }

    private SaxTreeNode buildChainsNode() {
        return new SaxTreeNodeQNameImpl("chains") {
            public void startElement(String uri, String localName,
                    String qName, Attributes attributes) {
                chainList = new BasicDBList();
            }

            public void endElement(String uri, String localName, String qName) {
                if (!chainList.isEmpty()) {
                    result.put("chains", chainList);
                }
            }
        }.putChildren(new SaxTreeNodeQNameImpl("chain") {
            public void startElement(String uri, String localName,
                    String qName, Attributes attributes) {
                BasicDBObject chainObject = new BasicDBObject();
                copyAttributeValuesAsString(attributes, chainObject,
                        "chainCode", "chainName");
                String[] attValues = getAttributeValues(attributes, "chainId",
                        "fromDate", "toDate");
                if (attValues[0] != null) {
                    chainObject.put("chainId", Integer.parseInt(attValues[0]));
                }
                if (attValues[1] != null) {
                    chainObject
                            .put("fromDate",
                                    DatatypeConverter.parseDate(attValues[1])
                                            .getTime());
                }
                if (attValues[2] != null) {
                    chainObject
                            .put("toDate",
                                    DatatypeConverter.parseDate(attValues[2])
                                            .getTime());
                }
                chainList.add(chainObject);
            }
        }).getParent();
    }

    @Override
    public void clear() {
        // TODO Auto-generated method stub

    }

}
