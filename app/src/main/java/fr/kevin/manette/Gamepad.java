package fr.kevin.manette;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class Gamepad extends AppCompatActivity {

    private RelativeLayout layout_joystick;
    private TextView textView1, textView2, textView5;
    private ImageButton button_a,button_b,button_x,button_y;
    private JoyStick js;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // FullScreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_gamepad);

        textView1 = (TextView)findViewById(R.id.textView1);
        textView2 = (TextView)findViewById(R.id.textView2);
        textView5 = (TextView)findViewById(R.id.textView5);

        button_a = (ImageButton)findViewById(R.id.button_a);
        button_b = (ImageButton)findViewById(R.id.button_b);
        button_x = (ImageButton)findViewById(R.id.button_x);
        button_y = (ImageButton)findViewById(R.id.button_y);

        layout_joystick = (RelativeLayout)findViewById(R.id.layout_joystick);

        js = new JoyStick(getApplicationContext()
                , layout_joystick, R.drawable.image_button);
        js.setStickSize(150, 150);
        js.setLayoutSize(500, 500);
        js.setLayoutAlpha(150);
        js.setStickAlpha(100);
        js.setOffset(90);
        js.setMinimumDistance(50);

        layout_joystick.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                js.drawStick(arg1);
                if(arg1.getAction() == MotionEvent.ACTION_DOWN
                        || arg1.getAction() == MotionEvent.ACTION_MOVE) {
                    textView1.setText("X : " + String.valueOf(js.getX()));
                    textView2.setText("Y : " + String.valueOf(js.getY()));

                    int direction = js.get8Direction();
                    if(direction == JoyStick.STICK_UP) {
                        textView5.setText("Direction : Up");
                        ConnexionTCP b1 = new ConnexionTCP(getString(R.string.ip),getString(R.string.port));
                        b1.execute("u");
                    } else if(direction == JoyStick.STICK_UPRIGHT) {
                        textView5.setText("Direction : Up Right");
                        ConnexionTCP b1 = new ConnexionTCP(getString(R.string.ip),getString(R.string.port));
                        b1.execute("ur");
                    } else if(direction == JoyStick.STICK_RIGHT) {
                        textView5.setText("Direction : Right");
                        ConnexionTCP b1 = new ConnexionTCP(getString(R.string.ip),getString(R.string.port));
                        b1.execute("r");
                    } else if(direction == JoyStick.STICK_DOWNRIGHT) {
                        textView5.setText("Direction : Down Right");
                        ConnexionTCP b1 = new ConnexionTCP(getString(R.string.ip),getString(R.string.port));
                        b1.execute("dr");
                    } else if(direction == JoyStick.STICK_DOWN) {
                        textView5.setText("Direction : Down");
                        ConnexionTCP b1 = new ConnexionTCP(getString(R.string.ip),getString(R.string.port));
                        b1.execute("d");
                    } else if(direction == JoyStick.STICK_DOWNLEFT) {
                        textView5.setText("Direction : Down Left");
                        ConnexionTCP b1 = new ConnexionTCP(getString(R.string.ip),getString(R.string.port));
                        b1.execute("dl");
                    } else if(direction == JoyStick.STICK_LEFT) {
                        textView5.setText("Direction : Left");
                        ConnexionTCP b1 = new ConnexionTCP(getString(R.string.ip),getString(R.string.port));
                        b1.execute("l");
                    } else if(direction == JoyStick.STICK_UPLEFT) {
                        textView5.setText("Direction : Up Left");
                        ConnexionTCP b1 = new ConnexionTCP(getString(R.string.ip),getString(R.string.port));
                        b1.execute("ul");
                    } else if(direction == JoyStick.STICK_NONE) {
                        textView5.setText("Direction : Center");
                        ConnexionTCP b1 = new ConnexionTCP(getString(R.string.ip),getString(R.string.port));
                        b1.execute("c");
                    }
                } else if(arg1.getAction() == MotionEvent.ACTION_UP) {
                    textView1.setText("X :");
                    textView2.setText("Y :");
                    textView5.setText("Direction :");
                }
                return true;
            }
        });


        button_a.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    button_a.setBackground(getResources().getDrawable(R.drawable.ic_a_clic));
                    ConnexionTCP b2 = new ConnexionTCP(getString(R.string.ip),getString(R.string.port));
                    b2.execute("a");
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    button_a.setBackground(getResources().getDrawable(R.drawable.ic_a));

                    ConnexionTCP b2 = new ConnexionTCP(getString(R.string.ip),getString(R.string.port));
                    b2.execute("s");
                }
                return true;
            }
        });

        button_b.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    button_b.setBackground(getResources().getDrawable(R.drawable.ic_b_clic));
                    ConnexionTCP b3 = new ConnexionTCP(getString(R.string.ip),getString(R.string.port));
                    b3.execute("b");
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    button_b.setBackground(getResources().getDrawable(R.drawable.ic_b));

                    ConnexionTCP b3 = new ConnexionTCP(getString(R.string.ip),getString(R.string.port));
                    b3.execute("s");
                }
                return true;
            }
        });

        button_x.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    button_x.setBackground(getResources().getDrawable(R.drawable.ic_x_clic));
                    ConnexionTCP b4 = new ConnexionTCP(getString(R.string.ip),getString(R.string.port));
                    b4.execute("x");
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    button_x.setBackground(getResources().getDrawable(R.drawable.ic_x));

                    ConnexionTCP b4 = new ConnexionTCP(getString(R.string.ip),getString(R.string.port));
                    b4.execute("s");
                }
                return true;
            }
        });

        button_y.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    button_y.setBackground(getResources().getDrawable(R.drawable.ic_y_clic));
                    ConnexionTCP b5 = new ConnexionTCP(getString(R.string.ip),getString(R.string.port));
                    b5.execute("y");
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    button_y.setBackground(getResources().getDrawable(R.drawable.ic_y));

                    ConnexionTCP b5 = new ConnexionTCP(getString(R.string.ip),getString(R.string.port));
                    b5.execute("s");
                }
                return true;
            }
        });
    }
}
