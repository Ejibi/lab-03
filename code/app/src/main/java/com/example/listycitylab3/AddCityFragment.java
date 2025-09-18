package com.example.listycitylab3;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

public class AddCityFragment extends DialogFragment {
    private static final String ARG_CITY_TO_EDIT = "city_to_edit";
    private static final String ARG_CITY_POSITION = "city_position";

    private EditText editCityName;
    private EditText editProvinceName;
    private City cityToEdit;
    private int cityPosition = -1;


    public static AddCityFragment newInstance() {
        return new AddCityFragment();
    };

    public static AddCityFragment newInstance(City city, int position) {
        AddCityFragment CityFragment = new AddCityFragment();
        Bundle argBundle = new Bundle();
        argBundle.putSerializable(ARG_CITY_TO_EDIT, city);
        argBundle.putInt(ARG_CITY_POSITION, position);
        CityFragment.setArguments(argBundle);
        return CityFragment;
    };

    interface AddCityDialogListener {
        void addCity(City city);
        void editCity(City currentCity, int cityPosition);
    }


    private AddCityDialogListener listener;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddCityDialogListener) {
            listener = (AddCityDialogListener) context;
        } else {
            throw new RuntimeException(context + " must implement AddCityDialogListener");
        }
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_city, null);
        EditText editCityName = view.findViewById(R.id.edit_text_city_text);
        EditText editProvinceName = view.findViewById(R.id.edit_text_province_text);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        String dialogTitle = "add a city";
        String positiveButton = "add";
        Bundle args = getArguments();
        if (args != null) {
            cityToEdit = (City) args.getSerializable(ARG_CITY_TO_EDIT);
            cityPosition = args.getInt(ARG_CITY_POSITION, -1);
            if (cityToEdit != null) {
                editCityName.setText(cityToEdit.getName());
                editProvinceName.setText(cityToEdit.getProvince());
                dialogTitle = "Edit City";
                positiveButton = "Save";
            };
        };
        return builder
                .setView(view)
                .setTitle(dialogTitle)
                .setNegativeButton("Cancel", null)
                .setPositiveButton(positiveButton, (dialog, which) -> {
                    String cityName = editCityName.getText().toString();
                    String provinceName = editProvinceName.getText().toString();
                    if (cityName.isEmpty() || provinceName.isEmpty()) {
                        Toast.makeText(getContext(), "City name and province cannot be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    City currentCity = new City(cityName, provinceName);
                    if (cityToEdit != null && cityPosition != -1) {
                        listener.editCity(currentCity, cityPosition);
                    } else {
                        listener.addCity(currentCity);
                    }
                })
                .create();
    }
}
