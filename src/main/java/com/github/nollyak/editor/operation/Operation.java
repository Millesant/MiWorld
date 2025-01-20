package com.github.nollyak.editor.operation;

import cn.nukkit.level.Level;

import java.util.concurrent.CompletableFuture;

public sealed interface Operation
    permits SetOperation, ReplaceOperation, UndoOperation, RedoOperation {

    CompletableFuture<Boolean> execute(
        final Level level
    );

    String getName();

}
