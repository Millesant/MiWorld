package me.millesant.editor.operation;

import cn.nukkit.level.Level;

import java.util.concurrent.CompletableFuture;

public sealed interface Operation permits CopyOperation, CutOperation, FillOperation, FlipOperation, MoveOperation, OutlineOperation, PasteOperation, RedoOperation, ReplaceOperation, RotateOperation, ScaleOperation, SetOperation, UndoOperation {

    CompletableFuture<Boolean> execute(final Level level);

    String getName();

}
