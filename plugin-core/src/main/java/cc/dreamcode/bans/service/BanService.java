package cc.dreamcode.bans.service;

import cc.dreamcode.bans.Bans;
import cc.dreamcode.bans.config.MessageConfig;
import cc.dreamcode.bans.config.PluginConfig;
import cc.dreamcode.bans.profile.ProfileRepository;
import cc.dreamcode.bans.punishments.Ban;
import cc.dreamcode.bans.punishments.IpBan;
import cc.dreamcode.bans.punishments.TempBan;
import cc.dreamcode.bans.utils.MessageBroadcaster;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.tasker.bukkit.BukkitTasker;
import eu.okaeri.tasker.core.Tasker;
import eu.okaeri.tasker.core.TaskerDsl;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class BanService {

    private final Bans plugin;
    private final Tasker tasker;
    @Getter
    private final ProfileRepository profileRepository;
    private final MessageConfig messageConfig;
    private final PluginConfig pluginConfig;

    private final DiscordWebhookService discordWebhookService;


    public void createBan(@NonNull CommandSender sender,
                          @NonNull UUID targetUuid,
                          @NonNull String targetName,
                          String reason) {

        final Player online = Bukkit.getPlayer(targetUuid);
        if (online != null && online.hasPermission(this.pluginConfig.BanProtectedPermission)) {
            this.messageConfig.banProtected.with("player", targetName).send(sender);
            return;
        }

        if (reason == null || reason.isEmpty()) {
            reason = this.messageConfig.defaultReason;
        }

        String finalReason = reason;

        ((BukkitTasker) this.tasker).newSharedChain("dbops:ban:" + targetUuid)
                .next(TaskerDsl.supply(() -> this.profileRepository.findOrCreate(targetUuid, targetName)))
                .accept(profile -> {
                    profile.ban(finalReason, sender.getName());

                    Ban ban = new Ban(finalReason, sender.getName(), System.currentTimeMillis(), 0L);
                    profile.getBans().add(ban);

                    this.profileRepository.save(profile);

                    kickAndBroadcastBan(sender, targetName, finalReason, online,
                            this.messageConfig.banKick, this.messageConfig.banBroadcast,
                            "Ban", 0);

                    this.discordWebhookService.sendBanEmbed(
                            targetName, sender.getName(), finalReason, "Ban", 0
                    );
                })
                .execute();
    }

    public void createTempBan(@NonNull CommandSender sender,
                              @NonNull UUID targetUuid,
                              @NonNull String targetName,
                              long duration,
                              String reason) {

        final Player online = Bukkit.getPlayer(targetUuid);
        if (online != null && online.hasPermission(this.pluginConfig.BanProtectedPermission)) {
            this.messageConfig.banProtected.with("player", targetName).send(sender);
            return;
        }

        if (reason == null || reason.isEmpty()) {
            reason = this.messageConfig.defaultReason;
        }
        String finalReason = reason;

        long start = System.currentTimeMillis();
        long until = start + duration;

        ((BukkitTasker) this.tasker).newSharedChain("dbops:ban:" + targetUuid)
                .next(TaskerDsl.supply(() -> this.profileRepository.findOrCreate(targetUuid, targetName)))
                .accept(profile -> {
                    profile.ban(finalReason, sender.getName());
                    profile.setBanUntil(until);

                    TempBan tempBan = new TempBan(finalReason, sender.getName(), start, until);
                    profile.getTempBans().add(tempBan);

                    this.profileRepository.save(profile);

                    kickAndBroadcastBan(sender, targetName, finalReason, online,
                            this.messageConfig.tempBanKick, this.messageConfig.tempBanBroadcast,
                            "TempBan", until);

                    this.discordWebhookService.sendBanEmbed(
                            targetName, sender.getName(), finalReason, "TempBan", until
                    );
                })
                .execute();
    }

    public void createIpBan(@NonNull CommandSender sender,
                            @NonNull UUID targetUuid,
                            @NonNull String targetName,
                            @NonNull String ip,
                            String reason) {

        if (reason == null || reason.isEmpty()) {
            reason = this.messageConfig.defaultReason;
        }
        String finalReason = reason;

        ((BukkitTasker) this.tasker).newSharedChain("dbops:banip:" + targetUuid)
                .next(TaskerDsl.supply(() -> this.profileRepository.findOrCreate(targetUuid, targetName)))
                .accept(profile -> {
                    profile.banIp(ip, finalReason, sender.getName());

                    IpBan ipBan = new IpBan(ip, finalReason, sender.getName(), System.currentTimeMillis(), 0L);
                    profile.getIpBansCheck().add(ipBan);

                    this.profileRepository.save(profile);

                    Player online = Bukkit.getPlayer(targetUuid);
                    if (online != null) {
                        String kickMsg = this.messageConfig.ipBanKick
                                .replace("{reason}", finalReason)
                                .replace("{bannedBy}", sender.getName())
                                .replace("&", "§");
                        online.kickPlayer(kickMsg);
                    }

                    MessageBroadcaster.broadcast(
                            this.messageConfig.ipBanBroadcast,
                            targetName,
                            sender.getName(),
                            "IP-Ban",
                            finalReason,
                            0
                    );

                    this.discordWebhookService.sendBanEmbed(
                            targetName, sender.getName(), finalReason, "IP-Ban", 0
                    );
                })
                .execute();
    }

    public void removeBan(@NonNull CommandSender sender,
                          @NonNull UUID targetUuid,
                          @NonNull String targetName) {

        ((BukkitTasker) this.tasker).newSharedChain("dbops:unban:" + targetUuid)
                .next(TaskerDsl.supply(() -> this.profileRepository.findOrCreate(targetUuid, targetName)))
                .accept(profile -> {
                    profile.unban();
                    this.profileRepository.save(profile);

                    Player online = Bukkit.getPlayer(targetUuid);
                    if (online != null) {
                        this.messageConfig.unbanNotify.send(online);
                    }
                })
                .execute();
    }

    public void removeIpBan(@NonNull CommandSender sender,
                            @NonNull String ip) {

        ((BukkitTasker) this.tasker).newSharedChain("dbops:unbanip:" + ip)
                .accept(unused -> {
                    this.profileRepository.findAll().forEach(profile -> {
                        if (profile.hasIpBan(ip)) {
                            profile.unbanIp(ip);
                            this.profileRepository.save(profile);
                        }
                    });
                })
                .execute();
    }

    public void removeIpBan(@NonNull CommandSender sender,
                            @NonNull UUID targetUuid,
                            @NonNull String targetName) {

        ((BukkitTasker) this.tasker).newSharedChain("dbops:unbanip:" + targetUuid)
                .next(TaskerDsl.supply(() -> this.profileRepository.findOrCreate(targetUuid, targetName)))
                .accept(profile -> {
                    if (profile.getLastIp() != null) {
                        profile.unbanIp(profile.getLastIp());
                        this.profileRepository.save(profile);
                    }
                })
                .execute();
    }


    private void kickAndBroadcastBan(CommandSender sender,
                                     String targetName,
                                     String reason,
                                     Player online,
                                     String kickTemplate,
                                     String broadcastTemplate,
                                     String type,
                                     long until) {
        Bukkit.getScheduler().runTask(this.plugin, () -> {
            if (online != null) {
                String msg = kickTemplate
                        .replace("{reason}", reason)
                        .replace("{target}", targetName)
                        .replace("{bannedBy}", sender.getName())
                        .replace("{banExpire}", until > 0
                                ? new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(until))
                                : "PERM")
                        .replace("&", "§");
                online.kickPlayer(msg);
            }
        });

        MessageBroadcaster.broadcast(
                broadcastTemplate,
                targetName,
                sender.getName(),
                type,
                reason,
                until
        );
    }
}
