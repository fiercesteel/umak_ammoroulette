package com.example.ammoroulette;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link menutab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class menutab extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public menutab() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment test.
     */
    // TODO: Rename and change types and number of parameters
    public static menutab newInstance(String param1, String param2) {
        menutab fragment = new menutab();
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

        View fragment = inflater.inflate(R.layout.fragment_menutab, container, false);

        //Setting onClickListener for Returning to the Game
        TextView txt = fragment.findViewById(R.id.returngame);
        txt.setOnClickListener(v -> {

            //Removing this Fragment
            getFragmentManager().beginTransaction()
                    .remove(this).commit();

            //Removing the Blur
            //FKBlurView view = getActivity().findViewById(R.id.blur);
            //view.setBlur(getActivity(), view, 0);

            //Putting back the Menu Button
//            ImageView menu = getActivity().findViewById(R.id.menu_button);
//            menu.setVisibility(View.VISIBLE);
        });

        //Setting onClickListener for showing Settings Tab
        TextView txt1 = fragment.findViewById(R.id.settings);
        txt1.setOnClickListener(v -> {

            //Removing this Fragment
            getFragmentManager().beginTransaction()
                    .remove(this).commit();

            //Showing the Settings Tab
            settingstab fragment1 = new settingstab();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            transaction.replace(R.id.fragment_container, fragment1);
            transaction.setReorderingAllowed(true);
            transaction.commit();
        });
        return fragment;

    }
}