package fr.rhodless.arena.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoDatabase;
import fr.rhodless.arena.Arena;
import fr.rhodless.arena.mongo.collections.ProfileCollection;
import lombok.Getter;
import org.bson.codecs.LongCodec;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bukkit.Bukkit;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

@Getter
public class MongoHandler {
    protected static final CodecRegistry CODEC_REGISTRY = CodecRegistries.fromRegistries(
            MongoClientSettings.getDefaultCodecRegistry(),
            CodecRegistries.fromCodecs(new LongCodec()));

    private final Arena arena;

    private final MongoClient client;
    private final MongoDatabase database;

    private final ProfileCollection profileCollection;

    public MongoHandler(Arena arena, String databaseName, String uri) {
        this.arena = arena;

        try {
            client = MongoClients.create(MongoClientSettings.builder()
                    .codecRegistry(CODEC_REGISTRY)
                    .applicationName("Akira-Arena")
                    .applyConnectionString(new ConnectionString(uri))
                    .retryWrites(true)
                    .applyToConnectionPoolSettings(builder -> builder.maxConnectionIdleTime(30, TimeUnit.SECONDS))
                    .build());
        } catch (Exception e) {
            arena.getLogger().log(Level.SEVERE, "Couldn't connect to MongoDB, shutting down.");
            Bukkit.shutdown();
            throw e;
        }
        this.database = client.getDatabase(databaseName);

        System.out.println("Connected to MongoDB with db " + this.database.getName());
        this.profileCollection = new ProfileCollection(this);
    }
}
