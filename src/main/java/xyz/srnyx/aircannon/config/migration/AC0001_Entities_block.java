package xyz.srnyx.aircannon.config.migration;

import eu.okaeri.configs.migrate.builtin.NamedMigration;

import static eu.okaeri.configs.migrate.ConfigMigrationDsl.*;


public class AC0001_Entities_block extends NamedMigration {
    public AC0001_Entities_block() {
        super("migrates flat entities settings into shared entities block", multi(
                move("crouch_grounding", "entities.crouch_grounding"),
                move("entities_blacklist", "entities.blacklist")));
    }
}
