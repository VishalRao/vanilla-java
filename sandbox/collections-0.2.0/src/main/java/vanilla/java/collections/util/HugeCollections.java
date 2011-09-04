package vanilla.java.collections.util;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public enum HugeCollections {
  ;
  private static final int PROCESSORS = Runtime.getRuntime().availableProcessors();
  private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(PROCESSORS, new ThreadFactory() {
    private int n;

    @Override
    public Thread newThread(Runnable r) {
      Thread t = new Thread(r, "huge-collection-" + n++);
      t.setDaemon(true);
      t.setPriority(Thread.MIN_PRIORITY);
      return t;
    }
  });

  public static int hashCode(int i) {
    return i;
  }

  public static long hashCode(Object o) {
    return o == null ? 0 : o.hashCode();
  }

  public static <Recycleable> void recycle(Recycleable recycleable) {
    if (recycleable instanceof vanilla.java.collections.api.Recycleable) {
      try {
        ((vanilla.java.collections.api.Recycleable) recycleable).recycle();
      } catch (Exception ignored) {
        // ignored
      }
    }
  }

  public static <Closeable> void close(Closeable closeable) {
    if (closeable instanceof java.io.Closeable) try {
      ((java.io.Closeable) closeable).close();
    } catch (IOException ignored) {
      // ignored
    }
  }
}
