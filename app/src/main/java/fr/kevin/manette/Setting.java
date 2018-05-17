package fr.kevin.manette;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiManager;
import android.net.wifi.aware.WifiAwareManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.UUID;

public class Setting extends AppCompatActivity {

    private final String TAG = Setting.class.getSimpleName();

    //-------------------Bluetooth-------------------
    /**
     * Label qui affiche le statut
     */
    private TextView statut;
    /**
     * Image Bouton du bluetooth
     */
    private ImageButton bluetooth;
    /**
     * Bouton pour afficher les pair
     */
    private Button pair;
    /**
     * Bluetooth
     */
    private BluetoothAdapter bTAdapter;
    /**
     * Liste de pair
     */
    private Set<BluetoothDevice> pairDevices;
    /**
     * Liste
     */
    private ArrayAdapter<String> bTArrayAdapter;
    /**
     * Affichage de la liste des appareils
     */
    private ListView devicesListView;
    /**
     * Gestionnaire principal qui recevra des notifications de rappel
     */
    private Handler handler;
    /**
     * Bluetooth en tache de fond pour envoyer et recevoir des données
     */
    private ConnectedThread mConnectedThread;
    private BluetoothSocket mBTSocket = null;

    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // "random" unique identifiant


    //------------------- Wifi-------------------
    /**
     * Gestion du wifi
     */
    private WifiManager wifiManager;
    /**
     * Image Bouton du wifi
     */
    private ImageButton wifi;
    /**
     * Pour déterminer si le wifi et actif ou inacif
     */
    private Boolean wifiOnOff;
    /**
     *  Bouton pour ajouter une adresse ip
     */
    private Button addIP;
    /**
     * Zone de text Ip
     */
    private EditText ip;
    /**
     * Zone de text port
     */
    private EditText port;
    /**
     *  IP enregistré
     */
    private static String ip_text;
    /**
     *  port enregistré
     */
    private static String port_text;



    //#defines pour identifier les types partagés entre les fonctions d'appel
    private final static int MESSAGE_READ = 2; // utilisé dans le gestionnaire bluetooth pour identifier la mise à jour des messages
    private final static int CONNECTING_STATUS = 3; // utilisé dans le gestionnaire bluetooth pour identifier l'état du message


    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // composants et configuration bluetooth
        statut = (TextView) findViewById(R.id.bluetoothStatus);
        bluetooth = (ImageButton) findViewById(R.id.scan2);
        pair = (Button) findViewById(R.id.pairdBtn);

        bTArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        bTAdapter = BluetoothAdapter.getDefaultAdapter(); // contrôle de la radio Bluetooth

        // composants  et configuration wifi
        wifi = (ImageButton) findViewById(R.id.wifi);
        port = (EditText) findViewById(R.id.port);
        ip = (EditText) findViewById(R.id.ip);
        addIP = (Button) findViewById(R.id.buttonAdd);
        //listeIp = (ListView)findViewById(R.id.listeIP);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiOnOff=true;



        if (bTAdapter == null) {
            Log.i(TAG, "Bluetooth non supporté");
            Toast.makeText(getApplicationContext(), "Veuillez vous munir d'un téléphone avec un Bluetooth", Toast.LENGTH_SHORT).show();
            finish();
        }

        devicesListView = (ListView) findViewById(R.id.devicesListView);
        devicesListView.setAdapter(bTArrayAdapter); // assign model to view
        devicesListView.setOnItemClickListener(mDeviceClickListener);


