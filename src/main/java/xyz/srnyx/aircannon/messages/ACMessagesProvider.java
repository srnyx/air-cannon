package xyz.srnyx.aircannon.messages;

import org.jetbrains.annotations.NotNull;
import xyz.srnyx.aircannon.AirCannon;
import xyz.srnyx.annoyingapi.file.okaeri.ConfigBuilder;
import xyz.srnyx.annoyingapi.message.MessagesProvider;


public class ACMessagesProvider extends MessagesProvider {
    @NotNull private final AirCannon plugin;

    public ACMessagesProvider(@NotNull AirCannon plugin) {
        this.plugin = plugin;

        defaults
                .prefix("&2&lAIR CANNON &8&l| &a")
                .p("&a")
                .s("&2");
    }

    @Override @NotNull
    public AirCannon getAnnoyingPlugin() {
        return plugin;
    }

    @Override
    public void mutateBuilder(@NotNull ConfigBuilder builder) {
        builder.config(new ACMessages(plugin));
    }

    @Override @NotNull
    public ACMessages get() {
        return (ACMessages) messages;
    }
}
