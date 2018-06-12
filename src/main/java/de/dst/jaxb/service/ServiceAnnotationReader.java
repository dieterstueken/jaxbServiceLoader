package de.dst.jaxb.service;

import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.bind.api.JAXBRIContext;
import com.sun.xml.internal.bind.v2.model.annotation.Locatable;
import com.sun.xml.internal.bind.v2.model.annotation.LocatableAnnotation;
import com.sun.xml.internal.bind.v2.model.annotation.RuntimeAnnotationReader;
import com.sun.xml.internal.bind.v2.model.annotation.RuntimeInlineAnnotationReader;
import com.sun.xml.internal.bind.v2.model.core.ErrorHandler;

import javax.xml.bind.annotation.XmlSeeAlso;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  13.08.2015 17:20
 * modified by: $Author$
 * modified on: $Date$
 */

/**
 * Class ServiceAnnotationReader is an extended RuntimeAnnotationReader to include additional
 * JAXB classes from a ServiceLoader.
 *
 * Each query of XmlSeeAlso will also query if the bean has an XmlServiceType annotation.
 * If so, the list of SeeAlso classes is extended by all classes loades from the ServiceLoader.
 */

public class ServiceAnnotationReader implements RuntimeAnnotationReader {

    private final RuntimeInlineAnnotationReader reader = new RuntimeInlineAnnotationReader();

    // since RuntimeInlineAnnotationReader is final we have to delegate all methods
    // instead of just overriding getClassAnnotation.

    /**
     * Create a property to be configure JAXBContext.newInstance().
     * @return configuration property.
     */
    public static Map<String,?> properties() {
        return Collections.singletonMap(JAXBRIContext.ANNOTATION_READER, new ServiceAnnotationReader());
    }

    /**
     * Extended annotation reader to merge/extend XmlSeeAlso annotations
     * with list from a ServiceLoader if this class is annotated by XmlServiceType.
     * @param bean to be analyzed.
     * @param srcPos location within the source XML document.
     * @return an XmlSeeAlso annotation, or null.
     */
    private XmlSeeAlso getSeeAlsoAnnotation(Class<?> bean, Locatable srcPos) {

        // load regular annotation (may be null)
        XmlSeeAlso seeAlso = reader.getClassAnnotation(XmlSeeAlso.class, bean, srcPos);

        // no XmlServiceType: return regular result.
        if(bean.getAnnotation(XmlServiceType.class) == null) {
            return seeAlso;
        }

        // prepare the list of classes to add.
        final Collection<Class> classes = new HashSet<>();

        // add all classes returned by ServiceLoader for this type of bean.
        new ServiceLocator().loadClasses(bean).forEach(classes::add);

        // todo: generate error or warning if empty?

        // add any regular seeAlso classes.
        if(seeAlso!=null) {
            Collections.addAll(classes, seeAlso.value());
        }

        // generate a replacement annotation of the merged class list.
        seeAlso = new XmlSeeAlso(){
            @Override
            public Class<? extends Annotation> annotationType() {
                return XmlSeeAlso.class;
            }

            @Override
            public Class[] value() {
                return classes.stream().toArray(Class[]::new);
            }
        };

        // convert into a location aware annotation to provide expressive error messages.
        seeAlso = LocatableAnnotation.create(seeAlso, srcPos);

        return seeAlso;
    }

    @Override
    @Nullable
    @SuppressWarnings("unchecked")
    public <A extends Annotation> A getClassAnnotation(Class<A> annotationClass, Class bean, Locatable srcPos) {

        if(annotationClass==XmlSeeAlso.class) {
            return (A) getSeeAlsoAnnotation(bean, srcPos);
        } else {{
            return reader.getClassAnnotation(annotationClass, bean, srcPos);
        }}
    }

    @Override
    public Annotation[] getAllFieldAnnotations(Field field, Locatable srcPos) {
        return reader.getAllFieldAnnotations(field, srcPos);
    }

    @Override
    public Annotation[] getAllMethodAnnotations(Method method, Locatable srcPos) {
        return reader.getAllMethodAnnotations(method, srcPos);
    }

    @Override
    public Type[] getClassArrayValue(Annotation a, String name) {
        return reader.getClassArrayValue(a, name);
    }

    @Override
    public Type getClassValue(Annotation a, String name) {
        return reader.getClassValue(a, name);
    }

    @Override
    public <A extends Annotation> A getFieldAnnotation(Class<A> annotation, Field field, Locatable srcpos) {
        return reader.getFieldAnnotation(annotation, field, srcpos);
    }

    @Override
    public <A extends Annotation> A getMethodAnnotation(Class<A> annotation, Method getter, Method setter, Locatable srcpos) {
        return reader.getMethodAnnotation(annotation, getter, setter, srcpos);
    }

    @Override
    public <A extends Annotation> A getMethodAnnotation(Class<A> annotation, Method method, Locatable srcpos) {
        return reader.getMethodAnnotation(annotation, method, srcpos);
    }

    @Override
    @Nullable
    public <A extends Annotation> A getMethodParameterAnnotation(Class<A> annotation, Method method, int paramIndex, Locatable srcPos) {
        return reader.getMethodParameterAnnotation(annotation, method, paramIndex, srcPos);
    }

    @Override
    @Nullable
    public <A extends Annotation> A getPackageAnnotation(Class<A> annotation, Class clazz, Locatable srcpos) {
        return reader.getPackageAnnotation(annotation, clazz, srcpos);
    }

    @Override
    public boolean hasClassAnnotation(Class clazz, Class<? extends Annotation> annotationType) {
        return reader.hasClassAnnotation(clazz, annotationType);
    }

    @Override
    public boolean hasFieldAnnotation(Class<? extends Annotation> annotationType, Field field) {
        return reader.hasFieldAnnotation(annotationType, field);
    }

    @Override
    public boolean hasMethodAnnotation(Class<? extends Annotation> annotation, Method method) {
        return reader.hasMethodAnnotation(annotation, method);
    }

    @Override
    public boolean hasMethodAnnotation(Class<? extends Annotation> annotation, String propertyName, Method getter, Method setter, Locatable srcPos) {
        return reader.hasMethodAnnotation(annotation, propertyName, getter, setter, srcPos);
    }

    @Override
    public void setErrorHandler(ErrorHandler errorHandler) {
        reader.setErrorHandler(errorHandler);
    }
}
