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
    public boolean debug = true;

    @Comment
    @Comment("Ponizej znajduja sie dane do logowania bazy danych:")
    @CustomKey("storage-config")
    public StorageConfig storageConfig = new StorageConfig("storage");

    @Comment("Czas (sek) po jakim prośba TPA wygasa")
    public int requestExpireSeconds = 60;

    @Comment("Opóźnienie (sek) przed teleportacją po akceptacji (anty-ruch, anty-combat)")
    public int teleportDelaySeconds = 3;

    @Comment("Cooldown (sek) pomiędzy wysyłaniem kolejnych próśb do tego samego gracza")
    public int requestCooldownSeconds = 10;

    @Comment("Anuluj TPA jeśli gracz ruszy się podczas odliczania")
    public boolean cancelOnMove = true;

    @Comment("Anuluj TPA jeśli gracz otrzyma obrażenia podczas odliczania")
    public boolean cancelOnDamage = true;

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
