package com.github.nollyak.session;

import com.github.nollyak.editor.history.History;
import com.github.nollyak.editor.selection.Selection;
import com.github.nollyak.editor.history.change.HistoryChange;

import java.util.List;

public interface Session {

    String name();

    Selection selection();

    History history();

    List<HistoryChange> getClipboard();

    void setClipboard(List<HistoryChange> clipboard);

}
