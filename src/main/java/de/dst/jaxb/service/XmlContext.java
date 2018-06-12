package de.dst.jaxb.service;

import javax.xml.bind.*;
import javax.xml.bind.util.JAXBResult;
import javax.xml.bind.util.JAXBSource;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  29.11.2017 11:47
 * modified by: $Author$
 * modified on: $Date$
 */
public class XmlContext<T> {

    protected final JAXBContext context;

    protected final Class<T> declaredType;

    protected XmlContext(Class<T> declaredType, JAXBContext context) {
        this.context = context;
        this.declaredType = declaredType;
    }

    public static <T> XmlContext<T> newContext(Class<T> declaredType, Class ... seeAlso) {
        try {
            Class types[] = new Class[seeAlso.length+1];
            types[0] = declaredType;
            System.arraycopy(seeAlso, 0, types, 1, seeAlso.length);
            JAXBContext context = JAXBContext.newInstance(types, ServiceAnnotationReader.properties());
            return new XmlContext<>(declaredType, context);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public JAXBContext getContext() {
        return context;
    }

    public Class<T> getDeclaredType() {
        return declaredType;
    }

    public JAXBSource marshal(T data) {
        try {
            Marshaller marshaller = context.createMarshaller();
            return new JAXBSource(marshaller, data);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public JAXBSource marshal(T data, String tagName) {
        try {
            Marshaller marshaller = context.createMarshaller();
            JAXBElement<T> element = new JAXBElement<>(new QName(tagName), declaredType, data);
            return new JAXBSource(marshaller, element);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public T unmarshal(Source source, ValidationEventHandler validator) {
        try {
            Unmarshaller unmarshaller = context.createUnmarshaller();
            unmarshaller.setEventHandler(validator);
            JAXBElement<T> element = unmarshaller.unmarshal(source, declaredType);
            return element.getValue();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public T unmarshal(Consumer<? super JAXBResult> reader, ValidationEventHandler validator) {
        try {
            Unmarshaller unmarshaller = context.createUnmarshaller();
            unmarshaller.setEventHandler(validator);
            JAXBResult result = new JAXBResult(unmarshaller);

            reader.accept(result);

            Object l_result = result.getResult();

            if(l_result instanceof JAXBElement) {
                l_result = ((JAXBElement)l_result).getValue();
            }

            return declaredType.cast(l_result);
        }  catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }
}
