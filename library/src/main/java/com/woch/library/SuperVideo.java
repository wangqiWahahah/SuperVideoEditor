package com.woch.library;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.FFmpegExecuteResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

import java.util.List;

/**
 * Created by wangdake on 2017/11/25.
 */

public class SuperVideo {

    private static SuperVideo superVideo;
    private FFmpeg fFmpeg;

    private static final int EXE_GIF= 1000;  //Gif 图
    private static final int EXE_COMPRESS = 1001;  //压缩
    private static final int EXE_SCREENSHOT = 1002;  //截图
    private static final int EXE_SPLIT_A_V = 1003;   //分割视频 音频
    private static final int EXE_TRANSCODE = 1004;   //转码
    private static final int EXE_MARK = 1005;  //水印
    private static final int EXE_CLIP = 1006;  //视频剪辑
    private static final int EXE_FADE = 1007;  //淡入淡出
    private static final int EXE_BSF = 1008;  //滤镜
    private static final int EXE_CROP = 1009;  //视频裁剪
    private static final int EXE_IMAGE = 1010;  //镜像视频
    public static final int EXE_GET_IMAGE = 1011;  //镜像视频
    public static final int EXE_SCREENCAP = 1012;  //

    private SuperVideo(){

    }

    public void loadFFmpeg(Context context) {

        fFmpeg = FFmpeg.getInstance(context);
        try {
            fFmpeg.loadBinary(new LoadBinaryResponseHandler(){

                @Override
                public void onSuccess() {
                    super.onSuccess();
                    Log.e("supervideoeditor", "onSuccess------loadBinary");
                }

                @Override
                public void onFailure() {
                    super.onFailure();
                    Log.e("supervideoeditor", "onFailure------loadBinary");
                }

            });
        } catch (FFmpegNotSupportedException e) {
            e.printStackTrace();
            Log.e("supervideoeditor", "------loadBinary" + e.toString());
        }

    }

    private String inputLs, outputLs;
    private String[] gifStr;

