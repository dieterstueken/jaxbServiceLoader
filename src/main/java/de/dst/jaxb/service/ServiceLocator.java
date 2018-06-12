package de.dst.jaxb.service;

import java.io.*;
import java.net.URL;
import java.util.Enumeration;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  29.01.2018 14:11
 * modified by: $Author$
 * modified on: $Date$
 */

/**
 * Class ServiceLocator is an alternative to java.util.ServiceLoader.
 * Instead of loading instances, this implementation returns a Java 8 stream of classes.
 */
public class ServiceLocator {

    private final String root;

    final ClassLoader loader;

    public ServiceLocator() {
        this("META-INF/services/");
    }

    public ServiceLocator(String root) {
        this(root, Thread.currentThread().getContextClassLoader());
    }

    public ServiceLocator(String root, ClassLoader loader) {
        this.root = root;
        this.loader = loader;
    }

    public <T> Stream<T> load(Class<T> type) {
        return loadClasses(type).map(ServiceLocator::newInstance);
    }

    static <T> T newInstance(Class<T> type) {
        try {
            return type.newInstance();
        } catch (InstantiationException|IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> Stream<Class<? extends T>> loadClasses(Class<T> type) {

        return loadNames(type.getName())
                .map(this::loadClass)
                .map(t -> t.asSubclass(type));
    }

    public Stream<String> loadNames(String name) {

        return asStream(getResources(name))
                .flatMap(this::readResources)
                .map(ServiceLocator::trim)
                .filter(line -> !line.isEmpty());
    }

    Enumeration<URL> getResources(String name) {
        try {
            String fullName = root + name;
            return loader.getResources(fullName);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    Stream<String> readResources(URL url) {
        try {
            InputStream in = url.openStream();
            Reader reader = new InputStreamReader(in);
            return new BufferedReader(reader)
                    .lines();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    static String trim(String line) {

        // strip trailing comments
        int i = line.indexOf('#');
        if(i>=0)
            line = line.substring(0, i);

        line = line.trim();

        return line;
    }

    Class<?> loadClass(String name) {
        try {
            return loader.loadClass(name);
        } catch (ClassNotFoundException e) {
            throw new TypeNotPresentException(name, e);
        }
    }

    public static <T> Stream<T> asStream(Enumeration<T> e) {
        return StreamSupport.stream(
                new Spliterators.AbstractSpliterator<T>(Long.MAX_VALUE, Spliterator.ORDERED) {
                    public boolean tryAdvance(Consumer<? super T> action) {
                        if (e.hasMoreElements()) {
                            action.accept(e.nextElement());
                            return true;
                        }
                        return false;
                    }

                    public void forEachRemaining(Consumer<? super T> action) {
                        while (e.hasMoreElements()) action.accept(e.nextElement());
                    }
                }, false);
    }

}
