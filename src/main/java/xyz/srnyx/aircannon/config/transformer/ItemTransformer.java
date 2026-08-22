package xyz.srnyx.aircannon.config.transformer;

import org.jetbrains.annotations.NotNull;
import xyz.srnyx.aircannon.AirCannon;
import xyz.srnyx.aircannon.config.AirConfig;
import xyz.srnyx.annoyingapi.data.ItemData;
import xyz.srnyx.annoyingapi.file.okaeri.serdes.itemstack.transformer.DataItemStackTransformer;


public class ItemTransformer extends DataItemStackTransformer<AirConfig> {
    @Override
    public void transform(@NotNull ItemData data, @NotNull Context<AirConfig> context) {
        data.set(AirCannon.ITEM_KEY, true);
    }
}
