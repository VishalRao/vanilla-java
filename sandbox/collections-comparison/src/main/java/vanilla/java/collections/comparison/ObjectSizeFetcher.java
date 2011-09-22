package vanilla.java.collections.comparison;

import java.lang.instrument.Instrumentation;

/**
 * Determine the size of an object using instrumentation.
 *
 * @author c.cerbo
 */
public class ObjectSizeFetcher {
    private static Instrumentation instrumentation;
    private static boolean logged = false;

    public static void premain(String args, Instrumentation inst) {
        instrumentation = inst;
    }

    public static long getObjectSize(Object o) {
        if (instrumentation == null) {
            if (!logged) {
                System.err.println("To enable the ObjectSizeFetcher add to the command line: -javaagent:/path/to/collections-comparison-0.2.0.jar");
                logged = true;
            }
            return -1;
        }
        return instrumentation.getObjectSize(o);
    }
}

