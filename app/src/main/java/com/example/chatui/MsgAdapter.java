package com.example.chatui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder> {

    List<Msg> mMsgList;

    static class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout leftLayout;
        LinearLayout rightLayout;
        ImageView image;
        TextView leftText;
        TextView rightText;
        TextView ip_text_right;
        TextView ip_text_left;
        TextView time_left;
        TextView time_right;


        public ViewHolder(View view) {
            super(view);
            leftLayout = (LinearLayout) view.findViewById(R.id.left_layout);
            rightLayout = (LinearLayout) view.findViewById(R.id.right_layout);
            leftText = (TextView) view.findViewById(R.id.left_msg);
            rightText = (TextView) view.findViewById(R.id.right_msg);
            image = (ImageView) view.findViewById(R.id.my_image);
            ip_text_right = (TextView) view.findViewById(R.id.ip_text_right);
            ip_text_left = (TextView) view.findViewById(R.id.ip_text_left);
            time_left = (TextView) view.findViewById(R.id.time_left);
            time_right = (TextView) view.findViewById(R.id.time_right);
        }
    }

    public MsgAdapter (List<Msg> list) {
        mMsgList = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_item, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        Msg msg = mMsgList.get(position);

        if (msg.getType() == Msg.TYPE_RECEIVED) {
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.image.setVisibility(View.GONE);
            holder.rightLayout.setVisibility(View.GONE);
            holder.ip_text_right.setVisibility(View.GONE);
            holder.time_right.setVisibility(View.GONE);
            holder.leftText.setText(msg.getContent());
            holder.ip_text_left.setText(msg.getIp());
            holder.time_left.setText(msg.getTime());
        } else if (msg.getType() == Msg.TYPE_SEND) {
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.image.setVisibility(View.VISIBLE);
            holder.leftLayout.setVisibility(View.GONE);
            holder.ip_text_left.setVisibility(View.GONE);
            holder.time_left.setVisibility(View.GONE);
            holder.rightText.setText(msg.getContent());
            if ("255.255.255.255".equals(msg.getIp())) {
                holder.ip_text_right.setText("所有人消息");
            } else {
                holder.ip_text_right.setText("个人消息" + " 发给 " + msg.getIp());
            }
            holder.time_right.setText(msg.getTime());
        }
    }

    @Override
    public int getItemCount() {
        return mMsgList.size();
    }
}
