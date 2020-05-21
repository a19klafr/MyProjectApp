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
        TextView textInfo = findViewById(R.id.text_info);
        ImageView imageView = findViewById(R.id.imageView);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            String name = extras.getString("name");
            String location = extras.getString("location");
            String image = extras.getString("image");
            int riverlength = extras.getInt("length");
            String length = String.valueOf(riverlength);


            textName.setText(name);
            textInfo.setText("Floden " + name + " sträcker sig genom " + location + " och är "
                    + length + "km lång");
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
