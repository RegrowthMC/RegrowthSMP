package org.lushplugins.regrowthsmp.module.pinata.command;

import me.xemor.enchantedbosses.BossHandler;
import me.xemor.enchantedbosses.EnchantedBosses;
import me.xemor.enchantedbosses.SkillEntity;
import org.bukkit.boss.BarStyle;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.lushplugins.lushlib.command.Command;
import org.lushplugins.lushlib.libraries.chatcolor.ChatColorHandler;
import org.lushplugins.lushlib.timer.BossBarTimer;
import org.lushplugins.lushlib.timer.RainbowBossBarTimer;
import org.lushplugins.regrowthsmp.module.pinata.Pinata;

import java.util.List;
import java.util.logging.Level;

public class SpawnPinataCommand extends Command {

    public SpawnPinataCommand() {
        super("spawnpinata");
        addRequiredPermission("%s.pinata.spawn".formatted(Pinata.getInstance().getPlugin().getName().toLowerCase()));
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull org.bukkit.command.Command command, @NotNull String s, @NotNull String[] strings, @NotNull String[] strings1) {
        BossBarTimer timer = new RainbowBossBarTimer(
            "<gradient:#FF8C8D:#FEB070:#FDE689:#DFFF94:#B2E6FC:#F0BDFF>Pinata spawning in %remaining_duration% seconds!",
            BarStyle.SEGMENTED_10,
            Pinata.getInstance().getPlugin(),
            30);
        timer.onFinish(SpawnPinataCommand::spawnRandomPinata);
        timer.addOnlinePlayers();
        timer.start();
        Pinata.getInstance().getConfigManager().getOptionalMessage("pre-spawn").ifPresent(ChatColorHandler::broadcastMessage);
        return true;
    }

    private static void spawnRandomPinata() {
        List<String> pinataTypes = Pinata.getInstance().getConfigManager().getPinatas();
        String pinataType = pinataTypes.get(Pinata.getRandom().nextInt(pinataTypes.size()));

        BossHandler bossHandler = EnchantedBosses.getInstance().getBossHandler();
        SkillEntity skillEntity = bossHandler.getBoss(pinataType);
        if (skillEntity == null) {
            Pinata.getInstance().getPlugin().getLogger().log(Level.WARNING, "'%s' is not a valid boss type".formatted(pinataType));
            return;
        }

        bossHandler.spawn(skillEntity, Pinata.getInstance().getConfigManager().getSpawnLocation());
        Pinata.getInstance().getConfigManager().getOptionalMessage("spawn").ifPresent(ChatColorHandler::broadcastMessage);
    }
}
