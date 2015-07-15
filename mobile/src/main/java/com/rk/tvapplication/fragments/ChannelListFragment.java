/**
 * MyTasksFragment.java
 * Created Date :: 06/12/2015
 * @Author :: Rakesh
 */
package com.rk.tvapplication.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rk.tvapplication.R;
import com.rk.tvapplication.adapters.ChannelsAdapter;
import com.rk.tvapplication.webservices.WebServiceHandler;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} class.
 * Use the {@link ChannelListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChannelListFragment extends Fragment {
    public  String TAG = ChannelListFragment.class.getCanonicalName();
    private WebServiceHandler webServiceHandler = null;

    // UI references.
    private  RecyclerView recyclerView;
    private  TextView  emptyView;
    private ChannelsAdapter adapter;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ChannelListFragment.
     */
    public static ChannelListFragment newInstance() {
        ChannelListFragment fragment = new ChannelListFragment();
        return fragment;
    }


    // Required empty public constructor
    public ChannelListFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webServiceHandler =  WebServiceHandler.getInstance();
        adapter = null;

        if(savedInstanceState==null)
         requestForTasks(0);
    }

    /**
     * This method requests to server
     * for get all MyTasks for
     * corresponding page no.
     * @param page_no page number to loa, initially it has to be zero.
     */
    private void requestForTasks(int page_no){
        if(emptyView!=null){
            emptyView.setText(R.string.no_records);
        }
            try {
                webServiceHandler =  WebServiceHandler.getInstance();
                webServiceHandler.init("", tasksListRequestHandler,WebServiceHandler.RequestType.GET, "");
            } catch (Exception e) {
                Log.i(TAG, "EXCEPTION requestToServer :: " + e.getMessage());
                e.printStackTrace();
            }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_channel_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        emptyView = (TextView) view.findViewById(R.id.empty_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        // actually VERTICAL is the default,
        // just remember: LinearLayoutManager
        // supports HORIZONTAL layout out of the box
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // you can set the first visible item like this:
        layoutManager.scrollToPosition(0);
        recyclerView.setLayoutManager(layoutManager);

        // allows for optimizations if all items are of the same size:
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        /*recyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                requestForTasks(current_page);
            }
        });*/
        if(adapter!=null) {
            recyclerView.setAdapter(adapter);
        }
    }

    /**
     * Callback listener for Web Services request for MyTasks
     */
    WebServiceHandler.WebServiceCallBackListener tasksListRequestHandler = new WebServiceHandler.WebServiceCallBackListener() {
        @Override
        public void onWebRequestDone(String response) {
            try{
                JSONObject responceData = XML.toJSONObject(response);
                System.out.println(responceData);
                /*JSONArray myTasks = new JSONArray();
                if(response!=null && !TextUtils.isEmpty(response)) {
                    myTasks = new JSONArray(response);
                }
                emptyView.setText(R.string.no_records);*/
                JSONObject jobj = responceData.getJSONObject("channels");
                List<JSONObject> items = getData(jobj.getJSONArray("channel"));
                if(adapter==null) {
                    adapter = new ChannelsAdapter(items);
                    adapter.registerAdapterDataObserver(observer);
                    recyclerView.setAdapter(adapter);
                }else{
                    adapter.addAll(items);
                }checkIfEmpty();
            }catch(Exception e){
                e.printStackTrace();
                Log.i(TAG, "Exception :: " + e.getMessage());
            }
        }
    };

    /**
     * This method is used for
     * get list of MyTasks
     * from augmented JsonArray
     * @return A list of MyTasks JsonObjects
     */
    private List<JSONObject> getData(JSONArray jsonArray){
        List<JSONObject> myTasks = new ArrayList<JSONObject>();
        try{
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jobj = jsonArray.getJSONObject(i);
                myTasks.add(jobj);
            }
        }catch (Exception e){ e.printStackTrace();}
        return myTasks;
    }

    final private RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            checkIfEmpty();
        }
    };

    void checkIfEmpty() {
        if (emptyView != null && adapter != null) {
            final boolean emptyViewVisible = adapter.getItemCount() == 0;
            emptyView.setVisibility(emptyViewVisible ? View.VISIBLE : View.GONE);
            recyclerView.setVisibility(emptyViewVisible ? View.GONE : View.VISIBLE);
        }
    }
}
