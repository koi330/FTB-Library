package com.feed_the_beast.ftblib.client;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import com.feed_the_beast.ftblib.client.resource.IResourceType;
import com.feed_the_beast.ftblib.client.resource.ISelectiveResourceReloadListener;
import com.feed_the_beast.ftblib.lib.io.DataReader;
import com.google.gson.JsonElement;

/**
 * @author LatvianModder
 */
public enum FTBLibClientConfigManager implements ISelectiveResourceReloadListener {

    INSTANCE;

    @Override
    @SuppressWarnings("unchecked")
    public void onResourceManagerReload(IResourceManager manager, Predicate<IResourceType> resourcePredicate) {
        if (!resourcePredicate.test(FTBLibResourceType.FTB_CONFIG)) {
            return;
        }

        FTBLibClient.CLIENT_CONFIG_MAP.clear();

        for (String domain : (Set<String>) manager.getResourceDomains()) {
            try {
                for (IResource resource : (List<IResource>) manager
                        .getAllResources(new ResourceLocation(domain, "client_config.json"))) {
                    for (JsonElement e : DataReader.get(resource).json().getAsJsonArray()) {
                        ClientConfig c = new ClientConfig(e.getAsJsonObject());
                        FTBLibClient.CLIENT_CONFIG_MAP.put(c.id, c);
                    }
                }
            } catch (Exception ex) {
                if (!(ex instanceof FileNotFoundException)) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
