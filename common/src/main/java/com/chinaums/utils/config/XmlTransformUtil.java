package com.chinaums.utils.config;


import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

public class XmlTransformUtil
{
    public static Document builerDocument(InputStream is)
            throws ParserConfigurationException, FactoryConfigurationError, SAXException, IOException
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false);
        factory.setIgnoringComments(true);
        factory.setIgnoringElementContentWhitespace(false);
        factory.setCoalescing(false);
        factory.setExpandEntityReferences(true);

        DocumentBuilder builder = factory.newDocumentBuilder();

        return builder.parse(is);
    }

    public static String transDOM2String(Node dom)
    {
        TransformerFactory tFactory = TransformerFactory.newInstance();
        StringWriter sw = new StringWriter();
        StreamResult sr = new StreamResult(sw);
        String strXML = null;
        try {
            DOMSource domSource = new DOMSource(dom);
            Transformer transformer = tFactory.newTransformer();
            transformer.setOutputProperty("encoding", "GB2312");
            transformer.transform(domSource, sr);
            strXML = sw.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strXML;
    }
}