package net.azisaba.builders.worldeditlimiter;

import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.extent.AbstractDelegateExtent;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.block.BlockStateHolder;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;


public class BlockPlaceValidationExtent extends AbstractDelegateExtent {

  private final Player player;
  private final World world;

  private final long timeoutMillisecond;
  private final long operationTimeout;
  private boolean sentTimeoutMessage = false;

  protected BlockPlaceValidationExtent(Player player, World world, long timeoutMillisecond,
      Extent extent) {
    super(extent);
    this.player = player;
    this.world = world;
    this.timeoutMillisecond = timeoutMillisecond;
    this.operationTimeout = System.currentTimeMillis() + timeoutMillisecond;
  }

  @Override
  public <T extends BlockStateHolder<T>> boolean setBlock(BlockVector3 location, T block)
      throws WorldEditException {
    if (operationTimeout < System.currentTimeMillis()) {
      if (!sentTimeoutMessage) {
        player.sendMessage(ChatColor.RED + "WorldEditの処理に" + ((double) timeoutMillisecond / 1000d)
            + "秒以上かかったため、処理を停止しました！");
        sentTimeoutMessage = true;
      }
      return false;
    }
    if (isInside(world.getWorldBorder(), location)) {
      return super.setBlock(location, block);
    }
    return false;
  }

  private boolean isInside(WorldBorder border, BlockVector3 v) {
    Location loc = new Location(world, v.getBlockX(), v.getBlockY(), v.getBlockZ());
    return border.isInside(loc);
  }
}
