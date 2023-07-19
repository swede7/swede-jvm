package org.swede.junit5;

import org.junit.platform.commons.support.AnnotationSupport;
import org.junit.platform.commons.support.ReflectionSupport;
import org.junit.platform.engine.EngineDiscoveryRequest;
import org.junit.platform.engine.discovery.ClassSelector;
import org.junit.platform.engine.discovery.ClasspathRootSelector;
import org.junit.platform.engine.discovery.PackageSelector;
import org.swede.junit5.annotation.SwedeTest;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class ConfigurationResolver {

    private static final Predicate<Class<?>> CONTAINS_SWEDE_TEST_ANNOTATION_PREDICATE = classCandidate -> AnnotationSupport.isAnnotated(classCandidate, SwedeTest.class);

    public List<SwedeExecuteConfiguration> findExecuteConfigurations(EngineDiscoveryRequest request) {
        var configurations = new ArrayList<SwedeExecuteConfiguration>();

        request.getSelectorsByType(ClassSelector.class).forEach(classSelector -> {
            var configurationOptional = findExecuteConfigurationInClass(classSelector.getJavaClass());
            configurationOptional.ifPresent(configurations::add);
        });

        request.getSelectorsByType(PackageSelector.class).forEach(packageSelector -> {
            configurations.addAll(findExecuteConfigurationsInPackage(packageSelector.getPackageName()));
        });

        request.getSelectorsByType(ClasspathRootSelector.class).forEach(classpathRootSelector -> {
            configurations.addAll(findExecuteConfigurationsInClasspathRoot(classpathRootSelector.getClasspathRoot()));
        });

        return configurations;
    }

    private List<SwedeExecuteConfiguration> findExecuteConfigurationsInClasspathRoot(URI uri) {
        return ReflectionSupport.findAllClassesInClasspathRoot(uri, CONTAINS_SWEDE_TEST_ANNOTATION_PREDICATE, name -> true) //
                .stream().map(this::findExecuteConfigurationInClass).filter(Optional::isPresent).map(Optional::get).toList();
    }

    private List<SwedeExecuteConfiguration> findExecuteConfigurationsInPackage(String packageName) {
        return ReflectionSupport.findAllClassesInPackage(packageName, CONTAINS_SWEDE_TEST_ANNOTATION_PREDICATE, name -> true) //
                .stream().map(this::findExecuteConfigurationInClass).filter(Optional::isPresent).map(Optional::get).toList();
    }

    private Optional<SwedeExecuteConfiguration> findExecuteConfigurationInClass(Class<?> javaClass) {
        var annotation = javaClass.getAnnotation(SwedeTest.class);

        if (annotation == null) {
            return Optional.empty();
        }

        var configuration = Utils.mapToExecuteConfiguration(annotation);
        return Optional.of(configuration);
    }

}
