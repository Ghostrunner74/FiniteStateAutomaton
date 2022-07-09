package com.example.finitestateautomaton;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;

public class MainActivity extends AppCompatActivity {
    private TableLayout bulb;
    private Boolean start = false;
    private Button button;
    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bulb = findViewById(R.id.bulb);
        button = findViewById(R.id.button);
    }


    public void onClickStart(View view) {
        if (!start) {
            button.setText("Stop");
            start = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(start) {
                    counter++;
                    switch (counter) {
                        case 1:
                        bulb.setBackgroundColor(Color.RED);
                        break;

                        case 2:
                        bulb.setBackgroundColor(Color.BLACK);
                        counter = 0;
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        }
        else {
            start = false;
            button.setText("Start");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        start = false;
    }
}