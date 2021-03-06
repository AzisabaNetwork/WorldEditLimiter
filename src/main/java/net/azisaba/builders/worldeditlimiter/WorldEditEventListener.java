package net.azisaba.builders.worldeditlimiter;

import com.sk89q.worldedit.EditSession.Stage;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.event.extent.EditSessionEvent;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.util.eventbus.Subscribe;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class WorldEditEventListener {

  private final WorldEditLimiter plugin;

  @Subscribe
  public void onEditSessionEvent(EditSessionEvent event) {
    if (event.getStage() != Stage.BEFORE_CHANGE) {
      return;
    }

    Actor actor = event.getActor();
    if (actor == null || !actor.isPlayer()) {
      return;
    }

    Player player = Bukkit.getPlayer(event.getActor().getUniqueId());
    limitMaximumBlocks(player, plugin.getPluginConfig().getMaxWorldEditBlockCount());

    if (event.getWorld() == null) {
      return;
    }
    World world = Bukkit.getWorld(event.getWorld().getName());

    try {
      event.setExtent(new BlockPlaceValidationExtent(plugin.getApi(), player, world,
          plugin.getPluginConfig().getWorldEditTimeoutMilliseconds(), event.getExtent()));
    } catch (IllegalAccessError e) {
      if (!e.getMessage().startsWith("tried to access method ")) {
        e.printStackTrace();
      }
    }
  }

  private void limitMaximumBlocks(Player p, int num) {
    LocalSession session = getSession(p);
    if (session == null) {
      return;
    }
    session.setBlockChangeLimit(num);
  }

  private LocalSession getSession(Player p) {
    WorldEditPlugin worldEdit = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
    if (worldEdit == null) {
      return null;
    }
    return worldEdit.getSession(p);
  }
}
