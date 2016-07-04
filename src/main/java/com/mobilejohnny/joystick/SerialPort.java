package com.mobilejohnny.joystick;

import com.mobilejohnny.joystick.serial.SerialPortUtils;

import java.io.IOException;
import java.io.OutputStream;

/**检测端口
 * Created by zwb on 2016/4/8.
 */
public class SerialPort implements Runnable {

    private static boolean exit = false;


    public static void exit()
    {
        exit = true;
    }

    private static String port;

    public static void connnect(String port)
    {
        SerialPort.port = port;

        reconnect();
    }

    public static void reconnect()
    {
        new Thread(new SerialPort()).start();
    }

    public static void disconnect()
    {
        connnect(null);
        SerialPortUtils.close();
    }

    private SerialPortUtils.DataReadyEventListener dataReadyListener = (data, len) -> {
        OutputStream outputStream = Network.getOutputStream();
        if(outputStream!=null)
        {
            try {
                outputStream.write(data,0,len);
            } catch (IOException e) {
                System.out.println("network write error");
            }
        }
        else
        {
            System.out.println("network connection not found");
        }
    };

    @Override
    public void run() {
        try {
            exit = false;
            while(!exit)
            {
                String[] ports = SerialPortUtils.getPorts();

                for (int i = 0; i < ports.length; i++) {
                    if(ports[i].equals(port))
                    {
                        if(SerialPortUtils.connect(port,dataReadyListener))
                        {
                            System.out.println("已连接端口 "+port);
                            exit = true;
                            break;
                        }
                    }
                }

                Thread.sleep(1000,0);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
