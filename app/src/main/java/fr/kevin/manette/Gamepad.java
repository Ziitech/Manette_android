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
    private TextView textView1, textView2, textView3, textView5;
    private ImageButton button_a,button_b,button_x,button_y;
    private JoyStickClass js;


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

        js = new JoyStickClass(getApplicationContext()
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
                    if(direction == JoyStickClass.STICK_UP) {
                        textView5.setText("Direction : Up");
                        Connexion b1 = new Connexion();
                        b1.execute("up");
                    } else if(direction == JoyStickClass.STICK_UPRIGHT) {
                        textView5.setText("Direction : Up Right");
                        Connexion b1 = new Connexion();
                        b1.execute("up_right");
                    } else if(direction == JoyStickClass.STICK_RIGHT) {
                        textView5.setText("Direction : Right");
                        Connexion b1 = new Connexion();
                        b1.execute("right");
                    } else if(direction == JoyStickClass.STICK_DOWNRIGHT) {
                        textView5.setText("Direction : Down Right");
                        Connexion b1 = new Connexion();
                        b1.execute("down_right");
                    } else if(direction == JoyStickClass.STICK_DOWN) {
                        textView5.setText("Direction : Down");
                        Connexion b1 = new Connexion();
                        b1.execute("down");
                    } else if(direction == JoyStickClass.STICK_DOWNLEFT) {
                        textView5.setText("Direction : Down Left");
                        Connexion b1 = new Connexion();
                        b1.execute("down_left");
                    } else if(direction == JoyStickClass.STICK_LEFT) {
                        textView5.setText("Direction : Left");
                        Connexion b1 = new Connexion();
                        b1.execute("left");
                    } else if(direction == JoyStickClass.STICK_UPLEFT) {
                        textView5.setText("Direction : Up Left");
                        Connexion b1 = new Connexion();
                        b1.execute("up_left");
                    } else if(direction == JoyStickClass.STICK_NONE) {
                        textView5.setText("Direction : Center");
                        Connexion b1 = new Connexion();
                        b1.execute("center");
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
                    Connexion b1 = new Connexion();
                    b1.execute("a");
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    button_a.setBackground(getResources().getDrawable(R.drawable.ic_a));
                }
                return true;
            }
        });

        button_b.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    button_b.setBackground(getResources().getDrawable(R.drawable.ic_b_clic));
                    Connexion b1 = new Connexion();
                    b1.execute("b");
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    button_b.setBackground(getResources().getDrawable(R.drawable.ic_b));
                }
                return true;
            }
        });

        button_x.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    button_x.setBackground(getResources().getDrawable(R.drawable.ic_x_clic));
                    Connexion b1 = new Connexion();
                    b1.execute("x");
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    button_x.setBackground(getResources().getDrawable(R.drawable.ic_x));
                }
                return true;
            }
        });

        button_y.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    button_y.setBackground(getResources().getDrawable(R.drawable.ic_y_clic));
                    Connexion b1 = new Connexion();
                    b1.execute("y");
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    button_y.setBackground(getResources().getDrawable(R.drawable.ic_y));
                }
                return true;
            }
        });
    }
}
