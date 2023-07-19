package org.swede.junit5.descriptor;

import lombok.Getter;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor;

import java.io.File;

@Getter
public class SwedeFeatureFileDescriptor extends AbstractTestDescriptor {

    private final Class<?> stepImplClass;
    private final File featureFile;

    public SwedeFeatureFileDescriptor(UniqueId uniqueId, File featureFile, Class<?> stepImplClass) {
        super(uniqueId, featureFile.getName());
        this.featureFile = featureFile;
        this.stepImplClass = stepImplClass;
    }

    @Override
    public Type getType() {
        return Type.CONTAINER_AND_TEST;
    }
}
