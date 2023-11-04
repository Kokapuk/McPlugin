package org.kokapuk.mcplugin;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;

import static org.bukkit.Bukkit.getPlayer;
import static org.bukkit.Bukkit.getServer;

public class Listeners implements Listener {
    @EventHandler
    public void snowballGrounded(ProjectileHitEvent event) {
        Location location = null;
        World world = null;

        if (!(event.getEntity() instanceof Snowball)) {
            return;
        }

        if (event.getHitBlock() != null) {
            Block block = event.getHitBlock();
            location = block.getLocation().add(0, 2, 0);
            world = block.getWorld();
        } else if (event.getHitEntity() != null) {
            Entity entity = event.getHitEntity();
            location = entity.getLocation().add(0, 1, 0);
            world = entity.getWorld();
        }

        if (location != null && world != null) {
            world.createExplosion(location, 5, false, true, (Entity) event.getEntity().getShooter());
//            world.strikeLightning(location);
        }
    }

    @EventHandler
    public void eggGrounded(ProjectileHitEvent event) {
        Location location = null;
        World world = null;

        if (!(event.getEntity() instanceof Egg)) {
            return;
        }

        if (event.getHitBlock() != null) {
            Block block = event.getHitBlock();
            location = block.getLocation().add(0, 2, 0);
            world = block.getWorld();
        } else if (event.getHitEntity() != null) {
            Entity entity = event.getHitEntity();
            location = entity.getLocation().add(0, 1, 0);
            world = entity.getWorld();
        }

        if (location != null && world != null) {
            world.createExplosion(location, 3, false, false, (Entity) event.getEntity().getShooter());
        }
    }

//    @EventHandler
//    public void entityExploded(EntityExplodeEvent event) {
//        List<Block> blocks = event.blockList();
//        World world = event.getEntity().getWorld();
//
//        for (Block block : blocks) {
//            Location location = block.getLocation();
//            world.spawnFallingBlock(location.add(0, 2, 0), block.getBlockData());
//        }
//
//        world.createExplosion(event.getLocation(), 5, false, false);
//    }

    @EventHandler
    public void entityDied(EntityDeathEvent event) {
        LivingEntity damaged = event.getEntity();
        World world = damaged.getWorld();
        Location location = damaged.getLocation().add(0, 1, 0);

        world.spawnParticle(Particle.BLOCK_CRACK, location, 200, Material.REDSTONE_BLOCK.createBlockData());
        world.playSound(location, Sound.BLOCK_STONE_BREAK, SoundCategory.NEUTRAL, 3, 1);
        world.spawnParticle(Particle.BLOCK_CRACK, location.add(0, 1, 0), 200, Material.REDSTONE_BLOCK.createBlockData());

        Player killer = damaged.getKiller();

        if (killer == null) {
            return;
        }

        killer.playSound(killer.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 5.f, 1.55f);
    }

    @EventHandler
    public void playerInteractedEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();

        switch (player.getInventory().getItemInMainHand().getType()) {
            case BLAZE_ROD:
                event.getRightClicked().addPassenger(player);
                break;
        }
    }

    @EventHandler
    public void playerInteracted(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        switch (player.getInventory().getItemInMainHand().getType()) {
            case WITHER_SKELETON_SKULL:
                player.launchProjectile(WitherSkull.class);
                break;
            case FIRE_CHARGE:
                player.launchProjectile(Fireball.class);
                break;
        }
    }

    @EventHandler
    public void blockPlaced(BlockPlaceEvent event) {
        switch (event.getBlockPlaced().getType()) {
            case WITHER_SKELETON_SKULL:
            case WITHER_SKELETON_WALL_SKULL:
                event.setCancelled(true);
                break;
        }
    }

    @EventHandler
    public void entityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Warden)) {
            return;
        }

        Damageable damageable = (Damageable) event.getEntity();

        getServer().broadcastMessage(damageable.getName() + ' ' + Double.toString(damageable.getHealth()) + '/' + Double.toString(damageable.getMaxHealth()));
    }
}
