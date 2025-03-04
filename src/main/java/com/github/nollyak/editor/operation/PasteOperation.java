package com.github.nollyak.editor.operation;

import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import com.github.nollyak.session.Session;
import com.github.nollyak.editor.history.change.DefaultHistoryChange;
import com.github.nollyak.editor.history.change.HistoryChange;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public record PasteOperation(
    Session session
) implements Operation {

    @Override
    public CompletableFuture<Boolean> execute(
        final Level level
    ) {
        return CompletableFuture.supplyAsync(() -> {
            final var clipboard = this.session()
                .getClipboard();

            if (
                clipboard.isEmpty()
            ) return false;

            final var historyChanges = new ArrayList<HistoryChange>();

            for (
                final var historyChange : clipboard
            ) {
                final var position = historyChange.position();
                final var newBlock = historyChange.newBlock();

                final var oldBlock = level.getBlock(position);

                historyChanges.add(new DefaultHistoryChange(position, oldBlock, newBlock));

                level.setBlock(position, newBlock);
            }

            this.session()
                .history()
                .addChange(historyChanges);
            return true;
        });
    }

    @Override
    public String getName() {
        return "PasteOperation";
    }

}
