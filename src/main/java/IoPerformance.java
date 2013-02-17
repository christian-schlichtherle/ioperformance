import java.io.*;
import static java.lang.System.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Christian Schlichtherle
 */
public class IoPerformance {

    private static final int ITERATIONS = 10;
    private static final int KILOBYTE = 1024;
    private static final int MEGABYTE = 1024 * 1024;
    private static final int DATA_SIZE = 100 * MEGABYTE;
    private static final int NANOS_PER_SECOND = 1000 * 1000 * 1000;
    private static final long COEFFICIENT = (long) DATA_SIZE / KILOBYTE * NANOS_PER_SECOND;

    public static void main(final String[] args) throws IOException {
        final Random rnd = ThreadLocalRandom.current();
        final byte[] data = new byte[DATA_SIZE];
        final byte[] clone = new byte[DATA_SIZE];
        final File tmpdir = new File(0 < args.length
                ? args[0]
                : getProperty("java.io.tmpdir"));
        out .append("<!DOCTYPE html>\n")
            .append("<html>\n")
            .append("  <head>\n")
            .append("    <title>I/O Performance Test</title>\n")
            .append("    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=" + Charset.defaultCharset().name() + "\">\n")
            .append("  </head>\n")
            .append("  <body>\n")
            .append("    <table>\n")
            .append("      <caption>Results for temp dir <code>" + tmpdir + "<code></caption>\n")
            .append("      <thead align=\"right\"><tr><th>Iteration</th><th>Sustainable<br>Write Rate<br>[KB/s]</th><th>Sustainable<br>Read Rate<br>[KB/s]</th></tr></thead>\n")
            .append("      <tbody align=\"right\">\n");
        for (int i = 1; i <= ITERATIONS; i++) {
            rnd.nextBytes(data);
            final File file = File.createTempFile("tmp", null, tmpdir);
            try {
                final long write = nanoTime();
                {
                    final OutputStream out = new FileOutputStream(file);
                    try {
                        out.write(data);
                    } finally {
                        out.close();
                    }
                }
                final long read = nanoTime();
                {
                    final InputStream in = new FileInputStream(file);
                    try {
                        new DataInputStream(in).readFully(clone);
                    } finally {
                        in.close();
                    }
                }
                final long done = nanoTime();
                if (!Arrays.equals(data, clone))
                    throw new IllegalArgumentException();
                out.printf("        <tr><td>%d</td><td>%,d</td><td>%,d</td></tr>\n",
                        i,
                        COEFFICIENT / (read - write),
                        COEFFICIENT / (done - read));
            } finally {
                file.delete();
            }
        }
        out .append("      </tbody>\n")
            .append("    </table>\n")
            .append("  </body>\n")
            .append("</html>\n");
    }
}
