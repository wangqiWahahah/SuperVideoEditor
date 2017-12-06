package com.woch.supervideoeditor;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.VideoView;

import com.woch.library.SuperVideo;
import com.woch.library.SuperVideoConfigure;
import com.woch.library.SuperVideoView;

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111 && resultCode == RESULT_OK) {
            //mediaProjection = projectionManager.getMediaProjection(resultCode, data);
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

    private void intUI() {

        recyclerView = findViewById(R.id.recyclerView);
        videoView = findViewById(R.id.video);
        textView2 = findViewById(R.id.textView2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        svv = findViewById(R.id.svv);

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
        strs.add("screencap");
        //if (mPropertyAdapter == null)
        mPropertyAdapter = new PropertyAdapter(this, strs);
        recyclerView.setAdapter(mPropertyAdapter);
        mPropertyAdapter.setTime(sss ,t);

        SuperVideo.getInstance().loadFFmpeg(this);
        SuperVideoConfigure superVideoConfigure = new SuperVideoConfigure.Builder().setExeType(SuperVideoConfigure.EXE_GIF).setInput(getPath(this, selectedVideoUri)).setOutput("/storage/emulated/0/Pictures/VideoEditor3/t1oo.gif").setGifTime(new String[]{"3","5","100x100"}).builder();

    }
    private String getTime(int seconds) {
        int hr = seconds / 3600;
        int rem = seconds % 3600;
        int mn = rem / 60;
        int sec = rem % 60;
        return String.format("%02d", hr) + ":" + String.format("%02d", mn) + ":" + String.format("%02d", sec);
    }

}
