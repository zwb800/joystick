package com.mobilejohnny.joystick.serial;

import gnu.io.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;

/** 串口
 * Created by zwb on 2016/4/8.
 */
public class SerialPortUtils {

    private static OutputStream outputStream;
    private static InputStream inputStream;
    private static SerialPort serialPort;

    public static String[] getPorts()
    {
        Enumeration ports = CommPortIdentifier.getPortIdentifiers();
        ArrayList<String> list = new ArrayList<>(1);
        while(ports.hasMoreElements())
        {
            CommPortIdentifier port = (CommPortIdentifier) ports.nextElement();

            if(port.getCurrentOwner()==null)
            {

            }

            System.out.println(port.getName()+" "+port.getCurrentOwner()+" "+port.getPortType());

            if(port.getPortType() == CommPortIdentifier.PORT_SERIAL)
            {
                list.add(port.getName());
            }
        }

        return list.toArray(new String[0]);
    }

    public static boolean connect(String portName)
    {
        boolean result = false;
        try {
            CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);

            if(portIdentifier.isCurrentlyOwned())
            {
                throw new PortInUseException();
            }
            else
            {
                int timeout = 2000;

                CommPort port = portIdentifier.open(SerialPortUtils.class.getName(),timeout);
                if(port instanceof SerialPort)
                {
                    serialPort = (SerialPort)port;
                    int baudRate = 115200;
                    int bit = SerialPort.DATABITS_8;
                    int stopBit = SerialPort.STOPBITS_1;
                    int parity = SerialPort.PARITY_NONE;//奇偶校验

                    serialPort.setSerialPortParams(baudRate,bit,stopBit,parity);
                    inputStream = serialPort.getInputStream();
                    outputStream = serialPort.getOutputStream();
                    result = true;

                }
                else
                {
                    System.out.println("Error:Only serial ports are handled");
                }
            }
        } catch (NoSuchPortException e) {
            e.printStackTrace();
            System.out.println("Error:Port not found or no permission");
        } catch (PortInUseException e) {
            System.out.println("Error:Port is currently in use");
        } catch (UnsupportedCommOperationException e) {
            System.out.println("Error:Wrong port settings");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static void close()
    {
        if(serialPort!=null) {
            serialPort.close();
        }
    }

    public static InputStream getInputStream() {
        return inputStream;
    }

    public static OutputStream getOutputStream() {
        return outputStream;
    }
}
