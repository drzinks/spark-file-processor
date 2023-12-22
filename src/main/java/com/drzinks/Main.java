package com.drzinks;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.drzinks.scala.SparkFileProcessorApp;

import java.io.*;

public class Main {

    private static String filePathInDropbox;
    private static String convertedFileName;
    private static String accessToken;
    static void populateProperties(String ... args){
        if(args == null || args.length == 0){
            System.out.println("App should contain at least one argument, which is token."); //TODO: introduce logger
            System.exit(1);
        }
        accessToken = args[0];
        if(args.length == 3){
            filePathInDropbox = args[1];
            convertedFileName = args[2];
        } else {
            filePathInDropbox = "/testfolder/people.csv";
            convertedFileName = "converted.txt";
        }
    }
    public static void main(String ... args) {
        populateProperties(args);
        DbxRequestConfig config = DbxRequestConfig.newBuilder("spark-file-processor")
                .build();
        DbxClientV2 client = new DbxClientV2(config, accessToken);

        try {
            OutputStream convertedFileOutputStream = new FileOutputStream(convertedFileName);
            FileMetadata a = client
                    .files()
                    .downloadBuilder(filePathInDropbox)
                    .download(convertedFileOutputStream);

            File file = new File(convertedFileName);
            SparkFileProcessorApp scalaApp = new SparkFileProcessorApp();
            scalaApp.processFile(file);

        } catch (FileNotFoundException e) {
            System.out.println("Not a proper file name, exitting."); //TODO: introduce logger
            System.exit(1);
        } catch (IOException | DbxException e) {
            System.out.println("Cannot download file:"); //TODO: introduce logger
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }
}