        handler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == MESSAGE_READ) {
                    String readMessage = null;
                    try {
                        readMessage = new String((byte[]) msg.obj, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }

                if (msg.what == CONNECTING_STATUS) {
                    if (msg.arg1 == 1)
                        statut.setText("Connexion à l'appareil : " + (String) (msg.obj));
                    else
                        statut.setText("Connexion échoué");
                }
            }
        };

        // Action sur le bouton bluetooth
        bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothOnOff(v);
            }
        });

        // Action sur le bouton pair
        pair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listPairedDevices(v);
            }
        });

        // Action sur le bouton wifi
        wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wifiOnOff(v);
            }
        });

        // Action sur le bouton add
        addIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addip(v);
            }
        });
    }

    /**
     * Activation / Désactivation du wifi
     *
     * @param view v
     */
    private void wifiOnOff(View view) {
        if (wifiOnOff) {
            wifiManager.setWifiEnabled(false);
            Toast.makeText(getApplicationContext(), "Wifi désactivé", Toast.LENGTH_SHORT).show();
            wifi.setImageDrawable(getResources().getDrawable(R.drawable.ic_wifi_off));
            wifiOnOff=false;
        } else {
            wifiManager.setWifiEnabled(true);
            Toast.makeText(getApplicationContext(), "Wifi activé", Toast.LENGTH_SHORT).show();
            wifi.setImageDrawable(getResources().getDrawable(R.drawable.ic_wifi));
            wifiOnOff=true;

        }
    }

    /**
     * Ajout du port et de l'ip à la liste
     *
     * @param view v
     */
    private void addip(View view) {
        if (!ip.getText().toString().isEmpty() && !port.getText().toString().isEmpty()) {
            ip_text = ip.getText().toString();
            port_text = port.getText().toString();
            Toast.makeText(getApplicationContext(), "L'adress IP a été enregistré", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getApplicationContext(), "Veuillez selectionner une IP et un port", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Activation / Désactivation du bluetooth
     *
     * @param view v
     */
    private void bluetoothOnOff(View view) {
        if (!bTAdapter.isEnabled()) {
            bTAdapter.enable();
            statut.setText("Bluetooth activé");
            Toast.makeText(getApplicationContext(), "Bluetooth activé", Toast.LENGTH_SHORT).show();
            bluetooth.setImageDrawable(getResources().getDrawable(R.drawable.bluetooth_on));
        } else {
            bTAdapter.disable(); // désactivé
            statut.setText("Bluetooth désactivé");
            Toast.makeText(getApplicationContext(), "Bluetooth désactivé", Toast.LENGTH_SHORT).show();
            bluetooth.setImageDrawable(getResources().getDrawable(R.drawable.bluetooth_off));
        }
    }

    /**
     * Affichage des pairs jumelés
     *
     * @param view
     */
    private void listPairedDevices(View view) {
        pairDevices = bTAdapter.getBondedDevices();
        if (bTAdapter.isEnabled()) {
            for (BluetoothDevice device : pairDevices)
                bTArrayAdapter.add(device.getName() + "\n" + device.getAddress());

            Toast.makeText(getApplicationContext(), "Afficher les appareils jumelés", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(getApplicationContext(), "Bluetooth non activé", Toast.LENGTH_SHORT).show();
    }

    /**
     * Selection de l'appareil à connecter
     */
    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {

            if (!bTAdapter.isEnabled()) {
                Toast.makeText(getBaseContext(), "Bluetooth non activé", Toast.LENGTH_SHORT).show();
                return;
            }

            statut.setText("Connection...");
            // Récupération de l'adresse MAC de l'appareil
            String info = ((TextView) v).getText().toString();
            final String address = info.substring(info.length() - 17);
            final String name = info.substring(0, info.length() - 17);

            // Générer un nouveau thread pour éviter de bloquer l'interface graphique
            new Thread() {
                public void run() {
                    boolean fail = false;

                    BluetoothDevice device = bTAdapter.getRemoteDevice(address);

                    try {
                        mBTSocket = createBluetoothSocket(device);
                    } catch (IOException e) {
                        fail = true;
                        Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_SHORT).show();
                    }
                    // Etabli le Bluetooth socket connection.
                    try {
                        mBTSocket.connect();
                    } catch (IOException e) {
                        try {
                            fail = true;
                            mBTSocket.close();
                            handler.obtainMessage(CONNECTING_STATUS, -1, -1)
                                    .sendToTarget();
                        } catch (IOException e2) {
                            Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if (fail == false) {
                        mConnectedThread = new ConnectedThread(mBTSocket);
                        mConnectedThread.start();

                        handler.obtainMessage(CONNECTING_STATUS, 1, -1, name)
                                .sendToTarget();
                    }
                }
            }.start();
        }
    };

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        try {
            final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", UUID.class);
            return (BluetoothSocket) m.invoke(device, BTMODULEUUID);
        } catch (Exception e) {
            Log.e(TAG, "Impossible de créer une connexion RFComm sécurisée", e);
        }
        return device.createRfcommSocketToServiceRecord(BTMODULEUUID);
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Obtenez les flux d'entrée et de sortie, en utilisant des objets temporaires, car
            // les flux de membres sont définitifs
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            bTAdapter.cancelDiscovery(); // On annule la découverte des périphériques

            byte[] buffer = new byte[1024];  // mémoire tampon pour le flux
            int bytes; // octets renvoyés par read()
            // Continuez à écouter InputStream jusqu'à ce qu'une exception se produise
            while (true) {
                try {
                    // Lire depuis l'InputStream
                    bytes = mmInStream.available();
                    if (bytes != 0) {
                        buffer = new byte[1024];
                        SystemClock.sleep(100); //pause et attendez le reste des données. Ajustez ceci en fonction de votre vitesse d'envoi.
                        bytes = mmInStream.available(); // combien d'octets sont prêts à être lus?
                        bytes = mmInStream.read(buffer, 0, bytes); // enregistrer combien d'octets nous lisons réellement
                        handler.obtainMessage(MESSAGE_READ, bytes, -1, buffer).sendToTarget(); // Envoyer les octets obtenus à l'activité d'interface utilisateur

                    }
                } catch (IOException e) {
                    e.printStackTrace();

                    break;
                }
            }
        }

        /*Appelez ceci depuis l'activité principale pour envoyer des données à l'appareil distant */
        public void write(String input) {
            byte[] bytes = input.getBytes();           //convertit String en octets
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
            }
        }

        /* Appelez ceci depuis l'activité principale pour arrêter la connexion */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }

}













