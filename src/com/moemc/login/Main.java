/*
 * 版权所有(C)Niconico Craft 保留所有权利
 * 您不得在未经作者许可的情况下，擅自发布本软件的任何部分或全部内容
 * 否则将会追究二次发布者的法律责任
 */
package com.moemc.login;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 *
 * @author KasuganoSora
 */
public class Main extends JavaPlugin implements Listener {
    
    public static Map<String, Boolean> loginStatus = new HashMap<String, Boolean>();
    public static Map<String, String> accountStatus = new HashMap<String, String>();
    public static FileConfiguration pluginConfig;
    public static String prefix, textColor, loginAPI, apiPass;
    public static int configPort;
    
    @Override
    public void onEnable() {
        getLogger().info("Sakura Login plugin enabled!");
        getLogger().info("Author: KasuganoSora");
        File file = new File(getDataFolder(), "config.yml");
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
            getLogger().info("Plugin config folder not exist, trying create it...");
        }
        if (!file.exists()) {
            this.saveDefaultConfig();
            getLogger().info("Successful save default config.");
            this.reloadConfig();
            getLogger().info("Successful load new config.");
        }
        try {
            pluginConfig = this.load(file);
            getServer().getPluginManager().registerEvents(this, this);
            getLogger().info("Events register successful!");
            prefix = pluginConfig.getString("prefix").replaceAll("&", "§");
            textColor = pluginConfig.getString("color").replaceAll("&", "§");
            configPort = pluginConfig.getInt("port");
            loginAPI = cfgString("apiurl");
            apiPass = cfgString("connectpass");
            getLogger().info("Config file load successful.");
        } catch (Exception ex) {
            getLogger().info("An internal error when load this plugin, error: " + ex.getLocalizedMessage());
        }
        if (cfgBoolean("remote")) {
            try {
                new Thread() {
                    @Override
                    public void run() {
                        getLogger().info("Starting remote login api...");
                        getLogger().info("Socket bind on port: " + configPort);
                        try {
                            int port = configPort;
                            ServerSocket server = new ServerSocket(port);
                            getLogger().info("Successful start remote login api.");
                            while (true) {
                                Socket socket = server.accept();
                                new Thread(new SocketServer.Task(socket)).start();
                            }
                        } catch (IOException ex) {
                            getLogger().info("An internal error when starting remote login api, error: " + ex.getLocalizedMessage());
                        }
                    }
                }.start();
            } catch (Exception er) {
                getLogger().info("Failed to start remote login api, error: " + er.getLocalizedMessage());
            }
        } else {
            getLogger().info("Remote login api disabled, now the player only can login in game.");
        }
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        loginStatus.put(player.getUniqueId().toString(), false);
        accountStatus.put(player.getUniqueId().toString(), "");
        String nameWithoutColorCode = color_decode(player.getName());
        String returnCode = Http.LoadHTTP(loginAPI + "?token=" + apiPass + "&user=" + nameWithoutColorCode, "");
        switch (returnCode) {
            case "200":
                player.sendMessage(prefix + textColor + cfgString("message.welcome"));
                break;
            case "404":
                if (cfgBoolean("register")) {
                    player.sendMessage(prefix + textColor + cfgString("message.register.regmsg"));
                } else {
                    player.kickPlayer(textColor + cfgString("message.account.notexist"));
                }
                break;
            case "500":
                player.kickPlayer(textColor + cfgString("message.account.format"));
                break;
            case "501":
                player.kickPlayer(textColor + cfgString("message.account.disable"));
                break;
            default:
                player.kickPlayer(textColor + cfgString("message.servererr"));
        }
    }
    
    @EventHandler
    public void onPlayerChatEvent(PlayerChatEvent event) {
        Player player = event.getPlayer();
        if (loginStatus.get(player.getUniqueId().toString()) == false) {
            if ("/////".equals(accountStatus.get(player.getUniqueId().toString()))) {
                player.kickPlayer(textColor + cfgString("message.errormax"));
                event.setCancelled(true);
                return;
            }
            player.sendMessage(prefix + textColor + cfgString("message.logging"));
            String Password = event.getMessage();
            String nameWithoutColorCode = color_decode(player.getName());
            event.setCancelled(true);
            new Thread() {
                @Override
                public void run() {
                    try {
                        String returnCode = Http.LoadHTTP(loginAPI + "?token=" + apiPass + "&user=" + nameWithoutColorCode + "&pass=" + Password, "");
                        switch (returnCode) {
                            case "200":
                                loginStatus.replace(player.getUniqueId().toString(), true);
                                player.sendMessage(prefix + textColor + cfgString("message.login.success"));
                                break;
                            case "201":
                                loginStatus.replace(player.getUniqueId().toString(), true);
                                player.sendMessage(prefix + textColor + cfgString("message.register.success"));
                                break;
                            case "502":
                                player.kickPlayer(textColor + cfgString("message.register.error"));
                                break;
                            case "403":
                                player.sendMessage(prefix + textColor + cfgString("message.login.error"));
                                player.sendMessage(prefix + textColor + cfgString("message.login.needpass"));
                                accountStatus.replace(player.getUniqueId().toString(), accountStatus.get(player.getUniqueId().toString()) + "/");
                                break;
                            default:
                                player.kickPlayer(textColor + cfgString("message.error").replace("%s", returnCode));
                        }
                    } catch (IllegalStateException | NullPointerException ex) {
                        // 我也不知道什么鬼问题
                    }
                }
            }.start();
        }
    }
    
    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (loginStatus.get(player.getUniqueId().toString())) {
            loginStatus.replace(player.getUniqueId().toString(), false);
        }
    }
    
    @EventHandler
    public void onPlayerKickEvent(PlayerKickEvent event) {
        Player player = event.getPlayer();
        loginStatus.replace(player.getUniqueId().toString(), false);
    }
    
    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {
        try {
            Player player = event.getPlayer();
            if (player.getUniqueId().toString() != null) {
                if (!loginStatus.get(player.getUniqueId().toString())) {
                    player.sendMessage(prefix + textColor + cfgString("message.login.notlogin"));
                    event.setCancelled(true);
                }
            } else {
                // 触发这个事件的，你不是一个人...
            }
        } catch (NullPointerException er) {
            // 滚！有异常我也不抛出，给你憋回去！
            // getLogger().info("哎~登录插件发生了一个空指针异常错误呢~");
        }
    }
    
    @EventHandler
    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        if (!loginStatus.get(player.getUniqueId().toString())) {
            player.sendMessage(prefix + textColor + cfgString("message.login.notlogin"));
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onPlayerBedEnterEvent(PlayerBedEnterEvent event) {
        Player player = event.getPlayer();
        if (!loginStatus.get(player.getUniqueId().toString())) {
            player.sendMessage(prefix + textColor + cfgString("message.login.notlogin"));
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onPlayerPickupItemEvent(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        if (!loginStatus.get(player.getUniqueId().toString())) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (!loginStatus.get(player.getUniqueId().toString())) {
            player.sendMessage(prefix + textColor + cfgString("message.login.notlogin"));
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (!loginStatus.get(player.getUniqueId().toString())) {
            player.sendMessage(prefix + textColor + cfgString("message.login.notlogin"));
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!loginStatus.get(player.getUniqueId().toString())) {
            Location from = event.getFrom();
            player.teleport(from);
        }
    }
    
    @EventHandler
    public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (!loginStatus.get(player.getUniqueId().toString())) {
            event.setCancelled(true);
        }
    }
    
    public static String color_decode(String Str) {
        return Str.replaceAll("§a", "").replaceAll("§b", "").replaceAll("§c", "").replaceAll("§d", "").replaceAll("§e", "").replaceAll("§f", "").replaceAll("§l", "").replaceAll("§n", "").replaceAll("§o", "").replaceAll("§r", "").replaceAll("§1", "").replaceAll("§2", "").replaceAll("§3", "").replaceAll("§4", "").replaceAll("§5", "").replaceAll("§6", "").replaceAll("§7", "").replaceAll("§8", "").replaceAll("§9", "").replaceAll("§0", "").replaceAll("§A", "").replaceAll("§B", "").replaceAll("§C", "").replaceAll("§D", "").replaceAll("§E", "").replaceAll("§F", "").replaceAll("§L", "").replaceAll("§N", "").replaceAll("§O", "").replaceAll("§R", "");
    }
    
    public FileConfiguration load(File file) {
        if (!(file.exists())) {
            try {
                file.createNewFile();
            } catch (IOException e) {
            }
        }
        return YamlConfiguration.loadConfiguration(file);
    }
    
    public static String cfgString(String str) {
        try {
            return pluginConfig.getString(str).replaceAll("&", "§");
        } catch (NullPointerException ex) {
            return "";
        }
    }
    
    public static int cfgInt(String str) {
        try {
            return pluginConfig.getInt(str);
        } catch (NullPointerException ex) {
            return 0;
        }
    }
    
    public static boolean cfgBoolean(String str) {
        try {
            return pluginConfig.getBoolean(str);
        } catch (NullPointerException ex) {
            return false;
        }
    }
}
