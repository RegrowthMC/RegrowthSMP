package org.lushplugins.regrowthsmp.module.crateanimation.opening;

import com.destroystokyo.paper.ParticleBuilder;
import me.thundertnt33.animatronics.api.Animatronic;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lushplugins.lushlib.utils.Pair;
import org.lushplugins.regrowthsmp.module.crateanimation.CrateAnimation;
import su.nightexpress.excellentcrates.CratesAPI;
import su.nightexpress.excellentcrates.api.crate.Reward;
import su.nightexpress.excellentcrates.api.event.CrateObtainRewardEvent;
import su.nightexpress.excellentcrates.crate.impl.CrateSource;
import su.nightexpress.excellentcrates.key.CrateKey;
import su.nightexpress.excellentcrates.opening.AbstractOpening;

import java.util.List;

public class AnimatronicOpening extends AbstractOpening {
    private boolean rolled = false;

    public AnimatronicOpening(@NotNull Player player, @NotNull CrateSource source, @Nullable CrateKey key) {
        super(CratesAPI.getPlugin(), player, source, key);
    }

    @Override
    public void instaRoll() {
        this.roll();
        this.stop();
    }

    @Override
    public boolean isCompleted() {
        return rolled;
    }

    @Override
    public long getInterval() {
        return 1;
    }

    @Override
    protected void onStart() {}

    @Override
    protected void onTick() {
        if (this.isRunning()) {
            this.roll();
            this.stop();
        }
    }

    @Override
    protected void onComplete() {}

    public void roll() {
        this.setRefundable(false);

        Animatronic animatronic = new Animatronic("DefaultCrate");
        animatronic.start();

        Block crateBlock = source.getBlock();
        if (crateBlock != null) {
            Location particleLocation = crateBlock.getLocation().clone().add(0.5, 2, 0.5);

            particleSchedule(List.of(
                new Pair<>(new ParticleBuilder(Particle.SPIT).count(3).extra(0), 30),
                new Pair<>(new ParticleBuilder(Particle.SPIT).count(3).extra(0), 40),
                new Pair<>(new ParticleBuilder(Particle.SPIT).count(3).extra(0), 50),
                new Pair<>(new ParticleBuilder(Particle.WAX_OFF)
                    .count(15)
                    .offset(0.3, 0.3, 0.3), 70),
                new Pair<>(new ParticleBuilder(Particle.SPIT).count(5).extra(0), 70),
                new Pair<>(new ParticleBuilder(Particle.SPIT).count(5).extra(0), 173)
            ), particleLocation);

            String[][] soundScheduler = {
                {"BLOCK_SCAFFOLDING_HIT", "1", "0.5", "30"},
                {"BLOCK_AMETHYST_BLOCK_STEP", "1", "1.3", "30"},
                {"BLOCK_SCAFFOLDING_HIT", "1", "0.5", "40"},
                {"BLOCK_AMETHYST_BLOCK_STEP", "1", "1.3", "40"},
                {"BLOCK_SCAFFOLDING_HIT", "1", "0.5", "50"},
                {"BLOCK_AMETHYST_BLOCK_STEP", "1", "1.3", "50"},
                {"BLOCK_ENCHANTMENT_TABLE_USE", "2", "1.7", "70"},
                {"BLOCK_NOTE_BLOCK_PLING", "1", "2", "173"}
            };
            soundSchedule(soundScheduler, particleLocation);
        }

        JavaPlugin plugin = CrateAnimation.getInstance().getPlugin();
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            Reward reward = crate.rollReward(player);

            ArmorStand armorStand = animatronic.getArmorstand();
            if (armorStand != null && reward.getPreview().canProduceItem()) {
                armorStand.getEquipment().setHelmet(reward.getPreview().createItemStack());
            }

            Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
                if (armorStand != null && reward.getPreview().canProduceItem()) {
                    armorStand.getEquipment().setHelmet(reward.getPreview().createItemStack());
                }

                Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
                    reward.give(player);

                    CrateObtainRewardEvent rewardEvent = new CrateObtainRewardEvent(reward, this.player);
                    this.plugin.getPluginManager().callEvent(rewardEvent);

                    Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
                        rolled = true;
                        animatronic.gotoStart();
                        CrateAnimation.getInstance().unlockCrate(crate.getName());
                    }, 20);
                }, 61);
            }, 42);
        }, 70);
    }

    private void particleSchedule(List<Pair<ParticleBuilder, Integer>> particleSchedule, Location location) {
        for (Pair<ParticleBuilder, Integer> pair : particleSchedule) {
            ParticleBuilder particleData = pair.first();
            int delay = pair.second();

            particleData.location(location);
            Bukkit.getScheduler().runTaskLater(CrateAnimation.getInstance().getPlugin(), particleData::spawn, delay);
        }
    }

    private void soundSchedule(String[][] soundScheduleArr, Location location) {
        for (String[] strings : soundScheduleArr) {
            Sound sound = Sound.valueOf(strings[0]);
            float volume = Float.parseFloat(strings[1]);
            float pitch = Float.parseFloat(strings[2]);
            int delay = Integer.parseInt(strings[3]);

            Bukkit.getScheduler().runTaskLater(CrateAnimation.getInstance().getPlugin(), () -> {
                location.getWorld().playSound(location, sound, volume, pitch);
            }, delay);
        }
    }
}
