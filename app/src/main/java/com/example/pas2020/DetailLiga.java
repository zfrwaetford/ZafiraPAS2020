package com.example.pas2020;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import io.realm.Realm;
import io.realm.RealmConfiguration;
public class DetailLiga extends AppCompatActivity {
    Realm realm;
    RealmHelper realmHelper;
    ModelMovieRealm timModel;

    Bundle extras;
    String team;
    String liga;
    String deskripsi;
    String poster;
    int id;

    TextView tvjudul;
    ImageView ivposter;
    TextView tvdesc;
    Button btnbookmark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_liga);
        extras = getIntent().getExtras();
        tvjudul = (TextView)findViewById(R.id.tvjudul);
        tvdesc = (TextView)findViewById(R.id.txtdeskripsi);
        ivposter = (ImageView) findViewById(R.id.ivposter);
        btnbookmark = (Button) findViewById(R.id.btnbookmark);

        if (extras != null) {
            team = extras.getString("Team");
            id = extras.getInt("id");
            liga = extras.getString("liga");
            deskripsi = extras.getString("deskripsi");
            poster = extras.getString("poster");
            tvjudul.setText(team);
            tvdesc.setText(deskripsi);
            Glide.with(DetailLiga.this)
                    .load(poster)
                    .override(Target.SIZE_ORIGINAL)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(ivposter);
            // and get whatever type user account id is
        }else {
            Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
        }

        //Set up Realm
        Realm.init(DetailLiga.this);
        RealmConfiguration configuration = new RealmConfiguration.Builder().build();
        realm = Realm.getInstance(configuration);

        btnbookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timModel = new ModelMovieRealm();
                timModel.setstrDescriptionEN(deskripsi);
                timModel.setstrTeam(team);
                timModel.setstrLeague(liga);
                timModel.setstrTeamBadge(poster);

                realmHelper = new RealmHelper(realm);
                realmHelper.save(timModel);


            }
        });
    }
}
