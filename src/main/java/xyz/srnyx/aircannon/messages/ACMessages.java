package xyz.srnyx.aircannon.messages;

import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.Include;
import eu.okaeri.configs.annotation.IncludePosition;
import eu.okaeri.validator.annotation.NotNull;
import xyz.srnyx.aircannon.AirCannon;
import xyz.srnyx.annoyingapi.file.okaeri.SubConfig;
import xyz.srnyx.annoyingapi.message.AnnoyingMessages;
import xyz.srnyx.annoyingapi.message.json.message.JsonChatMessage;


@Include(value = AnnoyingMessages.class, position = IncludePosition.BEFORE)
public class ACMessages extends AnnoyingMessages {
    public ACMessages(@org.jetbrains.annotations.NotNull AirCannon plugin) {
        super(plugin);
    }

    @Comment
    @NotNull public JsonChatMessage reload = defaultMessage("%prefix%Plugin successfully reloaded!@@%p%%command%@@%command%");

    @Comment
    @NotNull public Give give = new Give(this);

    public static class Give extends SubConfig<ACMessages, ACMessages> {
        public Give(@org.jetbrains.annotations.NotNull ACMessages defaultsParent) {
            super(defaultsParent);
        }

        @NotNull public JsonChatMessage self = getRoot().defaultMessage("%prefix%You have been given an Air Cannon!@@%p%%command%@@%command%");

        @Comment
        @Comment("Placeholders: %player%")
        @NotNull public JsonChatMessage other = getRoot().defaultMessage("%prefix%%s%%player%%p% has been given an Air Cannon!@@%p%%command%@@%command%");
    }
}
