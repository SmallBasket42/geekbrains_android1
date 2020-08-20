package com.geekbrains.yourweather;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class ChooseCityFragment extends Fragment implements RVOnItemClick {

    TextView chooseCity;
    TextInputEditText enterCity;
    Button okEnterCity;
    static String currentCity = "";
    List<WeatherData> weekWeatherData = new ArrayList<>();
    final static String dataKey = "dataKey";
    final String myLog = "myLog";
    boolean isLandscape;
    private RecyclerView recyclerView;
    private CitiesRecyclerDataAdapter adapter;
    private ArrayList<String> citiesList = new ArrayList<>();
    ChooseCityActivityPresenter chooseCityActivityPresenter = ChooseCityActivityPresenter.getInstance();
    Pattern checkEnterCity = Pattern.compile("^[а-яА-ЯЁa-zA-Z]+(?:[\\s-][а-яА-ЯЁa-zA-Z]+)*$");
    private boolean isErrorShown;
    private static boolean citiesDeleted;

    static ChooseCityFragment create(CurrentDataContainer container) {
        ChooseCityFragment fragment = new ChooseCityFragment();    // создание
        Bundle args = new Bundle();
        args.putSerializable("currCity", container);
        fragment.setArguments(args);
        return fragment;
    }

    String getCurrentCity() {
        CurrentDataContainer currentDataContainer = (CurrentDataContainer) (Objects.requireNonNull(getArguments())
                .getSerializable("currCity"));
        try {
            return Objects.requireNonNull(currentDataContainer).currCityName;
        } catch (Exception e) {
            return "City";
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_choose_city, container, false);
        if(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar() != null) Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            updateWeatherData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        checkIsPositionLandscape();
        checkEnterCityField();
        takeCitiesListFromResources(getResources());
        setupRecyclerView();
        updateCitiesList();
        setOnBtnOkEnterCityClickListener();
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
        View.OnClickListener btnOkClickListener = view -> {
            enterCity.setEnabled(false);
            if(isErrorShown) {
                enterCity.setEnabled(true);
                Toast.makeText(Objects.requireNonNull(getActivity()), R.string.setOnBtnOkEnterCityToast, Toast.LENGTH_SHORT).show();
            }
            if(!isErrorShown) {
                enterCity.setEnabled(true);
                if (!Objects.requireNonNull(enterCity.getText()).toString().equals("")) {
                    currentCity = enterCity.getText().toString();
                    createRandomWeekWeatherData();
                    adapter.addNewCity(currentCity);
                    updateWeatherData();
                }
                enterCity.setText("");
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
            boolean[] switchSettingsArray = Objects.requireNonNull(ccc).switchSettingsArray;
            if (switchSettingsArray != null) {
                container.switchSettingsArray = switchSettingsArray;
                Log.d(myLog, "CHOOSE CITY FRAGMENT: getCurrentDataContainer(); !isLandscape; switchSettingsArray != null");
            }
            List<WeatherData> list =  Objects.requireNonNull(ccc).weekWeatherData;
            if(list.size() != 0) container.weekWeatherData = list;
            ArrayList<String> cities = ccc.citiesList;
            container.citiesList = this.citiesList;
        } else {
            CurrentDataContainer currentCityContainer = (CurrentDataContainer) Objects.requireNonNull(getArguments()).getSerializable("currCity");
            if (currentCityContainer != null) container.switchSettingsArray = currentCityContainer.switchSettingsArray;
            Log.d(myLog, "CHOOSE CITY FRAGMENT: getCurrentDataContainer(); else; currentCityContainer != null");
        }
        return container;
    }

    private void updateCitiesList() {
        if (!isLandscape) {
            Log.d(myLog, "ChooseCityFragment; updateCitiesList; !isLandscape");
            if(Objects.requireNonNull(getActivity()).getIntent().getSerializableExtra("currCity") != null) {
                CurrentDataContainer ccc = (CurrentDataContainer) Objects.requireNonNull(getActivity()).getIntent().getSerializableExtra("currCity");
                assert ccc != null;
                if(ccc.citiesList != null && ccc.citiesList.size() > 0) {
                    this.citiesList = ccc.citiesList;
                    Log.d(myLog, "ChooseCityFragment; updateCitiesList; citieslist = " + citiesList.toString());
                    adapter.refreshCitiesList(ccc.citiesList);
                }
                assert ccc.citiesList != null;
                if(ccc.citiesList.size() == 0 && citiesDeleted){
                    this.citiesList = ccc.citiesList;
                    adapter.refreshCitiesList(ccc.citiesList);
                }
            }
        } else {
            assert getArguments() != null;
            if (getArguments().getSerializable("currCity") != null) {
                CurrentDataContainer currentCityContainer = (CurrentDataContainer) Objects.requireNonNull(getArguments()).getSerializable("currCity");
                if (currentCityContainer != null && currentCityContainer.citiesList.size() > 0) {
                    this.citiesList = currentCityContainer.citiesList;
                    adapter.refreshCitiesList(currentCityContainer.citiesList);
                }
                assert currentCityContainer != null;
                if(currentCityContainer.citiesList.size() == 0 && citiesDeleted){
                    this.citiesList = currentCityContainer.citiesList;
                    adapter.refreshCitiesList(currentCityContainer.citiesList);
                }
            }
        }
    }

    public static void changeCitiesDeletedFlag(){citiesDeleted = true;}

    public void takeCitiesListFromResources(android.content.res.Resources resources){
        if (citiesList.size() == 0) {
            String[] cities = resources.getStringArray(R.array.cities);
            List<String> cit = Arrays.asList(cities);
            citiesList = new ArrayList<>(cit);
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

    private void checkEnterCityField() {
        final TextView[] tv = new TextView[1];
        enterCity.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                tv[0] = (TextView) v;
                validate(tv[0], checkEnterCity, getString(R.string.HintTextInputEditText));
                hideSoftKeyboard(Objects.requireNonNull(getActivity()), enterCity);
            }
        });
    }

    public static void hideSoftKeyboard (Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }

    private void validate(TextView tv, Pattern check, String message){
        String value = tv.getText().toString();
        if (check.matcher(value).matches()) {
            hideError(tv);
            isErrorShown = false;
        } else {
            showError(tv, message);
            isErrorShown = true;
        }
    }

    private void showError(TextView view, String message) {
        view.setError(message);
    }

    private void hideError(TextView view) {
        view.setError(null);
    }

}