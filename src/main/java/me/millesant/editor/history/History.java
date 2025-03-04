package me.millesant.editor.history;

import me.millesant.editor.history.change.HistoryChange;

import java.util.Deque;
import java.util.List;
import java.util.Optional;

public interface History {

    Deque<List<HistoryChange>> undoStack();

    Deque<List<HistoryChange>> redoStack();

    void addChange(final List<HistoryChange> historyChanges);

    Optional<List<HistoryChange>> undoChange();

    Optional<List<HistoryChange>> redoChange();

}
