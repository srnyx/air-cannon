package xyz.srnyx.aircannon.reflection.org.bukkit;

import org.jetbrains.annotations.Nullable;

import xyz.srnyx.annoyingapi.utility.ReflectionUtility;


public enum RefParticle {;
    @Nullable public static final Class<? extends Enum> PARTICLE_ENUM = ReflectionUtility.getEnum(1, 9, 0, RefParticle.class);
}
