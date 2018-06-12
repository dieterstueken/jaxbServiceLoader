package de.dst.jaxb.sample;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Objects;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  11.06.2018 18:41
 * modified by: $Author$
 * modified on: $Date$
 */

@XmlType(name="XmlSampleImpl")
public class XmlSampleImpl extends XmlSampleType {

    @XmlElement(name="name")
    private String name;

    public XmlSampleImpl() {
    }

    public XmlSampleImpl(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return XmlSampleType.class.getSimpleName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        XmlSampleImpl xmlSample = (XmlSampleImpl) o;
        return Objects.equals(name, xmlSample.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
