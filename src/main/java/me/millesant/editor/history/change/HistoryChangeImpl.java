package me.millesant.editor.history.change;

import cn.nukkit.block.Block;
import cn.nukkit.level.Position;

public record HistoryChangeImpl(Position position, Block oldBlock, Block newBlock) implements HistoryChange {

}
