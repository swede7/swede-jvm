package org.swede.core.command;

import picocli.CommandLine.Command;

@Command(
        name = "swede",
        subcommands = {LspCommand.class, VersionCommand.class, FormatCommand.class}
)
public class BaseCommand {
}
