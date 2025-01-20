package com.github.nollyak.editor.history.change;

import cn.nukkit.block.Block;

import cn.nukkit.level.Position;

public record DefaultHistoryChange(
    Position position,
    Block oldBlock,
    Block newBlock
) implements HistoryChange {
}
