package com.example.felipetorres.mapboxspike;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.PropertyValue;
import com.mapbox.mapboxsdk.style.layers.RasterLayer;

import java.io.IOException;


public class MainActivity extends Activity {

    private MapView mapView;
    //private MapView mapView2;
    private MediaPlayer mediaPlayer;
    private TextureView textureView;
    private MapboxMap mapboxMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, "pk.eyJ1IjoiYWdlcmFjZS1nbG9iYW50IiwiYSI6ImNqOXlpbG00bTE0amczMmxnb2I0ZXdldDgifQ.easDK84eoiROS4TqgZUTSA");
        setContentView(R.layout.activity_main);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                mapboxMap.getLayers();
            }
        });

        //mapView2 = findViewById(R.id.mapView2);
        //mapView2.onCreate(savedInstanceState);
        //mapView2.getMapAsync(new OnMapReadyCallback() {
        //    @Override
        //    public void onMapReady(MapboxMap mapboxMap) {
         //       mapboxMap.getLayers();
         //   }
        //});

        Button button = findViewById(R.id.button);
        button.setOnClickListener(v -> {
           Intent i = new Intent(this, AdjustLayerOpacityActivity.class);
           startActivity(i);
        });
        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(v -> {
            Intent i = new Intent(this, OfflineManagerActivity.class);
            startActivity(i);
        });
        Button button3 = findViewById(R.id.button3);
        button3.setOnClickListener(v -> {
            Intent i = new Intent(this, SimpleOfflineMapActivity.class);
            startActivity(i);
        });
        textureView = findViewById(R.id.texture_view);
        try {
            mediaPlayer = new MediaPlayer();
            AssetFileDescriptor afd = this.getResources().openRawResourceFd(R.raw.water_small);
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getDeclaredLength());
            afd.close();
            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(mediaPlayer1 ->
                    textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
                        @Override
                        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                            scaleVideo(mediaPlayer1, textureView);
                            playLoopedVideo(surface, mediaPlayer1);
                        }

                        @Override
                        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {
                            // Nothing
                        }

                        @Override
                        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
                            return false;
                        }

                        @Override
                        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
                            // Nothing
                        }
                    }));
        } catch (IOException e) {

        }

    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
       // mapView2.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
       // mapView2.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
       // mapView2.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
        //mapView2.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
       // mapView2.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        //mapView2.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
        //mapView2.onSaveInstanceState(outState);
    }


    public static void playLoopedVideo(@NonNull SurfaceTexture surfaceTexture, @NonNull MediaPlayer mediaPlayer) {
        Surface surface = new Surface(surfaceTexture);
        mediaPlayer.setSurface(surface);
        surface.release();
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    public static void scaleVideo(@NonNull MediaPlayer mediaPlayer, @NonNull TextureView textureView) {
        View parent = (View) textureView.getParent();
        final int width = parent.getMeasuredWidth();
        final int height = parent.getMeasuredHeight();
        int videoWidth = mediaPlayer.getVideoWidth();
        int videoHeight = mediaPlayer.getVideoHeight();
        float widthScale = ((float) width) / ((float) videoWidth);
        float heightScale = ((float) height) / ((float) videoHeight);
        float scale = widthScale > heightScale ? widthScale : heightScale;
        ViewGroup.LayoutParams layoutParams = textureView.getLayoutParams();
        if (scale <= AnimationValues.ONE_FLOAT) {
            //Small screen
            layoutParams.width = Math.round(videoWidth / 2);
            layoutParams.height = Math.round(videoHeight / 2);
            textureView.setScaleX(scale + AnimationValues.ONE_FLOAT);
            textureView.setScaleY(scale + AnimationValues.ONE_FLOAT);
        } else {
            //Big screen
            layoutParams.width = videoWidth;
            layoutParams.height = videoHeight;
            textureView.setScaleX(scale);
            textureView.setScaleY(scale);
        }
        textureView.setLayoutParams(layoutParams);
    }

}
