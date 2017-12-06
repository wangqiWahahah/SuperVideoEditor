package com.woch.supervideoeditor;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by wangdake on 2017/11/24.
 */

public class VideoPropertyHodler extends RecyclerView.ViewHolder {

    public TextView textView;

    public VideoPropertyHodler(View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.textView);
    }

}
