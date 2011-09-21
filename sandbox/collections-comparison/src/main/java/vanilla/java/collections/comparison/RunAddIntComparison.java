package vanilla.java.collections.comparison;

import com.sun.jmx.remote.internal.ArrayQueue;
import gnu.trove.TIntArrayList;
import javolution.util.FastList;
import javolution.util.FastTable;
import vanilla.java.collections.HugeArrayBuilder;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.*;

/**
 * Measure time and memory consumption of an add operation.
 *
 * @author c.cerbo
 */
public class RunAddIntComparison {
	private static final int ADD_ITERATIONS = 10000000;

	static private final String[] ENV_PROPS = {"java.vm.name",
												  "java.runtime.version", "os.name", "os.arch", "os.version"};

	private PrintWriter out = new PrintWriter(
												 new OutputStreamWriter(System.out), true);
	private List<Operation> operations = new ArrayList<Operation>();

	public RunAddIntComparison() {
		operations.add(createTIntArrayListAddOperation());
		operations.add(createArrayQueueAddOperation());
		operations.add(createListAddOperation(ArrayList.class));
		operations.add(createListAddOperation(LinkedList.class));
		operations.add(createListAddOperation(Vector.class));
		operations.add(createListAddOperation(Stack.class));
		operations.add(createListAddOperation(FastList.class));
		operations.add(createListAddOperation(FastTable.class));
		operations.add(createHugeArrayListAddOperation());
		//TODO add HugeCollection
		//TODO add Google Collections - http://code.google.com/p/guava-libraries
		//TODO add PCJ - http://pcj.sourceforge.net
		//TODO add Apache Commons Collections - http://commons.apache.org/collections
		//TODO add Fastutil - http://fastutil.dsi.unimi.it

// takes too long.
//		operations.add(createListAddOperation(CopyOnWriteArrayList.class));
	}

	private Operation createListAddOperation(final Class<? extends List> listClass) {
		return new Operation("Performing {0} " + listClass.getSimpleName() + ".add(int) operations", ADD_ITERATIONS) {

			@Override
			public Object execute() throws InstantiationException, IllegalAccessException {
				List<Integer> list = (List) listClass.newInstance();
				for (int i = 0; i < iterations; i++) {
					list.add(i);
				}
				return list;
			}
		};
	}

	private Operation createArrayQueueAddOperation() {
		return new Operation("Performing {0} ArrayQueue.add(int) operations", ADD_ITERATIONS) {

			@Override
			public Object execute() {
				List<Integer> list = new ArrayQueue<Integer>(ADD_ITERATIONS);
				for (int i = 0; i < iterations; i++) {
					list.add(i);
				}
				return list;
			}
		};
	}

	private Operation createTIntArrayListAddOperation() {
		return new Operation("Performing {0} TIntArrayList.add(int) operations", ADD_ITERATIONS) {

			@Override
			public Object execute() {
				TIntArrayList list = new TIntArrayList();
				for (int i = 0; i < iterations; i++) {
					list.add(i);
				}
				return list;
			}
		};
	}

	interface Int {
		public void setInt(int i);

		public int getInt();
	}

	private Operation createHugeArrayListAddOperation() {
		return new Operation("Performing {0} HugeArrayList<Int>.add(int) operations", ADD_ITERATIONS) {

			@Override
			public Object execute() {
				final HugeArrayBuilder<Int> builder = new HugeArrayBuilder<Int>() {{
					capacity = ADD_ITERATIONS;
				}};
				List<Int> list = builder.create();
				Int element = builder.createBean();
				for (int i = 0; i < iterations; i++) {
					element.setInt(i);
					list.add(element); // copies the value.
				}
				return list;
			}
		};
	}

	public void setPrintWriter(PrintWriter out) {
		this.out = out;
	}

	public void run() {
		printHeader();
		for (Operation operation : operations) {
			out.print(operation.getDescription());
			out.flush();
			long[] times = new long[31];
			try {
				Object list = null;
				for (int i = 0; i < times.length; i++) {
					// slower with recycling.
//					if (list instanceof FastList) FastList.recycle((FastList) list);
					if (list instanceof FastTable) FastTable.recycle((FastTable) list);

					long start = System.nanoTime();
					list = operation.execute();
					times[i] = System.nanoTime() - start;
				}
				Arrays.sort(times);
				long median = times[times.length / 2];
				out.print(", took (ms), " + median / 1000000);
				long ninetyth = times[times.length * 9 / 10];
				out.print(", 90%tile took (ms), " + ninetyth / 1000000);
				out.print(", memory consumed (bytes), " + ObjectSizeFetcher.getObjectSize(list));
			} catch (Exception e) {
				e.printStackTrace(out);
			}
			out.println();
		}
	}

	private void printHeader() {
		out.println("--------------------------------");
		out.println("Collections Comparison");
		out.println("--------------------------------");
		Map<String, String> props = new LinkedHashMap<String, String>((Map) System.getProperties());
		props.keySet().retainAll(Arrays.asList(ENV_PROPS));
		props.put("maxMemory", String.format("%,d MB", Runtime.getRuntime().maxMemory() / 1024 / 1024));
		out.println(props);
		out.println("--------------------------------");
		out.println();
	}

	public static void main(String[] args) {
		new RunAddIntComparison().run();
	}
}
