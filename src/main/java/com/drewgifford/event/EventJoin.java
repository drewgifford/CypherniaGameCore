package com.drewgifford.event;

import com.drewgifford.CypherniaMinigames;
import com.drewgifford.PlayerData.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class EventJoin implements Listener {

    CypherniaMinigames plugin;

    public EventJoin(CypherniaMinigames plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){


        if(plugin.allowJoins = false){

            e.getPlayer().kickPlayer("The server is still initiating. Join back in a few seconds");
            e.setJoinMessage("The server is still initiating. Join back in a few seconds");
            return;

        }
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        plugin.getGameManager().setIngame(p, false);

        plugin.playermanager.put(uuid, new PlayerManager(uuid, false, false, 0, 0));


        if(plugin.ingame == false){
            //p.teleport(plugin.spawnLoc);
            Bukkit.broadcastMessage(plugin.color("&a&l+ &f"+p.getName()));
            plugin.getGameManager().lobbyCheck();

        } else {
            p.kickPlayer("The game has already started.");
            e.setJoinMessage("The game has already started.");
        }

    }
}
