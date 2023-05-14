package com.feed_the_beast.ftblib.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import com.feed_the_beast.ftblib.FTBLibConfig;
import com.feed_the_beast.ftblib.client.resource.IResourceType;
import com.feed_the_beast.ftblib.client.resource.ISelectiveResourceReloadListener;
import com.feed_the_beast.ftblib.events.SidebarButtonCreatedEvent;
import com.feed_the_beast.ftblib.lib.io.DataReader;
import com.feed_the_beast.ftblib.lib.util.JsonUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @author LatvianModder
 */
public enum SidebarButtonManager implements ISelectiveResourceReloadListener {

    INSTANCE;

    public final List<SidebarButtonGroup> groups = new ArrayList<>();

    @Override
    @SuppressWarnings("unchecked")
    public void onResourceManagerReload(IResourceManager manager, Predicate<IResourceType> resourcePredicate) {
        if (!resourcePredicate.test(FTBLibResourceType.FTB_CONFIG)) {
            return;
        }

        groups.clear();

        JsonElement element = DataReader
                .get(new File(Minecraft.getMinecraft().mcDataDir, "local/client/sidebar_buttons.json")).safeJson();
        JsonObject sidebarButtonConfig;

        if (element.isJsonObject()) {
            sidebarButtonConfig = element.getAsJsonObject();
        } else {
            sidebarButtonConfig = new JsonObject();
        }

        Map<ResourceLocation, SidebarButtonGroup> groupMap = new HashMap<>();

        for (String domain : (Set<String>) manager.getResourceDomains()) {
            try {
                for (IResource resource : (List<IResource>) manager
                        .getAllResources(new ResourceLocation(domain, "sidebar_button_groups.json"))) {
                    JsonElement json = DataReader.get(resource).json();

                    for (Map.Entry<String, JsonElement> entry : json.getAsJsonObject().entrySet()) {
                        if (entry.getValue().isJsonObject()) {
                            JsonObject groupJson = entry.getValue().getAsJsonObject();
                            int y = 0;

                            if (groupJson.has("y")) {
                                y = groupJson.get("y").getAsInt();
                            }

                            SidebarButtonGroup group = new SidebarButtonGroup(
                                    new ResourceLocation(domain, entry.getKey()),
                                    y);
                            groupMap.put(group.getId(), group);
                        }
                    }
                }
            } catch (Exception ex) {
                if (!(ex instanceof FileNotFoundException)) {
                    ex.printStackTrace();
                }
            }
        }

        for (String domain : (Set<String>) manager.getResourceDomains()) {
            try {
                for (IResource resource : (List<IResource>) manager
                        .getAllResources(new ResourceLocation(domain, "sidebar_buttons.json"))) {
                    JsonElement json = DataReader.get(resource).json();

                    if (json.isJsonObject()) {
                        for (Map.Entry<String, JsonElement> entry : json.getAsJsonObject().entrySet()) {
                            if (entry.getValue().isJsonObject()) {
                                JsonObject buttonJson = entry.getValue().getAsJsonObject();

                                if (!buttonJson.has("group")) {
                                    continue;
                                }

                                if (!FTBLibConfig.debugging.dev_sidebar_buttons && buttonJson.has("dev_only")
                                        && buttonJson.get("dev_only").getAsBoolean()) {
                                    continue;
                                }

                                SidebarButtonGroup group = groupMap
                                        .get(new ResourceLocation(buttonJson.get("group").getAsString()));

                                if (group == null) {
                                    continue;
                                }

                                SidebarButton button = new SidebarButton(
                                        new ResourceLocation(domain, entry.getKey()),
                                        group,
                                        buttonJson);

                                group.getButtons().add(button);

                                if (sidebarButtonConfig.has(button.id.getResourceDomain())) {
                                    JsonElement e = sidebarButtonConfig.get(button.id.getResourceDomain());

                                    if (e.isJsonObject() && e.getAsJsonObject().has(button.id.getResourcePath())) {
                                        button.setConfig(
                                                e.getAsJsonObject().get(button.id.getResourcePath()).getAsBoolean());
                                    }
                                } else if (sidebarButtonConfig.has(button.id.toString())) {
                                    button.setConfig(sidebarButtonConfig.get(button.id.toString()).getAsBoolean());
                                }
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                if (!(ex instanceof FileNotFoundException)) {
                    ex.printStackTrace();
                }
            }
        }

        for (SidebarButtonGroup group : groupMap.values()) {
            if (!group.getButtons().isEmpty()) {
                group.getButtons().sort(null);
                groups.add(group);
            }
        }

        groups.sort(null);

        for (SidebarButtonGroup group : groups) {
            for (SidebarButton button : group.getButtons()) {
                new SidebarButtonCreatedEvent(button).post();
            }
        }

        saveConfig();
    }

    public void saveConfig() {
        JsonObject o = new JsonObject();

        for (SidebarButtonGroup group : groups) {
            for (SidebarButton button : group.getButtons()) {
                JsonObject o1 = o.getAsJsonObject(button.id.getResourceDomain());

                if (o1 == null) {
                    o1 = new JsonObject();
                    o.add(button.id.getResourceDomain(), o1);
                }

                o1.addProperty(button.id.getResourcePath(), button.getConfig());
            }
        }

        JsonUtils.toJsonSafe(new File(Minecraft.getMinecraft().mcDataDir, "local/client/sidebar_buttons.json"), o);
    }
}
