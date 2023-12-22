package com.drzinks;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

    private static String filePathInDropbox;
    private static String convertedFileName;
    private static String accessToken;
    static void populateProperties(String ... args){
        if(args == null || args.length == 0){
            System.out.println("App should contain at least one argument, whic is token."); //TODO: introduce logger
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
            client
                    .files()
                    .downloadBuilder(filePathInDropbox)
                    .download(convertedFileOutputStream);
            String content = Files.readString(Path.of(convertedFileName));
            System.out.println(content);

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