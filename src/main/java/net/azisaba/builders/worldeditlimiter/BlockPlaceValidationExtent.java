package net.azisaba.builders.worldeditlimiter;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.extent.AbstractDelegateExtent;
import com.sk89q.worldedit.extent.Extent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;

public class BlockPlaceValidationExtent extends AbstractDelegateExtent {

  private final WorldEditLimiterAPI api;
  private final Player player;
  private final World world;

  private final long timeoutMillisecond;
  private final long operationTimeout;
  private boolean sentTimeoutMessage = false;

  protected BlockPlaceValidationExtent(WorldEditLimiterAPI api, Player player, World world,
      long timeoutMillisecond,
      Extent extent) {
    super(extent);
    this.api = api;
    this.player = player;
    this.world = world;
    this.timeoutMillisecond = timeoutMillisecond;
    this.operationTimeout = System.currentTimeMillis() + timeoutMillisecond;
  }

  @Override
  public boolean setBlock(Vector location, BaseBlock block) throws WorldEditException {
    if (operationTimeout < System.currentTimeMillis()) {
      if (!sentTimeoutMessage) {
        player.sendMessage(ChatColor.RED + "WorldEditの処理に" + ((double) timeoutMillisecond / 1000d)
            + "秒以上かかったため、処理を停止しました！");
        sentTimeoutMessage = true;
      }
      return false;
    }
    if (!isInside(world.getWorldBorder(), location)) {
      return false;
    }
    if (!api.executeFunction(player, getLocation(world, location))) {
      return false;
    }
    return super.setBlock(location, block);
  }

  private boolean isInside(WorldBorder border, Vector v) {
    return border.isInside(getLocation(world, v));
  }

  private Location getLocation(World world, Vector v) {
    return new Location(world, v.getBlockX(), v.getBlockY(), v.getBlockZ());
  }
}
