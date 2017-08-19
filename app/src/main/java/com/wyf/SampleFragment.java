package com.wyf;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wyf.view.ViewUtils;
import com.wyf.view.annotation.BindContent;
import com.wyf.view.annotation.BindView;
import com.wyf.view.annotation.OnClick;
import com.wyf.view.annotation.OnLongClick;

/**
 * Created by wyf on 2017/8/19.
 */
@BindContent(R.layout.fragment_sample)
public class SampleFragment extends Fragment {

    @BindView(R.id.tv)
    private TextView tv;
    @BindView(R.id.rv)
    private RecyclerView rv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return ViewUtils.inject(this, inflater, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rv.setAdapter(new RvAdapter());
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @OnClick(R.id.btn)
    private void onClick(View v) {
        tv.append("onClick->");
    }

    @OnLongClick(R.id.btn)
    private boolean onLongClick(View v) {
        tv.append("onLongClick->");
        return true;
    }

    private class RvAdapter extends RecyclerView.Adapter<RvAdapter.RvHolder> {

        @Override
        public RvHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new RvHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item, parent, false));
        }

        @Override
        public void onBindViewHolder(RvHolder holder, int position) {
            holder.tv.setText("这是" + position);
        }

        @Override
        public int getItemCount() {
            return 10;
        }

        class RvHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.tv)
            TextView tv;

            public RvHolder(View itemView) {
                super(itemView);
                ViewUtils.inject(this, itemView);
            }
        }

    }
}
