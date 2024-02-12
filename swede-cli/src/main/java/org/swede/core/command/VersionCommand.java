package org.swede.core.command;

import picocli.CommandLine.Command;

@Command(name = "version")
public class VersionCommand implements Runnable {

    private static final String VERSION = "0.1";

    @Override
    public void run() {
        System.out.println("version: " + VERSION);
    }
}
