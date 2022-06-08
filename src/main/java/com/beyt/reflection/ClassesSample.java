package com.beyt.reflection;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.reflect.ClassPath;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.base.StandardSystemProperty.JAVA_CLASS_PATH;
import static com.google.common.base.StandardSystemProperty.PATH_SEPARATOR;

@SuppressWarnings("all")
public class ClassesSample {
    public static void main(String[] args) throws IOException {
        // All Classses
        getAllClassesWithGuava();

        System.out.println("----------------------------------");

        //Class Path
        System.out.println(JAVA_CLASS_PATH);

        System.out.println("----------------------------------");

        //
        System.out.println(Stream.of(JAVA_CLASS_PATH.value().split(PATH_SEPARATOR.value())).collect(Collectors.joining("\n")));

        System.out.println("----------------------------------");

        splitClassPath();
    }


    private static void getAllClassesWithGuava() throws IOException {
        for (ClassPath.ClassInfo allClass : ClassPath.from(ClassLoader.getSystemClassLoader())
                .getAllClasses()) {
            System.out.println(allClass.getName());
        }
    }

    private static void splitClassPath() {
        ImmutableList.Builder<URL> urls = ImmutableList.builder();
        for (String entry : Splitter.on(PATH_SEPARATOR.value()).split(JAVA_CLASS_PATH.value())) {
            try {
                try {
                    urls.add(new File(entry).toURI().toURL());
                } catch (SecurityException e) { // File.toURI checks to see if the file is a directory
                    urls.add(new URL("file", null, new File(entry).getAbsolutePath()));
                }
            } catch (MalformedURLException e) {
            }
        }
        for (URL url : urls.build()) {
            System.out.println(url);
        }
    }
}
