package com.refactify;

import com.refactify.arguments.ConversionArguments;
import com.refactify.arguments.ConversionArguments.ConversionType;
import com.refactify.arguments.ConversionArgumentsParser;
import com.refactify.arguments.TargetFileNameBuilder;
import com.refactify.printer.UsagePrinter;
import java.io.File;
import liquibase.changelog.ChangeLogParameters;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.exception.LiquibaseException;
import liquibase.parser.ChangeLogParser;
import liquibase.parser.ChangeLogParserFactory;
import liquibase.resource.FileSystemResourceAccessor;
import liquibase.resource.ResourceAccessor;
import liquibase.serializer.ChangeLogSerializer;
import liquibase.serializer.ChangeLogSerializerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Liquify {
    private static final Logger logger = LoggerFactory.getLogger(Liquify.class);
    private final static ConversionArgumentsParser parser = new ConversionArgumentsParser();
    private final static UsagePrinter usagePrinter = new UsagePrinter();
    private final static TargetFileNameBuilder targetFileNameBuilder = new TargetFileNameBuilder();

    public static void main(final String[] args) {
//        ConversionArguments conversionArguments = parser.parseArguments(args);
        ConversionArguments conversionArguments = new ConversionArguments();
        conversionArguments.setConversionType(ConversionType.SQL);
        conversionArguments.setDatabase("mysql");
//        conversionArguments.setSource("./src/main/resources/db/changelog/liquify_migration_to_query.xml");
        conversionArguments.setSource("WEB-INF/classes/liquify_migration_to_query.xml");
        if(conversionArguments.areValid()) {
            convertDatabaseChangeLog(conversionArguments);
        }
        else {
            usagePrinter.printUsage();
        }
    }

    private static void convertDatabaseChangeLog(final ConversionArguments conversionArguments) {
        String targetFileName = targetFileNameBuilder.buildFilename(conversionArguments);
        try {
            ResourceAccessor resourceAccessor = new FileSystemResourceAccessor(new File(System.getProperty("user.dir")));
//            ResourceAccessor resourceAccessor = new FileSystemResourceAccessor(new File(conversionArguments.getSource()));
            ChangeLogParser parser = ChangeLogParserFactory.getInstance().getParser(conversionArguments.getSource(), resourceAccessor);
            DatabaseChangeLog changeLog = parser.parse(conversionArguments.getSource(), new ChangeLogParameters(), resourceAccessor);
            ChangeLogSerializer serializer = ChangeLogSerializerFactory.getInstance().getSerializer(targetFileName);
            for (ChangeSet set : changeLog.getChangeSets()) {
                set.setFilePath(targetFileName);
            }
            serializer.write(changeLog.getChangeSets(), new FileOutputStream(targetFileName));
        }
        catch (LiquibaseException e) {
            logger.info("There was a problem parsing the source file.", e);
            deleteTargetFile(targetFileName);
        }
        catch (IOException e) {
            logger.info("There was a problem serializing the source file.", e);
            deleteTargetFile(targetFileName);
        }
        catch(IllegalStateException e) {
            logger.info("Database generator for type '{}}' was not found.",
                    conversionArguments.getDatabase());
            deleteTargetFile(targetFileName);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private static void deleteTargetFile(final String targetFileName) {
        try {
            Files.deleteIfExists(Paths.get(targetFileName));
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
