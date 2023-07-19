package org.swede.junit5;

import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.EngineDescriptor;

public class SwedeEngineDescriptor extends EngineDescriptor {
    public SwedeEngineDescriptor(UniqueId uniqueId) {
        super(uniqueId, "swede");
    }
}
