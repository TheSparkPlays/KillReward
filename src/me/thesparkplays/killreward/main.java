package me.thesparkplays.killreward;

import java.util.HashMap;
import java.util.logging.Logger;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cod;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Dolphin;
import org.bukkit.entity.Donkey;
import org.bukkit.entity.Drowned;
import org.bukkit.entity.ElderGuardian;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Husk;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Llama;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Mule;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.Pig;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.PolarBear;
import org.bukkit.entity.PufferFish;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Salmon;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.SkeletonHorse;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Snowman;
import org.bukkit.entity.Squid;
import org.bukkit.entity.Stray;
import org.bukkit.entity.TropicalFish;
import org.bukkit.entity.Turtle;
import org.bukkit.entity.Vex;
import org.bukkit.entity.Vindicator;
import org.bukkit.entity.Witch;
import org.bukkit.entity.Wither;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.bukkit.entity.ZombieVillager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;

public class main extends JavaPlugin implements Listener
{
  private static String prefix;
  public static Economy econ = null;
  
  public main() {}
  
  public void onEnable() { saveDefaultConfig();
    getServer().getPluginManager().registerEvents(this, this);
    if (!setupEconomy())
    {
      getServer().getLogger().severe(String.format("[KillRewards]Disabled because vault could not be found.", new Object[0]));
      getServer().getPluginManager().disablePlugin(this);
      return;
    }
    
    prefix = getConfig().getString("prefix");
  }
  

  public static HashMap<String, String> messageData = new HashMap();
  
