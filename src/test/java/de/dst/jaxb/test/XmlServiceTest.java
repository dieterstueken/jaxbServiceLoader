package de.dst.jaxb.test;

import de.dst.jaxb.sample.XmlSample;
import de.dst.jaxb.sample.XmlSampleImpl;
import de.dst.jaxb.sample.XmlSampleType;
import de.dst.jaxb.service.XmlContext;
import org.junit.Test;

import javax.xml.transform.*;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;

import static junit.framework.TestCase.assertEquals;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  12.06.2018 14:19
 * modified by: $Author$
 * modified on: $Date$
 */
public class XmlServiceTest {

    private XmlContext<XmlSample> context = XmlContext.newContext(XmlSample.class);

    private SAXTransformerFactory factory = ((SAXTransformerFactory) TransformerFactory.newInstance());

    private String marshal(XmlSample sample) throws TransformerException {
        CharArrayWriter output = new CharArrayWriter();
        Result result = new StreamResult(output);

        Transformer transformer = factory.newTransformer();
        transformer.transform(context.marshal(sample), result);
        return output.toString();
    }

    private XmlSample unmarshal(String xml) {
        StreamSource source = new StreamSource(new StringReader(xml));
        return context.unmarshal(source, null);
    }

    @Test
    public void serviceTest() throws TransformerException {

        XmlSample sample = new XmlSample(new XmlSampleImpl("hello world"));

        String xml = marshal(sample);

        XmlSample result = unmarshal(xml);

        assertEquals(result, sample);
    }
}
