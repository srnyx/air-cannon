package xyz.srnyx.aircannon.config.transformer;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.srnyx.aircannon.config.AirConfig;
import xyz.srnyx.annoyingapi.file.okaeri.serdes.recipe.transformer.result.ResultTransformer;


public class RecipeResultTransformer implements ResultTransformer<AirConfig> {
    @Override @NotNull
    public ItemStack apply(@Nullable ItemStack item, @NotNull Context<AirConfig> context) {
        return context.rootConfig().item;
    }
}
