package com.drewgifford.event;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import com.drewgifford.CypherniaMinigames;
import com.drewgifford.PlayerData.PlayerManager;

public class EventJoin implements Listener {

	CypherniaMinigames plugin;

	public EventJoin(CypherniaMinigames plugin){
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onJoin(PlayerJoinEvent e){

		Player p = e.getPlayer();

		if(plugin.getGameManager().getIngamePlayers().size() == 0){
			e.setJoinMessage(plugin.color("&a&l+ &f"+p.getName()));
			plugin.getGameManager().lobbyCheck(Bukkit.getServer().getOnlinePlayers().size());
		} else {
			if (plugin.getConfig().getBoolean("bungeecord.useBungee")) {
				e.getPlayer().sendMessage("§cThis game has already started!");
				e.setJoinMessage("");
				plugin.playersQuit.add(e.getPlayer().getUniqueId());
				new BukkitRunnable() {
					@Override
					public void run() {
						plugin.connectToBungeeServer(e.getPlayer(), plugin.getConfig().getString("bungeecord.fallback-server"));
					}
				}.runTaskLater(plugin, 10L);

			} else {
				p.kickPlayer("The game has already started.");
				e.setJoinMessage("The game has already started.");
			}
			return;
		}

		if(plugin.allowJoins == false){
			if (plugin.getConfig().getBoolean("bungeecord.useBungee")) {
				e.getPlayer().sendMessage("§6The server is still initiating. Join back in a few seconds");
				e.setJoinMessage("");
				plugin.playersQuit.add(e.getPlayer().getUniqueId());
				new BukkitRunnable() {
					@Override
					public void run() {
						plugin.connectToBungeeServer(e.getPlayer(), plugin.getConfig().getString("bungeecord.fallback-server"));
					}
				}.runTaskLater(plugin, 10L);
			} else {
				e.getPlayer().kickPlayer("The server is still initiating. Join back in a few seconds");
				e.setJoinMessage("The server is still initiating. Join back in a few seconds");
			}
			return;
		}

		p.setAllowFlight(false);
		p.setFlying(false);
		p.getInventory().clear();
		p.getInventory().setHelmet(null);
		p.getInventory().setChestplate(null);
		p.getInventory().setLeggings(null);
		p.getInventory().setBoots(null);
		p.updateInventory();
		p.setGameMode(GameMode.SURVIVAL);
		p.setHealth(20);
		p.setFoodLevel(20);
		p.setExp((float) 0);
		for (PotionEffect effect : p.getActivePotionEffects()) {
			p.removePotionEffect(effect.getType());
		}
		p.getInventory().setItem(4, plugin.getKitSelector());
		if (plugin.getConfig().getBoolean("bungeecord.useBungee")) { 
			p.getInventory().setItem(8, plugin.getHubSelector());
		}
		p.updateInventory();
		p.setFireTicks(0);

		UUID uuid = p.getUniqueId();

		plugin.getScoreboardManager().addPlayer(p);
		plugin.getGameManager().setIngame(p, false);

		plugin.playermanager.put(uuid, new PlayerManager(uuid, false, false, 0, 0));
	}
}
