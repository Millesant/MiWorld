package com.github.nollyak.editor.operation;

import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import com.github.nollyak.editor.history.change.DefaultHistoryChange;
import com.github.nollyak.editor.history.change.HistoryChange;
import com.github.nollyak.session.Session;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public record ScaleOperation(
    Session session,
    double factor
) implements Operation {

    @Override
    public CompletableFuture<Boolean> execute(
        final Level level
    ) {
        return CompletableFuture.supplyAsync(() -> {
            final var min = this.session()
                .selection()
                .getMin();

            final var max = this.session()
                .selection()
                .getMax();

            final var historyChanges = new ArrayList<HistoryChange>();

            for (
                var x = min.getX();
                x <= max.getX();
                x++
            ) {
                for (
                    var y = min.getY();
                    y <= max.getY();
                    y++
                ) {
                    for (
                        var z = min.getZ();
                        z <= max.getZ();
                        z++
                    ) {
                        final var position = new Position(x, y, z, level);
                        final var currentBlock = level.getBlock(position);

                        final var newX = (int) (x * this.factor());
                        final var newY = (int) (y * this.factor());
                        final var newZ = (int) (z * this.factor());

                        final var newPosition = new Position(newX, newY, newZ, level);

                        historyChanges.add(new DefaultHistoryChange(position, currentBlock, level.getBlock(newPosition)));

                        level.setBlock(newPosition, currentBlock);
                    }
                }
            }

            this.session()
                .history()
                .addChange(historyChanges);
            return true;
        });
    }

    @Override
    public String getName() {
        return "ScaleOperation";
    }

}
