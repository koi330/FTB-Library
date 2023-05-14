package com.feed_the_beast.ftblib.lib.config;

import java.util.Collection;

import javax.annotation.Nullable;

import net.minecraft.server.MinecraftServer;

import com.mojang.authlib.GameProfile;

/**
 * @author LatvianModder
 */
public interface IRankConfigHandler {

    void registerRankConfig(RankConfigValueInfo info);

    Collection<RankConfigValueInfo> getRegisteredConfigs();

    ConfigValue getConfigValue(MinecraftServer server, GameProfile profile, String node);

    @Nullable
    RankConfigValueInfo getInfo(String node);
}
