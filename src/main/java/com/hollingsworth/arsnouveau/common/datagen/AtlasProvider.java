package com.hollingsworth.arsnouveau.common.datagen;

import com.google.common.collect.ImmutableMap;
import com.hollingsworth.arsnouveau.ArsNouveau;
import com.hollingsworth.arsnouveau.setup.registry.BlockRegistry;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.texture.atlas.sources.SingleFile;
import net.minecraft.client.resources.model.Material;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SpriteSourceProvider;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

public class AtlasProvider extends SpriteSourceProvider {
    public static final Map<Block, EnumMap<ChestType, Material>> MATERIALS;

    static {
        ImmutableMap.Builder<Block, EnumMap<ChestType, Material>> builder = ImmutableMap.builder();

        builder.put(BlockRegistry.ARCHWOOD_CHEST.get(), chestMaterial("archwood"));
        MATERIALS = builder.build();
    }
    public AtlasProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, helper, ArsNouveau.MODID);
    }

    @Override
    protected void addSources() {
        MATERIALS.values().stream().flatMap(e -> e.values().stream()).map(Material::texture)
                .forEach(resourceLocation -> this.atlas(CHESTS_ATLAS).addSource(new SingleFile(resourceLocation, Optional.empty())));
    }


    private static EnumMap<ChestType, Material> chestMaterial(String type) {
        EnumMap<ChestType, Material> map = new EnumMap<>(ChestType.class);

        map.put(ChestType.SINGLE, new Material(Sheets.CHEST_SHEET, new ResourceLocation(ArsNouveau.MODID, "model/chest/" + type + "/" + type)));
        map.put(ChestType.LEFT, new Material(Sheets.CHEST_SHEET, new ResourceLocation(ArsNouveau.MODID, "model/chest/" + type + "/left")));
        map.put(ChestType.RIGHT, new Material(Sheets.CHEST_SHEET, new ResourceLocation(ArsNouveau.MODID, "model/chest/" + type + "/right")));

        return map;
    }
}