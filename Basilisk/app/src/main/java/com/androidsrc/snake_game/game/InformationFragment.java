package com.androidsrc.snake_game.game;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidsrc.snake_game.R;

public class InformationFragment extends Fragment {

    private static GameActivity act;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        act = (GameActivity)context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = getActivity().getLayoutInflater().inflate(R.layout.activity_information_dialog, null);
        TextView infoContent = (TextView) rootView.findViewById(R.id.infoview);
        infoContent.setLinksClickable(true);
        infoContent.setAutoLinkMask(Linkify.EMAIL_ADDRESSES);
        return rootView;

    }
}
