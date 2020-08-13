package com.geekbrains.yourweather.fragments;

import android.annotation.SuppressLint;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.geekbrains.yourweather.CoatContainer;
import com.geekbrains.yourweather.R;
import java.util.Objects;

public class CoatOfArmsFragment extends Fragment {
    static CoatOfArmsFragment create(CoatContainer container) {
        CoatOfArmsFragment fragment = new CoatOfArmsFragment();

        Bundle args = new Bundle();
        args.putSerializable("index", container);
        fragment.setArguments(args);
        return fragment;
    }

    int getIndex() {
        CoatContainer coatContainer = (CoatContainer) (Objects.requireNonNull(getArguments())
                .getSerializable("index"));

        try {
            return coatContainer.position;
        } catch (Exception e) {
            return 0;
        }
    }

    String getCityName() {
        CoatContainer coatContainer = (CoatContainer) (Objects.requireNonNull(getArguments())
                .getSerializable("index"));

        try {
            return coatContainer.cityName;
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    @SuppressLint("Recycle")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        TextView cityNameTextView = new TextView(getContext());
        String cityName = getCityName();
        cityNameTextView.setText(cityName);
        ImageView coatOfArms = new ImageView(getActivity());
        TypedArray images = getResources().obtainTypedArray(R.array.coatofarms_imgs);
        coatOfArms.setImageResource(images.getResourceId(getIndex(), -1));
        layout.addView(cityNameTextView);
        layout.addView(coatOfArms);
        return layout;
    }
}
