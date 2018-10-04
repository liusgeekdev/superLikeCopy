package com.lius.superlikecopy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sum.slike.SuperLikeLayout;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * @author: Chris Liu
 * @date: 2018/10/4  17:59
 */
public class RecyclerViewActivity extends AppCompatActivity{

    private SuperLikeLayout superLikeLayout;
    private RecyclerView recyclerView;
    private CommentAdapter commentAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recycler_view);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentAdapter = new CommentAdapter();
        commentAdapter.setAdapterItemListener(adapterItemListener);
        recyclerView.setAdapter(commentAdapter);

        superLikeLayout = findViewById(R.id.super_like_layout);
        superLikeLayout.setProvider(BitmapProviderFactory.getHDProvider(this));
    }

    CommentAdapter.AdapterItemListener<Boolean> adapterItemListener = new CommentAdapter.AdapterItemListener<Boolean>() {
        long duration = 1000;
        HashMap<Integer, Long> lastClickTimeMap = new LinkedHashMap<>();

        @Override
        public void onItemClickListener(Boolean isLike, int position, int id, View v) {
            Long lastClickTime = lastClickTimeMap.get(position);
            if(lastClickTime == null || System.currentTimeMillis() - lastClickTime> duration){ // 防抖
                isLike = !isLike;
                commentAdapter.updateLikeStatusByPosition(isLike, position);
                // 发起改变like状态的网络请求
            }
            lastClickTimeMap.put(position, System.currentTimeMillis());
            if(isLike){
                int[] itemPosition = new int[2];
                int[] superLikePosition = new int[2];
                v.getLocationOnScreen(itemPosition);
                superLikeLayout.getLocationOnScreen(superLikePosition);
                int x = itemPosition[0] + v.getWidth() / 2;
                int y = (itemPosition[1] - superLikePosition[1]) + v.getHeight() / 2;
                superLikeLayout.launch(x, y);
            }


        }
    };


}

