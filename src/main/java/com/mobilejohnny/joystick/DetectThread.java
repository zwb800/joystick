package com.mobilejohnny.joystick;

import com.mobilejohnny.joystick.serial.SerialPortUtils;

/**检测端口
 * Created by zwb on 2016/4/8.
 */
public class DetectThread implements Runnable {

    private static boolean exit = false;

    public static void exit()
    {
        exit = true;
    }

    @Override
    public void run() {
        try {
            while(true)
            {
                String[] ports = SerialPortUtils.getPorts();

                for (int i = 0; i < ports.length; i++) {
                    if(ports[i].equals("COM3"))
                    {
                        if(SerialPortUtils.connect("COM3"))
                        {
                            System.out.println("已连接端口");
                            exit();
                            break;
                        }
                    }
                }

                if(exit)
                {
                    break;
                }

                System.out.println("未发现端口");
                Thread.sleep(1000,0);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
