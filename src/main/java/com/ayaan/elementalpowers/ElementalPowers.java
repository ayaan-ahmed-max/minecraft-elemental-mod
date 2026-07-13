package com.ayaan.elementalpowers;

import com.ayaan.elementalpowers.item.ElementalOrbItem;
import com.ayaan.elementalpowers.item.ElementalOrbItem.Element;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod(ElementalPowers.MODID)
public final class ElementalPowers {
    public static final String MODID = "elementalpowers";

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final RegistryObject<Item> FIRE_ORB = registerOrb("fire_orb", Element.FIRE, Rarity.UNCOMMON);
    public static final RegistryObject<Item> WATER_ORB = registerOrb("water_orb", Element.WATER, Rarity.UNCOMMON);
    public static final RegistryObject<Item> EARTH_ORB = registerOrb("earth_orb", Element.EARTH, Rarity.UNCOMMON);
    public static final RegistryObject<Item> LIGHTNING_ORB = registerOrb("lightning_orb", Element.LIGHTNING, Rarity.RARE);
    public static final RegistryObject<Item> PRIMAL_ORB = registerOrb("primal_orb", Element.PRIMAL, Rarity.EPIC);

    public static final RegistryObject<CreativeModeTab> ELEMENTAL_TAB = CREATIVE_MODE_TABS.register("elemental_tab",
        () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.elementalpowers"))
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> PRIMAL_ORB.get().getDefaultInstance())
            .displayItems((_, output) -> {
                output.accept(FIRE_ORB.get());
                output.accept(WATER_ORB.get());
                output.accept(EARTH_ORB.get());
                output.accept(LIGHTNING_ORB.get());
                output.accept(PRIMAL_ORB.get());
            }).build());

    private static RegistryObject<Item> registerOrb(String name, Element element, Rarity rarity) {
        return ITEMS.register(name, () -> new ElementalOrbItem(element, new Item.Properties()
            .setId(ITEMS.key(name))
            .stacksTo(1)
            .rarity(rarity)));
    }

    public ElementalPowers(FMLJavaModLoadingContext context) {
        var modBusGroup = context.getModBusGroup();

        ITEMS.register(modBusGroup);
        CREATIVE_MODE_TABS.register(modBusGroup);

        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
}
