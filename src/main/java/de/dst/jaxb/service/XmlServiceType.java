package de.dst.jaxb.service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  13.08.2015 18:14
 * modified by: $Author$
 * modified on: $Date$
 */

/**
 * Class XmlServiceType annotates a class which may be loaded via JAXB.
 * It works like @SeeAlso but the other way around.
 */
@Target({ElementType.TYPE})
@Retention(RUNTIME)
public @interface XmlServiceType {
}
