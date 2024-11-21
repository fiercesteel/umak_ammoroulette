package com.example.ammoroulette;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link waiver#newInstance} factory method to
 * create an instance of this fragment.
 */
public class waiver extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public waiver() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment waiver.
     */
    // TODO: Rename and change types and number of parameters
    public static waiver newInstance(String param1, String param2) {
        waiver fragment = new waiver();
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
        View view = inflater.inflate(R.layout.fragment_waiver, container, false);

        MainActivity activity = (MainActivity) getActivity();

        EditText edit = view.findViewById(R.id.edittext);
        Button done = view.findViewById(R.id.donebutton);

        //Getting the current filters
        InputFilter[] editFilters = edit.getFilters();
        InputFilter[] newFilters = new InputFilter[editFilters.length + 1];
        System.arraycopy(editFilters, 0, newFilters, 0, editFilters.length);

        //Setting up the new filters
        newFilters[editFilters.length] = new InputFilter.AllCaps();
        edit.setFilters(newFilters);

        //Text listener if the field is empty
        edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.equals("")){
                    done.setVisibility(View.VISIBLE);
                }
            }
        });

        done.setOnClickListener(v -> {

            if(!edit.getText().toString().isEmpty()){

                //Removing this Fragment
                FragmentManager manager = activity.getSupportFragmentManager();
                manager.beginTransaction()
                        .remove(this).commit();

                //Showing the Settings Tab
                leveltab fragment = new leveltab();
                manager.beginTransaction().replace(R.id.fragment_container, fragment)
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .setReorderingAllowed(true)
                        .commit();

                //Saving username
                assert activity != null;
                activity.username = edit.getText().toString();
            } else {
                done.setVisibility(View.GONE);
            }
        });

        return view;
    }
}