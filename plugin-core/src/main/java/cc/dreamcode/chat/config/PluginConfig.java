package cc.dreamcode.chat.config;

import cc.dreamcode.platform.bukkit.component.configuration.Configuration;
import cc.dreamcode.platform.persistence.StorageConfig;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.CustomKey;
import eu.okaeri.configs.annotation.Header;

import java.util.List;

@Configuration(child = "config.yml")
@Header("## Dream-Tpa (Main-Config) ##")
public class PluginConfig extends OkaeriConfig {


    @Comment
    @Comment("Debug pokazuje dodatkowe informacje do konsoli. Lepiej wylaczyc. :P")
    @CustomKey("debug")
    public boolean debug = false;

    @Comment
    @Comment("Ponizej znajduja sie dane do logowania bazy danych:")
    @CustomKey("storage-config")
    public StorageConfig storageConfig = new StorageConfig("storage");

    @CustomKey("mute.blocked-commands")
    public List<String> muteBlockedCommands = List.of(
            "/msg",
            "/tell",
            "/whisper",
            "/r",
            "/mail"
    );

    @CustomKey("ban.protected-permission")
    public String BanProtectedPermission = "dream-chat.protected";

    @CustomKey("blacklist.players")
    public List<String> blacklistPlayers = List.of(
            "Notch",
            "Herobrine"
    );

    @CustomKey("discordWebhookUrl")
    public String discordWebhookUrl = "";

}
