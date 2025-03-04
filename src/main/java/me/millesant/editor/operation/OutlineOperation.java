package me.millesant.editor.operation;

import cn.nukkit.block.Block;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import me.millesant.editor.history.change.HistoryChangeImpl;
import me.millesant.editor.history.change.HistoryChange;
import me.millesant.session.Session;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public record OutlineOperation(Session session, Block block) implements Operation {

    @Override
    public CompletableFuture<Boolean> execute(final Level level) {
        return CompletableFuture.supplyAsync(() -> {
            final var min = this.session().getSelection().getMin();
            final var max = this.session().getSelection().getMax();

            final var historyChanges = new ArrayList<HistoryChange>();

            for (var x = min.getX(); x <= max.getX(); x++) {
                for (var y = min.getY(); y <= max.getY(); y++) {
                    for (var z = min.getZ(); z <= max.getZ(); z++) {
                        if (x == min.getX() || x == max.getX() || y == min.getY() || y == max.getY() || z == min.getZ() || z == max.getZ()) {
                            final var position = new Position(x, y, z, level);

                            final var oldBlock = level.getBlock(position);

                            historyChanges.add(new HistoryChangeImpl(position, oldBlock, this.block()));

                            level.setBlock(position, this.block());
                        }
                    }
                }
            }

            this.session().getHistory().addChange(historyChanges);
            return true;
        });
    }

    @Override
    public String getName() {
        return "OutlineOperation";
    }

}