  public void onDisable() {}
  
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
  {
    if (label.equalsIgnoreCase("kr"))
    {
      if (args.length == 0)
      {
        if ((sender instanceof Player)) {
          String killRewardsBasic = getConfig().getString("killrewardsbasic");
          sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + String.format(killRewardsBasic, new Object[0])));
        }
      }
      else if (args.length == 1)
      {
        String command = args[0];
        if (command.equalsIgnoreCase("reload"))
        {
          if ((sender instanceof Player))
          {
            if (sender.hasPermission("killreward.reload"))
            {
              reloadConfig();
              String pluginReload = getConfig().getString("pluginreload");
              sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + String.format(pluginReload, new Object[0])));
            }
            else
            {
              String noPermission = getConfig().getString("nopermission");
              sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + String.format(noPermission, new Object[0])));
            }
          }
          else if (sender.isOp())
          {
            reloadConfig();
            String pluginReload = getConfig().getString("pluginreload");
            getServer().getLogger().info(ChatColor.translateAlternateColorCodes('&', prefix + String.format(pluginReload, new Object[0])));
          }
        }
        else if ((sender instanceof Player)) {
          sendHelpMessage((Player)sender);
        }
      }
      else if (args.length == 2)
      {
        String command = args[0];
        String arg = args[1];
        if (command.equalsIgnoreCase("disableworld"))
        {
          if (Bukkit.getWorld(arg) != null)
          {
            if ((sender instanceof Player))
            {
              if (sender.hasPermission("killreward.disableworld"))
              {
                getConfig().set("worlds." + arg, Boolean.valueOf(true));
                saveConfig();
                

                String disableWorld = getConfig().getString("disableworld");
                String worldName = arg;
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + String.format(disableWorld, new Object[] { worldName })));
              }
              else
              {
                String noPermission = getConfig().getString("nopermission");
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + String.format(noPermission, new Object[0])));
              }
            }
            else if (sender.isOp())
            {
              getConfig().set("worlds." + arg, Boolean.valueOf(true));
              saveConfig();
              String disableWorld = getConfig().getString("disableworld");
              String worldName = arg;
              getServer().getLogger().info(ChatColor.translateAlternateColorCodes('&', prefix + String.format(disableWorld, new Object[] { worldName })));
            }
          }
          else if ((sender instanceof Player))
          {
            if (sender.hasPermission("killreward.disableworld")) {
              String worldNotFound = getConfig().getString("worldnotfound");
              String worldName = arg;
              sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + String.format(worldNotFound, new Object[] { worldName })));
            } else {
              String noPermission = getConfig().getString("nopermission");
              sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + String.format(noPermission, new Object[0])));
            }
          }
          else if (sender.isOp()) {
            String worldNotFound = getConfig().getString("worldnotfound");
            String worldName = arg;
            getServer().getLogger().info(ChatColor.translateAlternateColorCodes('&', prefix + String.format(worldNotFound, new Object[] { worldName })));
          }
          return true;
        }
        if (command.equalsIgnoreCase("enableworld"))
        {
          if (Bukkit.getWorld(arg) != null)
          {
            if ((sender instanceof Player)) {
              if ((sender instanceof Player))
              {
                if (sender.hasPermission("killreward.enableworld"))
                {
                  getConfig().set("worlds." + arg, Boolean.valueOf(false));
                  saveConfig();
                  
                  String enableWorld = getConfig().getString("enableworld");
                  String worldName = arg;
                  sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + String.format(enableWorld, new Object[] { worldName })));
                }
                else
                {
                  String noPermission = getConfig().getString("nopermission");
                  sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + String.format(noPermission, new Object[0])));
                }
              }
              else if (sender.isOp())
              {
                getConfig().set("worlds." + arg, Boolean.valueOf(false));
                saveConfig();
                String enableWorld = getConfig().getString("enableworld");
                String worldName = arg;
                getServer().getLogger().info(ChatColor.translateAlternateColorCodes('&', prefix + String.format(enableWorld, new Object[] { worldName })));
              }
            }
          }
          else if ((sender instanceof Player))
          {
            if (sender.hasPermission("killreward.enableworld")) {
              String worldNotFound = getConfig().getString("worldnotfound");
              String worldName = arg;
              sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + String.format(worldNotFound, new Object[] { worldName })));
            } else {
              String noPermission = getConfig().getString("nopermission");
              sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + String.format(noPermission, new Object[0])));
            }
          }
          else if (sender.isOp()) {
            getServer().getLogger().info(ChatColor.translateAlternateColorCodes('&', (String)messageData.get("worldNotFound")));
          }
          return true;
        }
        if ((sender instanceof Player)) {
          sendHelpMessage((Player)sender);
        }
      }
      else if ((sender instanceof Player))
      {
        sendHelpMessage((Player)sender);
      }
      return true;
    }
    return false;
  }
  
  public void sendHelpMessage(Player p)
  {
    if (p.hasPermission("killreward.help"))
    {
      ChatColor AQUA = ChatColor.AQUA;
      ChatColor GRAY = ChatColor.GRAY;
      ChatColor GOLD = ChatColor.GOLD;
      ChatColor DARKGRAY = ChatColor.DARK_GRAY;
      p.sendMessage(DARKGRAY + "---------------" + AQUA + " KillReward Help " + DARKGRAY + "---------------");
      p.sendMessage("");
      p.sendMessage(DARKGRAY + "- " + AQUA + "/kr " + GRAY + "help " + DARKGRAY + "- " + GRAY + "Show this page.");
      p.sendMessage(DARKGRAY + "- " + AQUA + "/kr " + GRAY + "reload " + DARKGRAY + "- " + GRAY + "Reload the config.");
      p.sendMessage(DARKGRAY + "- " + AQUA + "/kr " + GRAY + "disableworld " + GOLD + "<World> " + DARKGRAY + "- " + GRAY + "Disable a world.");
      p.sendMessage(DARKGRAY + "- " + AQUA + "/kr " + GRAY + "enableworld " + GOLD + "<World> " + DARKGRAY + "- " + GRAY + "Enable a world.");
      p.sendMessage("");
    }
    else {
      String noPermission = getConfig().getString("nopermission");
      p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + String.format(noPermission, new Object[0])));
    }
  }
  
  @EventHandler
  public void onEntityDeath(EntityDeathEvent ev)
  {
    if ((ev.getEntity().getKiller() instanceof Player))
    {
      Boolean temp = Boolean.valueOf(getConfig().getBoolean("worlds." + ev.getEntity().getKiller().getWorld().getName()));
      if (!temp.booleanValue())
      {
        Player p = ev.getEntity().getKiller();
        Entity e = ev.getEntity();
        EconomyResponse r = null;
        if ((e instanceof Skeleton))
        {
          double amount = getConfig().getDouble("skeleton");
          r = econ.depositPlayer(p.getName(), amount);
        }
        else if ((e instanceof Creeper))
        {
          double amount = getConfig().getDouble("creeper");
          r = econ.depositPlayer(p.getName(), amount);
        }
        else if ((e instanceof CaveSpider))
        {
          double amount = getConfig().getDouble("cave_spider");
          r = econ.depositPlayer(p.getName(), amount);
        }
        else if ((e instanceof org.bukkit.entity.Spider))
        {
          double amount = getConfig().getDouble("spider");
          r = econ.depositPlayer(p.getName(), amount);
        }
        else if ((e instanceof PigZombie))
        {
          double amount = getConfig().getDouble("pig_zombie");
          r = econ.depositPlayer(p.getName(), amount);
        }
        else if ((e instanceof Zombie))
        {
          double amount = getConfig().getDouble("zombie");
          r = econ.depositPlayer(p.getName(), amount);
        }
        else if ((e instanceof MagmaCube))
        {
          double amount = getConfig().getDouble("magma_cube");
          r = econ.depositPlayer(p.getName(), amount);
        }
        else if ((e instanceof Slime))
        {
          double amount = getConfig().getDouble("slime");
          r = econ.depositPlayer(p.getName(), amount);
        }
        else if ((e instanceof Ghast))
        {
          double amount = getConfig().getDouble("ghast");
          r = econ.depositPlayer(p.getName(), amount);
        }
        else if ((e instanceof Enderman))
        {
          double amount = getConfig().getDouble("enderman");
          r = econ.depositPlayer(p.getName(), amount);
        }
        else if ((e instanceof Silverfish))
        {
          double amount = getConfig().getDouble("silverfish");
          r = econ.depositPlayer(p.getName(), amount);
        }
        else if ((e instanceof Blaze))
        {
          double amount = getConfig().getDouble("blaze");
          r = econ.depositPlayer(p.getName(), amount);
        }
        else if ((e instanceof Bat))
        {
          double amount = getConfig().getDouble("bat");
          r = econ.depositPlayer(p.getName(), amount);
        }
        else if ((e instanceof Witch))
        {
          double amount = getConfig().getDouble("witch");
          r = econ.depositPlayer(p.getName(), amount);
        }
        else if ((e instanceof Pig))
        {
          double amount = getConfig().getDouble("pig");
          r = econ.depositPlayer(p.getName(), amount);
        }
        else if ((e instanceof Sheep))
        {
          double amount = getConfig().getDouble("sheep");
          r = econ.depositPlayer(p.getName(), amount);
        }
        else if ((e instanceof MushroomCow))
        {
          double amount = getConfig().getDouble("mushroom_cow");
          r = econ.depositPlayer(p.getName(), amount);
        }
        else if ((e instanceof Cow))
        {
          double amount = getConfig().getDouble("cow");
          r = econ.depositPlayer(p.getName(), amount);
        }
        else if ((e instanceof Chicken))
        {
          double amount = getConfig().getDouble("chicken");
          r = econ.depositPlayer(p.getName(), amount);
        }
        else if ((e instanceof Squid))
        {
          double amount = getConfig().getDouble("squid");
          r = econ.depositPlayer(p.getName(), amount);
        }
        else if ((e instanceof Wolf))
        {
          double amount = getConfig().getDouble("wolf");
          r = econ.depositPlayer(p.getName(), amount);
        }
        else if ((e instanceof Ocelot))
        {
          double amount = getConfig().getDouble("ocelot");
          r = econ.depositPlayer(p.getName(), amount);
        }
        else if ((e instanceof org.bukkit.entity.Villager))
        {
          double amount = getConfig().getDouble("villager");
          r = econ.depositPlayer(p.getName(), amount);
        }
        else if ((e instanceof org.bukkit.entity.EnderDragon))
        {
          double amount = getConfig().getDouble("enderdragon");
          r = econ.depositPlayer(p.getName(), amount);
        }
        else if ((e instanceof Wither))
        {
          double amount = getConfig().getDouble("wither");
          r = econ.depositPlayer(p.getName(), amount);
        }
        else if ((e instanceof Guardian))
        {
          double amount = getConfig().getDouble("guardian");
          r = econ.depositPlayer(p.getName(), amount);
        }
        else if ((e instanceof ElderGuardian))
        {
          double amount = getConfig().getDouble("elder_guardian");
          r = econ.depositPlayer(p.getName(), amount);
        }
        else if ((e instanceof Drowned))
        {
          double amount = getConfig().getDouble("drowned");
          r = econ.depositPlayer(p.getName(), amount);
        }
        else if ((e instanceof org.bukkit.entity.Endermite))
        {
          double amount = getConfig().getDouble("endermite");
          r = econ.depositPlayer(p.getName(), amount);
        }
        else if ((e instanceof Husk))
        {
          double amount = getConfig().getDouble("husk");
          r = econ.depositPlayer(p.getName(), amount);
        }
        else if ((e instanceof Phantom))
        {
          double amount = getConfig().getDouble("phantom");
          r = econ.depositPlayer(p.getName(), amount);
        }
        else if ((e instanceof org.bukkit.entity.Shulker))
        {
          double amount = getConfig().getDouble("shulker");
          r = econ.depositPlayer(p.getName(), amount);
        }
        else if ((e instanceof Stray))
        {
          double amount = getConfig().getDouble("stray");
          r = econ.depositPlayer(p.getName(), amount);
        }
        else if ((e instanceof Vex))
        {
          double amount = getConfig().getDouble("vex");
          r = econ.depositPlayer(p.getName(), amount);
        }
        else if ((e instanceof Vindicator))
        {
          double amount = getConfig().getDouble("vindicator");
          r = econ.depositPlayer(p.getName(), amount);
        }
        else if ((e instanceof WitherSkeleton))
        {
          double amount = getConfig().getDouble("wither_skeleton");
          r = econ.depositPlayer(p.getName(), amount);
        }
        else if ((e instanceof ZombieVillager))
        {
          double amount = getConfig().getDouble("zombie_villager");
          r = econ.depositPlayer(p.getName(), amount);
        }
        else if ((e instanceof IronGolem))
        {
          double amount = getConfig().getDouble("iron_golem");
          r = econ.depositPlayer(p.getName(), amount);
        }
        else if ((e instanceof Parrot))
        {
          double amount = getConfig().getDouble("parrot");
          r = econ.depositPlayer(p.getName(), amount);
        }
        else if ((e instanceof Llama))
        {
          double amount = getConfig().getDouble("llama");
          r = econ.depositPlayer(p.getName(), amount);
        }
        else if ((e instanceof Cod))
        {
          double amount = getConfig().getDouble("cod");
          r = econ.depositPlayer(p.getName(), amount);
        }
        else if ((e instanceof Salmon))
        {
          double amount = getConfig().getDouble("salmon");
          r = econ.depositPlayer(p.getName(), amount);
        }
        else if ((e instanceof TropicalFish))
        {
          double amount = getConfig().getDouble("tropical_fish");
          r = econ.depositPlayer(p.getName(), amount);
        }
        else if ((e instanceof Dolphin))
        {
          double amount = getConfig().getDouble("dolphin");
          r = econ.depositPlayer(p.getName(), amount);
        }
        else if ((e instanceof PufferFish))
        {
          double amount = getConfig().getDouble("puffer_fish");
          r = econ.depositPlayer(p.getName(), amount);
        }
        else if ((e instanceof SkeletonHorse))
        {
          double amount = getConfig().getDouble("skeleton_horse");
          r = econ.depositPlayer(p.getName(), amount);
        }
        else if ((e instanceof Turtle))
        {
          double amount = getConfig().getDouble("turtle");
          r = econ.depositPlayer(p.getName(), amount);
        }
        else if ((e instanceof Rabbit))
        {
          double amount = getConfig().getDouble("rabbit");
          r = econ.depositPlayer(p.getName(), amount);
        }
        else if ((e instanceof PolarBear))
        {
          double amount = getConfig().getDouble("polar_bear");
          r = econ.depositPlayer(p.getName(), amount);
        }
        else if ((e instanceof Mule))
        {
          double amount = getConfig().getDouble("mule");
          r = econ.depositPlayer(p.getName(), amount);
        }
        else if ((e instanceof Horse))
        {
          double amount = getConfig().getDouble("horse");
          r = econ.depositPlayer(p.getName(), amount);
        }
        else if ((e instanceof Donkey))
        {
          double amount = getConfig().getDouble("donkey");
          r = econ.depositPlayer(p.getName(), amount);
        }
        else if ((e instanceof Snowman))
        {
          double amount = getConfig().getDouble("snowman");
          r = econ.depositPlayer(p.getName(), amount);
        }
        if (r != null) {
          if (r.transactionSuccess()) {
            String mobKill = getConfig().getString("mobkill");
            String amountReceived = econ.format(amount);
            String AmountTotal = econ.format(balance);
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + String.format(mobKill, new Object[] { amountReceived, AmountTotal })));
          } else {
            p.sendMessage(String.format(prefix + "Error: " + ChatColor.AQUA + "%s" + ChatColor.RESET, new Object[] { errorMessage }));
          }
        }
      }
    }
  }
  
  private boolean setupEconomy()
  {
    if (getServer().getPluginManager().getPlugin("Vault") == null) {
      return false;
    }
    RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
    if (rsp == null) {
      return false;
    }
    econ = (Economy)rsp.getProvider();
    return econ != null;
  }
}
