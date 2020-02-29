package com.gmail.nossr50.datatypes.skills.behaviours;

import com.gmail.nossr50.core.MetadataConstants;
import com.gmail.nossr50.datatypes.meta.TrackedArrowMeta;
import com.gmail.nossr50.datatypes.skills.SubSkillType;
import com.gmail.nossr50.mcMMO;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

/**
 * These behaviour classes are a band-aid fix for a larger problem
 * Until the new skill system for mcMMO is finished/implemented, there is no good place to store the hardcoded behaviours for each skill
 * These behaviour classes server this purpose, they act as a bad solution to a bad problem
 * These classes will be removed when the new skill system is in place
 */
@Deprecated
public class ArcheryBehaviour {

    private final mcMMO pluginRef;

    public ArcheryBehaviour(mcMMO pluginRef) {
        this.pluginRef = pluginRef;
    }

    /**
     * Check for arrow retrieval.
     *
     * @param livingEntity The entity hit by the arrows
     */
    public void arrowRetrievalCheck(LivingEntity livingEntity) {
        if(livingEntity.hasMetadata(MetadataConstants.ARROW_TRACKER_METAKEY.getKey())) {
            pluginRef.getMiscTools().dropItems(livingEntity.getLocation(), new ItemStack(Material.ARROW), livingEntity.getMetadata(MetadataConstants.ARROW_TRACKER_METAKEY.getKey()).get(0).asInt());
        }
    }

    public void incrementArrowCount(LivingEntity livingEntity) {
        if(livingEntity.hasMetadata(MetadataConstants.ARROW_TRACKER_METAKEY.getKey())) {
            int arrowCount = livingEntity.getMetadata(MetadataConstants.ARROW_TRACKER_METAKEY.getKey()).get(0).asInt();
            livingEntity.getMetadata(MetadataConstants.ARROW_TRACKER_METAKEY.getKey()).set(0, new FixedMetadataValue((Plugin) pluginRef.getPlatformProvider(), arrowCount + 1));
        } else {
            livingEntity.setMetadata(MetadataConstants.ARROW_TRACKER_METAKEY.getKey(), new TrackedArrowMeta((Plugin) pluginRef.getPlatformProvider(), 1));
        }
    }

    public double getSkillShotBonusDamage(Player player, double oldDamage) {
        double damageBonusPercent = getSkillShotDamageBonusPercent(player);
        double newDamage = oldDamage + (oldDamage * damageBonusPercent);
        return Math.min(newDamage, (oldDamage + getSkillShotDamageCap()));
    }

    public double getSkillShotDamageBonusPercent(Player player) {
        return ((pluginRef.getRankTools().getRank(player, SubSkillType.ARCHERY_SKILL_SHOT)) * (pluginRef.getConfigManager().getConfigArchery().getSkillShotDamageMultiplier()) / 100.0D);
    }

    public double getSkillShotDamageCap() {
        return pluginRef.getConfigManager().getConfigArchery().getSkillShotDamageCeiling();
    }

    public double getDazeBonusDamage() {
        return pluginRef.getConfigManager().getConfigArchery().getDaze().getDazeBonusDamage();
    }

    public double getDistanceXpMultiplier() {
        return pluginRef.getConfigManager().getConfigExperience().getExperienceArchery().getDistanceMultiplier();
    }
}
