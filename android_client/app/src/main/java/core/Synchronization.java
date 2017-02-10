package core;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.hieuttc.distributedsystem.MainActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import android.util.Base64;

import org.joda.time.DateTimeUtils;

public class Synchronization implements Runnable {
    private static boolean isDone;
    public static int state = 0;
    private static File clientFile;
    private static File serverFile;
    private static ServerInterface server;
    private static ClientInterface client;
    private static Context mContext;
    private static final String ENCODING = "UTF-8";

    public Synchronization(ClientInterface client, ServerInterface server,
                           File serverFile, File clientFile, boolean isDone, Context context) {
        // TODO Auto-generated constructor stub
        this.client = client;
        this.server = server;
        this.clientFile = clientFile;
        this.serverFile = serverFile;
        this.isDone = isDone;
        mContext = context;
    }

    private static void syncFromClientToServer(File clientFile, String serverFilePath) throws Exception {
        if (clientFile.isDirectory()) {
            if (!server.isFileExist(serverFilePath)) {
                if (!server.mkdirs(serverFilePath)) {
                    throw new IOException("Could not create path "
                            + serverFilePath);
                }
            } else if (!server.isDirectory(serverFilePath)) {
                throw new IOException(
                        "Source and Destination not of the same type:"
                                + clientFile.getCanonicalPath() + " , "
                                + serverFilePath);
            }
            String[] sources = clientFile.list();
            Set<String> srcNames = new HashSet<String>(Arrays.asList(sources));
            String dests = server.getListFileName(serverFilePath);
            if(dests.equals("")){

            }else{
                String[] destsArray = dests.split(";");
                // delete files not present in client
                for (String fileName : destsArray) {
                    if (!srcNames.contains(fileName)) {
                        server.deleteFile(serverFilePath + File.separator + fileName);
                    }
                }
            }
            // copy each file from client to server
            for (String fileName : sources) {
                File srcFile = new File(clientFile, fileName);
                syncFromClientToServer(srcFile, serverFilePath + File.separator + fileName);
            }
        }else{
            if (server.isFileExist(serverFilePath) && server.isDirectory(serverFilePath)) {
                    server.deleteFile(serverFilePath);
            }
            if (clientFile.lastModified() > server.lastModified(serverFilePath)) {
                System.out.println("Sync from client to server: " + clientFile.getAbsolutePath());
                Log.e("Sync file: ", clientFile.getAbsolutePath());
                uploadFileToServer(clientFile, serverFilePath);
            } else if (clientFile.lastModified() < server.lastModified(serverFilePath)) {
                System.out.println("Sync from server to client: " + serverFilePath);
                Log.e("Sync file: ", clientFile.getAbsolutePath());
                downloadFileFromServer(serverFilePath, clientFile);
            }
        }
    }

    private static void syncFromServerToClient(String serverFilePath, File clientFile) throws Exception {
        if (server.isDirectory(serverFilePath)) {
            if (!clientFile.exists()) {
                if (!clientFile.mkdirs()) {
                    throw new IOException("Could not create path "
                            + serverFilePath);
                }
            } else if (!clientFile.isDirectory()) {
                throw new IOException(
                        "Source and Destination not of the same type:"
                                + clientFile.getCanonicalPath() + " , "
                                + serverFilePath);
            }
            String[] dests = clientFile.list();
            String sources = server.getListFileName(serverFilePath);
            String[] sourcesArray = sources.split(";");
            Set<String> srcNames = new HashSet<String>(Arrays.asList(sourcesArray));
            // delete files not present in server
            if(sources.equals("")){
                for (String fileName : dests) {
                    delete(new File(clientFile, fileName));
                }
                return;
            }else{
                for (String fileName : dests) {
                    if (!srcNames.contains(fileName)) {
                        delete(new File(clientFile, fileName));
                    }
                }
            }
            // copy each file from server to client
            for (String fileName : sourcesArray) {
                File clientOutputFile = new File(clientFile, fileName);
                syncFromServerToClient(serverFilePath + File.separator + fileName, clientOutputFile);
            }
        }else{
            if (clientFile.exists() && clientFile.isDirectory()) {
                delete(clientFile);
            }
            if (clientFile.lastModified() > server.lastModified(serverFilePath)) {
                System.out.println("Sync from client to server: " + clientFile.getAbsolutePath());
                Log.e("Sync file: ", clientFile.getAbsolutePath());
                uploadFileToServer(clientFile, serverFilePath);
            } else if (clientFile.lastModified() < server.lastModified(serverFilePath)) {
                System.out.println("Sync from server to client: " + serverFilePath);
                Log.e("Sync file: ", clientFile.getAbsolutePath());
                downloadFileFromServer(serverFilePath, clientFile);
            }
        }
    }

