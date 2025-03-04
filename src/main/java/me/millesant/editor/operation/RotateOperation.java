package me.millesant.editor.operation;

import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import me.millesant.editor.history.change.HistoryChangeImpl;
import me.millesant.editor.history.change.HistoryChange;
import me.millesant.session.Session;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public record RotateOperation(Session session, int angle) implements Operation {

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

                        final var newPosition = this.rotatePosition(position, angle, Position.fromObject(min, level), Position.fromObject(max, level));

                        historyChanges.add(new HistoryChangeImpl(position, currentBlock, level.getBlock(newPosition)));

                        level.setBlock(newPosition, currentBlock);
                    }
                }
            }

            this.session().getHistory().addChange(historyChanges);
            return true;
        });
    }

    private Position rotatePosition(Position position, int angle, Position min, Position max) {
        final var centerX = (min.getX() + max.getX()) / 2;
        final var centerZ = (min.getZ() + max.getZ()) / 2;

        final var x = position.getX() - centerX;
        final var z = position.getZ() - centerZ;

        return switch (angle) {
            case 90 -> new Position(centerX - z, position.getY(), centerZ + x);
            case 180 -> new Position(centerX - x, position.getY(), centerZ - z);
            case 270 -> new Position(centerX + z, position.getY(), centerZ - x);
            default -> position;
        };
    }

    @Override
    public String getName() {
        return "RotateOperation";
    }

}
