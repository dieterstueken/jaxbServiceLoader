# jaxbServiceLoader

Using polymorphic (abstract) classes with Jaxb requires all
implementations to be explicitly enumerated by a @SeeAlso annotation.

This is an attempt to extend jaxb by an extended RuntimeAnnotationReader.
This parses an additional @XmlServiceList annotation. Classes annotated
with @XmlServiceList trigger a scan of a Java ServiceLoader to search and
load additional classes via META-INF/services.

This way additional implementations of a class may be added with a jar
containing a service descrioption without any need to change the base package.
This is very usefull to avoid cyclic dependencies especially for future
Java 9+ modules.

Instead of using the build in Java ServiceLoader a new ServiceLocator is
used. It bases on Java 8 Streams and reads a list of classes instead
of a list of instances. This is just a playground. It would work using
standard Java ServiceLoader, too.