    private static void uploadFileToServer(File clientFile, String serverFilePath) throws Exception {
        try {
            FileInputStream fileInputStream = new FileInputStream(clientFile);
            byte imageData[] = new byte[(int) clientFile.length()];
            fileInputStream.read(imageData);
            // Converting Image byte array into Base64 String
            String imageDataString = Base64.encodeToString(imageData, Base64.URL_SAFE);
            server.uploadFileToServer(serverFilePath, imageDataString);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        boolean successTimestampOp = server.setLastModified(serverFilePath, clientFile.lastModified());
        if (!successTimestampOp) {
            System.out
                    .println("Could not change timestamp for {}. Index synchronization may be slow. "
                            + serverFilePath);
        }
    }

    private static void downloadFileFromServer(String serverFilePath, File clientFile) throws Exception {
        String stringByte = server.downloadFileFromServer(serverFilePath);
        byte[] imageByteArray = Base64.decode(stringByte, Base64.URL_SAFE);
        // Write a image byte array into file system
        FileOutputStream imageOutFile = null;
        try {
            imageOutFile = new FileOutputStream(clientFile);
            imageOutFile.write(imageByteArray);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (imageOutFile != null)
                try {
                    imageOutFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        boolean successTimestampOp = clientFile.setLastModified(server.lastModified(serverFilePath));
        if (!successTimestampOp) {
            System.out
                    .println("Could not change timestamp for {}. Index synchronization may be slow. "
                            + clientFile.getAbsolutePath());
        }
        Intent intent = new Intent();
        intent.setAction("distributed.exflorer.REFRESH");
        intent.putExtra("patenFolder",MainActivity.PARENT_FOLDER);
        mContext.sendBroadcast(intent);
    }

    public static void delete(File file) {
        if (file.isDirectory()) {
            for (File subFile : file.listFiles()) {
                delete(subFile);
            }
        }
        if (file.exists()) {
            if (!file.delete()) {
                System.out.println("Could not delete {}" + file);
            }
        }
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        boolean smart = true;
        try {
            while (!isDone) {
                if ((clientFile.length() != server.getFileSize(serverFile.getAbsolutePath())) ||
                        (clientFile.lastModified() != server.lastModified(serverFile.getAbsolutePath()))) {
                    if (clientFile.lastModified() > server.lastModified(serverFile.getAbsolutePath())) {
                        // state = 1 sync from client to server
                        state = 1;
                        syncFromClientToServer(clientFile, serverFile.getAbsolutePath());
                        Intent intent = new Intent();
                        intent.setAction("distributed.exflorer.REFRESH");
                        intent.putExtra("patenFolder", MainActivity.PARENT_FOLDER);
                        mContext.sendBroadcast(intent);
                        server.setLastModified(serverFile.getAbsolutePath(), clientFile.lastModified());
                        Log.e("Synchronization", "Syncing from client to servers");
                    } else if (clientFile.lastModified() < server.lastModified(serverFile.getAbsolutePath())) {
                        // state = 2 sync from server to client
                        state = 2;
                        syncFromServerToClient(serverFile.getAbsolutePath(), clientFile);
                        Intent intent = new Intent();
                        intent.setAction("distributed.exflorer.REFRESH");
                        intent.putExtra("patenFolder",MainActivity.PARENT_FOLDER);
                        mContext.sendBroadcast(intent);
                        clientFile.setLastModified(server.lastModified(serverFile.getAbsolutePath()));
                        Log.e("Synchronization", "Syncing from server to client");
                    }
                } else {
                    // state = 0 do nothing
                    System.out.println("Synced!");
                    state = 0;
                }

                client.setSyncState(state);
                Thread.sleep(10000);

                if (server.isStart())
                    continue;
                else {
                    System.out.println("Server just stopped");
                    server.disconnect(client);
                    break;
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void stopSync() {
        System.out.println("Stop sync");
        DateTimeUtils.setCurrentMillisSystem();
        System.out.println("Current time" + DateTimeUtils.currentTimeMillis());
        isDone = true;
    }

}
