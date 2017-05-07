package com.mudo.halfcandydemo;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by MarvelY on 2017/1/7 15:33
 */

public class PageFragment extends Fragment {

    private int mPage;
    private String mTitle;

    private Fruit[] fruits = {new Fruit("Apple", R.mipmap.apple),
            new Fruit("banana", R.mipmap.banana),
            new Fruit("orange", R.mipmap.orange),
            new Fruit("watermelon", R.mipmap.watermelon),
            new Fruit("pear", R.mipmap.pear),
            new Fruit("grape", R.mipmap.grape),
            new Fruit("pineapple", R.mipmap.pineapple),
            new Fruit("strawberry", R.mipmap.strawberry),
            new Fruit("cherry", R.mipmap.cherry),
            new Fruit("mango", R.mipmap.mango)
    };

    private List<Fruit> fruitList = new ArrayList<>();
    private FruitAdapter adapter;

    public static PageFragment newInstance(int page, String title) {
        Bundle args = new Bundle();
        args.putInt("ARG_PAGE", page);
        args.putString("ARG_TITLE", title);
        PageFragment pageFragment = new PageFragment();
        pageFragment.setArguments(args);
        return pageFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt("ARG_PAGE");
        mTitle = getArguments().getString("ARG_TITLE");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_page, container, false);

        RecyclerView recycleview = (RecyclerView) rootView.findViewById(R.id.recycleview);

        initFruits();

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        recycleview.setLayoutManager(layoutManager);
        adapter = new FruitAdapter(fruitList);
        recycleview.setAdapter(adapter);
        return rootView;
    }

    private void initFruits() {
        fruitList.clear();
        for (int i = 0; i < 10; i++) {
            Random random = new Random();
            int index = random.nextInt(fruits.length);
            fruitList.add(fruits[index]);
        }
    }
}
