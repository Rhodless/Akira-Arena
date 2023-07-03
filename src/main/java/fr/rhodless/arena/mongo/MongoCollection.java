package fr.rhodless.arena.mongo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.reactivestreams.client.FindPublisher;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.json.JsonWriterSettings;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;

import static com.mongodb.client.model.Filters.regex;

public abstract class MongoCollection {
    protected static final JsonWriterSettings JSON_WRITER_SETTINGS = JsonWriterSettings.builder()
            .objectIdConverter((objectId, strictJsonWriter) -> strictJsonWriter.writeString(objectId.toHexString()))
            .int64Converter((value, writer) -> writer.writeNumber(value.toString()))
            .build();

    protected Gson gson;

    protected com.mongodb.reactivestreams.client.MongoCollection<Document> collection;
    protected String collectionName;

    public MongoCollection(String collectionName, MongoHandler mongoHandler) {
        this.collection = mongoHandler.getDatabase().getCollection(collectionName);
        this.collectionName = collectionName;

        this.collection.listIndexes();
        this.gson = new GsonBuilder().create();
    }

    public static Bson equalsIgnoreCase(String key, String value) {
        return regex(key, value, "i");
    }

    public Mono<Document> delete(Bson query) {
        return Mono.from(collection.findOneAndDelete(query));
    }

    public <T> Mono<T> findOne(Class<T> clazz, Bson query) {
        return findOne(clazz, query, null);
    }

    public <T> Mono<T> findOne(Class<T> clazz, Bson query, Bson sort) {
        FindPublisher<Document> req = collection.find(query);
        if (sort != null) {
            req = req.sort(sort);
        }
        return Mono.from(req.first()).map(document -> gson.fromJson(document.toJson(JSON_WRITER_SETTINGS), clazz));
    }

    public Mono<Document> findOne(Bson query) {
        return Mono.from(collection.find(query).first());
    }

    public <T> Flux<T> findAll(Class<T> clazz) {
        return Flux.from(collection.find()).map(document -> gson.fromJson(document.toJson(JSON_WRITER_SETTINGS), clazz));
    }

    public <T> Flux<T> find(Class<T> clazz, Bson query) {
        return Flux.from(collection.find(query)).map(document -> gson.fromJson(document.toJson(JSON_WRITER_SETTINGS), clazz));
    }

    public Mono<Long> count(Bson query) {
        return Mono.from(collection.countDocuments(query));
    }

    public <T> Flux<T> findIds(Class<T> aClass) {
        return Flux.from(collection.distinct("_id", aClass));
    }

    public Mono<UpdateResult> replace(Bson query, Object obj) {
        return replace(query, obj, false);
    }

    public Mono<UpdateResult> replace(Bson query, Object obj, boolean upsert) {
        return Mono.from(
                collection.replaceOne(
                        query,
                        Document.parse(gson.toJson(obj)),
                        new ReplaceOptions().upsert(
                                upsert)));
    }

    public Mono<UpdateResult> replace(Bson query, Document document) {
        return replace(query, document, false);
    }

    public Mono<UpdateResult> replace(Bson query, Document document, boolean upsert) {
        return Mono.from(collection.replaceOne(query, document, new ReplaceOptions().upsert(upsert)));
    }

    public Mono<UpdateResult> updateOne(Bson query, Bson document, boolean upsert) {
        return Mono.from(collection.updateOne(query, document, new UpdateOptions().upsert(upsert)));
    }

    public Mono<InsertOneResult> insert(Object obj) {
        // Convert the object to json and insert it.
        return Mono.from(collection.insertOne(Document.parse(gson.toJson(obj))));
    }

    public <T> Flux<T> aggregate(Class<T> clazz, Bson... pipeline) {
        return Flux.from(collection.aggregate(Arrays.asList(pipeline)))
                .map(document -> gson.fromJson(document.toJson(JSON_WRITER_SETTINGS), clazz));
    }

    public Flux<Document> aggregate(Bson... pipeline) {
        return Flux.from(collection.aggregate(Arrays.asList(pipeline)));
    }
}
