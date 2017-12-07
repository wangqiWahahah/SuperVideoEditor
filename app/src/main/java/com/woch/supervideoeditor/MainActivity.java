package com.woch.supervideoeditor;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Surface;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.woch.library.SuperVideo;
import com.woch.library.SuperVideoConfigure;
import com.woch.library.SuperVideoView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private VideoView videoView;
    private TextView textView2;

    private PropertyAdapter mPropertyAdapter;

    private Uri selectedVideoUri;


    private static final int REQUEST_TAKE_GALLERY_VIDEO = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intUI();
        setListener();

    }

    private void setListener() {

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {

            }
        });

        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                return false;
            }
        });

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {

            }
        });

        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Intent intent = new Intent();
                    intent.setType("video/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Video"), REQUEST_TAKE_GALLERY_VIDEO);
                } catch (Exception e) {

                }

            }
        });

    }

    private int mScreenWidth, mScreenHeight, mScreenDensity;
    private void getScreenBaseInfo() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mScreenWidth = metrics.widthPixels;
        mScreenHeight = metrics.heightPixels;
        mScreenDensity = metrics.densityDpi;
    }
    private VirtualDisplay virtualDisplay;

    private void createVirtualDisplay() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            virtualDisplay = mediaProjection.createVirtualDisplay(
                    "MainScreen",
                    mScreenWidth,
                    mScreenHeight,
                    mScreenDensity,
                    DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                    mediaRecorder.getSurface(),
                    null, null);
        }
    }


    private void initRecorder() {
        File file = new File("/storage/emulated/0/Pictures/VideoEditor3/lu.mp4");
        if (mediaRecorder == null)
        mediaRecorder = new MediaRecorder();

        //mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setVideoEncodingBitRate(512 * 1000);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        //mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setVideoSize(mScreenWidth, mScreenHeight);
        mediaRecorder.setVideoFrameRate(30);
        mediaRecorder.setOutputFile(file.getAbsolutePath());
        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private Surface surface;
    private boolean running = false;

    public boolean startRecord() {
        if (mediaProjection == null || running) {
            return false;
        }
        initRecorder();
        handler.sendEmptyMessageDelayed(1, 5000);

        return true;
    }

    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            createVirtualDisplay();
            mediaRecorder.start();
            running = true;


        }
    };

    private MediaProjection mediaProjection;
    private MediaRecorder mediaRecorder;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111 && resultCode == RESULT_OK) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mediaProjection = projectionManager.getMediaProjection(resultCode, data);
//                initRecorder();
//                createVirtualDisplay();
                startRecord();
            }
        }
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_GALLERY_VIDEO) {
                selectedVideoUri = data.getData();
                videoView.setVideoURI(selectedVideoUri);
                videoView.start();

                mPropertyAdapter.setUri(selectedVideoUri);

                //videoView.setOnCompletionListener();

                videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {

                        SuperVideo.getInstance().setVideoTime(mediaPlayer.getDuration()/1000);

                        svv.setRangeValues(0, mediaPlayer.getDuration());
                        svv.setOnMSeekListener(new SuperVideoView.OnMSeekListener() {
                            @Override
                            public void getSeek(int v) {

                                videoView.seekTo(v);

                            }
                        });
                    }
                });

                        // TODO Auto-generated method stub
//                        duration = mp.getDuration() / 1000;
//                        tvLeft.setText("00:00:00");
//
//                        tvRight.setText(getTime(mp.getDuration() / 1000));
//                        mp.setLooping(true);
//                        rangeSeekBar.setRangeValues(0, duration);
//                        rangeSeekBar.setSelectedMinValue(0);
//                        rangeSeekBar.setSelectedMaxValue(duration);
//                        rangeSeekBar.setEnabled(true);
//
//                        rangeSeekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
//                            @Override
//                            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
//                                videoView.seekTo((int) minValue * 1000);
//
//                                tvLeft.setText(getTime((int) bar.getSelectedMinValue()));
//
//                                tvRight.setText(getTime((int) bar.getSelectedMaxValue()));
//
//                            }
//                        });

//                        final Handler handler = new Handler();
//                        handler.postDelayed(r = new Runnable() {
//                            @Override
//                            public void run() {
//
//                                if (videoView.getCurrentPosition() >= rangeSeekBar.getSelectedMaxValue().intValue() * 1000)
//                                    videoView.seekTo(rangeSeekBar.getSelectedMinValue().intValue() * 1000);
//                                handler.postDelayed(r, 1000);
//                            }
//                        }, 1000);

                    }



