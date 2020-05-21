package com.example.myprojectapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        TextView textName = findViewById(R.id.text_name);
        TextView textLocation = findViewById(R.id.text_location);
        TextView textLength = findViewById(R.id.text_length);
        ImageView imageView = findViewById(R.id.imageView);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            String name = extras.getString("name");
            String location = extras.getString("location");
            int length = extras.getInt("length");
            String image = extras.getString("image");


            textName.setText(name);
            textLocation.setText(location);
            textLength.setText(String.valueOf(length));
            Picasso.get().load(image).into(imageView);
        }

        Button close = findViewById(R.id.close_details);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
