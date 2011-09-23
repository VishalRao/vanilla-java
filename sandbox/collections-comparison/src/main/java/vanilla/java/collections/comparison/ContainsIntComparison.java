package vanilla.java.collections.comparison;

import com.sun.jmx.remote.internal.ArrayQueue;
import gnu.trove.TIntArrayList;
import javolution.util.FastList;
import javolution.util.FastTable;
import vanilla.java.collections.HugeArrayBuilder;

import java.io.Closeable;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.*;

/**
 * Measure time and memory consumption of an add operation.
 *
 * @author c.cerbo
 */
@SuppressWarnings("restriction")
public class ContainsIntComparison {
	static private final int ITERATIONS = System.getProperty("iterations") != null ? Integer.parseInt(System.getProperty("iterations")) :  1000000;

    static private final String[] ENV_PROPS = {"java.vm.name",
            "java.runtime.version", "os.name", "os.arch", "os.version"};

    private PrintWriter out = new PrintWriter(
            new OutputStreamWriter(System.out), true);
    private List<Operation> operations = new ArrayList<Operation>();

    public ContainsIntComparison() {
        operations.add(createHugeArrayListContainsOperation());
        operations.add(createTIntArrayListContainsOperation());
        operations.add(createListContainsOperation(ArrayList.class));
        operations.add(createListContainsOperation(Vector.class));
        operations.add(createListContainsOperation(Stack.class));
        operations.add(createListContainsOperation(FastTable.class));

        operations.add(createListContainsOperation(FastList.class));
        operations.add(createListContainsOperation(LinkedList.class));
        operations.add(createArrayQueueContainsOperation());

// takes too long.
//		operations.add(createListAddOperation(CopyOnWriteArrayList.class));
    }

    @SuppressWarnings("rawtypes")
	private Operation createListContainsOperation(final Class<? extends List> listClass) {
        return new Operation("Performing {0} " + listClass.getSimpleName() + ".contains(int) operations", ITERATIONS) {
          
        	private List<Integer> list;
           
        	@Override
			@SuppressWarnings("unchecked")
        	public void init() throws Exception {
        		 list = (List<Integer>) listClass.newInstance();
                 for (int i = 0; i < iterations; i++) {
                     list.add(i);
                 }
        	}
			
        	@Override
            public Object execute() throws InstantiationException, IllegalAccessException {
        		for (int i = 0; i < iterations; i++) {
                    if(!list.contains(i)) {
                    	throw new IllegalStateException("The list does not contain the element " + i);
                    }
                }
                return list;
            }			
			
        };
    }

    private Operation createArrayQueueContainsOperation() {
        return new Operation("Performing {0} ArrayQueue.contains(int) operations", ITERATIONS) {
        	
            private List<Integer> list;
            
        	@Override
            public void init() {
        		list = new ArrayQueue<Integer>(ITERATIONS);
                	for (int i = 0; i < iterations; i++) {
                 		list.add(i);
                }
        	}
        	 
			@Override			
            public Object execute() {
				for (int i = 0; i < iterations; i++) {
                    if(!list.contains(i)) {
                    	throw new IllegalStateException("The list does not contain the element " + i);
                    }
                }
				
                return list;
            }
        };
    }

    private Operation createTIntArrayListContainsOperation() {
        return new Operation("Performing {0} TIntArrayList.contains(int) operations", ITERATIONS) {
        	private TIntArrayList list;
        	
            @Override
            public void init() {
                list = new TIntArrayList();
                for (int i = 0; i < iterations; i++) {
                    list.add(i);
                }
            }
            
            @Override
            public Object execute() {
            	for (int i = 0; i < iterations; i++) {
                    if(!list.contains(i)) {
                    	throw new IllegalStateException("The list does not contain the element " + i);
                    }
                }
            	
            	return list;
            }
        };
    }

    private Operation createHugeArrayListContainsOperation() {
        return new Operation("Performing {0} HugeArrayList<Int>.contains(int) operations", ITERATIONS) {
        	private List<Int> list;
        	private final HugeArrayBuilder<Int> builder = new HugeArrayBuilder<Int>() {{
                capacity = ITERATIONS;
                setRemoveReturnsNull = true;
            }};
            
            @Override
            public void init() {
                
                list = builder.create();
                Int element = builder.createBean();
                for (int i = 0; i < iterations; i++) {
                    element.setInt(i);
                    list.add(element); // copies the value.
                }
            }
            
            @Override
            public Object execute() {     
            	Int element = builder.createBean();            	
            	for (int i = 0; i < iterations; i++) {
            		element.setInt(i);
                    if(!list.contains(element)) {
                    	throw new IllegalStateException("The list does not contain the element " + i);
                    }
                }
            	
            	return list;
            }
        };
    }

    public void setPrintWriter(PrintWriter out) {
        this.out = out;
    }

    @SuppressWarnings("rawtypes")
	public void run() {
        ObjectSizeFetcher.getObjectSize(1);
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
                    if (list instanceof Closeable)
                        ((Closeable) list).close();

                    operation.init();
                    
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

    @SuppressWarnings({ "unchecked", "rawtypes" })
	private void printHeader() {
        out.println("--------------------------------");
        out.println("Collections Comparison");
        out.println("--------------------------------");
        Map<String, String> props = new LinkedHashMap<String, String>((Map) System.getProperties());
        props.keySet().retainAll(Arrays.asList(ENV_PROPS));
        props.put("maxMemory", String.format("%,d MB", Runtime.getRuntime().maxMemory() / 1000 / 1000));
        out.println(props);
        out.println("--------------------------------");
        out.println();
    }

    public static void main(String[] args) {
        new ContainsIntComparison().run();
    }
}
