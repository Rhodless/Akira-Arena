package fr.rhodless.arena.mongo.collections;

import com.mongodb.client.model.Filters;
import com.mongodb.client.result.UpdateResult;
import fr.rhodless.arena.leaderboard.QuickLeaderboardData;
import fr.rhodless.arena.mongo.MongoCollection;
import fr.rhodless.arena.mongo.MongoHandler;
import fr.rhodless.arena.profile.impl.CoreProfile;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Sorts.descending;

public class ProfileCollection extends MongoCollection {
    public ProfileCollection(MongoHandler mongoHandler) {
        super("profiles", mongoHandler);
    }

    public Mono<CoreProfile> loadProfile(UUID uuid) {
        return findOne(CoreProfile.class, Filters.eq("_id", uuid.toString()));
    }

    public Mono<CoreProfile> loadProfileByName(String name) {
        return findOne(CoreProfile.class, Filters.eq(equalsIgnoreCase("displayName", name)));
    }

    public Mono<Boolean> hasProfile(UUID uuid) {
        return count(Filters.eq("_id", uuid.toString())).map(count -> count > 0);
    }

    public Mono<UpdateResult> saveProfile(CoreProfile coreProfile) {
        return replace(Filters.eq("_id", coreProfile.getUniqueId().toString()), coreProfile, true);
    }

    public <T> void getLeaderboard(String field, List<QuickLeaderboardData<T>> listToEdit) {
        listToEdit.clear();

        aggregate(
                match(Filters.exists(field)),
                sort(descending(field)),
                limit(10)
        ).subscribe(document -> {
            String name = document.getString("displayName");
            if (name == null || name.equals(" ") || name.equals("") || name.equals("null")) return;

            T value = (T) document.get(field);
            listToEdit.add(new QuickLeaderboardData<>(name, value));
        });
    }
}
