package com.ulpf.plugin;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.core.AppenderBase;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Custom Logback Appender to intercept application logs and store them
 * directly into MongoDB in Document format.
 */
public class MongoLogAppender extends AppenderBase<ILoggingEvent> {

    private String connectionUri;
    private String databaseName = "ulpf_db";
    private String collectionName = "app_logs";
    private String applicationName = "default-app";

    private MongoClient mongoClient;
    private MongoCollection<Document> collection;

    public String getConnectionUri() {
        return connectionUri;
    }

    public void setConnectionUri(String connectionUri) {
        this.connectionUri = connectionUri;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    @Override
    public void start() {
        if (connectionUri == null || connectionUri.trim().isEmpty()) {
            addError("MongoLogAppender: connectionUri is missing!");
            return;
        }
        try {
            this.mongoClient = MongoClients.create(connectionUri);
            MongoDatabase db = mongoClient.getDatabase(databaseName);
            this.collection = db.getCollection(collectionName);
            super.start();
            addInfo("MongoLogAppender started successfully. Writing logs to MongoDB collection: " + collectionName);
        } catch (Exception e) {
            addError("Failed to initialize MongoLogAppender connection to MongoDB", e);
        }
    }

    @Override
    public void stop() {
        if (mongoClient != null) {
            try {
                mongoClient.close();
            } catch (Exception e) {
                addError("Error closing MongoClient in MongoLogAppender", e);
            }
        }
        super.stop();
    }

    @Override
    protected void append(ILoggingEvent eventObject) {
        if (!isStarted() || collection == null) {
            return;
        }

        try {
            Document doc = new Document();
            doc.append("applicationName", applicationName);
            doc.append("timestamp", new Date(eventObject.getTimeStamp()));
            doc.append("level", eventObject.getLevel().toString());
            doc.append("loggerName", eventObject.getLoggerName());
            doc.append("threadName", eventObject.getThreadName());
            doc.append("message", eventObject.getFormattedMessage());

            // Add MDC Context Map if present
            Map<String, String> mdc = eventObject.getMDCPropertyMap();
            if (mdc != null && !mdc.isEmpty()) {
                doc.append("mdc", new Document(new HashMap<>(mdc)));
            }

            // Exception details if available
            IThrowableProxy throwableProxy = eventObject.getThrowableProxy();
            if (throwableProxy != null) {
                Document errDoc = new Document();
                errDoc.append("className", throwableProxy.getClassName());
                errDoc.append("message", throwableProxy.getMessage());

                StringBuilder stackTrace = new StringBuilder();
                if (throwableProxy.getStackTraceElementProxyArray() != null) {
                    for (StackTraceElementProxy step : throwableProxy.getStackTraceElementProxyArray()) {
                        stackTrace.append(step.toString()).append("\n");
                    }
                }
                errDoc.append("stackTrace", stackTrace.toString());
                doc.append("exception", errDoc);
            }

            // Write Document to MongoDB asynchronously or synchronously
            collection.insertOne(doc);
        } catch (Exception e) {
            addError("Failed to append log event to MongoDB", e);
        }
    }
}
