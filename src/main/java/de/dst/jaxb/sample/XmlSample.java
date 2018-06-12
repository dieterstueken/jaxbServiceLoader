package de.dst.jaxb.sample;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  12.06.2018 15:34
 * modified by: $Author$
 * modified on: $Date$
 */
@XmlRootElement
public class XmlSample {

    @XmlElement(name="type")
    XmlSampleType type;

    public XmlSample() {
    }

    public XmlSample(XmlSampleType type) {
        this.type = type;
    }

    public String toString() {
        return type==null ? "null" : type.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        XmlSample sample = (XmlSample) o;
        return Objects.equals(type, sample.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }
}
