package com.example.tapordie;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ActionBar;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
public class settingActivity extends AppCompatActivity {
    /** Used to actually adjust the volume */
    private AudioManager mAudioManager;
    /** Used to control the volume for a given stream type */
    private SeekBar mVolumeControls;
    /** True is the volume controls are showing, false otherwise */
    private boolean mShowingControls;
    private SeekBar seekBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        // Control the media volume
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        // Initialize the AudioManager
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        // Inflate the custom ActionBar View
        final View view = getLayoutInflater().inflate(R.layout.activity_main, null);
        mVolumeControls = (SeekBar) view.findViewById(android.R.id.progress);
        // Set the max range of the SeekBar to the max volume stream type
        mVolumeControls.setMax(mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        // Bind the OnSeekBarChangeListener
        mVolumeControls.setOnSeekBarChangeListener((SeekBar.OnSeekBarChangeListener) this);
        // Apply the custom View to the ActionBar
        getActionBar().setCustomView(view, new ActionBar.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        // Toggle the custom View's visibility
        mShowingControls = !mShowingControls;
        getActionBar().setDisplayShowCustomEnabled(mShowingControls);
        // Set the progress to the current volume level of the stream
        mVolumeControls.setProgress(mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
//
//        public void onProgressChanged (SeekBar seekBar, int progress, boolean fromUser) {
//            // Adjust the volume for the given stream type
//            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
//        }
    }
}