//                }
            }

    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    public String getPath(final Context context, final Uri uri) {

        if (uri == null)
            return "";
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }

                    // TODO handle non-primary volumes
                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {

                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    return getDataColumn(context, contentUri, null, null);
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{
                            split[1]
                    };

                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            }
            // MediaStore (and general)
            else if ("content".equalsIgnoreCase(uri.getScheme())) {
                return getDataColumn(context, uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri.
     */
    private String getDataColumn(Context context, Uri uri, String selection,
                                 String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public SuperVideoView svv;

    String sss;
    String t;
    private MediaProjectionManager projectionManager;

    private void intUI() {

        recyclerView = findViewById(R.id.recyclerView);
        videoView = findViewById(R.id.video);
        textView2 = findViewById(R.id.textView2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        svv = findViewById(R.id.svv);
        getScreenBaseInfo();

        List<String> ImageLs = new ArrayList<>();
        ImageLs.add(com.woch.library.R.drawable.ic_launcher + "");
        ImageLs.add(com.woch.library.R.drawable.ic_launcher + "");
        ImageLs.add(com.woch.library.R.drawable.ic_launcher + "");
        ImageLs.add(com.woch.library.R.drawable.ic_launcher + "");
        ImageLs.add(com.woch.library.R.drawable.ic_launcher + "");
        ImageLs.add(com.woch.library.R.drawable.ic_launcher + "");
        ImageLs.add(com.woch.library.R.drawable.ic_launcher + "");
        ImageLs.add(com.woch.library.R.drawable.ic_launcher + "");
        ImageLs.add(com.woch.library.R.drawable.ic_launcher + "");
        ImageLs.add(com.woch.library.R.drawable.ic_launcher + "");
        svv.setImageLs(ImageLs);


        List strs = new ArrayList();
        strs.add("GIF");
        strs.add("Compress");
        strs.add("ScreenShot");
        strs.add("split");
        strs.add("transcode");
        strs.add("mark");
        strs.add("fade");
        strs.add("image");
        strs.add("crop");
        strs.add("bsf");
        strs.add("get15image");
        strs.add("screencap");
        strs.add("stop");
        //if (mPropertyAdapter == null)

        projectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);

        mPropertyAdapter = new PropertyAdapter(this, strs);
        recyclerView.setAdapter(mPropertyAdapter);
        mPropertyAdapter.setTime(sss ,t);
        mPropertyAdapter.setItemListener(new PropertyAdapter.OnItemListener() {
            @Override
            public void onItemClick(View view, int position) {

                if (position == 11){
                    Intent captureIntent= null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        captureIntent = projectionManager.createScreenCaptureIntent();
                        startActivityForResult(captureIntent, 111);
                    }
                }

                if (position == 11){

                    if(virtualDisplay != null) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            virtualDisplay.release();
                        }
                        virtualDisplay = null;
                    }
                    if(mediaRecorder != null) {
                        mediaRecorder.setOnErrorListener(null);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            mediaProjection.stop();
                        }
                        mediaRecorder.reset();
                    }
                    if(mediaProjection != null) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            mediaProjection.stop();
                        }
                        mediaProjection = null;
                    }


                }



            }
        });

        SuperVideo.getInstance().loadFFmpeg(this);
        SuperVideoConfigure superVideoConfigure = new SuperVideoConfigure.Builder().setExeType(SuperVideoConfigure.EXE_GIF).setInput(getPath(this, selectedVideoUri)).setOutput("/storage/emulated/0/Pictures/VideoEditor3/t1oo.gif").setGifTime(new String[]{"3","5","100x100"}).builder();
        SuperVideo.getInstance().setOnSuccessListener(new SuperVideo.OnSuccessListener() {
            @Override
            public void onSuccess(int type, String path) {

                switch (type){

                    case SuperVideoConfigure.EXE_GIF:

                        return;




                    case SuperVideoConfigure.EXE_SCREENSHOT:


                        return;




                    case SuperVideoConfigure.EXE_GET_IMAGE:
                        //svv.setImageLs();

                        return;


                }

                Toast.makeText(MainActivity.this, "成功", Toast.LENGTH_SHORT).show();

                videoView.setVideoPath(path);
                videoView.start();

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
    protected void onDestroy() {
        super.onDestroy();


        if(virtualDisplay != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                virtualDisplay.release();
            }
            virtualDisplay = null;
        }
        if(mediaRecorder != null) {
            mediaRecorder.setOnErrorListener(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mediaProjection.stop();
            }
            mediaRecorder.reset();
        }
        if(mediaProjection != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mediaProjection.stop();
            }
            mediaProjection = null;
        }

    }
}
