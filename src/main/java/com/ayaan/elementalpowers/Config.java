package com.ayaan.elementalpowers;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = ElementalPowers.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.IntValue FIRE_COOLDOWN = BUILDER
            .comment("Cooldown of the Orb of Fire, in seconds")
            .defineInRange("fireCooldownSeconds", 5, 1, 300);

    private static final ForgeConfigSpec.IntValue WATER_COOLDOWN = BUILDER
            .comment("Cooldown of the Orb of Tides, in seconds")
            .defineInRange("waterCooldownSeconds", 6, 1, 300);

    private static final ForgeConfigSpec.IntValue EARTH_COOLDOWN = BUILDER
            .comment("Cooldown of the Orb of Stone, in seconds")
            .defineInRange("earthCooldownSeconds", 8, 1, 300);

    private static final ForgeConfigSpec.IntValue LIGHTNING_COOLDOWN = BUILDER
            .comment("Cooldown of the Orb of Storms, in seconds")
            .defineInRange("lightningCooldownSeconds", 7, 1, 300);

    private static final ForgeConfigSpec.IntValue PRIMAL_COOLDOWN = BUILDER
            .comment("Cooldown of the Primal Orb, in seconds")
            .defineInRange("primalCooldownSeconds", 20, 1, 300);

    private static final ForgeConfigSpec.DoubleValue EARTH_RADIUS = BUILDER
            .comment("Radius of the Seismic Slam shockwave, in blocks")
            .defineInRange("earthRadius", 4.0, 2.0, 8.0);

    private static final ForgeConfigSpec.IntValue WATER_DOUSE_RADIUS = BUILDER
            .comment("Radius in which the Orb of Tides extinguishes fire blocks")
            .defineInRange("waterDouseRadius", 3, 1, 5);

    private static final ForgeConfigSpec.IntValue LIGHTNING_RANGE = BUILDER
            .comment("Maximum targeting range of the Orb of Storms, in blocks")
            .defineInRange("lightningRange", 32, 8, 64);

    private static final ForgeConfigSpec.BooleanValue LIGHTNING_IGNITES_FIRE = BUILDER
            .comment("Whether summoned lightning can start fires (off by default: server-friendly and cheaper)")
            .define("lightningIgnitesFire", false);

    private static final ForgeConfigSpec.DoubleValue PARTICLE_DENSITY = BUILDER
            .comment("Scales all ability particle counts. Lower this on low-end PCs (0.0 disables extra particles)")
            .defineInRange("particleDensity", 0.35, 0.0, 1.0);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static int fireCooldownSeconds;
    public static int waterCooldownSeconds;
    public static int earthCooldownSeconds;
    public static int lightningCooldownSeconds;
    public static int primalCooldownSeconds;
    public static double earthRadius;
    public static int waterDouseRadius;
    public static int lightningRange;
    public static boolean lightningIgnitesFire;
    public static double particleDensity;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        fireCooldownSeconds = FIRE_COOLDOWN.get();
        waterCooldownSeconds = WATER_COOLDOWN.get();
        earthCooldownSeconds = EARTH_COOLDOWN.get();
        lightningCooldownSeconds = LIGHTNING_COOLDOWN.get();
        primalCooldownSeconds = PRIMAL_COOLDOWN.get();
        earthRadius = EARTH_RADIUS.get();
        waterDouseRadius = WATER_DOUSE_RADIUS.get();
        lightningRange = LIGHTNING_RANGE.get();
        lightningIgnitesFire = LIGHTNING_IGNITES_FIRE.get();
        particleDensity = PARTICLE_DENSITY.get();
    }
}
