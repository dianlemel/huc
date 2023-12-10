package com.dianlemel.huc;

import com.dianlemel.huc.util.BukkitUtil;
import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public class UHCPlayer {

    private static final Map<UUID, UHCPlayer> players = Maps.newConcurrentMap();

    public static UHCPlayer getUHCPlayer(UUID uuid) {
        return players.computeIfAbsent(uuid, UHCPlayer::new);
    }

    private final UUID uuid;
    private final String name;
    private Optional<Player> player;
    private boolean isDead = false;
    private boolean isStart = false;

    public UHCPlayer(UUID uuid) {
        this.uuid = uuid;
        this.name = Bukkit.getServer().getPlayer(uuid).getName();
    }

    public void start() {
        isStart = true;
        isDead = false;
    }

    public void stop() {
        isStart = false;
        isDead = false;
    }

    public void setHealth(double h) {
        BukkitUtil.setHealth(getPlayer(), h);
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public String getName() {
        return this.name;
    }

    public void online() {
        player = Optional.of(Bukkit.getPlayer(uuid));
    }

    public void offline() {
        player = Optional.empty();
    }

    public void ifOnline(Consumer<Player> call) {
        player.ifPresent(call);
    }

    public boolean isOnline() {
        return player.isPresent();
    }

    public void setGameMode(GameMode gameMode) {
        ifOnline(player -> player.setGameMode(gameMode));
    }

    public void teleport(Location location) {
        ifOnline(player -> player.teleport(location));
    }

    public Player getPlayer() {
        return player.orElseGet(() -> null);
    }

    public GameMode getGameMode() {
        if (isOnline()) {
            return getPlayer().getGameMode();
        } else {
            return GameMode.SPECTATOR;
        }
    }

    public UHCTeam getTeam() {
        return UHCTeam.getTeam(uuid);
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public boolean isStart() {
        return isStart;
    }
}
