import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class Server {

	@SuppressWarnings("resource")
	public static void main(String[] args) {

		int port = 10086;
		ServerSocket serverSocket = null;
		try {

			Date d = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss :");
			String dateNowStr = sdf.format(d);
			serverSocket = new ServerSocket(port);
			System.out.println(dateNowStr + "Server had started!\n now listen" + port + "\n");
		} catch (Exception e) {
			e.printStackTrace();
		}

		while (true) {
			Socket socket=null;
			try {
				socket = serverSocket.accept();
				new Thread(new MyRunnable(socket)).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

class MyRunnable implements Runnable {
	Socket socket;
	String version = "1.17.5";

	MyRunnable(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {

		try {

			InputStream is;
			is = socket.getInputStream();
			InputStreamReader isr = new InputStreamReader(is, "utf-8");
			BufferedReader br = new BufferedReader(isr);

			String info = null;

			int psw = 0;

			Vector<String> v = new Vector<String>();
			Vector<String> v2 = new Vector<String>();
			while ((info = br.readLine()) != null) {

				System.out.print(info+"\t"); // because the data is too much to print!

				if (info.equals("send_" + version)) {
					psw = 1;
					Date d = new Date();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss : ");
					String dateNowStr = sdf.format(d);
					System.out.println("\n" + dateNowStr + "From client receive data!");
					continue;
				} else if (info.equals("receive_" + version)) {
					Date d = new Date();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss : ");
					String dateNowStr = sdf.format(d);
					System.out.println("\n" + dateNowStr + "send data to client!");
					psw = 2;
					continue;
				} else if (info.equals("today_" + version)) {
					psw = 3;
					continue;
				}

				else
					;

				if (psw == 1) {

					if (!info.contains("null")) {
						v.add(info);
					}
				} else if (psw == 2) {
					Date d = new Date();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss :");
					String dateNowStr = sdf.format(d);
					System.out.println(dateNowStr + "send data to client !");
				} else if (psw == 3) {// todayPrice
					v2.add(info);
				} else {
					Date d = new Date();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss :");
					String dateNowStr = sdf.format(d);
					System.out.println(dateNowStr + "data exception!");
					socket.shutdownInput();
				}
			}
			if (psw == 2 || psw == 1 || psw == 3)
				Sql.connect();
			if (psw != 2 && (psw == 1 || psw == 3)) {
				Sql.write(v);

				Sql.write_todayPrice(v2);
			}

			socket.shutdownInput();

			// tell client the data has been received
			OutputStream os = socket.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os, "utf-8");
			PrintWriter pw = new PrintWriter(osw);
			if (psw == 1 || psw == 3) {
				pw.print("success");
				pw.flush();
			} else if (psw == 2) {
				pw.println(Sql.read());
				pw.flush();
			} else {
				pw.println("Illegal visit! Contact the administrator18010472947");
				pw.flush();
			}
			br.close();
			isr.close();
			is.close();
		} catch (IOException e) {
			// e.printStackTrace();
			System.out.println("Illegal visit!");
			Thread.yield();
		}
	}
}
