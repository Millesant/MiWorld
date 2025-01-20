package com.github.nollyak.editor.operation;

import cn.nukkit.level.Level;

import com.github.nollyak.session.Session;

import java.util.concurrent.CompletableFuture;

public record RedoOperation(
    Session session
) implements Operation {

    @Override
    public CompletableFuture<Boolean> execute(
        final Level level
    ) {
        return CompletableFuture.supplyAsync(() -> {
            final var optionalHistoryChanges = this.session()
                .history()
                .redoChange();

            if (
                optionalHistoryChanges.isEmpty()
            ) return false;

            final var historyChanges = optionalHistoryChanges.get();

            for (
                final var historyChange : historyChanges
            ) {
                level.setBlock(historyChange.position(), historyChange.newBlock());
            }
            return true;
        });
    }

    @Override
    public String getName() {
        return "RedoOperation";
    }

}
