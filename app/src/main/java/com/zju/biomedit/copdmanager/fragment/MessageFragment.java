package com.zju.biomedit.copdmanager.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.zju.biomedit.copdmanager.R;
import com.zju.biomedit.copdmanager.adapter.MessageAdapter;
import com.zju.biomedit.copdmanager.model.Message;
import com.zju.biomedit.copdmanager.model.PatientUserManager;
import com.zju.biomedit.copdmanager.support.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment {
    @BindView(R.id.recyclerView_kno)
    RecyclerView rvKno;
    private List<Message> MessageList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_health_kno, container, false);
        ButterKnife.bind(this,view);
        getKnoData();

        return view;
    }


    private void initRecyclerView() {
        rvKno.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        rvKno.setHasFixedSize(true);
        MessageAdapter mMessageAdapter = new MessageAdapter(this.getActivity(),MessageList);
        rvKno.setAdapter(mMessageAdapter);

    }

    private void getKnoData() {
        MessageList = new ArrayList<>();
        String url = "http://120.27.141.50:8080/copd/message/getKnoList?type=0&patientId="+ PatientUserManager.getInstance().getPatientId();

        MaterialDialog.Builder builder = new MaterialDialog.Builder(this.getActivity())
                .content(Utils.MESSAGE_GET_KNO_LIST)
                .progress(true, 0);
        MaterialDialog dialog = builder.build();
        //dialog.show();
        Ion.with(this.getActivity()).load(url).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                //dialog.dismiss();
                Log.d("wzymessage", result+"");
                if (e != null) {
                    Log.d("wzymessage", "load message error");
                }else {
                    Gson gson = new Gson();
                    List<Message> knoList = new ArrayList<>();
                    try {
                        JsonArray jsonKno = result.get("result").getAsJsonObject().get("message").getAsJsonArray();
                        knoList = gson.fromJson(jsonKno, new TypeToken<List<Message>>() {
                        }.getType());
                        Log.d("wzymessage", knoList+"");
                        if (knoList != null && knoList.size() > 0) {
                            MessageList = knoList;
                            initRecyclerView();
                        }
                    } catch (JsonSyntaxException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

    }

}
