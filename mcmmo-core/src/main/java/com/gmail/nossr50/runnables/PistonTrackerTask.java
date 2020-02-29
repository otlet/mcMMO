package com.gmail.nossr50.runnables;

import com.gmail.nossr50.core.MetadataConstants;
import com.gmail.nossr50.mcMMO;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class PistonTrackerTask extends BukkitRunnable {
    private final mcMMO pluginRef;
    private List<Block> blocks;
    private BlockFace direction;
    private Block futureEmptyBlock;

    public PistonTrackerTask(mcMMO pluginRef, List<Block> blocks, BlockFace direction, Block futureEmptyBlock) {
        this.pluginRef = pluginRef;
        this.blocks = blocks;
        this.direction = direction;
        this.futureEmptyBlock = futureEmptyBlock;
    }

    @Override
    public void run() {
        // Check to see if futureEmptyBlock is empty - if it isn't; the blocks didn't move
        if (!pluginRef.getBlockTools().isPistonPiece(futureEmptyBlock.getState())) {
            return;
        }

        if (pluginRef.getPlaceStore().isTrue(futureEmptyBlock)) {
            pluginRef.getPlaceStore().setFalse(futureEmptyBlock);
        }

        for (Block b : blocks) {
            Block nextBlock = b.getRelative(direction);

            if (nextBlock.hasMetadata(MetadataConstants.PISTON_TRACKING_METAKEY.getKey())) {
                pluginRef.getPlaceStore().setTrue(nextBlock);
                nextBlock.removeMetadata(MetadataConstants.PISTON_TRACKING_METAKEY.getKey(), (Plugin) pluginRef.getPlatformProvider());
            } else if (pluginRef.getPlaceStore().isTrue(nextBlock)) {
                // Block doesn't have metadatakey but isTrue - set it to false
                pluginRef.getPlaceStore().setFalse(nextBlock);
            }
        }
    }
}
