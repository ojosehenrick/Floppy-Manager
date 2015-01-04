package com.grarak.kerneladiutor.elements;

import android.app.Fragment;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.LightingColorFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.grarak.kerneladiutor.R;
import com.grarak.kerneladiutor.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by willi on 22.12.14.
 */
public class RecyclerViewFragment extends Fragment {

    protected View view;
    protected View backgroundView;
    protected LayoutInflater inflater;
    protected ViewGroup container;

    private ProgressBar progressBar;
    private final List<DAdapter.DView> views = new ArrayList<>();
    private RecyclerView recyclerView;
    private DAdapter.Adapter adapter;
    private Handler hand;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        this.inflater = inflater;
        this.container = container;

        recyclerView = getRecyclerView();
        recyclerView.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public int getItemCount() {
                return 0;
            }
        });
        setRecyclerView(recyclerView);
        int padding = getSidePadding();
        recyclerView.setPadding(padding, 0, padding, 0);

        progressBar = new ProgressBar(getActivity());
        setProgressBar(progressBar);

        if (isAdded()) new Task().execute(savedInstanceState);

        return view;
    }

    protected View getParentView(int layout) {
        return view != null ? view : (view = inflater.inflate(layout, container, false));
    }

    public RecyclerView getRecyclerView() {
        backgroundView = getParentView(R.layout.recyclerview_vertical).findViewById(R.id.background_view);
        return (RecyclerView) getParentView(R.layout.recyclerview_vertical).findViewById(R.id.recycler_view);
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setSmoothScrollbarEnabled(true);
        recyclerView.setLayoutManager(layoutManager);
    }

    public void setProgressBar(ProgressBar progressBar) {
        progressBar.getIndeterminateDrawable().setColorFilter(new LightingColorFilter(0xFF000000,
                getResources().getColor(android.R.color.white)));
        ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM, ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(progressBar, new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL | Gravity.END));
    }

    public void init(Bundle savedInstanceState) {
    }

    public void addView(DAdapter.DView view) {
        if (views.indexOf(view) < 0) {
            views.add(view);
            adapter.notifyDataSetChanged();
        }
    }

    public void removeView(DAdapter.DView view) {
        int position = views.indexOf(view);
        if (position > -1) {
            views.remove(view);
            adapter.notifyDataSetChanged();
        }
    }

    public void removeAllViews() {
        views.clear();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int padding = getSidePadding();
        recyclerView.setPadding(padding, 0, padding, 0);
    }

    private int getSidePadding() {
        if (backgroundView != null)
            if (Utils.getScreenOrientation(getActivity()) == Configuration.ORIENTATION_LANDSCAPE) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) backgroundView.getLayoutParams();
                params.height = getViewHeight();
                backgroundView.setLayoutParams(params);
                backgroundView.setVisibility(View.VISIBLE);

                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.top_to_bottom);
                animation.setDuration(1500);
                backgroundView.startAnimation(animation);
            } else backgroundView.setVisibility(View.GONE);

        double padding = getResources().getDisplayMetrics().widthPixels * 0.08361204013;
        return Utils.getScreenOrientation(getActivity()) == Configuration.ORIENTATION_LANDSCAPE ? (int) padding : 0;
    }

    public int getViewHeight() {
        TypedArray ta = getActivity().obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
        int actionBarSize = ta.getDimensionPixelSize(0, 100);
        int height = getResources().getDisplayMetrics().heightPixels;
        return height / 3 - actionBarSize;
    }

    private class Task extends AsyncTask<Bundle, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            hand = new Handler();
            views.clear();
            adapter = new DAdapter.Adapter(views);
        }

        @Override
        protected String doInBackground(final Bundle... params) {
            if (isAdded()) init(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            recyclerView.setAdapter(adapter);
            recyclerView.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.bottom_to_top));
            if (hand != null) hand.post(run);

            ((ViewGroup) progressBar.getParent()).removeView(progressBar);
        }

    }

    public Handler getHandler() {
        return hand;
    }

    public boolean onRefresh() {
        return false;
    }

    private final Runnable run = new Runnable() {
        @Override
        public void run() {
            if (isAdded()) {
                if (onRefresh()) {
                    if (hand != null) hand.postDelayed(run, 1000);
                } else if (hand != null) hand.removeCallbacks(run);
            } else if (hand != null) hand.postDelayed(run, 1000);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (hand != null) hand.removeCallbacks(run);
    }

}
