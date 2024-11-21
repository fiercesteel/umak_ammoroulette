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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link healthtab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class healthtab extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public healthtab() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment healthtab.
     */
    // TODO: Rename and change types and number of parameters
    public static healthtab newInstance(String param1, String param2) {
        healthtab fragment = new healthtab();
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
        View view = inflater.inflate(R.layout.fragment_healthtab, container, false);

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
        Bundle arguments = getArguments();
        String who_loses = arguments.getString("who_loses");
        String who_did = arguments.getString("who_did");
        int delay, delayfragment;

        //----------------------------------------------------------------------------------------------------------//
        //------------------------------------------------ ONCREATE ------------------------------------------------//
        //----------------------------------------------------------------------------------------------------------//

        //Initializing Components
        Handler handler = new Handler();
        ConstraintLayout hplayout = view.findViewById(R.id.hplayout);
        ConstraintLayout congratslayout = view.findViewById(R.id.congratslayout);
        TextView dealerlabel = view.findViewById(R.id.dealerlabel);
        TextView userlabel = view.findViewById(R.id.userlabel);
        TextView congratslabel = view.findViewById(R.id.congratslabel);
        LinearLayout dealerhp = view.findViewById(R.id.dealerhp);
        LinearLayout userhp = view.findViewById(R.id.userhp);
        int beep = soundPool.load(getContext(), R.raw.beep, 1);
        boolean inGame = true;

        //Putting username
        assert activity != null;
        userlabel.setText(activity.username);

        assert who_did != null;
        if(who_did.equals("show")){

            //Getting the level
            int level;
            level = activity.level;

            switch(level){
                case 1:
                    activity.dealerhp += 2;
                    activity.userhp += 2;
                    break;
                case 2:
                    activity.dealerhp += 4;
                    activity.userhp += 4;
                    break;
                case 3:
                    activity.dealerhp += 6;
                    activity.userhp += 6;
                    break;
            }

            //Setting up delay
            delay = 2000;
            delayfragment = 6500;
        } else {

            //Setting up delay
            delay = 0;
            delayfragment = 7000;
        }

        //Showing up hp
        handler.postDelayed(() -> {

            //Delay of showing the names
            hplayout.setVisibility(View.VISIBLE);
            dealerlabel.setVisibility(View.VISIBLE);
            userlabel.setVisibility(View.VISIBLE);
            dealerhp.setVisibility(View.VISIBLE);
            userhp.setVisibility(View.VISIBLE);

            //Setting up healthpoints for dealer
            handler.postDelayed(() -> {

                //Showing up healthpoints
                int i;
                if(activity.level != 3){

                    //Adding the healthpoints
                    i = 1;
                    while(i <= activity.dealerhp) {

                        //Initializing Imageview
                        ImageView imageView = new ImageView(activity.getApplicationContext());
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(120, ViewGroup.LayoutParams.MATCH_PARENT);
                        imageView.setLayoutParams(lp);
                        imageView.setId(i);
                        imageView.setImageResource(R.drawable.hp);
                        dealerhp.addView(imageView);
                        i++;
                    }
                } else{

                    //Adding the critical healthpoints
                    i = 1;
                    while(i <= 2) {

                        //Initializing Imageview
                        ImageView imageView = new ImageView(activity.getApplicationContext());
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(120, ViewGroup.LayoutParams.MATCH_PARENT);
                        imageView.setLayoutParams(lp);
                        imageView.setId(i);
                        imageView.setImageResource(R.drawable.hplast);
                        dealerhp.addView(imageView);
                        i++;
                    }

                    //Adding the healthpoints
                    while(i <= activity.dealerhp) {

                        //Initializing Imageview
                        ImageView imageView = new ImageView(activity.getApplicationContext());
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(120, ViewGroup.LayoutParams.MATCH_PARENT);
                        imageView.setLayoutParams(lp);
                        imageView.setId(i);
                        imageView.setImageResource(R.drawable.hp);
                        dealerhp.addView(imageView);
                        i++;
                    }
                }
            }, 2000);

            //Setting up healthpoints for user
            handler.postDelayed(() -> {

                //Delay of showing the names
                dealerlabel.setVisibility(View.VISIBLE);
                userlabel.setVisibility(View.VISIBLE);
                dealerhp.setVisibility(View.VISIBLE);
                userhp.setVisibility(View.VISIBLE);

                    //Showing up healthpoints
                    int i;
                    if(activity.level != 3){

                        //Adding the healthpoints
                        i = 1;
                        while(i <= activity.userhp) {

                            //Initializing Imageview
                            ImageView imageView = new ImageView(activity.getApplicationContext());
                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(120, ViewGroup.LayoutParams.MATCH_PARENT);
                            imageView.setLayoutParams(lp);
                            imageView.setId(i);
                            imageView.setImageResource(R.drawable.hp);
                            userhp.addView(imageView);
                            i++;
                        }
                    } else{

                        //Adding the critical healthpoints
                        i = 1;
                        while(i <= 2) {

                            //Initializing Imageview
                            ImageView imageView = new ImageView(activity.getApplicationContext());
                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(120, ViewGroup.LayoutParams.MATCH_PARENT);
                            imageView.setLayoutParams(lp);
                            imageView.setId(i);
                            imageView.setImageResource(R.drawable.hplast);
                            userhp.addView(imageView);
                            i++;
                        }

                        //Adding the healthpoints
                        while(i <= activity.userhp) {

                            //Initializing Imageview
                            ImageView imageView = new ImageView(activity.getApplicationContext());
                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(120, ViewGroup.LayoutParams.MATCH_PARENT);
                            imageView.setLayoutParams(lp);
                            imageView.setId(i);
                            imageView.setImageResource(R.drawable.hp);
                            userhp.addView(imageView);
                            i++;
                        }
                    }
            }, 2000);

            //Playing SFX
            handler.postDelayed(() -> {
                soundPool.play(beep, activity.volume, activity.volume, 0, 0, 1);
            }, 2000);
        }, delay);

        assert who_loses != null;
        if(who_loses.equals("dealer")){

            //Removing HP
            dealerhp.removeView(view.findViewById(R.id.dealerhp));
            activity.dealerhp--;
            if(activity.dealerhp == 0){

                inGame = false;
                handler.postDelayed(() -> {
                    congratslabel.setText(activity.username + " WINS!");
                    hplayout.setVisibility(View.GONE);
                    congratslayout.setVisibility(View.VISIBLE);
                    soundPool.play(beep, activity.volume, activity.volume, 0, 0, 1);

                    handler.postDelayed(() -> {
                        if(activity.level == 3){
                            //Removing this Fragment
                            requireFragmentManager().beginTransaction()
                                    .remove(this).commit();

                            activity.toTitle();
                        } else{

                            activity.userhp = 0;
                            activity.round = 1;
                            activity.liveround = 0;
                            activity.blankround = 0;
                            activity.magazine.clear();
                            activity.level++;

                            //Removing this Fragment
                            requireFragmentManager().beginTransaction()
                                    .remove(this).commit();

                            //Showing the Levels Tab
                            leveltab fragment = new leveltab();
                            FragmentManager manager = activity.getSupportFragmentManager();
                            manager.beginTransaction().replace(R.id.fragment_container, fragment)
                                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                    .setReorderingAllowed(true)
                                    .commit();
                        }
                    }, 2000);
                }, 2000);
            }
        } else if(who_loses.equals("user")){

            //Removing HP
            userhp.removeView(view.findViewById(R.id.userhp));
            activity.userhp--;
            if(activity.userhp == 0){

                inGame = false;
                handler.postDelayed(() -> {
                    congratslabel.setText(R.string.lose);
                    hplayout.setVisibility(View.GONE);
                    congratslayout.setVisibility(View.VISIBLE);
                    soundPool.play(beep, activity.volume, activity.volume, 0, 0, 1);

                    handler.postDelayed(() -> {

                        //Removing this Fragment
                        FragmentManager manager = activity.getSupportFragmentManager();
                        manager.beginTransaction()
                                .remove(this).commit();

                        activity.toTitle();
                    }, 3000);
                }, 2000);
            }
        }

        if(inGame){
            handler.postDelayed(() -> {

                //Removing this Fragment
                FragmentManager manager = activity.getSupportFragmentManager();
                manager.beginTransaction()
                        .remove(this).commit();

                handler.postDelayed(() -> {
                    switch(who_did){
                        case "show":
                            activity.loadGun();
                            break;
                        case "user":
                            activity.dealer_hover("turn");

                            if(activity.level != 1){

                                //Putting the inventory buttons back
                                activity.user.animate()
                                        .translationY(0).start();
                                activity.dealer.animate()
                                        .translationY(0).start();
                            }
                            break;
                        case "dealer":
                            activity.startAnimation();

                            if(activity.level != 1){

                                //Putting the inventory buttons back
                                activity.user.animate()
                                        .translationY(0).start();
                                activity.dealer.animate()
                                        .translationY(0).start();
                            }
                            break;
                        case "nextround":
                            activity.round++;
                            activity.loadGun();
                            break;
                    }
                }, 1000);

                soundPool.release();
                activity.shotgun.setImageResource(R.drawable.a_start);
            }, delayfragment);
        }

        return view;
    }
}