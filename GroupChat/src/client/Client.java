/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Junaed Younus Khan
 */
public class Client {

    /**
     * @param args the command line arguments
     */
    // Socket clientSocket;
    public static void main(String[] args) throws IOException {

        System.out.println("Enter Your Name Please :");
        Scanner sc = new Scanner(System.in);
        String name = sc.nextLine();

        Socket clientSocket = new Socket("localhost", 6789);

        DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
        DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());

        BufferedReader br = new BufferedReader(new InputStreamReader(dis));

        dos.writeBytes(name + '\n');
        Thread t = null;
        new Thread() {

            @Override
            public void run() {
                while (true) {

                    try {
                        Scanner sc = new Scanner(System.in);
                        String sentence = sc.nextLine();

                        if (sentence.equals("Logout")) {
                            dos.writeBytes("##left_msg" + name + " has left the group chat." + '\n');
                           clientSocket.close();
                            this.stop();
                            System.exit(0);
                            return;
                        }

                        // System.out.println(name + ": " + sentence);
                        dos.writeBytes(name + ": " + sentence + '\n');

                    } catch (IOException ex) {
                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

            }

        }.start();

        t = new Thread() {

            public void run() {
                while (true) {
                    try {
                       
                        String upperFromServer = upperFromServer = br.readLine();

                        if (upperFromServer.startsWith("##join_msg")) {

                            String server_msg = upperFromServer.substring("##join_msg".length());
                            System.out.println("Server Message:" + server_msg);

                        } else if (upperFromServer.startsWith("##left_msg")) {

                            String server_msg = upperFromServer.substring("##left_msg".length());
                            System.out.println("Server Message:" + server_msg);

                        } else {
                            System.out.println(upperFromServer);
                        }

                    } catch (IOException ex) {
                      System.exit(0);
                    }
                }

            }

        };
        t.start();

        /*  Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                try {

                    dos.writeBytes("##left_msg" + name + " has left the group chat." + '\n');

                    Thread.sleep(12000);
                    clientSocket.close();
                    System.exit(0);
                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });*/
    }

}
