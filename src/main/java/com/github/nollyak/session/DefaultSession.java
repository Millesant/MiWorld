package com.github.nollyak.session;

import com.github.nollyak.editor.history.DefaultHistory;
import com.github.nollyak.editor.history.History;
import com.github.nollyak.editor.history.change.HistoryChange;
import com.github.nollyak.editor.selection.DefaultSelection;
import com.github.nollyak.editor.selection.Selection;

import java.util.ArrayList;
import java.util.List;

public record DefaultSession(
    String name,
    Selection selection,
    History history,
    List<HistoryChange> clipboard
) implements Session {

    public DefaultSession(
        final String name
    ) {
        this(name,
            new DefaultSelection(),
            new DefaultHistory(),
            new ArrayList<>());
    }

    @Override
    public List<HistoryChange> getClipboard() {
        return this.clipboard;
    }

    @Override
    public void setClipboard(List<HistoryChange> clipboard) {
        this.clipboard = clipboard;
    }

}
