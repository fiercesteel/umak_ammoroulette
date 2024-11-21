package com.example.ammoroulette;

import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link leveltab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class leveltab extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public leveltab() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment leveltab.
     */
    // TODO: Rename and change types and number of parameters
    public static leveltab newInstance(String param1, String param2) {
        leveltab fragment = new leveltab();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TransitionInflater inflater = TransitionInflater.from(requireContext());
        setEnterTransition(inflater.inflateTransition(R.transition.fade));
        setExitTransition(inflater.inflateTransition(R.transition.fade));

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leveltab, container, false);

        SoundPool soundPool;

        //Building SoundPool for SFX
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(3)
                    .setAudioAttributes(audioAttributes)
                    .build();
        }
        else {
            soundPool = new SoundPool.Builder().build();
        }

        MainActivity activity = (MainActivity) getActivity();

        int sfx = soundPool.load(getContext(), R.raw.level, 1);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                assert activity != null;
                soundPool.play(sfx, activity.volume, activity.volume, 0, -1, 1);
            }
        });

        //----------------------------------------------------------------------------------------------------------//
        //------------------------------------------------ ONCREATE ------------------------------------------------//
        //----------------------------------------------------------------------------------------------------------//

        int level;
        assert activity != null;
        level = activity.level;

        ImageView level1 = view.findViewById(R.id.level1);
        ImageView level2 = view.findViewById(R.id.level2);
        ImageView level3 = view.findViewById(R.id.level3);

        Animation animation = new AlphaAnimation(1, 0);
        animation.setDuration(1000);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);
        switch (level){
            case 1:
                level1.setImageResource(R.drawable.level);
                level1.startAnimation(animation);
                break;
            case 2:
                level2.setImageResource(R.drawable.level);
                level2.startAnimation(animation);
                break;
            case 3:
                level3.setImageResource(R.drawable.level);
                level3.startAnimation(animation);
                break;
        }

        Handler handler = new Handler();
        handler.postDelayed(() -> {

            //Removing this Fragment
            FragmentManager manager = activity.getSupportFragmentManager();
            manager.beginTransaction()
                    .remove(this).commit();

            //Passing action to fragment
            healthtab fragment = new healthtab();
            Bundle arguments = new Bundle();
            arguments.putString("who_did", "show");
            arguments.putString("who_loses", "");
            fragment.setArguments(arguments);

            //Showing the Health Monitor Tab
            manager.beginTransaction().replace(R.id.fragment_container, fragment)
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .setReorderingAllowed(true)
                    .commit();

            soundPool.stop(sfx);
            soundPool.release();
        }, 3000);

        return view;
    }
}