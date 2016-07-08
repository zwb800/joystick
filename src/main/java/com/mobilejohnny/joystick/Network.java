package com.mobilejohnny.joystick;

import com.mobilejohnny.joystick.serial.SerialPortUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * 网络
 * Created by zwb on 2016/7/4.
 */
public class Network implements Runnable {

    private int port;
    private Socket connection;
    private static boolean exit = false;

    public Network(int port)
    {
        this.port = port;
    }

    public static void exit()
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
                socket.setSoTimeout(3000);

                while(!exit)
                {
                    try{
                        connection = socket.accept();
                        outputStream = connection.getOutputStream();

                        new Thread(tcp2uart).start();

                    }catch (SocketTimeoutException ex)
                    {
                        //System.out.println("accept超时");
                    }
                }

                socket.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private Runnable tcp2uart = () -> {
        byte[] buffer= new byte[1024];

        try {
            InputStream inputStream = connection.getInputStream();

            while(!exit)
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
                            SerialPort.reconnect();
                        }
                    }
                    else
                    {
                        System.out.println("serial port not found");
                        SerialPort.reconnect();
                    }

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    };
}
