package org.swede.junit5;

import org.reflections.Reflections;
import org.swede.junit5.annotation.SwedeTest;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.List;

import static org.reflections.scanners.Scanners.SubTypes;

public final class Utils {
    private Utils() {
    }

    public static File getFileFromResource(String path) {
        var url = Utils.class.getClassLoader().getResource(path);
        if (url == null) {
            throw new IllegalArgumentException("file not found!");
        }

        try {
            return new File(url.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Class<?>> getAllClassesByClasspath(String classpath) {
        return new Reflections(classpath, SubTypes.filterResultsBy(c -> true)).getSubTypesOf(Object.class).stream().toList();
    }

    public static String readTextFromFile(File file) {
        try {
            return Files.readString(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static SwedeExecuteConfiguration mapToExecuteConfiguration(SwedeTest swedeTest) {
        var configuration = new SwedeExecuteConfiguration();
        configuration.setStepImplClasspath(swedeTest.stepImplClasspath());
        configuration.setFeatureDirectory(swedeTest.featureDirectory());
        return configuration;
    }
}
