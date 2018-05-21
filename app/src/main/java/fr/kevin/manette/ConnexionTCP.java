package fr.kevin.manette;

import android.os.AsyncTask;
import java.io.IOException;
import java.net.Socket;
import java.io.PrintWriter;

public class ConnexionTCP extends AsyncTask<String,Void,Void>
{
    private Socket socket;
    private PrintWriter writer;
    private String ip;
    private Integer port;

    public ConnexionTCP(String i, String p){
        ip = i.toString();
        port = 6000;
       // port = new Integer(p.toString());

    }

    @Override
    protected Void doInBackground(String... voids) {

        try
        {
            String message = voids[0];
            socket = new Socket(ip,port);
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