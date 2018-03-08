/*
 * 版权所有(C)Niconico Craft 保留所有权利
 * 您不得在未经作者许可的情况下，擅自发布本软件的任何部分或全部内容
 * 否则将会追究二次发布者的法律责任
 */
package com.moemc.login;

import static com.moemc.login.Main.accountStatus;
import static com.moemc.login.Main.apiPass;
import static com.moemc.login.Main.color_decode;
import static com.moemc.login.Main.loginAPI;
import static com.moemc.login.Main.loginStatus;
import static com.moemc.login.Main.prefix;
import static com.moemc.login.Main.textColor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.logging.Level;
import static org.bukkit.Bukkit.getLogger;
import static org.bukkit.Bukkit.getServer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class SocketServer {

    static class Task implements Runnable {

        private final Socket socket;

        public Task(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                // 创建线程，启动 Socket
                handlerSocket();
            } catch (Exception e) {
                getLogger().info("An internal error when start socket, error: " + e.getLocalizedMessage());
            }
        }

        private void handlerSocket() throws Exception {

            try (BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"))) {
                StringBuilder sb = new StringBuilder();
                String temp;
                int index;
                while ((temp = br.readLine()) != null) {
                    if ((index = temp.indexOf("eof")) != -1) {
                        sb.append(temp.substring(0, index));
                        break;
                    }
                    sb.append(temp);
                }
                String brind = sb.toString();
                String[] msgtag = brind.split("/");
                if (msgtag.length > 1) {
                    if (getServer().getPlayer(msgtag[0]) != null) {
                        Player player = getServer().getPlayer(msgtag[0]);
                        if (loginStatus.get(player.getUniqueId().toString()) == false) {
                            if ("/////".equals(accountStatus.get(player.getUniqueId().toString()))) {
                                player.kickPlayer(textColor + Main.cfgString("message.launcher.error"));
                                try (Writer writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8")) {
                                    writer.write("exit");
                                    writer.flush();
                                }
                                return;
                            }
                            player.sendMessage(prefix + textColor + Main.cfgString("message.logging"));
                            String Password = msgtag[1];
                            String nameWithoutColorCode = color_decode(player.getName());
                            new Thread() {
                                @Override
                                public void run() {
                                    String returnCode = Http.LoadHTTP(loginAPI + "?token=" + apiPass + "&user=" + nameWithoutColorCode + "&pass=" + Password, "");
                                    if (returnCode.equals("200")) {
                                        loginStatus.replace(player.getUniqueId().toString(), true);
                                        player.sendMessage(prefix + textColor + Main.cfgString("message.login.success"));
                                        player.loadData();
                                        player.setWalkSpeed(0.2f);
                                        player.removePotionEffect(PotionEffectType.BLINDNESS);
                                        player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                                        try (Writer writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8")) {
                                            writer.write("exit");
                                            writer.flush();
                                        } catch (IOException ex) {
                                        }
                                    } else {
                                        player.sendMessage(prefix + textColor + Main.cfgString("message.launcher.error"));
                                        player.sendMessage(prefix + textColor + Main.cfgString("message.login.needpass"));
                                        accountStatus.replace(player.getUniqueId().toString(), accountStatus.get(player.getUniqueId().toString()) + "/");
                                        try (Writer writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8")) {
                                            writer.write("exit");
                                            writer.flush();
                                        } catch (IOException ex) {
                                        }
                                    }
                                }
                            }.start();
                        } else {
                            try (Writer writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8")) {
                                writer.write("exit");
                                writer.flush();
                            }
                        }
                    } else {
                        try (Writer writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8")) {
                            writer.write("Player " + msgtag[0] + " offline");
                            writer.flush();
                        }
                    }
                } else {
                    try (Writer writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8")) {
                        writer.write("Bad Request");
                        writer.flush();
                    }
                }
            }
            socket.close();
        }
    }
}
