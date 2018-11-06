package com.fao.fmd.fmdappbeta;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class ErrorFragment extends Fragment {

    View view;
    Bundle bundle;

    public ErrorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_error, container, false);

        bundle = this.getArguments();

        TextView text = view.findViewById(R.id.text);
        text.append(bundle.getString("res"));

        Button startBtn = view.findViewById(R.id.startBtn);
        Button animalBtn = view.findViewById(R.id.animalBtn);
        Button timelineBtn = view.findViewById(R.id.timelineBtn);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LesionAgeing.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        animalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AnimalCreation.class);
                startActivity(intent);
            }
        });
        timelineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Timeline in development",
                        Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }

}
