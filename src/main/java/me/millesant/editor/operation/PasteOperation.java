package me.millesant.editor.operation;

import cn.nukkit.level.Level;
import me.millesant.editor.history.change.HistoryChangeImpl;
import me.millesant.editor.history.change.HistoryChange;
import me.millesant.session.Session;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public record PasteOperation(Session session) implements Operation {

    @Override
    public CompletableFuture<Boolean> execute(final Level level) {
        return CompletableFuture.supplyAsync(() -> {
            final var clipboard = this.session().getClipboard();

            if (clipboard.isEmpty()) return false;

            final var historyChanges = new ArrayList<HistoryChange>();

            for (final var historyChange : clipboard) {
                final var position = historyChange.position();
                final var newBlock = historyChange.newBlock();

                final var oldBlock = level.getBlock(position);

                historyChanges.add(new HistoryChangeImpl(position, oldBlock, newBlock));

                level.setBlock(position, newBlock);
            }

            this.session().getHistory().addChange(historyChanges);
            return true;
        });
    }

    @Override
    public String getName() {
        return "PasteOperation";
    }

}
