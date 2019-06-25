import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.net.*;

public class Main {

	static int tupleSize = 8;
	static ProfilingTable profilingTable;

	public static ArrayList<Executer> ScheduleExecuters(ArrayList<Slot> slots, int ExecuterNumber) {
		ArrayList<Executer> executers = new ArrayList<Executer>();
		for (int i = 0; i < slots.size(); i++) {
			executers.add(new Executer(slots.get(i)));
		}
		return executers;
	}

	public static void ScheduleTasks(ArrayList<Executer> executers, ArrayList<Task> tasks, ProfilingTable table) {
		for (int i = 0; i < tasks.size(); i++) {
			tasks.get(i).setExecuter(executers.get(i % executers.size()));
		}
	}

	public static void sendData(int port, String msg) {
		try {
			Socket socket = new Socket("127.0.0.1", port);
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
			out.println(msg);
			socket.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<Task> buildTopology(ArrayList<Thread> threads) {
		ArrayList<Task> tasks = new ArrayList<Task>();
		BlockingQueue<Tuple> queue1 = new LinkedBlockingDeque<Tuple>();
		Task parser = new Parser(queue1);
		parser.setInputRate(5); // tuples per second
		Thread T1 = new Thread(parser);
		tasks.add(parser);
		threads.add(T1);
		BlockingQueue<Tuple> queue2 = new LinkedBlockingDeque<Tuple>();
		BlockingQueue<Tuple> queue3 = new LinkedBlockingDeque<>();
		BlockingQueue<Tuple>[] stream1 = new BlockingQueue[2];
		stream1[0] = queue2;
		stream1[1] = queue3;
		Thread grouping1 = new Thread(new KeyGrouping(queue1, stream1));
		Thread T2 = new Thread(grouping1);
		threads.add(T2);
		BlockingQueue<Tuple> queue4 = new LinkedBlockingDeque<>();
		Task Counter1 = new Counter(queue2, queue4);
		Thread T3 = new Thread(Counter1);
		threads.add(T3);
		tasks.add(Counter1);
		BlockingQueue<Tuple> queue5 = new LinkedBlockingDeque<>();
		Task Counter2 = new Counter(queue3, queue5);
		Thread T4 = new Thread(Counter2);
		tasks.add(Counter2);
		threads.add(T4);
		BlockingQueue<Tuple>[] stream2 = new BlockingQueue[2];
		stream2[0] = queue4;
		stream2[1] = queue5;
		BlockingQueue<Tuple> queue6 = new LinkedBlockingDeque<>();
		Task accumulator = new Accumulator(stream2, queue6);
		Thread T5 = new Thread(accumulator);
		threads.add(T5);
		tasks.add(accumulator);
		return tasks;
	}

	public static ArrayList<Machine> buildCluster() {
		ArrayList<Executer> executers = new ArrayList<Executer>();
		Machine.Type CPUType = Machine.Type.MIPS;
		Machine machine1 = new Machine(CPUType, 4);
		ArrayList<Machine> machines = new ArrayList<Machine>();
		machines.add(machine1);
		return machines;
	}

	public static void Scheduling(ArrayList<Machine> machines, ArrayList<Task> tasks, ProfilingTable table) {
		for (int i = 0; i < machines.size(); i++) {
			ArrayList<Executer> executers = new ArrayList<Executer>();
			ArrayList<Slot> slots = machines.get(i).getSlots();
			executers = ScheduleExecuters(slots, 4);
			ScheduleTasks(executers, tasks, table);
		}
	}

	public static void main(String[] args) {
		ArrayList<Thread> threads = new ArrayList<Thread>();
		// sendData(9998,"HI");
		ArrayList<Task> tasks = buildTopology(threads);
		ArrayList<Machine> machines = buildCluster();
		double[][][] data = new double[tasks.size()][machines.size()][4];
		data = new double[][][] {
				{ { 1, 1, 0.0581, 12 }, { 2, 2, 0.103, 6 }, { 1, 1, 0.1915, 9 }, { 1, 1, 0.8765, 8 } } };
		profilingTable = new ProfilingTable(tasks, machines, data);
		// System.out.println(tasks.get(0).getClass());
		// System.out.println(machines.get(0).getClass());
		double[][] times = new double[tasks.size()][machines.size()];
		times = new double[][] { { 10 }, { 8 }, { 9 }, { 7 } };
		TimingTable timingTable = new TimingTable(tasks, machines, times);
		Scheduling(machines, tasks, profilingTable);
		for (int i = 0; i < threads.size(); i++) {
			threads.get(i).start();
			// System.out.println(threads.get(i).getClass());
		}

		try {
			ServerSocket serverConnect = new ServerSocket(JavaHTTPServer.PORT);
			System.out
					.println("Server started.\nListening for connections on port : " + JavaHTTPServer.PORT + " ...\n");

			while (true) {
				JavaHTTPServer myServer = new JavaHTTPServer(serverConnect.accept(), tasks);

				System.out.println("Connecton opened. (" + new Date() + ")");

				Thread thread = new Thread(myServer);
				thread.start();
			}

		} catch (IOException e) {
			System.err.println("Server Connection error : " + e.getMessage());
		}

	}
}
