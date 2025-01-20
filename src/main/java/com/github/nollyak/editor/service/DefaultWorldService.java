package com.github.nollyak.editor.service;

import cn.nukkit.level.Level;

import com.github.nollyak.NoWorld;

import com.github.nollyak.editor.operation.Operation;

import java.util.concurrent.CompletableFuture;

public record DefaultWorldService(
    NoWorld plugin
) implements WorldService {

    @Override
    public CompletableFuture<Boolean> execute(
        final Level level,
        final Operation operation
    ) {
        return operation.execute(
            level
        ).exceptionally(throwable -> {
            this.plugin()
                .getLogger()
                .error(String.format("An error occurred while executing the operation '%s'", operation.getName()), throwable);
            return false;
        });
    }

}
