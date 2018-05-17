package fr.kevin.manette;

import android.os.AsyncTask;
import java.io.IOException;
import java.net.Socket;
import java.io.PrintWriter;

public class ConnexionTCP extends AsyncTask<String,Void,Void>
{
    Socket socket;
    PrintWriter writer;

    @Override
    protected Void doInBackground(String... voids) {

        try
        {
            String message = voids[0];
            socket = new Socket("192.168.43.65",6000);
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