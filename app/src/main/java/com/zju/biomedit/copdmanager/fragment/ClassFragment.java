package com.zju.biomedit.copdmanager.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zju.biomedit.copdmanager.R;
import com.zju.biomedit.copdmanager.adapter.KnoAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ClassFragment extends Fragment {

    @BindView(R.id.kno_tablayout)
    TabLayout knoTabLayout;
    @BindView(R.id.kno_viewpager)
    ViewPager knoViewPager;

    private ArrayList<String> titleList;
    private ArrayList<Fragment> fragmentList;
    private HealthKnoFragment healthKnoFragment;
    private DrugKnoFragment drugKnoFragment;
    private ExpertKnoFragment expertKnoFragment;
    private MessageFragment messageFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        fragmentList.add(new RecommendKnoFragment());
//        titleList.add("个性推荐");
        initFragement();
    }


    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_class, container, false);
        ButterKnife.bind(this,view);
        knoViewPager.setAdapter(new KnoAdapter(getChildFragmentManager(),titleList,fragmentList));
        knoViewPager.setOffscreenPageLimit(3);
        knoTabLayout.setupWithViewPager(knoViewPager);
        return view;

    }

    private void initFragement(){
        titleList = new ArrayList<>();
        fragmentList = new ArrayList<>();
        healthKnoFragment = new HealthKnoFragment();
        drugKnoFragment = new DrugKnoFragment();
        expertKnoFragment = new ExpertKnoFragment();
        messageFragment = new MessageFragment();
        fragmentList.add(expertKnoFragment);
        fragmentList.add(drugKnoFragment);
        fragmentList.add(healthKnoFragment);
        fragmentList.add(messageFragment);
        titleList.add("专家讲堂");
        titleList.add("用药教程");
        titleList.add("健康知识");
        titleList.add("推送消息");

    }



}
