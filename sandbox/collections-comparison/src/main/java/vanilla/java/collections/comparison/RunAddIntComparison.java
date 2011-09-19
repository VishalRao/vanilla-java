package vanilla.java.collections.comparison;

import gnu.trove.TIntArrayList;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javolution.util.FastList;

/**
 * Measure time and memory consumption of an add operation.
 * 
 * @author c.cerbo
 *
 */
public class RunAddIntComparison {
	private static final int ADD_ITERATIONS = 1000000;

	static private final String[] ENV_PROPS = { "java.vm.name",
			"java.runtime.version", "os.name", "os.arch", "os.version" };

	private PrintWriter out = new PrintWriter(
			new OutputStreamWriter(System.out), true);
	private List<Operation> operations = new ArrayList<Operation>();

	public RunAddIntComparison() {
		operations.add(createArrayListAddOperation());
		operations.add(createTIntArrayListAddOperation());
		operations.add(createFastListtAddOperation());
		//TODO add HugeCollection
		//TODO add Google Collections - http://code.google.com/p/guava-libraries
		//TODO add PCJ - http://pcj.sourceforge.net
		//TODO add Apache Commons Collections - http://commons.apache.org/collections
		//TODO add Fastutil - http://fastutil.dsi.unimi.it
	}
		

	private Operation createArrayListAddOperation() {
		return new Operation("Performing {0} ArrayList.add(int) operations", ADD_ITERATIONS) {

			@Override
			public Object execute() {
				List<Integer> list = new ArrayList<Integer>();
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
	
	private Operation createFastListtAddOperation() {
		return new Operation("Performing {0} FastList.add(int) operations", ADD_ITERATIONS) {

			@Override
			public Object execute() {
				FastList<Integer> list = new FastList<Integer>();
				for (int i = 0; i < iterations; i++) {
					list.add(i);
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
			out.println(operation.getDescription());
			long elapsed = System.currentTimeMillis();
			Object list = operation.execute();
			elapsed = System.currentTimeMillis() - elapsed;
			out.println("Elapsed time (ms)      : " + elapsed);
			out.println("Memory consumed (bytes): " + ObjectSizeFetcher.getObjectSize(list));
			out.println();
		}
	}

	private void printHeader() {
		out.println("--------------------------------");
		out.println("Collections Comparison");
		out.println("--------------------------------");
		for (int i = 0; i < ENV_PROPS.length; i++) {
			String key = ENV_PROPS[i];
			out.println(key + "=" + System.getProperty(key));
		}
		out.println("maxMemory=" + Runtime.getRuntime().maxMemory());
		out.println("--------------------------------");
		out.println();
	}

	public static void main(String[] args) {
		new RunAddIntComparison().run();
	}
}
