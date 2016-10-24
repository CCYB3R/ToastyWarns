package me.CyberCube_.TW;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin
{
  public String prefix = ChatColor.GRAY + "[" + ChatColor.DARK_RED + "ToastyWarns" + ChatColor.GRAY + "]" + ChatColor.WHITE;
  Events events = new Events(this);

  public void onEnable()
  {
    Bukkit.getPluginManager().registerEvents(this.events, this);
    getLogger().info("Toasting some bread");
    getLogger().info("10%");
    getLogger().info("33%");
    getLogger().info("67%");
    getLogger().info("88%");
    getLogger().info("100%");
    getLogger().info("Toasty bread done!");
    getLogger().info("Plugin Fully Loaded!");
    getLogger().info("Plugin Made By: CyberCube_ (dev)");
    saveDefaultConfig();
  }

  public void onDisable()
  {
  }

  public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
  {
    try
    {
      if (command.getName().equalsIgnoreCase("warn")) {
        if (args.length == 1)
          try {
            if (addWarn(args[0].toLowerCase())) {
              sender.sendMessage(this.prefix + " " + getMessage("playerwarned"));
              try {
                Player p = Bukkit.getPlayer(args[0]);
                p.sendMessage(this.prefix + " " + getMessage("recivedwarning")); } catch (Exception localException1) {
              }
            } else {
              sender.sendMessage(this.prefix + " " + getMessage("warnfailuserbanned").replace("%a", getConfig().getString("customactiontext")));
            }
          } catch (Exception e) {
            sender.sendMessage(this.prefix + " " + getMessage("palyernotfound"));
          }
        else if (args.length == 0) {
          sender.sendMessage(this.prefix + " Use: /warn <PlayerName>");
        }
        return true;
      }
      if (command.getName().equalsIgnoreCase("warning")) {
        if ((args[0].equalsIgnoreCase("see")) && (args.length == 2)) {
          if (args[1].equalsIgnoreCase("all")) {
            List l = new ArrayList(getConfig().getConfigurationSection("warnings").getKeys(false));
            String a = "All warnings: ";
            String txt = "";
            int i = 0;
            for (String s : getConfig().getConfigurationSection("warnings").getKeys(false)) {
              int w = getPlayerWarns(s);
              if (i == 0) {
                txt = a + s + "(" + w + "); ";
                i++;
              } else {
                txt = txt + s + "(" + w + "); ";
              }
            }
            sender.sendMessage(txt);
          } else {
            sender.sendMessage(this.prefix + " Player warns: " + getPlayerWarns(args[1].toLowerCase()));
          }
        } else if ((args[0].equalsIgnoreCase("see")) && (args.length == 1)) {
          sender.sendMessage(this.prefix + " Avaliables commands:");
          sender.sendMessage(this.prefix + " /warn [Player]");
          sender.sendMessage(this.prefix + " /warning see/remove [Player]");
          return true;
        }
        if ((args[0].equalsIgnoreCase("remove")) && (args.length == 2)) {
          try {
            if (removeWarn(args[1].toLowerCase())) {
              sender.sendMessage(this.prefix + " " + getMessage("removedwarn"));
              try {
                Player p = Bukkit.getPlayer(args[1]);
                p.sendMessage(this.prefix + " " + getMessage("removedwarnuser"));
              } catch (Exception localException2) {
              }
            } else {
              sender.sendMessage(this.prefix + " " + getMessage("negativewarning"));
            }
          } catch (Exception e) {
            sender.sendMessage(this.prefix + " " + getMessage("palyernotfound"));
          }
        } else if ((args[0].equalsIgnoreCase("remove")) && (args.length == 1)) {
          sender.sendMessage(this.prefix + " Avaliables commands:");
          sender.sendMessage(this.prefix + " /warn [Player]");
          sender.sendMessage(this.prefix + " /warning see/remove [Player]");
          return true;
        }
        return true;
      }
    } catch (Exception e) {
      sender.sendMessage(this.prefix + " Avaliables commands:");
      sender.sendMessage(this.prefix + " /warn [Player]");
      sender.sendMessage(this.prefix + " /warning see/remove [Player]");
      return true;
    }
    return false;
  }

  public int getPlayerWarns(String p) {
    return getConfig().getInt("warnings." + p);
  }

  public boolean addWarn(String p)
  {
    if (getPlayerWarns(p) < getConfig().getInt("maxwarns")) {
      getConfig().set("warnings." + p, Integer.valueOf(getConfig().getInt("warnings." + p) + 1));
      saveConfig();
      return true;
    }
    if (getConfig().getBoolean("customaction")) {
      try {
        Player pl = Bukkit.getPlayer(p);
        pl.kickPlayer(this.prefix + " " + getMessage("youwaswarned").replace("%w", new StringBuilder(String.valueOf(getConfig().getInt("maxwarns"))).append(getConfig().getString("buymessage")).toString()).replace("%a", getConfig().getString("customactiontext"))); } catch (Exception localException) {
      }
      Bukkit.dispatchCommand(Bukkit.getConsoleSender(), getConfig().getString("customcommand").replace("%p", p));
    } else {
      try {
        Player pl = Bukkit.getPlayer(p);
        pl.kickPlayer(this.prefix + " " + getMessage("youwaswarned").replace("%w", new StringBuilder(String.valueOf(getConfig().getInt("maxwarns"))).append(" ").append(getConfig().getString("buymessage")).toString())); } catch (Exception localException1) {
      }
      Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ban " + p + " you have so many warnings");
    }
    return false;
  }

  public boolean removeWarn(String p)
  {
    if ((getPlayerWarns(p) <= getConfig().getInt("maxwarns")) && (getPlayerWarns(p) > -0)) {
      getConfig().set("warnings." + p, Integer.valueOf(getConfig().getInt("warnings." + p) - 1));
      saveConfig();
      if (Bukkit.getOfflinePlayer(p).isBanned()) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pardon " + p);
      }
      return true;
    }

    return false;
  }

  public String getMessage(String msgname)
  {
    return getConfig().getString(msgname);
  }

  private static class Events implements Listener
  {
    public static Main plugin;

    public Events(Main instance) {
      plugin = instance;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent evt) {
      if (!plugin.getConfig().contains("warnings." + evt.getPlayer().getName().toLowerCase())) {
        plugin.getConfig().createSection("warnings." + evt.getPlayer().getName().toLowerCase());
        plugin.getConfig().set("warnings." + evt.getPlayer().getName().toLowerCase(), Integer.valueOf(0));
        plugin.saveConfig();
      }
    }
  }
}