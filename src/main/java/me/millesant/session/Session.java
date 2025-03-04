package me.millesant.session;

import me.millesant.editor.history.History;
import me.millesant.editor.history.change.HistoryChange;
import me.millesant.editor.selection.Selection;

import java.util.List;

public interface Session {

    String getName();

    Selection getSelection();

    History getHistory();

    List<HistoryChange> getClipboard();

    void setClipboard(List<HistoryChange> clipboard);

}
