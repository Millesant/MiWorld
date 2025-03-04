package me.millesant.editor.operation;

import cn.nukkit.block.Block;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import me.millesant.editor.history.change.HistoryChangeImpl;
import me.millesant.editor.history.change.HistoryChange;
import me.millesant.session.Session;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public record ReplaceOperation(Session session, Block fromBlock, Block toBlock) implements Operation {

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

                        if (currentBlock.getId() == this.fromBlock().getId() && currentBlock.getDamage() == this.fromBlock().getDamage()) {
                            historyChanges.add(new HistoryChangeImpl(position, currentBlock, this.toBlock()));

                            level.setBlock(position, this.toBlock());
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
        return "ReplaceBlockOperation";
    }

}
