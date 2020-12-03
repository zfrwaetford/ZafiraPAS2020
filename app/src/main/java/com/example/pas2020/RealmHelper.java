package com.example.pas2020;

import android.util.Log;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class RealmHelper {

    Realm realm;

    public  RealmHelper(Realm realm){
        this.realm = realm;
    }

    // untuk menyimpan data
    public void save(final ModelMovieRealm timModel) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (realm != null) {
                    Log.e("Created", "Database was created");
                    Number currentIdNum = realm.where(ModelMovieRealm.class).max("idTeam");
                    int nextId;
                    if (currentIdNum==null) {
                        nextId = 1;
                    } else {
                        nextId = currentIdNum.intValue()+1;
                    }
                    timModel.setidTeam(nextId);
                    ModelMovieRealm modelRealm = realm.copyToRealm(timModel);
                } else {
                    Log.e("ppppp", "execute: Database not Exist");
                }
            }
        });
    }

    // untuk memanggil semua data
    public List<ModelMovieRealm> getAllTim() {
        RealmResults<ModelMovieRealm> results = realm.where(ModelMovieRealm.class).findAll();
        return results;
    }

    public void delete (Integer idTeam){
        final RealmResults<ModelMovieRealm> model = realm.where(ModelMovieRealm.class).equalTo("idTeam", idTeam).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                model.deleteFromRealm(0);
            }
        });
    }
}

