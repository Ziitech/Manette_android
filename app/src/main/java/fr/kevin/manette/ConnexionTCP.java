package fr.kevin.manette;

import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.Socket;
import java.io.PrintWriter;

public class ConnexionTCP extends AsyncTask<String,Void,Void>
{
    private Socket socket;
    private PrintWriter writer;
    private String ip2;
    private Integer port;

    public ConnexionTCP(String i, String p){
        ip2 = i.toString();
        port = 6000;
       // port = new Integer(p.toString());

    }

    @Override
    protected Void doInBackground(String... voids) {

       Log.i("TEST","ADRESSE IP INT------------- : "+ip2);
       Log.i("TEST","PORT ------------- : "+port);

        try
        {
            String message = voids[0];
            socket = new Socket(ip2,port);
            writer = new PrintWriter(socket.getOutputStream());
            writer.write(message);
            writer.flush();
            writer.close();

        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }
}