package com.github.nollyak.session;

import com.github.nollyak.editor.history.History;

import com.github.nollyak.editor.selection.Selection;

public interface Session {

    String name();

    Selection selection();

    History history();

}
