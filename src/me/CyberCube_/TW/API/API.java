package me.CyberCube_.TW.API;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class API
{
  Plugin pl = Bukkit.getPluginManager().getPlugin("ToastyWarnings");

  public int getPlayerWarnings(Player p) {
    return this.pl.getConfig().getInt("warning." + p.getName());
  }
}