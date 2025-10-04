package cc.dreamcode.bans.service;

import cc.dreamcode.bans.Bans;
import cc.dreamcode.bans.config.MessageConfig;
import cc.dreamcode.bans.profile.ProfileRepository;
import cc.dreamcode.bans.punishments.Mute;
import cc.dreamcode.bans.punishments.TempMute;
import cc.dreamcode.bans.utils.MessageBroadcaster;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.tasker.bukkit.BukkitTasker;
import eu.okaeri.tasker.core.Tasker;
import eu.okaeri.tasker.core.TaskerDsl;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class MuteService {

    private final Bans plugin;
    private final Tasker tasker;
    private final ProfileRepository profileRepository;
    private final MessageConfig messageConfig;
    private final DiscordWebhookService discordWebhookService;

    public void createMute(@NonNull org.bukkit.command.CommandSender sender,
                           @NonNull UUID targetUuid,
                           @NonNull String targetName,
                           String reason) {
        if (reason == null || reason.isEmpty()) {
            reason = this.messageConfig.defaultReason;
        }
        String finalReason = reason;

        ((BukkitTasker) this.tasker).newSharedChain("dbops:mute:" + targetUuid)
                .next(TaskerDsl.supply(() -> this.profileRepository.findOrCreate(targetUuid, targetName)))
                .accept(profile -> {
                    profile.mute(finalReason, sender.getName());

                    Mute mute = new Mute(finalReason, sender.getName(), System.currentTimeMillis());
                    profile.getMutes().add(mute);

                    this.profileRepository.save(profile);

                    MessageBroadcaster.broadcast(
                            this.messageConfig.muteBroadcast,
                            targetName,
                            sender.getName(),
                            "Mute",
                            finalReason,
                            0
                    );

                    this.discordWebhookService.sendBanEmbed(
                            targetName,
                            sender.getName(),
                            finalReason,
                            "Mute",
                            0
                    );
                })
                .execute();
    }

    public void createTempMute(@NonNull org.bukkit.command.CommandSender sender,
                               @NonNull UUID targetUuid,
                               @NonNull String targetName,
                               long duration,
                               String reason) {

        if (reason == null || reason.isEmpty()) {
            reason = this.messageConfig.defaultReason;
        }
        String finalReason = reason;
        long start = System.currentTimeMillis();
        long until = start + duration;

        ((BukkitTasker) this.tasker).newSharedChain("dbops:tempmute:" + targetUuid)
                .next(TaskerDsl.supply(() -> this.profileRepository.findOrCreate(targetUuid, targetName)))
                .accept(profile -> {
                    profile.mute(finalReason, sender.getName());
                    profile.setMuteUntil(until);

                    TempMute tempMute = new TempMute(finalReason, sender.getName(), start, until);
                    profile.getTempMutes().add(tempMute);

                    this.profileRepository.save(profile);

                    MessageBroadcaster.broadcast(
                            this.messageConfig.tempMuteBroadcast,
                            targetName,
                            sender.getName(),
                            "TempMute",
                            finalReason,
                            profile.getMuteUntil()
                    );

                    this.discordWebhookService.sendBanEmbed(
                            targetName,
                            sender.getName(),
                            finalReason,
                            "TempMute",
                            until
                    );
                })
                .execute();
    }

    public void removeMute(@NonNull org.bukkit.command.CommandSender sender,
                           @NonNull UUID targetUuid,
                           @NonNull String targetName) {

        ((BukkitTasker) this.tasker).newSharedChain("dbops:unmute:" + targetUuid)
                .next(TaskerDsl.supply(() -> this.profileRepository.findOrCreate(targetUuid, targetName)))
                .accept(profile -> {
                    profile.unmute();
                    this.profileRepository.save(profile);

                    this.messageConfig.unmuteNotify.send(sender);

                    this.discordWebhookService.sendBanEmbed(
                            targetName,
                            sender.getName(),
                            "—",
                            "Unmute",
                            0
                    );
                })
                .execute();
    }
}
