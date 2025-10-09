package cc.dreamcode.bans.profile;

import cc.dreamcode.platform.DreamLogger;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.tasker.bukkit.BukkitTasker;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@Getter
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class ProfileService {

    private final ProfileCache profileCache;
    private final ProfileRepository profileRepository;
    private final BukkitTasker tasker;
    private final DreamLogger logger;

    public CompletableFuture<Profile> loadAsync(@NonNull UUID uuid, @NonNull String name) {
        CompletableFuture<Profile> future = new CompletableFuture<>();

        tasker.newChain()
                .supply(() -> {
                    try {
                        return profileRepository.findOrCreate(uuid, name);
                    } catch (Exception e) {
                        logger.error("Błąd przy wczytywaniu profilu " + name);
                        e.printStackTrace();
                        return null;
                    }
                })
                .runSync(() -> {
                    Profile profile = future.isDone() ? null : null;
                })
                .runSync(() -> {
                    Profile profile = future.isDone() ? null : null;
                })
                .accept(profile -> {
                    if (profile != null) {
                        profileCache.add(profile);
                        future.complete(profile);
                    } else {
                        future.completeExceptionally(new IllegalStateException("Profile is null"));
                    }
                })
                .execute();

        return future;
    }

    public void saveAsync(@NonNull Profile profile) {
        tasker.newSharedChain("profile-save-" + profile.getUniqueId())
                .run(() -> {
                    try {
                        profile.save();
                    } catch (Exception e) {
                        logger.error("Błąd zapisu profilu " + profile.getUniqueId());
                        e.printStackTrace();
                    }
                })
                .execute();
    }

    public void modify(@NonNull UUID uuid, @NonNull Consumer<Profile> consumer) {
        Profile profile = profileCache.get(uuid);
        if (profile == null) return;

        consumer.accept(profile);
        saveAsync(profile);
    }

    public void handleJoin(@NonNull Player player) {
        loadAsync(player.getUniqueId(), player.getName());
    }

    public void handleQuit(@NonNull Player player) {
        Profile profile = profileCache.get(player);
        if (profile == null) return;

        tasker.newSharedChain("profile-save-" + profile.getUniqueId())
                .run(() -> {
                    try {
                        profile.save();
                    } catch (Exception e) {
                        logger.error("Błąd zapisu profilu przy wyjściu " + profile.getUniqueId());
                        e.printStackTrace();
                    }
                })
                .runSync(() -> profileCache.remove(player.getUniqueId()))
                .execute();
    }

    public void saveAllAsync() {
        tasker.newChain()
                .run(() -> {
                    profileCache.all().values().forEach(profile -> {
                        try {
                            profile.save();
                        } catch (Exception e) {
                            logger.error("Błąd auto-save profilu " + profile.getUniqueId());
                            e.printStackTrace();
                        }
                    });
                })
                .execute();
    }
}
