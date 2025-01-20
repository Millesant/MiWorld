package com.github.nollyak.editor.operation;

import cn.nukkit.block.Block;

import cn.nukkit.level.Level;

import cn.nukkit.level.Position;
import com.github.nollyak.editor.history.change.DefaultHistoryChange;
import com.github.nollyak.editor.history.change.HistoryChange;
import com.github.nollyak.session.Session;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public record SetOperation(
    Session session,
    Block block
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

                        final var oldBlock = level.getBlock(position);

                        historyChanges.add(new DefaultHistoryChange(position, oldBlock, this.block()));

                        level.setBlock(position, this.block());
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
        return "SetBlockOperation";
    }

}
