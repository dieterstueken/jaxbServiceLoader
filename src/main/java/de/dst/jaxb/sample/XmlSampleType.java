package de.dst.jaxb.sample;

import de.dst.jaxb.service.XmlServiceType;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  11.06.2018 18:41
 * modified by: $Author$
 * modified on: $Date$
 */

@XmlServiceType
@XmlRootElement(name="XmlSample")
abstract public class XmlSampleType {

    abstract public String getName();
}
