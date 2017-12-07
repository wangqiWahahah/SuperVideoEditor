package com.woch.supervideoeditor;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.woch.library.MarkEntity;
import com.woch.library.SuperVideo;
import com.woch.library.SuperVideoConfigure;
import com.woch.library.SuperVideoView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangdake on 2017/11/24.
 */

public class PropertyAdapter extends RecyclerView.Adapter {

    private MainActivity context;
    private List strs;
    private String sss, t;

    public PropertyAdapter(MainActivity context, List  strs){
        this.context = context;
        this.strs = strs;
        context.svv.setOnMRangeListener(new SuperVideoView.OnMRangeListener() {
            @Override
            public void getRange(int rangeViewX, int rangeViewRight) {

                sss = getTime(rangeViewX/1000);
                if((rangeViewRight-rangeViewX)/1000==0){
                    t = "1";
                }else {
                    t = ""+(rangeViewRight-rangeViewX)/1000;
                }

            }
        });
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_property_item,null);
        RecyclerView.ViewHolder holder = new VideoPropertyHodler(view);
        return holder;
    }

    private static final String TAG = "PropertyAdapter";

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        VideoPropertyHodler mVideoPropertyHodler = (VideoPropertyHodler) holder;
        mVideoPropertyHodler.textView.setText((String)strs.get(position));
        mVideoPropertyHodler.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                onItemListener.onItemClick(view, position);
                if (position == 0){

                    superVideoConfigure = new SuperVideoConfigure.Builder().setExeType(SuperVideoConfigure.EXE_GIF).setInput(context.getPath(context, uri)).setOutput("/storage/emulated/0/Pictures/VideoEditor3/t1oo.gif").setGifTime(new String[]{"3","5","100x100"}).builder();

                }else if (position == 1){

                    // 压缩 码率 一般是 文件大小字节/文件时间秒 = b
                    superVideoConfigure = new SuperVideoConfigure.Builder().setExeType(SuperVideoConfigure.EXE_COMPRESS).setInput(context.getPath(context, uri)).setOutput("/storage/emulated/0/Pictures/VideoEditor3/compress.mp4")
                            .setCompressProperty("h264","aac","200x200", "87382", "48k", "30", "44100", SuperVideoConfigure.V_TYPE_MEDIA).builder();
                    //SuperVideo.getInstance().exeCmdFFmpeg(superVideoConfigure);

                }else if (position == 2){

                    superVideoConfigure = new SuperVideoConfigure.Builder().setExeType(SuperVideoConfigure.EXE_SCREENSHOT).setInput(context.getPath(context, uri)).setOutput("/storage/emulated/0/Pictures/VideoEditor3/screenShot.jpg").setScreenShot("00:00:04").builder();
                    //SuperVideo.getInstance().exeCmdFFmpeg(superVideoConfigure);

                }else if (position == 3){

                    superVideoConfigure = new SuperVideoConfigure.Builder().setExeType(SuperVideoConfigure.EXE_SPLIT_A_V).setInput(context.getPath(context, uri)).setOutput("/storage/emulated/0/Pictures/VideoEditor3/split.mp3").builder();

                }else if (position == 4){

                    superVideoConfigure = new SuperVideoConfigure.Builder().setExeType(SuperVideoConfigure.EXE_TRANSCODE).setInput(context.getPath(context, uri)).setOutput("/storage/emulated/0/Pictures/VideoEditor3/trans1.avi").setTransCode("h264", "aac",SuperVideoConfigure.V_TYPE_MEDIA).builder();

                }else if (position == 5){

                    ArrayList arrayList = new ArrayList<MarkEntity>();
                    MarkEntity markEntity = new MarkEntity();
                    markEntity.setX(0);
                    markEntity.setY(0);
                    markEntity.setText(false);
                    markEntity.setMarkPath("/storage/emulated/0/Pictures/VideoEditor3/compressT3.png");
                    arrayList.add(markEntity);
                    MarkEntity markEntity1 = new MarkEntity();
                    markEntity1.setX(200);
                    markEntity1.setY(100);
                    markEntity1.setText(false);
                    markEntity1.setMarkPath("/storage/emulated/0/Pictures/VideoEditor3/compressT3.png");
                    arrayList.add(markEntity1);
                    MarkEntity markEntity2 = new MarkEntity();
                    markEntity2.setX(80);
                    markEntity2.setY(300);
                    markEntity2.setText(false);
                    markEntity2.setMarkPath("/storage/emulated/0/Pictures/VideoEditor3/compressT3.png");
                    arrayList.add(markEntity2);
                    MarkEntity markEntity3 = new MarkEntity();
                    markEntity3.setX(300);
                    markEntity3.setY(200);
                    markEntity3.setText(true);
                    markEntity3.setText("哈哈哈哈");
                    markEntity3.setFontcolor("green");
                    markEntity3.setFontsize("20");
                    markEntity3.setBoxcolor("red");
                    //arrayList.add(markEntity3);
                    MarkEntity markEntity4 = new MarkEntity();
                    markEntity4.setX(350);
                    markEntity4.setY(400);
                    markEntity4.setText(true);
                    markEntity4.setText("wahahah");
                    markEntity4.setFontcolor("blue");
                    markEntity4.setFontsize("10");
                    //arrayList.add(markEntity4);
                    superVideoConfigure = new SuperVideoConfigure.Builder().setExeType(SuperVideoConfigure.EXE_MARK).setInput(context.getPath(context, uri)).setOutput("/storage/emulated/0/Pictures/VideoEditor3/mark.mp4").setMark(arrayList).builder();

                }else if (position == 6){

                    superVideoConfigure = new SuperVideoConfigure.Builder().setExeType(SuperVideoConfigure.EXE_FADE).setInput(context.getPath(context, uri)).setOutput("/storage/emulated/0/Pictures/VideoEditor3/fade.mp4").setFade("3").builder();

                }else if (position == 7){

                    superVideoConfigure = new SuperVideoConfigure.Builder().setExeType(SuperVideoConfigure.EXE_IMAGE).setInput(context.getPath(context, uri)).setOutput("/storage/emulated/0/Pictures/VideoEditor3/image.mp4").builder();

                }else if (position == 8){


                    superVideoConfigure = new SuperVideoConfigure.Builder().setExeType(SuperVideoConfigure.EXE_CROP).setInput(context.getPath(context, uri)).setOutput("/storage/emulated/0/Pictures/VideoEditor3/crop.mp4").setVideoCrop(sss,t).builder();

                }else if (position == 9){

                    superVideoConfigure = new SuperVideoConfigure.Builder().setExeType(SuperVideoConfigure.EXE_BSF).setInput(context.getPath(context, uri)).setOutput("/storage/emulated/0/Pictures/VideoEditor3/bsf.mp4").setBsf("h264_mp4toannexb").builder();

                }else if (position == 10){

                    superVideoConfigure = new SuperVideoConfigure.Builder().setExeType(SuperVideoConfigure.EXE_GET_IMAGE).setInput(context.getPath(context, uri)).setOutput("/storage/emulated/0/Pictures/VideoEditor3/oyes%d.jpg").builder();




                }
                if (position < 11)
                    SuperVideo.getInstance().exeCmdFFmpeg(superVideoConfigure, context);

            }
        });

    }

    private String getTime(int seconds) {
        int hr = seconds / 3600;
        int rem = seconds % 3600;
        int mn = rem / 60;
        int sec = rem % 60;
        return String.format("%02d", hr) + ":" + String.format("%02d", mn) + ":" + String.format("%02d", sec);
    }

    @Override
    public int getItemCount() {
        return strs.size();
    }

    private Uri uri;
    private SuperVideoConfigure superVideoConfigure;

    public void setUri(Uri uri) {
        this.uri = uri;
    }
    public void setTime(String ss,String t) {
        //this.ss = ss;
        this.t = t;
    }

    private OnItemListener onItemListener;

    public void setItemListener(OnItemListener onItemListener){
        this.onItemListener = onItemListener;
    }

    public interface OnItemListener{
        public void onItemClick(View view, int position);
    }

}
