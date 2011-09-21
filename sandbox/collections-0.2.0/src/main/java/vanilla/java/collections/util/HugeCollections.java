/*
 * Copyright (c) 2011 Peter Lawrey
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package vanilla.java.collections.util;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.LongBuffer;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public enum HugeCollections {
  ;
  private static final int PROCESSORS = Runtime.getRuntime().availableProcessors();
  public static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(PROCESSORS, new ThreadFactory() {
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

  private static final Queue<LongBuffer> LONG_BUFFER_POOL = new ArrayDeque<LongBuffer>();
  public static final int LONG_BUFFER_SIZE = 4 * 1024;

  public static LongBuffer acquireLongBuffer() {
    LongBuffer buffer = LONG_BUFFER_POOL.poll();
    return buffer == null ? ByteBuffer.allocateDirect(LONG_BUFFER_SIZE * 8).order(ByteOrder.nativeOrder()).asLongBuffer() : buffer;
  }

  public static void recycle(LongBuffer buffer) {
    LONG_BUFFER_POOL.add(buffer);
  }
}
