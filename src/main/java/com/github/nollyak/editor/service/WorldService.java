package com.github.nollyak.editor.service;

import cn.nukkit.level.Level;

import com.github.nollyak.editor.operation.Operation;

import java.util.concurrent.CompletableFuture;

public interface WorldService {

    CompletableFuture<Boolean> execute(
        final Level level,
        final Operation operation
    );

}
