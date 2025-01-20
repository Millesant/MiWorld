package com.github.nollyak.session;

import com.github.nollyak.editor.history.DefaultHistory;

import com.github.nollyak.editor.history.History;

import com.github.nollyak.editor.selection.DefaultSelection;
import com.github.nollyak.editor.selection.Selection;

public record DefaultSession(
    String name,
    Selection selection,
    History history
) implements Session {

    public DefaultSession(
        final String name
    ) {
        this(name,
            new DefaultSelection(),
            new DefaultHistory());
    }

}
