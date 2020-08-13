package com.geekbrains.yourweather.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.geekbrains.yourweather.CoatContainer;
import com.geekbrains.yourweather.CoatOfArmsActivity;
import com.geekbrains.yourweather.R;
import java.util.Objects;

public class CitiesFragment extends Fragment {
    private ListView listView;
    private TextView emptyTextView;

    private boolean isExistCoatOfArms;
    private int currentPosition = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initList();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        isExistCoatOfArms = getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;

        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt("CurrentCity", 0);
        }

        if (isExistCoatOfArms) {
            listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            showCoatOfArms();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("CurrentCity", currentPosition);
        super.onSaveInstanceState(outState);
    }

    private void initViews(View view) {
        listView = view.findViewById(R.id.cities_list_view);
        emptyTextView = view.findViewById(R.id.cities_list_empty_view);
    }

    private void initList() {

        ArrayAdapter adapter =
                ArrayAdapter.createFromResource(Objects.requireNonNull(getActivity()), R.array.cities,
                        android.R.layout.simple_list_item_activated_1);
        listView.setAdapter(adapter);

        listView.setEmptyView(emptyTextView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentPosition = position;
                showCoatOfArms();
            }
        });
    }

    private void showCoatOfArms() {
        if (isExistCoatOfArms) {
            listView.setItemChecked(currentPosition, true);
            CoatOfArmsFragment detail = (CoatOfArmsFragment)
                    Objects.requireNonNull(getFragmentManager()).findFragmentById(R.id.coat_of_arms);

            if (detail == null || detail.getIndex() != currentPosition) {

                detail = CoatOfArmsFragment.create(getCoatContainer());
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.coat_of_arms, detail);  // замена фрагмента
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.addToBackStack("Some_Key");
                ft.commit();
            }
        } else {
            Intent intent = new Intent();
            intent.setClass(Objects.requireNonNull(getActivity()), CoatOfArmsActivity.class);
            intent.putExtra("index", getCoatContainer());
            startActivity(intent);
        }
    }

    private CoatContainer getCoatContainer() {
        String[] cities = getResources().getStringArray(R.array.cities);
        CoatContainer container = new CoatContainer();
        container.position = currentPosition;
        container.cityName = cities[currentPosition];
        return container;
    }
}