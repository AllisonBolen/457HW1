//package proj1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Scanner;
import java.nio.file.Files;

public class server {
    public static void main(String args[]) {
        try {
            // scanner for reading in data
            Scanner scan = new Scanner(System.in);
            // port to use provided by server
            //int port = Integer.parseInt(getPort(scan));
            int port = 9999;
            // opens a channnel to communicate through
            DatagramChannel c = DatagramChannel.open();
            // think of as a set of channels - along with an associated operation, int eh set for reading or writing? meant help check multiple channels at a time.
            Selector s = Selector.open();
            // is there data available at this instance? if then return if not go past the line // block forever or not but the selector lets us block for a certain amount of time
            c.configureBlocking(false);
            // ??????
            c.register(s, SelectionKey.OP_READ);
            // lsiten on a port
            c.bind(new InetSocketAddress(port));
            // get the info as long as there is a thing open to read from
            while (true) {
                //check if there is data to read
                int n = s.select(5000);
                if (n == 0) {
                    // didnt get any packets
                    System.out.println("Got a timeout");
                } else { // got something
                    // ????????
                    Iterator i = s.selectedKeys().iterator();
                    // while the iterator had something in it?
                    while (i.hasNext()) {
                        // ????????
                        SelectionKey k = (SelectionKey) i.next();
                        // New channel to communicate with
                        DatagramChannel myc = (DatagramChannel) k.channel();
                        // allocate space for the data to be held in the buffer
                        ByteBuffer buffer = ByteBuffer.allocate(1028);
                        //ByteBuffer buffer2 = ByteBuffer.allocate(1028);
                        //ByteBuffer buffer3 = ByteBuffer.allocate(1028);
                        //ByteBuffer buffer4 = ByteBuffer.allocate(1028);
                        //ByteBuffer buffer5 = ByteBuffer.allocate(1028);
                        //takes the info received form the buffer and returns the data
                        SocketAddress clientaddr = c.receive(buffer);
                        //flip the buffer to restrict the buffer to the content
                        buffer.flip();
                        // the bytes of the message
                        byte[] msgBuffer = new byte[buffer.remaining()];
                        // get the data from the buffer to the msgbuffer
                        buffer.get(msgBuffer);
                        // convert byte buffer into a string just a new way to do it old can still work old way is better tho// only good for fresh buffers ^
                        String message = new String(msgBuffer);
                        // print the message to the console
                        System.out.println(message);
                        // fresh buffer to return info to the client with
                        ByteBuffer buf2 = ByteBuffer.allocate(1028);
                        // from the file present we send that byte array to teh buffer
                        buf2.put(filePresent(message));
                        //flip the buffer to restrict the buffer to the content, flip to read.
                        buf2.flip();
                        // get the test data from the buffer buff2
                        byte[] testData = new byte[buf2.remaining()];
                        // get the data from the buffer to the msgbuffer
                        buf2.get(testData);
                        // teh data in test data
                        String testData2 = new String(testData);
                        // print it to console
                        System.out.println(testData2);
                        // changes the shrunken space to the allocated space
                        buf2.rewind();
                        //sed it to the client
                        myc.send(buf2, clientaddr);
                        // me here ^
                        // remove the iterator
                        i.remove();
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Got an error: " + e);
        }
    }

    public static String getPort(Scanner scan) {
        System.out.println("Please enter a port to connect to");
        String info = scan.next();
        return info;
    }

    public static byte[] filePresent(String fileName) {
        File myFile = new File(fileName);
        byte[] bytesArray;
        if (myFile.exists() && !myFile.isDirectory()) {
            //String out = "";
            try {
                System.out.println("It exists");
                bytesArray = new byte[(int) myFile.length()];
                FileInputStream fis = new FileInputStream(myFile);
                fis.read(bytesArray); //read file into bytes[]
                fis.close();
                return bytesArray;
            } catch (IOException e) {
                System.out.println("error occured when reading file");
            }
        }
        System.out.println("it doesnt exist");
        bytesArray = new byte[(int) fileName.length()];
        String dne = "Does not exist";
        bytesArray = dne.getBytes();
        return bytesArray;
    }
}
