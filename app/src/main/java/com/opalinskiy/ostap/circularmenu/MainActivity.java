package com.opalinskiy.ostap.circularmenu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private CircularViewLayout menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        menu = (CircularViewLayout) findViewById(R.id.layout);
        menu.setHandler(new CircularMenuHandler() {
            @Override
            public void onItemClick(int itemId) {
                switch (itemId) {
                    case 0:
                        Toast.makeText(MainActivity.this, R.string.bluetooth, Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(MainActivity.this, R.string.call_transfer, Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(MainActivity.this, R.string.mms, Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(MainActivity.this, R.string.cellular_network, Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        Toast.makeText(MainActivity.this, R.string.end_call, Toast.LENGTH_SHORT).show();
                        break;
                    case 5:
                        Toast.makeText(MainActivity.this, R.string.high_connection, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }
}
