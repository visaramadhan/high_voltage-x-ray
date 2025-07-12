import com.fazecast.jSerialComm.*;

import java.util.Scanner;

public class SerialController {
    SerialPort serialPort;
    Scanner data;

    public String[] listAvailablePorts() {
        SerialPort[] ports = SerialPort.getCommPorts();
        String[] portNames = new String[ports.length];
        for (int i = 0; i < ports.length; i++) {
            portNames[i] = ports[i].getSystemPortName();
        }
        return portNames;
    }

    public boolean connect(String portName) {
        serialPort = SerialPort.getCommPort(portName);
        serialPort.setComPortParameters(9600, 8, 1, 0);
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);

        boolean opened = serialPort.openPort();
        if (opened) {
            data = new Scanner(serialPort.getInputStream());
        }
        return opened;
    }

    public void send(String cmd) {
        if (serialPort != null && serialPort.isOpen()) {
            try {
                serialPort.getOutputStream().write((cmd + "\n").getBytes(), 0, cmd.length() + 1);
                serialPort.getOutputStream().flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String read() {
        if (data != null && data.hasNextLine()) {
            return data.nextLine();
        }
        return "";
    }
}