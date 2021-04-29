package net.azisaba.builders.worldeditlimiter;

import com.sk89q.worldedit.WorldEdit;
import lombok.Getter;
import net.azisaba.builders.worldeditlimiter.config.WorldEditLimiterConfig;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class WorldEditLimiter extends JavaPlugin {

  @Getter
  private WorldEditLimiterConfig pluginConfig;
  @Getter
  private final WorldEditLimiterAPI api = new WorldEditLimiterAPI();

  private WorldEditEventListener worldEditEventListener;

  @Override
  public void onEnable() {
    saveDefaultConfig();
    pluginConfig = new WorldEditLimiterConfig(this).load();

    worldEditEventListener = new WorldEditEventListener(this);
    WorldEdit.getInstance().getEventBus().register(worldEditEventListener);

    Bukkit.getLogger().info(getName() + " enabled.");
  }

  @Override
  public void onDisable() {
    WorldEdit.getInstance().getEventBus().unregister(worldEditEventListener);
    WorldEdit.getInstance().getSessionManager().clear();

    Bukkit.getLogger().info(getName() + " disabled.");
  }
}