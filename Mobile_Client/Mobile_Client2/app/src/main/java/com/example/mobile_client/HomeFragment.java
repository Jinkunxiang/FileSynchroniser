package com.example.mobile_client;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 */
 public class HomeFragment extends Fragment {
 // public class HomeFragment extends Fragment{

    //initial all variables
    private  SwipeRefreshLayout refreshLayout;
    FileAdapter adapter;

    ListView listView;



    public HomeFragment() {
        // Required empty public constructor
    }


    public List<File_icon> fileList = new ArrayList<File_icon>();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String username = (String) MySharedPreferences.getuserName(getActivity());
        //SharedPreferences preferences = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        //initFile(username);
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        refreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh_layout);




        // refreshing home fragment
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                fileList.clear();
                //onItemsLoadComplete();
                adapter.notifyDataSetChanged();
                refreshLayout.setRefreshing(true);
                onUpdate();

                initFile(username);
            }

        });

        // post content
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                fileList.clear();
               // adapter.notifyDataSetChanged();

                refreshLayout.setRefreshing(false);
                //onItemsLoadComplete();
                //onUpdate();
                initFile(username);
            }
        });

       // FileAdapter
        adapter = new FileAdapter(getActivity(),R.layout.file_item,fileList);

        listView = (ListView)view.findViewById(R.id.home_list);
        listView.setAdapter(adapter);

        // file list view
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                    int number =listView.getAdapter().getCount();
                    System.out.println(number);
                    String nameFile = fileList.get(position).getName().trim();
                   // final String selected =  nameFile;
                    System.out.println(nameFile);
                    Intent intent = new Intent(getActivity(),Item_detail_Page.class);
                    intent.putExtra("fileName", nameFile);
                    startActivity(intent);

            }
        });

        return view;
    }


    // get the file list from server
    private void initFile(String username) {
            System.out.println("111111111111111111111111111111111");
            // set okhttp
            OkHttpClient okHttpClient = new OkHttpClient();

            Request request = new Request.Builder()
                    .get()
                    .url("http://teamparamount.cn:8080/Paramount/catalogroot?username=" + username)
                    .build();

            Call call = okHttpClient.newCall(request);

            System.out.println("22222222222");

            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.println("fail to connect");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {

                        System.out.println(String.valueOf(response.code()));


                        try {
                            JSONObject my = new JSONObject(response.body().string());
                            System.out.println(my.getString("status"));
                            System.out.println(my.getString("info"));

                            //Toast.makeText(Login_Page.this,my.getString("status"),Toast.LENGTH_SHORT).show();
                            String status1 = "success";
                            String status2 = "";
                            String rStatus1 = my.getString("status");
                            String rStatus2 = my.getString("info");

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (rStatus1.equals(status1)) {
                                        // parse file name from server
                                        String newString = rStatus2.replace("[", "");
                                        String newString1 = newString.replace("]", "");

                                        String[] strs = newString1.split(",");
                                        fileList.clear();
                                        for (int i = 0, len = strs.length; i < len; i++) {

                                            boolean pdf = strs[i].contains(".pdf");
                                            boolean docx = strs[i].contains(".dox");
                                            boolean doc = strs[i].contains(".doc");
                                            boolean rar = strs[i].contains(".rar");
                                            boolean zip = strs[i].contains(".zip");
                                            boolean ppt = strs[i].contains(".ppt");
                                            boolean pptx = strs[i].contains(".pptx");
                                            boolean png = strs[i].contains(".png");
                                            boolean jpg = strs[i].contains(".jpg");
                                            boolean excel = strs[i].contains(".xlsx");

                                            if(pdf){
                                                File_icon filePDF = new File_icon(strs[i], R.drawable.ic_pdf);
                                                fileList.add(filePDF);
                                            }else if(docx || doc){
                                                File_icon fileDOC = new File_icon(strs[i], R.drawable.ic_doc);
                                                fileList.add(fileDOC);
                                            }else if (rar){
                                                File_icon fileRAR = new File_icon(strs[i], R.drawable.ic_rar);
                                                fileList.add(fileRAR);

                                            }else if (zip){
                                                File_icon fileZIP = new File_icon(strs[i], R.drawable.ic_zip);
                                                fileList.add(fileZIP);

                                            }else if (ppt || pptx){
                                                File_icon filePPT = new File_icon(strs[i], R.drawable.ic_ppt);
                                                fileList.add(filePPT);

                                            }else if (png){
                                                File_icon filePNG = new File_icon(strs[i], R.drawable.ic_png);
                                                fileList.add(filePNG);

                                            }else if (jpg){
                                                File_icon fileJPG = new File_icon(strs[i], R.drawable.ic_jpg);
                                                fileList.add(fileJPG);

                                            }else if (excel){
                                                File_icon fileExcel = new File_icon(strs[i], R.drawable.ic_excel);
                                                fileList.add(fileExcel);

                                            }else
                                            {
                                                File_icon fileOther = new File_icon(strs[i], R.drawable.ic_filenew);
                                                fileList.add(fileOther);

                                            }
                                        }







                                        //System.out.println("file:" + rStatus2);


                                    } else {
                                        System.out.println("did not get json file");
                                    }

                                }
                            });

                        } catch (JSONException e) {
                            // Toast.makeText(Login_Page.this, "Login fail", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();

                        }


                    }


                }
            });

        //}
    }

    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // ...
        adapter.notifyDataSetChanged();
        // Stop refresh animation
        refreshLayout.setRefreshing(false);
    }

    public void onUpdate() {
        adapter = new FileAdapter(getActivity(),R.layout.file_item,fileList);
        listView.setAdapter(adapter);
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
    }


}




