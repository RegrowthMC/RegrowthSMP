package org.lushplugins.regrowthsmp.hook.luckperms.context;

import net.luckperms.api.context.ContextCalculator;
import net.luckperms.api.context.ContextConsumer;
import net.luckperms.api.context.ContextSet;
import net.luckperms.api.context.ImmutableContextSet;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.geysermc.floodgate.api.FloodgateApi;
import org.jetbrains.annotations.NotNull;

public class BedrockPlayerCalculator implements ContextCalculator<Player> {
    private static final String KEY = "is-bedrock-player";

    @Override
    public void calculate(@NotNull Player target, @NonNull ContextConsumer consumer) {
        boolean isBedrockPlayer = FloodgateApi.getInstance().isFloodgatePlayer(target.getUniqueId());
        consumer.accept(KEY, String.valueOf(isBedrockPlayer));
    }

    @Override
    public @NonNull ContextSet estimatePotentialContexts() {
        return ImmutableContextSet.builder()
            .add(KEY, "true")
            .add(KEY, "false")
            .build();
    }
}
