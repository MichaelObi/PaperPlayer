package net.devdome.paperplayer.ui.fragment;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import net.devdome.paperplayer.R;
import net.devdome.paperplayer.adapter.PlaylistAdapter;

/**
 * Created by Michael on 7/4/2016.
 */

public class PlaylistFragment extends BottomSheetDialogFragment {

    private View contentView;

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        contentView = View.inflate(getActivity(), R.layout.bottomsheet_playlist, null);
        dialog.setContentView(contentView);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setState(BottomSheetBehavior.STATE_EXPANDED);
            ((BottomSheetBehavior) behavior).setPeekHeight(250);
        }

        setUpPlaylist();
    }

    private void setUpPlaylist() {
        RecyclerView rvPlaylist = (RecyclerView) contentView.findViewById(R.id.rv_playlist);
        rvPlaylist.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvPlaylist.setAdapter(new PlaylistAdapter());
    }
}
