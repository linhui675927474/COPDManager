package com.zju.biomedit.copdmanager.fragment;


import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.zju.biomedit.copdmanager.R;
import com.zju.biomedit.copdmanager.adapter.HealthVideoAdapter;
import com.zju.biomedit.copdmanager.model.HealthVideo;
import com.zju.biomedit.copdmanager.support.Utils;

import java.util.ArrayList;
import java.util.List;

import androidilearn.com.mobileusability.UserAgent;
import butterknife.BindView;
import butterknife.ButterKnife;
import fm.jiecao.jcvideoplayer_lib.JCUserAction;
import fm.jiecao.jcvideoplayer_lib.JCUserActionStandard;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

/**
 * A simple {@link Fragment} subclass.
 */
public class DrugKnoFragment extends Fragment {
    @BindView(R.id.recyclerView_kno)
    RecyclerView rvKno;
    private List<HealthVideo> healthVideoList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_expert_kno, container, false);
        ButterKnife.bind(this,view);
        JCVideoPlayer.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_NOSENSOR;
        getKnoData();
        return view;
    }

    private void initRecyclerView() {
        rvKno.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        rvKno.setHasFixedSize(true);
        HealthVideoAdapter mHealthVideoAdapter = new HealthVideoAdapter(this.getActivity(),healthVideoList);
        rvKno.setAdapter(mHealthVideoAdapter);
        JCVideoPlayer.setJcUserAction(new MyUserActionStandard());

    }

    @Override
    public void onPause() {
        super.onPause();
        //UserAgent.OnPause(this.getActivity().getApplicationContext(), Utils.P_CLASS_VIDEO);
        JCVideoPlayer.releaseAllVideos();
    }


    private void getKnoData() {
        healthVideoList = new ArrayList<>();
        String url = "http://120.27.141.50:8080/health-knowledge/GetCOPDVideoInfo.jsp?videoType=0";

        Ion.with(this.getActivity()).load(url).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                Log.d("wzykno", result+"");
                if (e != null) {
                    Log.d("wzyvideo", "load kno error");
                }else {
                    Gson gson = new Gson();
                    List<HealthVideo> videoList = new ArrayList<>();
                    JsonArray jsonKno = result.get("result").getAsJsonObject().get("video").getAsJsonArray();
                    videoList = gson.fromJson(jsonKno, new TypeToken<List<HealthVideo>>() {
                    }.getType());
                    Log.d("wzyvideo", videoList+"");
                    if (videoList != null && videoList.size() > 0) {
                        healthVideoList = videoList;
                        initRecyclerView();
                    }
                }
            }
        });

    }

    class MyUserActionStandard implements JCUserActionStandard {

        @Override
        public void onEvent(int type, String url, int screen, Object... objects) {
            switch (type) {
                case JCUserAction.ON_CLICK_START_ICON:
                    UserAgent.OnEvent(getActivity().getApplicationContext(), Utils.E_VIDEO_MEDICINE);
                    break;
            }
        }
    }


}
