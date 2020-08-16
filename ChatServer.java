import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.PrintStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.ServerSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class ChatServer {
    public static void main(String[] args) {
    	// getting server host ip and port input from console
    	String ip = "127.0.0.1"; // default
    	int port = 8000; // default
    	int backlog = 50; // default

    	// retrieving ip and port from command line args
    	// args.length must have to be even
    	// if args.length is odd, then it must contain just "--help"
    	if((args.length&1) == 1) {
    	    if(args[0].equals("--help")) {
    	        // show help
    	        System.out.println("format: java ChatServer <option <option-parameter>>");
    	        System.out.println("options: --host <host-ip>     The sever ip to host server [default: \"127.0.0.1\"]");
    	        System.out.println("         --port   <host-port>   The sever port to host server [default: \"8000\"]");
    	    } else {
    	        System.out.println("error: address or port may not provided properly");
    	    }
    	    return;
    	}

    	// 'i' should jump by 2
    	for(int i=0; i<args.length; i+=2) {
    	    if(args[i].equals("--port")) {
    	        port = Integer.parseInt(args[i+1]);
    	        System.out.println("port: " + port);
    	    } else if(args[i].equals("--host")) {
    	        ip = args[i+1];
    	        System.out.println("ip: " + ip);
    	    } else {
    	        System.out.println("error: illegal command.\n       use \"--help\" for help...");
    	        return;
    	    }
    	}

        Scanner sc = new Scanner(System.in);
        PrintStream out = System.out;

        Socket socket = null;
        ServerSocket serverSocket = null;

        try {
            
            InetAddress addr = InetAddress.getByName(ip);

            out.println("starting server...");
            serverSocket = new ServerSocket(port, backlog, addr);
            out.println("server has started...");
            out.println("inet addr: " + serverSocket.getInetAddress());
            out.println("port: " + serverSocket.getLocalPort());
            out.println("");

            out.println("Waiting for client...");
            socket = serverSocket.accept();
            out.println("accepted a client request...");
            out.println("");

            InetAddress clientAddr = socket.getInetAddress();
            int clientPort = socket.getPort();
            out.println("client addr: " + clientAddr);
            out.println("client port: " + clientPort);
            out.println("");

            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            OutputStream os = socket.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
            PrintWriter pw = new PrintWriter(bw);

            out.print("friend :: ");
            String message = br.readLine();
            while(true) {
                out.println(message);
                if(message.equals("exit") || message.equals("quit")) break;

                out.print("me :: ");
                String reply = sc.nextLine();
                pw.println(reply);
                pw.flush();
                if(reply.equals("exit") || reply.equals("quit")) {
                    Thread.sleep(1000);
                    break;
                }

                out.print("friend :: ");
                message = br.readLine();
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
                out.println("closing client socket...");
                socket.close();
                out.println("client socket closed...");
            }
            catch(IOException e) {
                e.printStackTrace();
            }
            try {
                out.println("closing server socket...");
                serverSocket.close();
                out.println("server socket closed...");
            }
            catch(IOException e) {
                e.printStackTrace();
            }
        }
    }
}
