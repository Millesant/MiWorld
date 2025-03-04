package com.github.nollyak.editor.operation;

import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import com.github.nollyak.session.Session;
import com.github.nollyak.editor.history.change.DefaultHistoryChange;
import com.github.nollyak.editor.history.change.HistoryChange;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public record CutOperation(
    Session session
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

            final var clipboard = new ArrayList<HistoryChange>();
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
                        final var block = level.getBlock(position);

                        clipboard.add(new DefaultHistoryChange(position, block, block));
                        historyChanges.add(new DefaultHistoryChange(position, block, Block.get(BlockID.AIR)));

                        level.setBlock(position, Block.get(BlockID.AIR));
                    }
                }
            }

            this.session()
                .setClipboard(clipboard);
            this.session()
                .history()
                .addChange(historyChanges);
            return true;
        });
    }

    @Override
    public String getName() {
        return "CutOperation";
    }

}
