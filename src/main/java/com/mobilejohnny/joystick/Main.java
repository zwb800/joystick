package com.mobilejohnny.joystick;

import com.mobilejohnny.joystick.serial.SerialPortUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/** 手柄遥控程序
 * Created by zwb on 2016/4/8.
 */
public class Main extends Base {

    protected static Properties getProperties()
    {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("config.properties"));
        } catch (IOException e) {
            print("配置文件未找到");
        }

        return properties;
    }

    public static void main(String[] args)
    {
        Properties properties = getProperties();
        byte[] buffer = new byte[128];

        int tcpPort = 3840;
        new Thread(new Network(tcpPort)).start();
        SerialPort.connnect(properties.getProperty("port","COM3"));
        try {
            while(true)
            {
                int len = System.in.read(buffer,0,buffer.length);
                if(len>0)
                {
                    String str = new String(buffer,0,len-1);
                    if(str.startsWith("exit"))
                    {
                        SerialPort.exit();
                        SerialPortUtils.close();
                        Network.exit();
                        print("exting...");
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
                    }else
                    {
                        print("Unknow command:"+str+"\n");
                    }
                }
            }
        } catch (IOException e) {
            print("IO错误");
        }
    }
}
