package com.example.ammoroulette;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link settingstab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class settingstab extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public settingstab() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment test2.
     */
    // TODO: Rename and change types and number of parameters
    public static settingstab newInstance(String param1, String param2) {
        settingstab fragment = new settingstab();
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

        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        View fragment = inflater.inflate(R.layout.fragment_settingstab, container, false);
        title_page activity = (title_page) getActivity();
        Boolean title = false;

        SeekBar bg = fragment.findViewById(R.id.bgseekBar);
        TextView bgtxt = fragment.findViewById(R.id.bgpercent);
        bg.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                bgtxt.setText(progress+ "%");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        SeekBar sfx = fragment.findViewById(R.id.sfxseekBar);
        TextView sfxtxt = fragment.findViewById(R.id.sfxpercent);
        sfx.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sfxtxt.setText(progress+ "%");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        //Setting onClickListener for Returning to the Main Menu
        TextView txt = fragment.findViewById(R.id.returnbutton);
        txt.setOnClickListener(v -> {

            //Removing this Fragment
            FragmentManager manager = activity.getSupportFragmentManager();
            manager.beginTransaction()
                    .remove(this).commit();

            if(title){

                //Showing the Main Menu Tab
                menutab fragment1 = new menutab();
                manager.beginTransaction().replace(R.id.fragment_container, fragment1)
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .setReorderingAllowed(true)
                        .commit();
            }
        });
        return fragment;
    }
}