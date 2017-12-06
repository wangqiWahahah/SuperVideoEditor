package com.woch.library;

import java.util.List;

/**
 * Created by wangdake on 2017/11/24.
 */

public class SuperVideoConfigure {

    public static final int EXE_GIF= 1000;  //Gif 图
    public static final int EXE_COMPRESS = 1001;  //压缩
    public static final int EXE_SCREENSHOT = 1002;  //截图
    public static final int EXE_SPLIT_A_V = 1003;   //分割视频 音频
    public static final int EXE_TRANSCODE = 1004;   //转码
    public static final int EXE_MARK = 1005;  //水印
    public static final int EXE_CLIP = 1006;  //视频剪辑
    public static final int EXE_FADE = 1007;  //淡入淡出
    public static final int EXE_BSF = 1008;  //滤镜
    public static final int EXE_CROP = 1009;  //视频裁剪
    public static final int EXE_IMAGE = 1010;  //镜像视频
    public static final int EXE_GET_IMAGE = 1011;  //
    public static final int EXE_SCREENCAP = 1012;  //

    public static final String A_TYPE_MEDIA = "a";
    public static final String V_TYPE_MEDIA = "v";

    private SuperVideoConfigure(Builder builder){

//        this.EXE_GIF = builder.EXE_GIF;
//        this.EXE_COMPRESS = builder.EXE_COMPRESS;
//        this.EXE_SCREENSHOT = builder.EXE_SCREENSHOT;
//        this.EXE_SPLIT_A_V = builder.EXE_SPLIT_A_V;
//        this.EXE_TRANSCODE = builder.EXE_TRANSCODE;
//        this.EXE_MARK = builder.EXE_MARK;
//        this.EXE_EDIT = builder.EXE_EDIT;
//        this.EXE_FADE = builder.EXE_FADE;
//        this.EXE_BSF = builder.EXE_BSF;
        this.exeType = builder.exeType;
        this.inputLs = builder.inputLs;
        this.outputLs = builder.outputLs;
        this.gifStr = builder.gifStr;
        this.compressStr = builder.compressStr;
        this.ss = builder.ss;
        this.transStr = builder.transStr;
        this.markLs = builder.markLs;
        this.inTime = builder.inTime;
        this.cropStr = builder.cropStr;
        this.bsf = builder.bsf;

    }

    public static class Builder{

//        private int EXE_GIF= 1000;  //Gif 图
//        private int EXE_COMPRESS = 1001;  //压缩
//        private int EXE_SCREENSHOT = 1002;  //截图
//        private int EXE_SPLIT_A_V = 1003;   //分割视频 音频
//        private int EXE_TRANSCODE = 1004;   //转码
//        private int EXE_MARK = 1005;  //水印
//        private int EXE_CLIP = 1006;  //视频剪辑
//        private int EXE_FADE = 1007;  //淡入淡出
//        private int EXE_BSF = 1008;  //滤镜
//        private final int EXE_CROP = 1009;  //视频裁剪
//        private final int EXE_IMAGE = 1010;  //镜像视频


        private int exeType;
        private String inputLs;
        private String outputLs, inTime, bsf;
        private String[] gifStr, compressStr, transStr, cropStr;
        private String ss;
        private List<MarkEntity> markLs;

        public Builder(){

        }

        public Builder setExeType(int type){
            this.exeType = type;
            return this;
        }

        public Builder setInput(String inputLs){
            this.inputLs = inputLs;
            return this;
        }

        public Builder setOutput(String outputLs){
            this.outputLs = outputLs;
            return this;
        }

        public Builder setGifTime(String[] gifStr){
            this.gifStr = gifStr;
            return this;
        }

        public Builder setCompressProperty(String vfmt, String afmt, String s, String vb, String ab, String vr, String ar, String mediaType){
            this.compressStr = new String[]{vfmt, afmt, s, vb, ab, vr, ar, mediaType};
            return this;
        }

        public Builder setCompressProperty(String vfmt, String afmt, String s, String mediaType){

            return this;
        }

        public Builder setScreenShot(String ss){

            this.ss = ss;
            return this;
        }

        public Builder setTransCode(String vfmt, String afmt, String mediaType){

            this.transStr = new String[]{vfmt, afmt, mediaType};
            return this;

        }

        public Builder setMark(List<MarkEntity> markLs){

            this.markLs = markLs;
            return this;
        }


        public Builder setClip(int start, int end){


            return this;

        }

        public Builder setFade(String inTime){
            this.inTime = inTime;
            return this;
        }

        public Builder setBsf(String bsf){
            this.bsf = bsf;
            return this;
        }

        public Builder setVideoCrop(String ss, String d){

            this.cropStr = new String[]{ss, d};
            return this;

        }


        public SuperVideoConfigure builder(){

            return new SuperVideoConfigure(this);

        }

    }

    private int exeType;
    private String inputLs;
    private String outputLs,inTime,bsf;
    private String[] gifStr, compressStr, transStr, cropStr;
    private String ss;
    private List<MarkEntity> markLs;

    public int getExeType(){
        return exeType;
    }

    public String getInput(){
        return inputLs;
    }

    public String getOutput(){
        return outputLs;
    }

    public String[] getGifTime(){
        return gifStr;
    }

    public String[] getCompressProperty(){

        return compressStr;

    }

    public String getScreenShot(){

        return ss;
    }

    public String[] getTransCode(){

        return transStr;

    }

    public List getMark(){
        return markLs;
    }

    public void getMarkPosition(List markPositionLs){

    }

    public void getClip(int start, int end){



    }

    public String getFade(){
        return inTime;
    }

    public String getBsf(){

        return bsf;
    }

    public String[] getVideoCrop(){

        return cropStr;

    }


}
