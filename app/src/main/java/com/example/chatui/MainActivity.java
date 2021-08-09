package com.example.chatui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.LitePal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Msg> msgList = new ArrayList<>();

    private String ip = "192.168.1.135";
    private int port = 12345;
    private Socket socket;
    private Button send;
    private EditText inputText;
    private static RecyclerView msgRecyclerView;
    private static MsgAdapter adapter;
    private Button clear;
    private Button record;
    private EditText ip_edit;
    private Button clearAll;
    private BufferedReader br;
    private PrintStream ps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LitePal.getDatabase();

        inputText = (EditText) findViewById(R.id.inputText);
        send = (Button) findViewById(R.id.send);
        clear = (Button) findViewById(R.id.clear);
        record = (Button) findViewById(R.id.record_button);
        ip_edit = (EditText) findViewById(R.id.ip_edit);
        clearAll = (Button) findViewById(R.id.clearAll);


        msgRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);
        adapter = new MsgAdapter(msgList);
        msgRecyclerView.setAdapter(adapter);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        new Thread() {
            @Override
            public void run() {
                try {
                    socket = new Socket(ip, port);
                    br = new BufferedReader(new InputStreamReader(socket.getInputStream(),"GBK"));
                    ps = new PrintStream(socket.getOutputStream(), true, "GBK");
                    new Thread(new Receive()).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = inputText.getText().toString();
                if (!"".equals(content) && socket != null ) {
                    try {
                        String ip = null;
                        ip = ip_edit.getText().toString();
                        String time = getCurrentTime();
                        int i = Msg.TYPE_SEND;
                        Msg msg = new Msg(content, ip, i, time);
                        msgList.add(msg);
                        Record record = new Record();
                        record.setContent(content);
                        record.setIp(ip);
                        record.setTime(time);
                        record.setType(i);
                        record.save();
                        new Thread() {
                            @Override
                            public void run() {
                                ps.println(msg.getContent());
                                ps.println(msg.getIp());
                                ps.println(msg.getTime());
                            }
                        }.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    adapter.notifyItemInserted(msgList.size()-1);
                    msgRecyclerView.scrollToPosition(msgList.size()-1);
                    inputText.setText("");
                } else if (socket == null) {
                    Toast.makeText(MainActivity.this, "没有连接到服务器，请检查是否连接服务器！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msgList.clear();
                adapter.notifyDataSetChanged();
            }
        });

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msgList.clear();
                adapter.notifyDataSetChanged();

                List<Record> list = LitePal.findAll(Record.class);

                for (Record record : list) {
                    Msg msg = new Msg(record.getContent(),
                            record.getIp(), record.getType(),record.getTime());
                    msgList.add(msg);
                    adapter.notifyItemInserted(msgList.size()-1);
                    msgRecyclerView.scrollToPosition(msgList.size()-1);
                }

            }
        });

        clearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("警告！！！");
                dialog.setMessage("你确定要删除所有的聊天记录吗？此操作不能撤回！");
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LitePal.deleteAll(Record.class);
                        msgList.clear();
                        adapter.notifyDataSetChanged();
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
            }
        });

    }


    private static String getCurrentTime() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        return sdf.format(d);
    }


    private class Receive implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    String content = br.readLine();
                    String time = br.readLine();
                    String IP = br.readLine();
                    Log.d("receive", content + time);
                    if (content == null || content == "") {
                        continue;
                    } else {
                        Msg msg = new Msg(content, IP, Msg.TYPE_RECEIVED, time);
                        msgList.add(msg);
                        Record record = new Record();
                        record.setContent(msg.getContent());
                        record.setTime(msg.getTime());
                        record.setType(Msg.TYPE_RECEIVED);
                        record.setIp(msg.getIp());
                        record.save();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyItemInserted(msgList.size()-1);
                                msgRecyclerView.scrollToPosition(msgList.size()-1);
                            }
                        });
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}