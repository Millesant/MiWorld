package me.millesant.editor.service;

import cn.nukkit.level.Level;
import me.millesant.editor.operation.Operation;

import java.util.concurrent.CompletableFuture;

public interface WorldService {

    CompletableFuture<Boolean> execute(final Level level, final Operation operation);

}