    public void exeCmdFFmpeg(SuperVideoConfigure superVideoConfigure, Context context){

        String mCmd;
        String[] cmd;
        StringBuffer sCmd = new StringBuffer();

        switch (superVideoConfigure.getExeType()){

            case EXE_GIF:

                inputLs = superVideoConfigure.getInput();
                outputLs = superVideoConfigure.getOutput();
                gifStr = superVideoConfigure.getGifTime();
                mCmd = "-ss "+gifStr[0]+" -t "+gifStr[1]+" -i "+inputLs + " -an -r 15 -vf fps=15,scale=270:-1 -pix_fmt rgb24 -y " + outputLs;
                cmd = mCmd.split(" ");
                Log.e("supervideoeditor", "mCmd============"+mCmd);
                exeFFmpeg(cmd,context);

                break;


            case EXE_COMPRESS:

                inputLs = superVideoConfigure.getInput();
                outputLs = superVideoConfigure.getOutput();
                String[] compressP = superVideoConfigure.getCompressProperty();
                //vfmt, afmt, s, vb, ab, vr, ar, mediaType
                if (SuperVideoConfigure.A_TYPE_MEDIA.equals(compressP[compressP.length-1])){
                    mCmd = "-i " +inputLs +" -ac "+compressP[1]+" -b:a "+ compressP[4]+" -ar "+compressP[6]+" "+outputLs;
                }else {
                    mCmd = "-i "+inputLs+" -vcodec "+compressP[0]+" -s "+compressP[2]+" -b:v "+compressP[3]+" -r "+compressP[5] +" -acodec "+compressP[1]+" -b:a "+ compressP[4]+" -ar "+compressP[6]+" "+outputLs;
                }

                Log.e("supervideoeditor", "mCmd============"+mCmd);
                cmd = mCmd.split(" ");
                exeFFmpeg(cmd,context);

                break;


            case EXE_SCREENSHOT:

                inputLs = superVideoConfigure.getInput();
                outputLs = superVideoConfigure.getOutput();
                String ss = superVideoConfigure.getScreenShot();

                mCmd = "-ss "+ ss +" -i "+inputLs+" -vframes 1 -y -f image2 " + outputLs;

                Log.e("supervideoeditor", "mCmd============"+mCmd);
                cmd = mCmd.split(" ");
                exeFFmpeg(cmd,context);
                break;


            case EXE_SPLIT_A_V:

                inputLs = superVideoConfigure.getInput();
                outputLs = superVideoConfigure.getOutput();
                mCmd = "-vn -i " + inputLs + " -y -f " + outputLs;
                Log.e("supervideoeditor", "mCmd============"+mCmd);
                cmd = mCmd.split(" ");
                exeFFmpeg(cmd,context);

                break;


            case EXE_TRANSCODE:

                inputLs = superVideoConfigure.getInput();
                outputLs = superVideoConfigure.getOutput();
                String[] compressT = superVideoConfigure.getTransCode();
                if (SuperVideoConfigure.A_TYPE_MEDIA.equals(compressT[compressT.length - 1])){
                    mCmd = "-i "+ inputLs + " -acodec " +compressT[1]+ outputLs;
                }else {

                    mCmd = "-i " + inputLs + " -vcodec " + compressT[0] + " -acodec " + compressT[1] +" "+outputLs;
                }
                Log.e("supervideoeditor", "mCmd============"+mCmd);
                cmd = mCmd.split(" ");
                exeFFmpeg(cmd,context);

                break;


            case EXE_MARK:

                inputLs = superVideoConfigure.getInput();
                outputLs = superVideoConfigure.getOutput();
                List<MarkEntity> markls = superVideoConfigure.getMark();
                sCmd.append("-i ").append(inputLs);
                for (MarkEntity entity:
                markls) {
                    if (!entity.isText()){
                        sCmd.append(" -i ").append(entity.getMarkPath());
                    }
                }
                sCmd.append(" -filter_complex");
                int i =1;
                for (MarkEntity entity:
                markls) {
                    if (entity.isText()){
                        sCmd.append(",drawtext=fontfile=FreeSans.ttf").append(":fontsize=").append(entity.getFontsize()).append(":fontcolor=")
                                .append(entity.getFontcolor()).append(":boxcolor=").append(entity.getBoxcolor())
                                .append(":x=").append(entity.getX()).append(":y=").append(entity.getY())
                                .append(":text=").append(entity.getText());
//                        sCmd.append(" drawtext=text=FreeSans.ttf").append(":fontsize=").append(entity.getFontsize()).append(":fontcolor=")
//                                .append(entity.getFontcolor()).append(":boxcolor=").append(entity.getBoxcolor())
//                                .append(":x=").append(entity.getX()).append(":y=").append(entity.getY())
//                                .append(":text=").append(entity.getText());
                    }else {
                        if (i !=1){
                            sCmd.append("[").append(i-1).append("]").append(";[").append(i-1).append("][");
                        }else {
                            sCmd.append(" [0:v][");
                        }
                        sCmd.append(i).append(":v]overlay=").append(entity.getX()).append(":").append(entity.getY());
                        ++i;
                    }
                }
                sCmd.append(" -y ").append(outputLs);
                //sCmd.append(" -filter_complex overlay='i':'0' -y ").append(outputLs);
                Log.e("supervideoeditor", "mCmd============"+sCmd);
                cmd = sCmd.toString().split(" ");
                exeFFmpeg(cmd,context);

                break;


            case EXE_CLIP:



                break;


            case EXE_FADE:

                inputLs = superVideoConfigure.getInput();
                outputLs = superVideoConfigure.getOutput();
                String t = superVideoConfigure.getFade();
                mCmd = "-i "+inputLs+" -vf fade=in:0:d="+t +" -y "+outputLs;
                Log.e("supervideoeditor", "mCmd============"+mCmd);
                cmd = mCmd.split(" ");
                exeFFmpeg(cmd,context);

                break;

            case EXE_BSF:

                inputLs = superVideoConfigure.getInput();
                outputLs = superVideoConfigure.getOutput();
                String bsf = superVideoConfigure.getBsf();
                sCmd.append("-i ").append(inputLs).append(" -bsf ").append(bsf).append(" -y ").append(outputLs);
                Log.e("supervideoeditor", "mCmd============"+sCmd);
                cmd = sCmd.toString().split(" ");
                exeFFmpeg(cmd,context);

                break;

            case EXE_CROP:

                inputLs = superVideoConfigure.getInput();
                outputLs = superVideoConfigure.getOutput();
                String[] strings = superVideoConfigure.getVideoCrop();
                sCmd.append("-ss ").append(strings[0]).append(" -i ").append(inputLs).append(" -t ").append(strings[1]).append(" -y ").append(outputLs);
                Log.e("supervideoeditor", "mCmd============"+sCmd);
                cmd = sCmd.toString().split(" ");
                exeFFmpeg(cmd,context);

                break;

            case EXE_IMAGE:

                inputLs = superVideoConfigure.getInput();
                outputLs = superVideoConfigure.getOutput();
                sCmd.append("-i ").append(inputLs).append(" -vf split[main][tmp];[tmp]crop=iw:ih/2:0:0,vflip[flip];[main][flip]overlay=0:H/2 -y ").append(outputLs);
                Log.e("supervideoeditor", "mCmd============"+sCmd);
                cmd = sCmd.toString().split(" ");
                exeFFmpeg(cmd,context);


                break;

            case EXE_GET_IMAGE:

                inputLs = superVideoConfigure.getInput();
                outputLs = superVideoConfigure.getOutput();
                sCmd.append("-i ").append(inputLs).append(" -vf fps=fps=1 -y ").append(outputLs);
                Log.e("supervideoeditor", "mCmd============"+sCmd);
                cmd = sCmd.toString().split(" ");
                exeFFmpeg(cmd,context);

                break;

            case EXE_SCREENCAP:

                inputLs = superVideoConfigure.getInput();
                outputLs = superVideoConfigure.getOutput();

                //-f x11grab -s 1600x900 -r 50 -vcodec libx264 –preset:v ultrafast –tune:v zerolatency -crf 18
//                sCmd.append("-f x11grab -r 30 -s 432*768 -vcodec h264 –preset:v ultrafast –tune:v zerolatency -crf 18 ").append(outputLs);
//                Log.e("supervideoeditor", "mCmd============"+sCmd);
//                cmd = sCmd.toString().split(" ");
//                exeFFmpeg(cmd,context);

                break;

        }



    }


    private void exeFFmpeg(String[] cmd, final Context context){

        try {
            fFmpeg.execute(cmd, new FFmpegExecuteResponseHandler() {
                @Override
                public void onSuccess(String message) {

                    Log.e("supervideoeditor", "onSuccess------"+message);
                    Toast.makeText(context, "成功", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onProgress(String message) {

                    Log.e("supervideoeditor", "onProgress--------"+message);

                }

                @Override
                public void onFailure(String message) {

                    Log.e("supervideoeditor", "onFailure--------"+message);
                }

                @Override
                public void onStart() {

                }

                @Override
                public void onFinish() {

                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            e.printStackTrace();
            Log.e("supervideoeditor", "------execute" + e.toString());
        }

    }

    public static SuperVideo getInstance(){

        if (superVideo == null){

            synchronized (SuperVideo.class){

                if (superVideo == null){

                    superVideo = new SuperVideo();

                }

            }

        }

        return superVideo;

    }


}
