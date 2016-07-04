package com.mobilejohnny.joystick;

import com.mobilejohnny.joystick.serial.SerialPortUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 网络
 * Created by zwb on 2016/7/4.
 */
public class Network implements Runnable {

    private int port;
    private Socket connection;
    private boolean exit = false;

    public Network(int port)
    {
        this.port = port;
    }

    public void exit()
    {
        exit = true;
    }

    private static OutputStream outputStream;

    public static OutputStream getOutputStream() {
        return outputStream;
    }

    public void run()  {
        try {
                ServerSocket socket = new ServerSocket(port);

                while(!exit)
                {
                    connection = socket.accept();
                    outputStream = connection.getOutputStream();

                    new Thread(() -> {
                        byte[] buffer= new byte[1024];
                        try {
                            InputStream inputStream = connection.getInputStream();

                            while(true)
                            {
                                if(inputStream.available()>0)
                                {
                                    int len = inputStream.read(buffer);

                                    OutputStream outputStream = SerialPortUtils.getOutputStream();

                                    if(outputStream!=null)
                                    {
                                        try {
                                            outputStream.write(buffer,0,len);
                                        }catch (IOException ex)
                                        {
                                            System.out.println("serial write error");
//                                            SerialPort.reconnect();
                                        }
                                    }
                                    else
                                    {
                                        System.out.println("serial port not found");
//                                        SerialPort.reconnect();
                                    }

                                }
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }).start();
                }

                socket.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
