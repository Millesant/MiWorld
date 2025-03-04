package me.millesant.editor.history;

import me.millesant.editor.history.change.HistoryChange;

import java.util.Deque;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedDeque;

public record HistoryImpl(Deque<List<HistoryChange>> undoStack,
                          Deque<List<HistoryChange>> redoStack) implements History {

    public HistoryImpl() {
        this(new ConcurrentLinkedDeque<>(), new ConcurrentLinkedDeque<>());
    }

    @Override
    public void addChange(final List<HistoryChange> historyChanges) {
        this.undoStack().push(historyChanges);
        this.redoStack().clear();
    }

    @Override
    public Optional<List<HistoryChange>> undoChange() {
        if (this.undoStack().isEmpty()) return Optional.empty();

        final var historyChanges = this.undoStack().pop();

        this.redoStack().push(historyChanges);

        return Optional.of(historyChanges);
    }

    @Override
    public Optional<List<HistoryChange>> redoChange() {
        if (this.redoStack().isEmpty()) return Optional.empty();

        final var historyChanges = this.redoStack().pop();

        this.undoStack().push(historyChanges);

        return Optional.of(historyChanges);
    }

}
