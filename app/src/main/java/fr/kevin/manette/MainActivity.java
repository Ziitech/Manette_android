package fr.kevin.manette;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

        public class MainActivity extends AppCompatActivity {

            ImageButton button_gamepad;
            ImageButton button_setting;

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);

                button_gamepad = (ImageButton) findViewById(R.id.bp_gamepad);
                button_setting = (ImageButton) findViewById(R.id.bp_setting);

                button_gamepad.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "Gamepad", Toast.LENGTH_LONG).show();
                        Intent y = new Intent(MainActivity.this, Gamepad.class);
                        startActivity(y);
                    }
                });

                button_setting.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "Setting", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(MainActivity.this, Setting.class);
                        startActivity(i);
                    }
                });

            }
        }
