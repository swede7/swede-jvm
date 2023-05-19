package org.swede.cli.command;

import picocli.CommandLine;

@CommandLine.Command(
        name = "swede",
        subcommands = {LspCommand.class}
)
public class BaseCommand {
}
