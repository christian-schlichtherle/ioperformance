# A Simple I/O Performance Test in Java

## Abstract

This is a simple Maven project which builds a standalone Java application for
testing the I/O performance in a file system directory.
It can get used to test the performance of any storage media, e.g.
internal/external hard disks, flash disks, SAN, NAS, etc.
The result is written to standard output as simple HTML.

## Build Instructions

    $ mvn clean install

## Usage Instructions

    $ java -jar target/ioperformance-*.jar [<dir>] >results.html

where <dir> is an optional parameter for the path name of the file system
directory where the temporary test file will be generated.
If not provided, the Java I/O temp dir will get used.
Use `.` to run the test in the current directory.

The results get printed to standard output in simple HTML.
I have chosen to redirect them to the file `results.html` in this example.

## Algorithm

The test runs ten iterations.
For each iteration, a buffer with 100 MB (= 1024 * 1024 bytes) of random data
gets written to a temporary file and then reread again.
The buffer size has been selected to eradicate the effect of any caches in the
OS or hardware.
The times for these operations get measured to compute the sustainable R/W
rates.
