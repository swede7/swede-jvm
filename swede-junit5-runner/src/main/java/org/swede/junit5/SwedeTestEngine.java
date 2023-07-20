package org.swede.junit5;

import org.junit.platform.engine.*;
import org.swede.junit5.descriptor.SwedeEngineDescriptor;
import org.swede.junit5.descriptor.SwedeFeatureFileDescriptor;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class SwedeTestEngine implements TestEngine {

    private static final String ENGINE_ID = "swede";

    @Override
    public String getId() {
        return ENGINE_ID;
    }

    @Override
    public TestDescriptor discover(EngineDiscoveryRequest request, UniqueId uniqueId) {
        var engineDescriptor = new SwedeEngineDescriptor(uniqueId);

        List<SwedeExecuteConfiguration> configurations = SwedeExecuteConfigurationResolver.findExecuteConfigurations(request);
        if (configurations.size() != 1) {
            return engineDescriptor;
        }

        SwedeExecuteConfiguration configuration = configurations.get(0);
        addTestsToEngineDescriptor(configuration, engineDescriptor);

        return engineDescriptor;
    }

    private void addTestsToEngineDescriptor(SwedeExecuteConfiguration configuration, SwedeEngineDescriptor engineDescriptor) {
        String featureDirectory = configuration.getFeatureDirectory();
        String stepImplClasspath = configuration.getStepImplClasspath();

        List<File> featureFiles = findFeatureFiles(featureDirectory);
        List<Class<?>> steImplClasses = findStepImplClasses(stepImplClasspath);

        for (File featureFile : featureFiles) {
            Class<?> stepImplClass = findStepImplClassByFeatureFileName(featureFile.getName(), steImplClasses).orElse(null);

            var featureFileUniqueId = engineDescriptor.getUniqueId().append("file", featureFile.getName());
            var featureFileDescriptor = new SwedeFeatureFileDescriptor(featureFileUniqueId, featureFile, stepImplClass);

            engineDescriptor.addChild(featureFileDescriptor);
        }
    }

    private Optional<Class<?>> findStepImplClassByFeatureFileName(String featureFileName, List<Class<?>> allStepImplClasses) {
        String featureName = featureFileName.replace(".swede", "");

        for (var stepImplClass : allStepImplClasses) {
            String implClassNameWithoutPostfix = stepImplClass.getSimpleName().replace("StepImpl", "");

            if (implClassNameWithoutPostfix.equals(featureName)) {
                return Optional.of(stepImplClass);
            }
        }
        return Optional.empty();
    }

    private List<File> findFeatureFiles(String path) {
        File featureDirFile = Utils.getFileFromResource(path);
        File[] featureFiles = featureDirFile.listFiles();

        if (featureFiles == null) {
            throw new RuntimeException("Cant find feature files");
        }
        return Arrays.stream(featureFiles).filter(File::isFile).toList();
    }

    private List<Class<?>> findStepImplClasses(String classpath) {
        return Utils.getAllClassesByClasspath(classpath);
    }


    @Override
    public void execute(ExecutionRequest request) {
        var executor = new Executor(request);
        executor.execute();
    }


}
