package me.millesant.editor.operation;

import cn.nukkit.level.Level;
import me.millesant.session.Session;

import java.util.concurrent.CompletableFuture;

public record UndoOperation(Session session) implements Operation {

    @Override
    public CompletableFuture<Boolean> execute(final Level level) {
        return CompletableFuture.supplyAsync(() -> {
            final var optionalHistoryChanges = this.session().getHistory().undoChange();

            if (optionalHistoryChanges.isEmpty()) return false;

            final var historyChanges = optionalHistoryChanges.get();

            for (final var historyChange : historyChanges) {
                level.setBlock(historyChange.position(), historyChange.oldBlock());
            }
            return true;
        });
    }

    @Override
    public String getName() {
        return "UndoOperation";
    }

}
