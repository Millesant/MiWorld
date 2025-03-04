package me.millesant.editor.operation;

import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import me.millesant.editor.history.change.HistoryChangeImpl;
import me.millesant.editor.history.change.HistoryChange;
import me.millesant.session.Session;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public record FlipOperation(Session session, String direction) implements Operation {

    @Override
    public CompletableFuture<Boolean> execute(final Level level) {
        return CompletableFuture.supplyAsync(() -> {
            final var min = this.session().getSelection().getMin();
            final var max = this.session().getSelection().getMax();

            final var historyChanges = new ArrayList<HistoryChange>();

            for (var x = min.getX(); x <= max.getX(); x++) {
                for (var y = min.getY(); y <= max.getY(); y++) {
                    for (var z = min.getZ(); z <= max.getZ(); z++) {
                        final var position = new Position(x, y, z, level);
                        final var currentBlock = level.getBlock(position);

                        final var newPosition = switch (this.direction()) {
                            case "horizontal" -> new Position(max.getX() - (x - min.getX()), y, z, level);
                            case "vertical" -> new Position(x, max.getY() - (y - min.getY()), z, level);
                            default -> throw new IllegalArgumentException("Invalid flip direction");
                        };

                        final var newBlock = level.getBlock(newPosition);

                        historyChanges.add(new HistoryChangeImpl(position, currentBlock, newBlock));

                        level.setBlock(newPosition, currentBlock);
                    }
                }
            }

            this.session().getHistory().addChange(historyChanges);
            return true;
        });
    }

    @Override
    public String getName() {
        return "FlipOperation";
    }

}
