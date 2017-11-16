package com.example.felipetorres.mapboxspike;

import android.content.res.AssetFileDescriptor;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.RasterLayer;
import com.mapbox.mapboxsdk.style.sources.RasterSource;

import java.io.IOException;

import timber.log.Timber;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.rasterOpacity;

/**
 * Use a seekbar to adjust the opacity of a raster layer on top of a map.
 */
public class AdjustLayerOpacityActivity extends AppCompatActivity {

    private MapView mapView;
    private MapboxMap map;
    private Layer chicago;
    private MediaPlayer mediaPlayer;
    private TextureView textureView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.
        Mapbox.getInstance(this, "pk.eyJ1IjoiY2FybG9zZmVsaXBldG9ycmVzIiwiYSI6ImNqYTFlZWdxdDk4dzMzM3M0aTR6dWg0NHkifQ.0VwtOZURToVK-F6SXAIbbA");

        // This contains the MapView in XML and needs to be called after the access token is configured.
        setContentView(R.layout.activity_adjust_layer_opacity);

        final SeekBar opacitySeekBar = (SeekBar) findViewById(R.id.seek_bar_layer_opacity);
        final TextView percentTextView = (TextView) findViewById(R.id.textview_opacity_value);

        opacitySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (chicago != null) {
                    chicago.setProperties(
                            rasterOpacity((float) progress / 100)
                    );
                }
                String progressPrecentage = progress + "%";
                percentTextView.setText(progressPrecentage);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                map = mapboxMap;

                RasterSource chicagoSource = new RasterSource("chicago-source", "mapbox://styles/carlosfelipetorres/cja1l8rtwahxg2smvg9ssn2k3");
                map.addSource(chicagoSource);

                RasterLayer chicagoLayer = new RasterLayer("chicago", "chicago-source");
                map.addLayer(chicagoLayer);

                chicago = map.getLayer("chicago");

            }
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
            Timber.e(e);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
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
