package net.azisaba.builders.worldeditlimiter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import lombok.NoArgsConstructor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@NoArgsConstructor
public class WorldEditLimiterAPI {

  private final List<BiFunction<Player, Location, Boolean>> functionList = new ArrayList<>();

  public void registerJudgement(BiFunction<Player, Location, Boolean> func) {
    functionList.add(func);
  }

  public void unregisterJudgement(BiFunction<Player, Location, Boolean> func) {
    functionList.remove(func);
  }

  public void clearJudgement() {
    functionList.clear();
  }

  protected boolean executeFunction(Player p, Location loc) {
    for (BiFunction<Player, Location, Boolean> func : functionList) {
      if (!func.apply(p, loc)) {
        return false;
      }
    }
    return true;
  }
}