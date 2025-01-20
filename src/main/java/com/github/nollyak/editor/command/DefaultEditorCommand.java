package com.github.nollyak.editor.command;

import cn.nukkit.Player;

import cn.nukkit.block.Block;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

import com.github.nollyak.NoWorld;

import com.github.nollyak.editor.operation.RedoOperation;
import com.github.nollyak.editor.operation.ReplaceOperation;
import com.github.nollyak.editor.operation.SetOperation;
import com.github.nollyak.editor.operation.UndoOperation;

import lombok.Getter;

@Getter
public class DefaultEditorCommand extends Command {

    private final NoWorld plugin;

    public DefaultEditorCommand(
        final NoWorld plugin
    ) {
        super(
            "editor",
            "The main command for the world editor",
            null,
            new String[]{"we"}
        );
        this.plugin = plugin;
    }

    @Override
    public boolean execute(
        final CommandSender sender,
        final String commandLabel,
        final String[] arguments
    ) {
        if (!(
            sender instanceof final Player player
        )) {
            sender.sendMessage("This command can only be executed by a player");
            return false;
        }
        if (!(
            player.hasPermission(this.getPermission())
        )) {
            player.sendMessage("You don't have permission to execute this command");
            return false;
        }

        if (
            arguments.length == 0
        ) {
            player.sendMessage("You have to provide a command");
            return false;
        }

        return switch (arguments[0]) {
            case "help", "h" -> {
                player.sendMessage("""
                    /editor help - Shows this help message
                    /editor selection[sel] - Selection commands
                    /editor set <block id> - Sets the block at the current selection
                    /editor replace <from block id> <to block id> - Replaces the block at the current selection with the given block
                    /editor undo - Undoes the last operation
                    /editor redo - Redoes the last undone operation
                    """);
                yield true;
            }
            case "selection", "sel" -> {
                if (
                    arguments.length == 1
                ) {
                    player.sendMessage("No arguments provided, use /editor selection help to see the available arguments");
                    yield false;
                }
                yield switch (arguments[1]) {
                    case "help", "h" -> {
                        player.sendMessage("""
                            /editor selection[sel] help - Shows this help message
                            /editor selection[sel] point[p] first[f] - Sets the first selection point
                            /editor selection[sel] point[p] second[s] - Sets the second selection point
                            /editor selection[sel] clear[c] - Clears the selection
                            """);
                        yield true;
                    }
                    case "point", "p" -> {
                        if (
                            arguments.length == 2
                        ) {
                            player.sendMessage("No point argument provided, try /editor selection[sel] point[p] first[f] or /editor selection[sel] point[p] second[s]");
                            yield false;
                        }
                        yield switch (arguments[2]) {
                            case "first", "f" -> {
                                final var session = this.getPlugin()
                                    .getSessionService()
                                    .getSession(player.getName())
                                    .orElseThrow();

                                final var selection = session.selection();

                                if (
                                    selection.hasFirstPoint()
                                ) {
                                    player.sendMessage("You already have a first point");
                                    yield false;
                                }

                                selection.setFirstPoint(player.getPosition().floor());

                                player.sendMessage(String.format(
                                    "Set first point to %s",
                                    player.getPosition()
                                ));
                                yield true;
                            }
                            case "second", "s" -> {
                                final var session = this.getPlugin()
                                    .getSessionService()
                                    .getSession(player.getName())
                                    .orElseThrow();

                                final var selection = session.selection();

                                if (
                                    selection.hasSecondPoint()
                                ) {
                                    player.sendMessage("You already have a second point");
                                    yield false;
                                }

                                selection.setSecondPoint(player.getPosition().floor());

                                player.sendMessage(String.format(
                                    "Set second point to %s",
                                    player.getPosition()
                                ));
                                yield true;
                            }
                            default -> {
                                player.sendMessage(String.format("Unknown point '%s' was provided", arguments[2]));
                                yield false;
                            }
                        };
                    }
                    case "clear", "c" -> {
                        final var session = this.getPlugin()
                            .getSessionService()
                            .getSession(player.getName())
                            .orElseThrow();

                        final var selection = session.selection();

                        selection.setFirstPoint(null);
                        selection.setSecondPoint(null);

                        player.sendMessage("Cleared all selection points");

                        yield true;
                    }
                    default -> {
                        player.sendMessage(String.format("Unknown selection command '%s' was provided", arguments[1]));
                        yield false;
                    }
                };
            }
            case "set" -> {
                final var session = this.getPlugin()
                    .getSessionService()
                    .getSession(player.getName())
                    .orElseThrow();

                final var selection = session.selection();

                if (!(
                    selection.hasFirstPoint() &&
                        selection.hasSecondPoint()
                )) {
                    player.sendMessage("You have to set a first point and a second point first");
                    yield false;
                }

                if (
                    arguments.length == 1
                ) {
                    player.sendMessage("You have to provide a block id");
                    yield false;
                }

                final var blockArgument = arguments[1];

                if (!(
                    blockArgument.matches("[0-9]+")
                )) {
                    player.sendMessage("Invalid block id provided");
                    yield false;
                }

                final var blockId = Integer.parseInt(blockArgument);

                final var block = Block.get(blockId);

                final var operation = new SetOperation(
                    session,
                    block
                );

                this.getPlugin()
                    .getWorldService()
                    .execute(player.getLevel(), operation);

                player.sendMessage(String.format(
                    "Operation '%s' executed successfully",
                    operation.getName()
                ));
                yield true;
            }
            case "replace" -> {
                final var session = this.getPlugin()
                    .getSessionService()
                    .getSession(player.getName())
                    .orElseThrow();

                final var selection = session.selection();

                if (!(
                    selection.hasFirstPoint() &&
                        selection.hasSecondPoint()
                )) {
                    player.sendMessage("You have to set a first point and a second point first");
                    yield false;
                }

                if (
                    arguments.length == 1
                ) {
                    player.sendMessage("You have to provide a block id from which the block should be replaced");
                    yield false;
                }

                final var fromBlockArgument = arguments[1];

                if (!(
                    fromBlockArgument.matches("[0-9]+")
                )) {
                    player.sendMessage("Invalid block id provided");
                    yield false;
                }

                if (
                    arguments.length == 2
                ) {
                    player.sendMessage("You have to provide a block id to which the block should be replaced");
                    yield false;
                }

                final var toBlockArgument = arguments[2];

                if (!(
                    toBlockArgument.matches("[0-9]+")
                )) {
                    player.sendMessage("Invalid block id provided");
                    yield false;
                }

                final var fromBlockId = Integer.parseInt(fromBlockArgument);
                final var toBlockId = Integer.parseInt(toBlockArgument);

                final var fromBlock = Block.get(fromBlockId);
                final var toBlock = Block.get(toBlockId);

                final var operation = new ReplaceOperation(
                    session,
                    fromBlock,
                    toBlock
                );

                this.getPlugin()
                    .getWorldService()
                    .execute(player.getLevel(), operation);

                player.sendMessage(String.format(
                    "Operation '%s' executed successfully",
                    operation.getName()
                ));
                yield true;
            }
            case "undo" -> {
                final var session = this.getPlugin()
                    .getSessionService()
                    .getSession(player.getName())
                    .orElseThrow();

                final var operation = new UndoOperation(
                    session
                );

                this.getPlugin()
                    .getWorldService()
                    .execute(player.getLevel(), operation);

                player.sendMessage(String.format(
                    "Operation '%s' executed successfully",
                    operation.getName()
                ));
                yield true;
            }
            case "redo" -> {
                final var session = this.getPlugin()
                    .getSessionService()
                    .getSession(player.getName())
                    .orElseThrow();

                final var operation = new RedoOperation(
                    session
                );

                this.getPlugin()
                    .getWorldService()
                    .execute(player.getLevel(), operation);

                player.sendMessage(String.format(
                    "Operation '%s' executed successfully",
                    operation.getName()
                ));
                yield true;
            }
            default -> {
                player.sendMessage(String.format("Unknown command '%s' was provided", arguments[0]));
                yield false;
            }
        };
    }

}
