package com.mobilejohnny.joystick;

import com.mobilejohnny.joystick.serial.SerialPortUtils;

import java.io.IOException;

/** 手柄遥控程序
 * Created by zwb on 2016/4/8.
 */
public class Main extends Base {

    public static void main(String[] args)
    {
        byte[] buffer = new byte[128];

        int tcpPort = 3840;
        new Thread(new Network(tcpPort)).start();
        try {
            while(true)
            {
                int len = System.in.read(buffer,0,buffer.length);
                if(len>0)
                {
                    String str = new String(buffer,0,len-1);
                    if(str.equals("exit"))
                    {
                        print("exting...");
                        SerialPortUtils.close();
                        break;
                    }
                    else if(str.startsWith("close"))
                    {
                        print("closing \n");
                        SerialPort.exit();
                        SerialPortUtils.close();
                    }
                    else if(str.startsWith("list"))
                    {
                        String[] ports = SerialPortUtils.getPorts();

                        for (int i = 0; i < ports.length; i++) {
                            print(ports[i]+"\n");
                        }
                    }
                    else if(str.startsWith("COM"))
                    {
                        print("connecting "+str+"\n");
                        SerialPort.connnect(str);
                    }

                    print("You enter:"+str+"\n");


                }
            }
        } catch (IOException e) {
//            e.printStackTrace();
        }


    }


}
