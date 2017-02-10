package com.example.hieuttc.distributedsystem;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.folderselector.FileChooserDialog;
import core.ClientInstance;
import core.ClientInterface;
import core.NtpMessage;
import core.ServerInterface;
import core.Synchronization;
import com.example.hieuttc.distributedsystem.recyclerview.DividerItemDecoration;
import com.example.hieuttc.distributedsystem.recyclerview.ExflorerAdapter;
import com.example.hieuttc.distributedsystem.recyclerview.RecyclerTouchListener;

import org.joda.time.DateTimeUtils;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;

import lipermi.handler.CallHandler;
import lipermi.handler.CallLookup;
import lipermi.net.Client;

public class MainActivity extends AppCompatActivity implements FileChooserDialog.FileCallback {

    public static final String PARENT_FOLDER_NAME = "DistributedSys";
    public static final String TAG = "MainActivity";
    public static final String DEFAULT_IP_SERVER = "localhost";
    public static final int PORT_SERVER = 1098;
    public static final File PARENT_FOLDER;
    private RecyclerView recyclerView;
    private ExflorerAdapter exflorerAdapter;
    List<File> files;
    private Stack<File> stackExflorer = new Stack<File>();
    private Stack<File> stackFolder = new Stack<File>();

    public ServerInterface server;
    public ClientInterface client;
    public static boolean isDone = false;
    RefreshFileReceiver refreshFile;

