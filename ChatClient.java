import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.util.Scanner;
import java.net.Socket;
import java.net.InetAddress;


public class ChatClient {
    public static void main(String[] args) {
        // defining ip for server

        // String destIP = "0.tcp.ngrok.io";
        // int destPort = 17615;
        String destIP = "127.0.0.1"; // default ip
        int destPort = 8000; // default port

        // retrieving ip and port from command line args
        // args.length must have to be even
        // if args.length is odd, then it must contain just "--help"
        if((args.length&1) == 1) {
            if(args[0].equals("--help")) {
                // show help
                System.out.println("format: java ChatClient <option <option-parameter>>");
                System.out.println("options: --server <server-ip>     The sever ip to connect server [default: \"127.0.0.1\"]");
                System.out.println("         --port   <server-port>   The sever port to connect server [default: \"8000\"]");
            } else {
                System.out.println("error: address or port may not provided properly");
            }
            return;
        }

        // 'i' should jump by 2
        for(int i=0; i<args.length; i+=2) {
            if(args[i].equals("--port")) {
                destPort = Integer.parseInt(args[i+1]);
                System.out.println("port: " + destPort);
            } else if(args[i].equals("--server")) {
                destIP = args[i+1];
                System.out.println("ip: " + destIP);
            } else {
                System.out.println("error: illegal command.\n       use \"--help\" for help...");
                return;
            }
        }



        Scanner sc = new Scanner(System.in);
        PrintStream out = System.out;
        
        Socket socket = null;

        try {
            InetAddress destAddr = InetAddress.getByName(destIP);
            String localHost = "127.0.0.1";
            int localPort = 0; // let the os choose the port to select that's why 0
            InetAddress localAddr = InetAddress.getByName(localHost);

            out.println("creating client socket...");
            // socket = new Socket(destAddr, destPort, localAddr, localPort);
            socket = new Socket(destAddr, destPort);
            out.println("client socket is created...");
            out.println("");

            out.println("dest addr: " + socket.getInetAddress());
            out.println("dest port: " + socket.getPort());
            out.println("local addr: " + socket.getLocalAddress());
            out.println("local port: " + socket.getLocalPort());
            out.println("");

            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            OutputStream os = socket.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
            PrintWriter pw = new PrintWriter(bw);

            out.print("me :: ");
            String reply = sc.nextLine();
            pw.println(reply);
            pw.flush();
            while(!reply.equals("exit") && !reply.equals("quit")) {
                out.print("friend :: ");
                String message = br.readLine();
                out.println(message);
                if(message.equals("exit") || message.equals("quit")) {
                    Thread.sleep(1000);
                    break;
                }

                out.print("me :: ");
                reply = sc.nextLine();
                pw.println(reply);
                pw.flush();
            }
            out.println("");
            
            br.close();
            pw.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            try {
                out.println("closing socket...");
                socket.close();
                out.println("socket closed...");
            }
            catch(IOException e) {
                e.printStackTrace();
            }
        }
    }
}
