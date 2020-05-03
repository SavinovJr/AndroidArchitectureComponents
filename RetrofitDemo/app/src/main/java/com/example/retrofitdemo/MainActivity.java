package com.example.retrofitdemo;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retrofitdemo.adapter.CountryAdapter;
import com.example.retrofitdemo.model.CountryInfo;
import com.example.retrofitdemo.model.Result;
import com.example.retrofitdemo.service.CountryService;
import com.example.retrofitdemo.service.RetrofitInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private List<Result> resultList;

    private CountryAdapter mAdapter;
    private RecyclerView mRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getCountries();
    }

    private List<Result> getCountries() {
        CountryService countryService = RetrofitInstance.getService();
        Call<CountryInfo> call = countryService.getResults();

        call.enqueue(new Callback<CountryInfo>() {
            @Override
            public void onResponse(Call<CountryInfo> call, Response<CountryInfo> response) {
                CountryInfo countryInfo = response.body();
                if (countryInfo != null && countryInfo.getRestResponse() != null) {
                    resultList = countryInfo.getRestResponse().getResult();
                    fillRecyclerView();
                }
            }

            @Override
            public void onFailure(Call<CountryInfo> call, Throwable t) {

            }
        });

        return resultList;
    }

    private void fillRecyclerView () {
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new CountryAdapter(resultList);
        mRecyclerView.setAdapter(mAdapter);
    }
}
