package com.example.pas2020;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListData extends AppCompatActivity {


    TextView tvnodata;
    ProgressDialog dialog;
    RecyclerView recyclerView;
    DataAdapter adapter;
    ArrayList<Model> DataArrayList; //kit add kan ke adapter
    ImageView tambah_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_data);
        recyclerView = (RecyclerView) findViewById(R.id.rvdata);
        dialog = new ProgressDialog(ListData.this);
        tvnodata = (TextView) findViewById(R.id.tvnodata);
        tvnodata.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        addDataOnline();
    }

    void addDataOnline(){
        //Loading Screen
        dialog.setMessage("Processing Data");
        dialog.show();
        //Bacjground Process
        AndroidNetworking.get("https://www.thesportsdb.com/api/v1/json/1/search_all_teams.php?l=English%20Premier%20League")
                .setTag("test")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Do Anything With Response
                        Log.d("hasiljson","onResponse: " + response.toString());
                        DataArrayList = new ArrayList<>();
                        Model modelku;
                        try {
                            Log.d("hasiljson", "onResponse: " + response.toString());
                            JSONArray jsonArray = response.getJSONArray("teams");
                            Log.d("hasiljson2", "onResponse: " + jsonArray.toString());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                modelku = new Model();
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                modelku.setTeam_name(jsonObject.getString("strTeam"));
                                modelku.setAlternate_name(jsonObject.getString("strAlternate"));
                                modelku.setLeague(jsonObject.getString("strLeague"));
                                modelku.setStadium(jsonObject.getString("strStadium"));
                                modelku.setBadge_path(jsonObject.getString("strTeamBadge"));
                                modelku.setDescription(jsonObject.getString("strDescriptionEN"));
                                modelku.setStadium_location(jsonObject.getString("strStadiumLocation"));
                                DataArrayList.add(modelku);
                            }
                            //Handle Click
                            adapter = new DataAdapter(DataArrayList, new DataAdapter.Callback() {
                                @Override
                                public void onClick(int position) {
                                    Model team = DataArrayList.get(position);
                                    Intent intent = new Intent(getApplicationContext(), DetailMovie.class);
                                    intent.putExtra("id",team.id);
                                    intent.putExtra("team",team.strTeam);
                                    intent.putExtra("alternate",team.strAlternate);
                                    intent.putExtra("league",team.strLeague);
                                    intent.putExtra("stadium",team.strStadium);
                                    intent.putExtra("badge",team.strTeamBadge);
                                    intent.putExtra("description",team.strDescriptionEN);
                                    intent.putExtra("location",team.strStadiumLocation);
                                    startActivity(intent);
                                    Toast.makeText(ListData.this, ""+position, Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void test() {

                                }
                            });
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ListData.this);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(adapter);
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        //Handle Error
                        Log.d("errorku","onError errorCode : " + anError.getErrorCode());
                        Log.d("errorku","onError errorBody : " + anError.getErrorBody());
                        Log.d("errorku","onError errorDetail : " + anError.getErrorDetail());
                    }
                });
    }
}

