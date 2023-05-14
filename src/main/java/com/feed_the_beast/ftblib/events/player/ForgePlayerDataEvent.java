package com.feed_the_beast.ftblib.events.player;

import java.util.function.Consumer;

import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import com.feed_the_beast.ftblib.lib.data.NBTDataStorage;

/**
 * @author LatvianModder
 */
public class ForgePlayerDataEvent extends ForgePlayerEvent {

    private final Consumer<NBTDataStorage.Data> callback;

    public ForgePlayerDataEvent(ForgePlayer player, Consumer<NBTDataStorage.Data> c) {
        super(player);
        callback = c;
    }

    public void register(NBTDataStorage.Data data) {
        callback.accept(data);
    }
}
