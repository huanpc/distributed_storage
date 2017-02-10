package com.example.hieuttc.distributedsystem.recyclerview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hieuttc.distributedsystem.FileUtil;
import com.example.hieuttc.distributedsystem.MainActivity;
import com.example.hieuttc.distributedsystem.R;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * Created by hieuapp on 30/03/2016.
 */
public class ExflorerAdapter extends RecyclerView.Adapter<ExflorerAdapter.MyViewHolder> {

    private List<File> files = null;
    private Activity context;

    public ExflorerAdapter(Activity context,List<File> listFile){
        this.files = listFile;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.file_list_row, parent, false);
        return new MyViewHolder(context,itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        File file = files.get(position);
        if(file.isDirectory()){
            holder.iconFile.setImageResource(R.mipmap.ic_folder);
        }else {
            holder.iconFile.setImageResource(R.mipmap.ic_file);
        }
        holder.tvName.setText(file.getName());
        Date dateModified = new Date(file.lastModified());
        String date = FileUtil.getFomatDate(dateModified);
        holder.tvDateModifi.setText("Modified: "+date);

        holder.iconMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.iv_action_more) {
                    FileUtil.confirmDeleteFile(context, getItemFile(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    public File getItemFile(int position){
        return files.get(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView tvName, tvDateModifi;
        public ImageView iconFile, iconMore;
        public MyViewHolder(final Context context, View itemView) {
            super(itemView);
            tvName = (TextView)itemView.findViewById(R.id.tv_file_name);
            Typeface regular = Typeface.createFromAsset(context.getAssets(),
                    "RobotoTTF/Roboto-Regular.ttf");
            tvName.setTypeface(regular);
            tvDateModifi = (TextView)itemView.findViewById(R.id.tv_modifi);
            Typeface thin = getTypeface("Thin");
            tvDateModifi.setTypeface(thin);
            iconFile = (ImageView)itemView.findViewById(R.id.iv_icon_file);
            iconMore = (ImageView)itemView.findViewById(R.id.iv_action_more);
        }

        public Typeface getTypeface(String type){
            Typeface typeface = Typeface.createFromAsset(context.getAssets(),
                    "RobotoTTF/Roboto-"+type+".ttf");
            return typeface;
        }
    }

}
