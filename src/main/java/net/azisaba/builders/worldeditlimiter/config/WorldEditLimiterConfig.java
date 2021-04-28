package net.azisaba.builders.worldeditlimiter.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.azisaba.builders.worldeditlimiter.WorldEditLimiter;
import org.bukkit.configuration.file.FileConfiguration;

@Getter
@RequiredArgsConstructor
public class WorldEditLimiterConfig {

  private final WorldEditLimiter plugin;

  private int maxWorldEditBlockCount;
  private long worldEditTimeoutMilliseconds;

  public WorldEditLimiterConfig load() {
    FileConfiguration conf = plugin.getConfig();

    maxWorldEditBlockCount = conf.getInt("WorldEdit.MaxEditBlock");
    worldEditTimeoutMilliseconds = conf.getLong("WorldEdit.TimeoutMilliseconds");

    return this;
  }
}