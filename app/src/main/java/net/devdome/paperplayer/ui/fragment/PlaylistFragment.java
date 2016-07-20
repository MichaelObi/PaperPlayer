package net.devdome.paperplayer.ui.fragment;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import net.devdome.paperplayer.R;
import net.devdome.paperplayer.adapter.PlaylistAdapter;
import net.devdome.paperplayer.playback.QueueManager;

/**
 * Created by Michael on 7/4/2016.
 */

public class PlaylistFragment extends BottomSheetDialogFragment {

    private View contentView;

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
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
        final CoordinatorLayout.Behavior behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setState(BottomSheetBehavior.STATE_EXPANDED);
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);

        }

        Toolbar toolbar = (Toolbar) contentView.findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.playlist));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        setUpPlaylist();
    }

    private void setUpPlaylist() {
        RecyclerView rvPlaylist = (RecyclerView) contentView.findViewById(R.id.rv_playlist);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        int currentPlayingIndex = QueueManager.getInstance().getCurrentIndex();
        layoutManager.scrollToPositionWithOffset(currentPlayingIndex, 20);
        layoutManager.supportsPredictiveItemAnimations();
        rvPlaylist.setLayoutManager(layoutManager);
        rvPlaylist.setAdapter(new PlaylistAdapter());
    }
}
