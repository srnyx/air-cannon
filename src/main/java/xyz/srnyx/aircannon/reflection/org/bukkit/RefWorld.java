package xyz.srnyx.aircannon.reflection.org.bukkit;

import org.bukkit.Location;
import org.bukkit.World;

import org.jetbrains.annotations.Nullable;

import xyz.srnyx.annoyingapi.utility.ReflectionUtility;

import java.lang.reflect.Method;


public class RefWorld {
    @Nullable public static final Method WORLD_SPAWN_PARTICLE_METHOD = ReflectionUtility.getMethod(1, 9, 0, World.class, "spawnParticle", RefParticle.PARTICLE_ENUM, Location.class, int.class, double.class, double.class, double.class, double.class);
}
