package org.swede.cli;

import org.swede.cli.command.BaseCommand;
import picocli.CommandLine;

public class Main {
    public static void main(String[] args) {
        int exitCode = new CommandLine(new BaseCommand()).execute(args);
        System.exit(exitCode);
    }
}
