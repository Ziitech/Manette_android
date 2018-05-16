package fr.kevin.manette;


import android.os.AsyncTask;
import java.io.IOException;
import java.net.Socket;
import java.io.PrintWriter;

public class ConnexionTCP extends AsyncTask<String,Void,Void>
{
    private Socket socket;
    private PrintWriter writter;

    @Override
    protected Void doInBackground(String... voids) {

        String message = voids[0];
        try
        {
            socket = new Socket("192.168.43.65",8284);
            writter = new PrintWriter(socket.getOutputStream());
            writter.write(message);
            writter.flush();
            writter.close();
            socket.close();

        }catch(IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}