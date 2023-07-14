package org.swede.core.command;

import picocli.CommandLine;

@CommandLine.Command(
        name = "swede",
        subcommands = {LspCommand.class, VersionCommand.class}
)
public class BaseCommand {
}