    static {
        PARENT_FOLDER = new File(Environment.getExternalStorageDirectory(),PARENT_FOLDER_NAME);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(!PARENT_FOLDER.exists()){
            PARENT_FOLDER.mkdirs();
            Log.d(TAG,"Create parent folder success!");
        }else {
            Log.d(TAG,"Parent folder is exist");
        }

        files = getListFile(PARENT_FOLDER);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        exflorerAdapter = new ExflorerAdapter(this,files);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(exflorerAdapter);
        recyclerView.invalidate();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),
                recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                File itemFile = files.get(position);
                if(itemFile.isDirectory()){
                    stackExflorer.push(itemFile.getParentFile());
                    stackFolder.push(itemFile);
                    files = getListFile(itemFile.getAbsoluteFile());
                    recyclerView.setAdapter(new ExflorerAdapter(MainActivity.this,files));
                }else {
                    openFile(itemFile.getPath());
                }
            }

            @Override
            public void onLongClick(View view, int position) {
                File itemFile = files.get(position);
                File parentFile = itemFile.getParentFile();
                String fileName = itemFile.getName();
                itemFile.delete();
                files.remove(position);
                refreshFolder(parentFile, recyclerView);
                Toast.makeText(getApplicationContext(),"Delete file: "+fileName,Toast.LENGTH_SHORT).show();
            }
        }));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooseDialog();
            }
        });

        ServerAsyncTask connectServer = new ServerAsyncTask();
        connectServer.execute();
        RecursiveFileObserver fileObserver = new RecursiveFileObserver(this,PARENT_FOLDER.getAbsolutePath());
        fileObserver.startWatching();

        //register intent refresh folder when delete file
        IntentFilter filter =  new IntentFilter("distributed.exflorer.REFRESH");
        refreshFile = new RefreshFileReceiver();
        registerReceiver(refreshFile,filter);
    }

    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(refreshFile);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            changeIpServer();
            return true;
        }
        if(id == R.id.action_sync){
            FileSync fileSync = new FileSync();
            if(server != null && server.isStart()){
                Log.e(TAG,"Server is running. Start sync...");
                fileSync.execute();
            }else {
                ServerAsyncTask serverAsyncTask = new ServerAsyncTask();
                serverAsyncTask.execute();
                fileSync.execute();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void changeIpServer(){
        String ipServer = getIpServer();
        if(ipServer == null){
            ipServer = DEFAULT_IP_SERVER;
        }
        new MaterialDialog.Builder(this)
                .title("IP Server")
                .content(ipServer)
                .inputType(InputType.TYPE_NUMBER_FLAG_DECIMAL)
                .positiveText("SAVE")
                .input("new IP server...", null, false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        saveIpServer(String.valueOf(input));
                    }
                }).show();
    }

    public void saveIpServer(String ip){
        SharedPreferences preferences = getSharedPreferences("IP_SERVER", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ip_server",ip);
        editor.commit();
    }

    public String getIpServer(){
        SharedPreferences preferences = getSharedPreferences("IP_SERVER", MODE_PRIVATE);
        String ipServer = preferences.getString("ip_server",null);
        return ipServer;
    }

    public List<File> getListFile(File rootFolder){
        ArrayList<File> listFile = new ArrayList<File>();
        File[] files = rootFolder.listFiles();
        if(files != null){
            for(File file : files){
                listFile.add(file);
            }
        }
        return listFile;
    }

    /**
     * Called when file on dialog onclick
     * @param dialog
     * @param file
     */
    @Override
    public void onFileSelection(FileChooserDialog dialog, File file) {
        boolean copySuccess = false;
        String urlDest = null;
        //neu stack rong, thi copy vao folder cha
        if(stackFolder.isEmpty()){
            copySuccess = FileUtil.copyFile(file.getAbsolutePath(),file.getName(),
                    PARENT_FOLDER.getAbsolutePath());
            urlDest = PARENT_FOLDER.getAbsolutePath();
        }else {
            //neu stack khong rong thi copy vao folder hien tai
            urlDest = stackFolder.peek().getAbsolutePath();
            copySuccess = FileUtil.copyFile(file.getAbsolutePath(),file.getName(),urlDest);
        }

        if(copySuccess){
            Toast.makeText(this,"Copied success "+file.getName(),Toast.LENGTH_LONG).show();
            refreshFolder(new File(urlDest), recyclerView);
        }else {
            Toast.makeText(this,"Copied failed "+file.getName(),Toast.LENGTH_LONG).show();
        }

    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public void openFile(String path){
        File file = new File(path);
        String mime = FileUtil.getMimeType(file.getName());
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), mime);
        startActivityForResult(intent,10);
    }

    @Override
    public void onBackPressed() {
        if(!stackFolder.isEmpty()){
            stackFolder.pop();
        }
        if(stackExflorer.size() >= 1){
            File currentFile = stackExflorer.pop();
            if(currentFile != null){
                files = getListFile(currentFile.getAbsoluteFile());
                recyclerView.setAdapter(new ExflorerAdapter(MainActivity.this,files));
            }
            return;
        }
        finish();
    }
    public void showFileChooseDialog(){
        FileChooserDialog.Builder dialog = new FileChooserDialog.Builder(MainActivity.this);
        dialog.show();
    }

    public void refreshFolder(File folder, RecyclerView recyclerView){
        List<File> listFile = getListFile(folder);
        files = listFile;
        recyclerView.setAdapter(new ExflorerAdapter(MainActivity.this, listFile));
    }

    public void connectServer(){
        Log.e(TAG,"Start connect to server at "+new Date());
        CallHandler callHandler = new CallHandler();
        try {
            String ipServer = getIpServer();
            if(ipServer == null){
                ipServer = DEFAULT_IP_SERVER;
            }
            Client clientRemote = new Client(ipServer,PORT_SERVER,callHandler);
            Log.e(TAG,"host: "+ipServer);
            server = (ServerInterface)clientRemote.getGlobal(ServerInterface.class);
            client = new ClientInstance(CallLookup.getCurrentSocket());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clockSync() throws Exception {
        String serverIP = getIpServer();

        // Send request
        DatagramSocket socket = new DatagramSocket();
        InetAddress address = InetAddress.getByName(serverIP);
        byte[] buf = new NtpMessage().toByteArray();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address,123);

        // Set the transmit timestamp *just* before sending the packet
        // ToDo: Does this actually improve performance or not?
        NtpMessage.encodeTimestamp(packet.getData(), 40,
                (System.currentTimeMillis() / 1000.0) + 2208988800.0);

        socket.send(packet);

        // Get response
        System.out.println("NTP request sent, waiting for response...\n");
        packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);

        // Immediately record the incoming timestamp
        double destinationTimestamp = (System.currentTimeMillis() / 1000.0) + 2208988800.0;

        // Process response
        NtpMessage msg = new NtpMessage(packet.getData());

        // Corrected, according to RFC2030 errata
        double roundTripDelay = (destinationTimestamp - msg.originateTimestamp)
                - (msg.transmitTimestamp - msg.receiveTimestamp);

        double localClockOffset = ((msg.receiveTimestamp - msg.originateTimestamp) + (msg.transmitTimestamp - destinationTimestamp)) / 2;

        // Display response
        System.out.println("NTP server: " + serverIP);
        System.out.println(msg.toString());

        System.out.println("Dest. timestamp:     "
                + NtpMessage.timestampToString(destinationTimestamp));

        System.out.println("Round-trip delay: "
                + new DecimalFormat("0.00").format(roundTripDelay * 1000)
                + " ms");

        System.out.println("Local clock offset: "
                + new DecimalFormat("0.00").format(localClockOffset * 1000)
                + " ms");
        System.out.println("Current time " + DateTimeUtils.currentTimeMillis());
        DateTimeUtils.setCurrentMillisOffset((long) (localClockOffset * 1000));
        System.out.println("Current time " + DateTimeUtils.currentTimeMillis());
        socket.close();
    }

    public void startSync(){
        Log.e("StartSync","bat dau dong bo, server = "+(server==null?"null":"ok"));
        File clientFile = new File(PARENT_FOLDER.getAbsolutePath());
        File serverFile = server.getServerFile();
//        String text = server.getTestText();
//        Log.e("FileSync","data from server: "+text);
        Log.e("StartSync: server file",serverFile.getAbsolutePath());
        Synchronization sync = new Synchronization(client, server,
                serverFile, clientFile, isDone, getApplicationContext());
        new Thread(sync).start();
    }

    class ServerAsyncTask extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... params) {
            connectServer();
            try {
                clockSync();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void param){
            Log.e("ServerAsyncTask","Done task connect!");
        }
    }

    class FileSync extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... params) {
            startSync();
            return null;
        }

        protected void onPostExecute(Void param){
            Log.e("FileSync","Done file sync!");
        }
    }

    class RefreshFileReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals("distributed.exflorer.REFRESH")){
                Bundle bundle = intent.getExtras();
                File folderRefresh = (File)bundle.get("patenFolder");
                if(folderRefresh.isFile())
                    return;
                List<File> listFile = getListFile(folderRefresh);
                files = listFile;
                recyclerView.setAdapter(new ExflorerAdapter(MainActivity.this, listFile));
            }
        }
    }
}
