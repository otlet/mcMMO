package com.gmail.nossr50.runnables;

import com.gmail.nossr50.datatypes.player.BukkitMMOPlayer;
import com.gmail.nossr50.mcMMO;
import com.gmail.nossr50.runnables.player.PlayerProfileSaveTask;
import org.bukkit.scheduler.BukkitRunnable;

public class SaveTimerTask implements Runnable {

    private final mcMMO pluginRef;

    public SaveTimerTask(mcMMO pluginRef) {
        this.pluginRef = pluginRef;
    }

    @Override
    public void run() {
        // All player data will be saved periodically through this
        int count = 1;

        for (BukkitMMOPlayer mcMMOPlayer : pluginRef.getUserManager().getPlayers()) {
            pluginRef.getPlatformProvider().getScheduler().getTaskBuilder().setAsync(true).setDelay((long) count).setTask(new PlayerProfileSaveTask(mcMMOPlayer.getProfile(), false)).schedule();
            count++;
        }

        pluginRef.getPartyManager().saveParties();
    }
}
