package me.millesant.session;

import me.millesant.editor.history.HistoryImpl;
import me.millesant.editor.history.History;
import me.millesant.editor.history.change.HistoryChange;
import me.millesant.editor.selection.SelectionImpl;
import me.millesant.editor.selection.Selection;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public final class SessionImpl implements Session {

    private final String name;

    private final Selection selection;

    private final History history;

    private List<HistoryChange> clipboard;

    public SessionImpl(final String name) {
        this.name = name;
        this.selection = new SelectionImpl();
        this.history = new HistoryImpl();
        this.clipboard = new ArrayList<>();
    }

}
