import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;

public class PercolationStats {
    private int T;
    private double mean, stddev;

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException();
        T = trials;
        double[] openSites = new double[T];

        while (trials-- > 0) {
            Percolation perc = new Percolation(n);

            while (!perc.percolates()) {
                int row = StdRandom.uniformInt(1, n + 1);
                int col = StdRandom.uniformInt(1, n + 1);

                if (!perc.isOpen(row, col)) perc.open(row, col);
            }

            openSites[trials] = (double) perc.numberOfOpenSites() / n / n;
        }

        mean = StdStats.mean(openSites);
        stddev = StdStats.stddev(openSites);
    }

    public double mean() {
        return mean;
    }

    public double stddev() {
        return stddev;
    }

    public double confidenceLo() {
        return mean - 1.96 * stddev / Math.sqrt(T);
    }

    public double confidenceHi() {
        return mean + 1.96 * stddev / Math.sqrt(T);
    }

    public static void main(String[] args) {
        System.out.print("Enter n and T: ");
        int n = StdIn.readInt();
        int T = StdIn.readInt();

        Stopwatch sw = new Stopwatch();
        PercolationStats pstats = new PercolationStats(n, T);
        System.out.println("mean\t\t\t\t\t= " + pstats.mean());
        System.out.println("stddev\t\t\t\t\t= " + pstats.stddev());
        System.out.println("95% confidence interval\t= [" + pstats.confidenceLo() + ", "
                                   + pstats.confidenceHi() + "]");
        System.out.println("Total time\t\t\t\t= " + sw.elapsedTime() + "s");
    }
}