package com.github.nollyak.editor.operation;

import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import com.github.nollyak.editor.history.change.DefaultHistoryChange;
import com.github.nollyak.editor.history.change.HistoryChange;
import com.github.nollyak.session.Session;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public record MoveOperation(
    Session session,
    int targetX,
    int targetY,
    int targetZ
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
                        final var oldPosition = new Position(x, y, z, level);
                        final var newPosition = new Position(
                            x + this.targetX(),
                            y + this.targetY(),
                            z + this.targetZ(),
                            level
                        );

                        final var oldBlock = level.getBlock(oldPosition);
                        final var newBlock = level.getBlock(newPosition);

                        historyChanges.add(new DefaultHistoryChange(oldPosition, oldBlock, newBlock));

                        level.setBlock(newPosition, oldBlock);
                        level.setBlock(oldPosition, newBlock);
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
        return "MoveOperation";
    }

}
