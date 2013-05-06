package com.example.neartweetclient;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import android.os.AsyncTask;

public class ClientConnectorTask extends AsyncTask<String, Void, Integer> {

      
        private PrintWriter printwriter;
        Socket  client;


        protected Integer doInBackground(String...strings) {
                // validate input parameters
               

                // connect to the server and send the message
                try {

                        	client = new Socket("192.168.1.3", 4444);
                        
                        printwriter = new PrintWriter(client.getOutputStream(),true);
                        printwriter.write(strings[0]);
                        printwriter.flush();
                        printwriter.close();
                        client.close();
                        
                } catch (UnknownHostException e) {
                        e.printStackTrace();
                } catch (IOException e) {
                        e.printStackTrace();
                }
                return 0;
        }

        protected void onPostExecute(Long result) {
                return;
        }
}
