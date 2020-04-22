package com.jonmpan.quiz;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class ContentMain extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
//        FragmentManager fragmentManager = getChildFragmentManager();
//        FragmentTransaction fragTrans = fragmentManager.beginTransaction();
//        HomeScreen homeScreen = new HomeScreen(); //my custom fragment
//        fragTrans.replace(android.R.id.content, homeScreen, "HOME_SCREEN");
//        fragTrans.addToBackStack(null);
//        fragTrans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//        fragTrans.commit();
//        HomeScreen homeScreen = (HomeScreen) fragmentManager.findFragmentById(R.id.homeScreen);
//        if(homeScreen != null){
//            Log.d("ContentMain", "Inside if");
//        } else {
//            Log.d("ContentMain", "Inside else");
//        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.content_main, container, false);
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
