package com.zju.biomedit.copdmanager.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.zju.biomedit.copdmanager.R;
import com.zju.biomedit.copdmanager.activity.MsgContentActivity;
import com.zju.biomedit.copdmanager.adapter.NewKnoAdapter;
import com.zju.biomedit.copdmanager.model.HealthKno;
import com.zju.biomedit.copdmanager.model.PatientUserManager;
import com.zju.biomedit.copdmanager.support.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class HealthKnoFragment extends Fragment {
    @BindView(R.id.recyclerView_kno)
    RecyclerView rvKno;
    private List<HealthKno> healthKnoList;
    private int mCurrentCounter;
    private int TOTAL_COUNTER;
    private int TOTAL_PAGES;
    private int PAGE_SIZE = 5;
    private int currentIndex;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_health_kno, container, false);
        ButterKnife.bind(this, view);
        getKnoData();

        return view;
    }

    @Override
    public void onResume() {//与activity周期同步
        super.onResume();
    }


    private void initRecyclerView() {
        rvKno.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        rvKno.setHasFixedSize(true);
        NewKnoAdapter newKnoAdapter = new NewKnoAdapter(R.layout.item_kno, healthKnoList);
        newKnoAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(HealthKnoFragment.this.getActivity(), MsgContentActivity.class);
                intent.putExtra("knoId", healthKnoList.get(position).getKnoId());
                HealthKnoFragment.this.getActivity().startActivity(intent);
            }
        });
//        mCurrentCounter = newKnoAdapter.getData().size();
//        HealthKnoAdapter mHealthKnoAdapter = new HealthKnoAdapter(this.getActivity(),healthKnoList);
        rvKno.setAdapter(newKnoAdapter);
        newKnoAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                rvKno.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (currentIndex > TOTAL_PAGES) {
                            //数据全部加载完毕
                            newKnoAdapter.loadMoreEnd();
                        } else {
                            //成功获取更多数据
                            String url = "http://120.27.141.50:8080/copd/message/getKnoListPaging?type=1&items_per_page=" + PAGE_SIZE + "&page_index=" + currentIndex + "&patientId=" + PatientUserManager.getInstance().getPatientId();
                            Ion.with(HealthKnoFragment.this.getActivity()).load(url).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {
                                    //dialog.dismiss();
                                    Log.d("wzykno", result + "");
                                    if (e != null) {
                                        Log.d("wzykno", "load kno error");
                                    } else {
                                        Gson gson = new Gson();
                                        List<HealthKno> knoList = new ArrayList<>();
                                        JsonArray jsonKno = null;
                                        try {
                                            jsonKno = result.get("result").getAsJsonObject().get("items").getAsJsonArray();
                                            knoList = gson.fromJson(jsonKno, new TypeToken<List<HealthKno>>() {
                                            }.getType());
                                            Log.d("wzyknonew", knoList + "");
                                            if (knoList != null && knoList.size() > 0) {
                                                newKnoAdapter.addData(knoList);
//                                                mCurrentCounter = newKnoAdapter.getData().size();
                                                currentIndex++;
                                                newKnoAdapter.loadMoreComplete();
                                            }
                                        } catch (Exception e1) {
                                            e1.printStackTrace();
                                        }
                                    }
                                }
                            });
                        }
                    }

                }, 0);
            }
        }, rvKno);
    }

    private void getKnoData() {
        healthKnoList = new ArrayList<>();
        currentIndex = 1;
//        String url = "http://120.27.141.50:8080/health-knowledge/COPDKnoService.jsp";
        String url = "http://120.27.141.50:8080/copd/message/getKnoListPaging?type=1&items_per_page=" + PAGE_SIZE + "&page_index=" + currentIndex + "&patientId=" + PatientUserManager.getInstance().getPatientId();

        MaterialDialog.Builder builder = new MaterialDialog.Builder(this.getActivity())
                .content(Utils.MESSAGE_GET_KNO_LIST)
                .progress(true, 0);
        MaterialDialog dialog = builder.build();
        //dialog.show();
        Ion.with(this.getActivity()).load(url).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                //dialog.dismiss();
                Log.d("wzykno", result + "");
                if (e != null) {
                    Log.d("wzykno", "load kno error");
                } else {
                    Gson gson = new Gson();
                    List<HealthKno> knoList = new ArrayList<>();
                    JsonArray jsonKno = null;
                    try {
                        TOTAL_COUNTER = result.getAsJsonObject("result").get("total_items").getAsInt();
                        TOTAL_PAGES = result.getAsJsonObject("result").get("total_pages").getAsInt();
                        jsonKno = result.get("result").getAsJsonObject().get("items").getAsJsonArray();
                        knoList = gson.fromJson(jsonKno, new TypeToken<List<HealthKno>>() {
                        }.getType());
                        Log.d("wzyknonew", knoList + "");
                        if (knoList != null && knoList.size() > 0) {
                            healthKnoList = knoList;
                            initRecyclerView();
                            currentIndex++;
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

    }
}
