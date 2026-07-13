package com.ayaan.elementalpowers.item;

import com.ayaan.elementalpowers.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.hurtingprojectile.SmallFireball;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;

/**
 * One item class powers every element. All ability logic runs server-side, only when the
 * player right-clicks — the mod registers no tick handlers and spawns no custom entities,
 * which keeps its runtime cost near zero on low-end machines.
 */
public final class ElementalOrbItem extends Item {
    private final Element element;

    public ElementalOrbItem(Element element, Properties properties) {
        super(properties);
        this.element = element;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level instanceof ServerLevel serverLevel && player instanceof ServerPlayer serverPlayer) {
            this.element.activate(serverLevel, serverPlayer);
            player.getCooldowns().addCooldown(stack, this.element.cooldownTicks());
            player.awardStat(Stats.ITEM_USED.get(this));
            return InteractionResult.SUCCESS_SERVER;
        }
        return InteractionResult.SUCCESS;
    }

    public enum Element {
        FIRE {
            @Override
            void activate(ServerLevel level, ServerPlayer player) {
                // Flame Burst: a fan of three small fireballs plus brief fire immunity
                player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 10 * 20, 0));
                Vec3 look = player.getLookAngle();
                for (int i = -1; i <= 1; i++) {
                    Vec3 dir = look.yRot((float) Math.toRadians(i * 12));
                    SmallFireball fireball = new SmallFireball(level, player, dir.scale(1.5));
                    level.addFreshEntity(fireball);
                }
                sound(level, player, SoundEvents.FIRECHARGE_USE);
                burst(level, ParticleTypes.FLAME, player.position().add(0, 1, 0), 12, 0.5, 0.05);
            }

            @Override
            int cooldownTicks() {
                return Config.fireCooldownSeconds * 20;
            }
        },
        WATER {
            @Override
            void activate(ServerLevel level, ServerPlayer player) {
                // Tidal Grace: swim like a dolphin, breathe underwater, douse yourself and nearby fire
                player.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 30 * 20, 0));
                player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 60 * 20, 0));
                player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 5 * 20, 0));
                player.clearFire();

                int r = Config.waterDouseRadius;
                BlockPos center = player.blockPosition();
                for (BlockPos pos : BlockPos.betweenClosed(center.offset(-r, -r, -r), center.offset(r, r, r))) {
                    if (level.getBlockState(pos).is(Blocks.FIRE)) {
                        level.removeBlock(pos, false);
                    }
                }
                for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class,
                        player.getBoundingBox().inflate(r), LivingEntity::isOnFire)) {
                    entity.clearFire();
                }
                sound(level, player, SoundEvents.PLAYER_SPLASH);
                burst(level, ParticleTypes.SPLASH, player.position().add(0, 1, 0), 20, 0.8, 0.1);
            }

            @Override
            int cooldownTicks() {
                return Config.waterCooldownSeconds * 20;
            }
        },
        EARTH {
            @Override
            void activate(ServerLevel level, ServerPlayer player) {
                // Seismic Slam: shockwave that damages and repels nearby hostiles, hardens your skin
                player.addEffect(new MobEffectInstance(MobEffects.RESISTANCE, 8 * 20, 1));
                slam(level, player, Config.earthRadius, 4.0F, 1.1);
                sound(level, player, SoundEvents.ANVIL_LAND);
                burst(level, ParticleTypes.CLOUD, player.position().add(0, 0.2, 0), 16, Config.earthRadius * 0.5, 0.02);
            }

            @Override
            int cooldownTicks() {
                return Config.earthCooldownSeconds * 20;
            }
        },
        LIGHTNING {
            @Override
            void activate(ServerLevel level, ServerPlayer player) {
                // Storm Strike: call a bolt where you look, gain a jolt of speed
                HitResult hit = player.pick(Config.lightningRange, 1.0F, false);
                strikeLightning(level, player, hit.getLocation());
                player.addEffect(new MobEffectInstance(MobEffects.SPEED, 5 * 20, 1));
                burst(level, ParticleTypes.ELECTRIC_SPARK, hit.getLocation(), 14, 0.6, 0.15);
            }

            @Override
            int cooldownTicks() {
                return Config.lightningCooldownSeconds * 20;
            }
        },
        PRIMAL {
            @Override
            void activate(ServerLevel level, ServerPlayer player) {
                // Elemental Chorus: every element answers at once
                player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 30 * 20, 0));
                player.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 30 * 20, 0));
                player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 30 * 20, 0));
                player.addEffect(new MobEffectInstance(MobEffects.RESISTANCE, 30 * 20, 0));
                player.addEffect(new MobEffectInstance(MobEffects.SPEED, 30 * 20, 0));
                player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 30 * 20, 1));
                player.clearFire();

                slam(level, player, 5.0, 6.0F, 1.4);
                level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(12),
                                entity -> entity instanceof Enemy)
                        .stream()
                        .min(Comparator.comparingDouble(player::distanceToSqr))
                        .ifPresent(target -> strikeLightning(level, player, target.position()));

                sound(level, player, SoundEvents.EVOKER_CAST_SPELL);
                burst(level, ParticleTypes.TOTEM_OF_UNDYING, player.position().add(0, 1, 0), 30, 1.0, 0.3);
            }

            @Override
            int cooldownTicks() {
                return Config.primalCooldownSeconds * 20;
            }
        };

        abstract void activate(ServerLevel level, ServerPlayer player);

        abstract int cooldownTicks();

        /** Damage and knock back hostile mobs around the player. */
        static void slam(ServerLevel level, ServerPlayer player, double radius, float damage, double knockback) {
            AABB area = player.getBoundingBox().inflate(radius);
            var source = level.damageSources().playerAttack(player);
            for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, area,
                    entity -> entity != player && entity instanceof Enemy)) {
                target.hurtServer(level, source, damage);
                target.knockback(knockback, player.getX() - target.getX(), player.getZ() - target.getZ(), source, damage);
            }
        }

        static void strikeLightning(ServerLevel level, ServerPlayer player, Vec3 pos) {
            LightningBolt bolt = new LightningBolt(EntityTypes.LIGHTNING_BOLT, level);
            bolt.setPos(pos.x, pos.y, pos.z);
            bolt.setCause(player);
            bolt.setVisualOnly(!Config.lightningIgnitesFire);
            level.addFreshEntity(bolt);
            if (!Config.lightningIgnitesFire) {
                // Visual-only bolts skip vanilla damage, so apply it manually
                for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class,
                        new AABB(pos, pos).inflate(2.5), entity -> entity != player)) {
                    target.hurtServer(level, level.damageSources().lightningBolt(), 7.0F);
                }
            }
        }

        static void sound(ServerLevel level, ServerPlayer player, SoundEvent soundEvent) {
            level.playSound(null, player.getX(), player.getY(), player.getZ(), soundEvent, SoundSource.PLAYERS, 0.9F, 1.0F);
        }

        /**
         * Sends a particle burst scaled by the particleDensity config, so servers and
         * low-end clients can dial the visual cost down to zero.
         */
        static void burst(ServerLevel level, ParticleOptions particle, Vec3 center, int baseCount, double spread, double speed) {
            int count = (int) Math.round(baseCount * Config.particleDensity);
            if (count <= 0) {
                return;
            }
            level.sendParticles(particle, center.x, center.y, center.z, count, spread, spread * 0.5, spread, speed);
        }
    }
}
