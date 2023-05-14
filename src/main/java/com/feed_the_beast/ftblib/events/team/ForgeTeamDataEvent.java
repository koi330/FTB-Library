package com.feed_the_beast.ftblib.events.team;

import java.util.function.Consumer;

import com.feed_the_beast.ftblib.lib.data.ForgeTeam;
import com.feed_the_beast.ftblib.lib.data.NBTDataStorage;

/**
 * @author LatvianModder
 */
public class ForgeTeamDataEvent extends ForgeTeamEvent {

    private final Consumer<NBTDataStorage.Data> callback;

    public ForgeTeamDataEvent(ForgeTeam team, Consumer<NBTDataStorage.Data> c) {
        super(team);
        callback = c;
    }

    public void register(NBTDataStorage.Data data) {
        callback.accept(data);
    }
}
