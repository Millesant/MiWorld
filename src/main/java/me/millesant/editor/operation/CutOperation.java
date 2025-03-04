package me.millesant.editor.operation;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockIds;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import me.millesant.editor.history.change.HistoryChangeImpl;
import me.millesant.editor.history.change.HistoryChange;
import me.millesant.session.Session;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public record CutOperation(Session session) implements Operation {

    @Override
    public CompletableFuture<Boolean> execute(final Level level) {
        return CompletableFuture.supplyAsync(() -> {
            final var min = this.session().getSelection().getMin();
            final var max = this.session().getSelection().getMax();

            final var clipboard = new ArrayList<HistoryChange>();
            final var historyChanges = new ArrayList<HistoryChange>();

            for (var x = min.getX(); x <= max.getX(); x++) {
                for (var y = min.getY(); y <= max.getY(); y++) {
                    for (var z = min.getZ(); z <= max.getZ(); z++) {
                        final var position = new Position(x, y, z, level);
                        final var block = level.getBlock(position);

                        clipboard.add(new HistoryChangeImpl(position, block, block));
                        historyChanges.add(new HistoryChangeImpl(position, block, Block.get(BlockIds.AIR)));

                        level.setBlock(position, Block.get(BlockIds.AIR));
                    }
                }
            }

            this.session().setClipboard(clipboard);
            this.session().getHistory().addChange(historyChanges);
            return true;
        });
    }

    @Override
    public String getName() {
        return "CutOperation";
    }

}
