package com.ankur.mcapp2.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ankur.mcapp2.R;
import com.ankur.mcapp2.domain.model.Song;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class ListFragment extends Fragment {

    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.tv_song_name)
    TextView tvSongName;

    Unbinder unbinder;
    @BindView(R.id.tv_play)
    TextView tvPlay;
    private ListAdapter listAdapter;
    private ArrayList<Song> mSongs;

    public static ListFragment newInstance(ArrayList<Song> songs) {
        Bundle args = new Bundle();
        args.putParcelableArrayList("Songs", songs);
        ListFragment fragment = new ListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        assert getArguments() != null;
        mSongs = getArguments().getParcelableArrayList("Songs");


        listAdapter = new ListAdapter(getContext());
        listAdapter.setSongs(mSongs);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvList.setLayoutManager(mLayoutManager);
        rvList.addItemDecoration(
                new DividerItemDecoration(Objects.requireNonNull(getContext()),
                        DividerItemDecoration.VERTICAL));
        rvList.setAdapter(listAdapter);

        Listener listener = new Listener() {
            @Override
            public void onItemClick(int index) {
                if (getActivity() instanceof MainActivity) {
                    tvSongName.setText(mSongs.get(index).getName());
                    ((MainActivity) getActivity()).playSong(mSongs.get(index).getData());
                }
            }
        };

        listAdapter.setListener(listener);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.tv_play)
    public void onTvPlayClicked() {
        if (getActivity() instanceof MainActivity) {
            tvSongName.setText("");
            ((MainActivity) getActivity()).stopSong();
        }
    }

    @OnClick(R.id.tv_internet)
    public void onViewClicked() {
        if (getActivity() instanceof MainActivity) {
            tvSongName.setText("Streaming Song. Please Wait..");
            ((MainActivity) getActivity()).streamSong();
        }
    }

    interface Listener {
        void onItemClick(int index);
    }
}
