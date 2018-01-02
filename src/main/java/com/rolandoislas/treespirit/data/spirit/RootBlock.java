package com.rolandoislas.treespirit.data.spirit;

import net.minecraft.block.Block;

public class RootBlock {
    private final Block block;
    private Integer meta;

    public RootBlock(Block block) {
        this.block = block;
    }

    public void setMeta(Integer meta) {
        this.meta = meta;
    }

    public Block getBlock() {
        return block;
    }
}
