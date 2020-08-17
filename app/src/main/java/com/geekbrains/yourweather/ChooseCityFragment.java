package com.geekbrains.yourweather;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ChooseCityFragment extends Fragment implements RVOnItemClick {

    TextView chooseCity;
    EditText enterCity;
    Button okEnterCity;
    static String currentCity = "";
    List<WeatherData> weekWeatherData = new ArrayList<>();
    final static String dataKey = "dataKey";
    final String myLog = "myLog";
    boolean isLandscape;
    private RecyclerView recyclerView;
    private CitiesRecyclerDataAdapter adapter;
    private List<String> citiesList = new ArrayList<>();
    ChooseCityActivityPresenter chooseCityActivityPresenter = ChooseCityActivityPresenter.getInstance();

    static ChooseCityFragment create(CurrentDataContainer container) {
        ChooseCityFragment fragment = new ChooseCityFragment();
        Bundle args = new Bundle();
        args.putSerializable("currCity", container);
        fragment.setArguments(args);
        return fragment;
    }

    String getCurrentCity() {
        CurrentDataContainer currentDataContainer = (CurrentDataContainer) (Objects.requireNonNull(getArguments())
                .getSerializable("currCity"));
        try {
            assert currentDataContainer != null;
            return currentDataContainer.currCityName;
        } catch (Exception e) {
            return "City";
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_choose_city, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        checkIsPositionLandscape();
        setOnBtnOkEnterCityClickListener();
        takeCitiesListFromResources(getResources());
        updateCitiesList();
        setupRecyclerView();
        showBackBtn();
    }

    private void checkIsPositionLandscape(){
        isLandscape = getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
    }

    private void initViews(View view) {
        chooseCity = view.findViewById(R.id.chooseYourCity);
        enterCity = view.findViewById(R.id.enterCity);
        okEnterCity = view.findViewById(R.id.okEnterCity);
        recyclerView = view.findViewById(R.id.cities);
    }

    private void setOnBtnOkEnterCityClickListener() {
        View.OnClickListener btnOkClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!enterCity.getText().toString().equals("")) {
                    currentCity = enterCity.getText().toString();
                    createRandomWeekWeatherData();
                    adapter.addNewCity(currentCity);
                    updateWeatherData();
                }
            }
        };
        okEnterCity.setOnClickListener(btnOkClickListener);
    }

    private void updateWeatherData(){
        if(isLandscape) {
            chooseCityActivityPresenter.updateWeatherInLandscape(getCurrentDataContainer(), Objects.requireNonNull(getFragmentManager()));
        } else {
            Intent intent = new Intent();
            intent.setClass(Objects.requireNonNull(getActivity()), MainActivity.class);
            intent.putExtra("currCity", getCurrentDataContainer());
            startActivity(intent);
            getActivity().finish();
        }
    }

    public CurrentDataContainer getCurrentDataContainer() {
        CurrentDataContainer container = new CurrentDataContainer();
        container.currCityName = currentCity;
        if (weekWeatherData.size() > 0 )container.weekWeatherData = weekWeatherData;
        container.citiesList = adapter.getCitiesList();
        if (!isLandscape) {
            CurrentDataContainer ccc = (CurrentDataContainer) Objects.requireNonNull(getActivity()).getIntent().getSerializableExtra("currCity");
            assert ccc != null;
            boolean[] switchSettingsArray = ccc.switchSettingsArray;
            if (switchSettingsArray != null) {
                container.switchSettingsArray = switchSettingsArray;
                Log.d(myLog, "CHOOSE CITY FRAGMENT: getCurrentDataContainer(); !isLandscape; switchSettingsArray != null");
            }
        } else {
            assert getArguments() != null;
            CurrentDataContainer currentCityContainer = (CurrentDataContainer) getArguments().getSerializable("currCity");
            if (currentCityContainer != null) container.switchSettingsArray = currentCityContainer.switchSettingsArray;
            Log.d(myLog, "CHOOSE CITY FRAGMENT: getCurrentDataContainer(); else; currentCityContainer != null");
        }
        return container;
    }

    private void updateCitiesList() {
        if (!isLandscape) {
            Log.d(myLog, "updateCitiesList; !isLandscape");
            CurrentDataContainer ccc = (CurrentDataContainer) Objects.requireNonNull(getActivity()).getIntent().getSerializableExtra("currCity");
            assert ccc != null;
            List<String> newCitiesList = ccc.citiesList;
            if (newCitiesList.size() > citiesList.size()) {
                Log.d(myLog, "updateCitiesList; !isLandscape; новый список боь");
                citiesList = newCitiesList;
            }
        } else {
            assert getArguments() != null;
            CurrentDataContainer currentCityContainer = (CurrentDataContainer) getArguments().getSerializable("currCity");
            if (currentCityContainer != null) {
                Log.d(myLog, "updateCitiesList; else; currentCityContainer != null ");
                List<String> newCitiesList = currentCityContainer.citiesList;
                if (newCitiesList.size() > citiesList.size()) {
                    Log.d(myLog, "updateCitiesList; else; новый список больше!");
                    citiesList = newCitiesList;
                }
            }
        }
    }

    public void takeCitiesListFromResources(android.content.res.Resources resources){
        if (citiesList.size() == 0) {
            String[] cities = resources.getStringArray(R.array.cities);
            citiesList = Arrays.asList(cities);
        }
    }

    @Override
    public void onItemClicked(String itemText) {
        currentCity = itemText;
        adapter.putChosenCityToTopInCitiesList(currentCity);
        createRandomWeekWeatherData();
        updateWeatherData();
    }

    private void createRandomWeekWeatherData(){
        for (int i = 0; i < 7; i++) {
            weekWeatherData.add(new WeatherData(getResources()));
        }
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(Objects.requireNonNull(getActivity()).getBaseContext());
        adapter = new CitiesRecyclerDataAdapter(citiesList, this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void showBackBtn() {
        if (isLandscape) {
            try {
                Objects.requireNonNull(Objects.requireNonNull(getActivity()).getActionBar()).setDisplayHomeAsUpEnabled(true);
            } catch (NullPointerException exc) {
                exc.printStackTrace();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            updateWeatherData();
        }
        return true;
    }

}