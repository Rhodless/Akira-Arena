package fr.rhodless.arena.profile;

import fr.rhodless.arena.Arena;
import fr.rhodless.arena.profile.impl.CoreProfile;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProfileHandler {
    private final Arena arena;

    private final Map<UUID, Profile> profiles = new HashMap<>();

    public ProfileHandler(Arena arena) {
        this.arena = arena;
    }

    public Collection<Profile> getProfiles() {
        return profiles.values();
    }

    public Profile getProfile(UUID uuid) {
        return this.getProfile(uuid, "", false);
    }

    public void unregisterPlayer(Player player) {
        this.profiles.remove(player.getUniqueId());
    }

    public Profile getProfile(Player player) {
        return this.getProfile(player.getUniqueId(), player.getName(), false);
    }

    public Profile getProfile(UUID uuid, String name, boolean shouldSearch) {
        if (this.profiles.containsKey(uuid)) {
            return this.profiles.get(uuid);
        }

        if (shouldSearch) {
            this.arena.getMongoHandler().getProfileCollection().hasProfile(uuid).subscribe(exists -> {
                if (exists) {
                    this.arena.getMongoHandler().getProfileCollection().loadProfile(uuid).subscribe(profile -> this.profiles.put(uuid, profile));
                } else {
                    CoreProfile coreProfile = new CoreProfile(uuid, name);
                    this.profiles.put(uuid, coreProfile);
                    this.arena.getMongoHandler().getProfileCollection().saveProfile(coreProfile).subscribe();
                }
            });
        }

        return null;
    }

    public void saveProfile(Profile profile) {
        this.arena.getMongoHandler().getProfileCollection().saveProfile((CoreProfile) profile).subscribe();
    }
}
