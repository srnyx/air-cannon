package xyz.srnyx.aircannon.config;

import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XEntityType;
import com.cryptomorin.xseries.XItemFlag;
import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.Header;
import eu.okaeri.configs.serdes.commons.duration.DurationSpec;
import eu.okaeri.validator.annotation.NotNull;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.srnyx.aircannon.AirCannon;
import xyz.srnyx.aircannon.config.transformer.ItemTransformer;
import xyz.srnyx.aircannon.config.transformer.RecipeResultTransformer;
import xyz.srnyx.annoyingapi.file.PlayableSound;
import xyz.srnyx.annoyingapi.file.UniversalParticle;
import xyz.srnyx.annoyingapi.file.okaeri.RootConfig;
import xyz.srnyx.annoyingapi.file.okaeri.SubConfig;
import xyz.srnyx.annoyingapi.file.okaeri.serdes.itemstack.spec.ItemStackSpec;
import xyz.srnyx.annoyingapi.file.okaeri.serdes.recipe.spec.RecipeFeature;
import xyz.srnyx.annoyingapi.file.okaeri.serdes.recipe.spec.RecipeSpec;
import xyz.srnyx.annoyingapi.reflection.org.bukkit.inventory.RefShapedRecipe;
import xyz.srnyx.annoyingapi.stats.Stat;
import xyz.srnyx.annoyingapi.utility.BukkitUtility;

import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Set;

import static xyz.srnyx.annoyingapi.reflection.org.bukkit.inventory.meta.RefItemMeta.ITEM_META_SET_UNBREAKABLE;


@Header("DOCUMENTATION: https://annoying-api.srnyx.com/wiki/File-objects")
public class AirConfig extends RootConfig {
    @Comment
    @Comment
    @Comment("Amount of time between each set of uses of the Air Cannon")
    @Comment("0 = no cooldown (infinite uses)")
    @DurationSpec(fallbackUnit = ChronoUnit.MILLIS) @Stat
    @NotNull public Duration cooldown = Duration.ofMillis(300);

    @Comment
    @Comment("Amount of uses of the Air Cannon before it goes on cooldown")
    @Comment("For infinite uses, set cooldown (above) to 0")
    @Stat
    public int uses = 1;

    @Comment
    @Comment("The multiplier for the player's velocity when using the Air Cannon")
    @Comment("Default: 1.5")
    @Stat
    public double power = 1.5;

    @Comment
    @Comment("Whether a player crouching will 'ground them' (only nearby entities are pushed/pulled)")
    @Stat
    public boolean crouch_grounding = false;

    @Comment
    @Comment("The entities to not (or to only) be affected by the Air Cannon push/pull")
    @Comment("Set list to [] and treat_as_whitelist to true to disable all entities")
    @NotNull public EntitiesBlacklist entities_blacklist = new EntitiesBlacklist(this);

    @Comment
    @Comment("The sound that's played whenever the Air Cannon is used")
    @NotNull public Sound sound = new Sound(this);

    @Comment
    @Comment("The particle that is spawned when the Air Cannon is used")
    @Comment("1.9+ ONLY!")
    @NotNull public Particle particle = new Particle(this);

    @Comment
    @Comment("The item that represents the Air Cannon")
    @Comment("ITEM (see documentation)")
    @ItemStackSpec(transformer = ItemTransformer.class)
    @NotNull public ItemStack item = new ItemStack(Material.IRON_HOE);

    @Comment
    @Comment("The recipe of the Air Cannon, the 'result' is overriden by the 'item' section above")
    @Comment("Requires a restart for changes to take effect")
    @NotNull public RecipeConfig recipe;


    @org.jetbrains.annotations.NotNull public transient final AirCannon plugin;

    public AirConfig(@org.jetbrains.annotations.NotNull AirCannon plugin) {
        this.plugin = plugin;

        // item
        item.addUnsafeEnchantment(XEnchantment.UNBREAKING.get(), 1);
        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(BukkitUtility.color("&f&lAir Cannon"));
        meta.setLore(BukkitUtility.color(
                "&7Right-click to boost forward",
                "&7Left-click to boost backward"));
        meta.addItemFlags(XItemFlag.HIDE_ENCHANTS.get(), XItemFlag.HIDE_UNBREAKABLE.get());
        // 1.11+ unbreakable
        if (ITEM_META_SET_UNBREAKABLE != null) try {
            ITEM_META_SET_UNBREAKABLE.invoke(meta, true);
        } catch (final IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        item.setItemMeta(meta);

        // recipe
        recipe = new RecipeConfig(this);
    }

    public static class EntitiesBlacklist extends SubConfig<AirConfig, AirConfig> {
        public EntitiesBlacklist(@org.jetbrains.annotations.NotNull AirConfig defaultsParent) {
            super(defaultsParent);
        }

        @Comment("https://srnyx.com/docs/spigot/org/bukkit/entity/EntityType")
        @NotNull public Set<XEntityType> list = Set.of(XEntityType.PLAYER);

        @Comment
        @Comment("If true, the list above will be treated as a whitelist instead of a blacklist (only entities in the list will be affected)")
        public boolean treat_as_whitelist = false;
    }

    public static class Sound extends SubConfig<AirConfig, AirConfig> {
        public Sound(@org.jetbrains.annotations.NotNull AirConfig defaultsParent) {
            super(defaultsParent);
        }

        @Stat
        public boolean enabled = true;

        @Comment
        @Comment("SOUND (see documentation)")
        @Stat
        @NotNull public PlayableSound sound = new PlayableSound(XSound.BLOCK_HONEY_BLOCK_PLACE);
    }

    public static class Particle extends SubConfig<AirConfig, AirConfig> {
        public Particle(@org.jetbrains.annotations.NotNull AirConfig defaultsParent) {
            super(defaultsParent);
        }

        @Stat
        public boolean enabled = true;

        @Comment
        @Comment("https://srnyx.com/docs/spigot/org/bukkit/Particle")
        @Stat
        @NotNull public UniversalParticle particle = new UniversalParticle("CLOUD");
    }

    public static class RecipeConfig extends SubConfig<AirConfig, AirConfig> {
        @org.jetbrains.annotations.NotNull private static final String RECIPE_KEY = "air_cannon";


        @Comment("Whether the Air Cannon should be craftable")
        @Stat
        public boolean enabled = true;

        @Comment
        @Comment("RECIPE (see documentation)")
        @RecipeSpec(name = RECIPE_KEY, resultTransformer = RecipeResultTransformer.class, disabledFeatures = RecipeFeature.RESULT)
        @NotNull public Recipe recipe;


        public RecipeConfig(@org.jetbrains.annotations.NotNull AirConfig defaultsParent) {
            super(defaultsParent);

            // recipe
            recipe = RefShapedRecipe.newShapedRecipe(getRoot().item, getRoot().plugin, RECIPE_KEY)
                    .shape(
                            "BIB",
                            "BDB",
                            "BIB")
                    .setIngredient('B', XMaterial.GLASS_BOTTLE.get())
                    .setIngredient('I', XMaterial.IRON_INGOT.get())
                    .setIngredient('D', XMaterial.DIAMOND.get());
        }
    }
}
