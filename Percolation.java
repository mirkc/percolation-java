import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private WeightedQuickUnionUF wqu;

    private byte[] sites;
    private int n, openSites;

    private boolean percolates;

    private static final byte SITE_OPEN = 0b001;
    private static final byte SITE_TOP = 0b010;
    private static final byte SITE_BOTTOM = 0b100;

    public Percolation(int n) {
        validate(n);

        this.wqu = new WeightedQuickUnionUF(n * n);

        this.sites = new byte[n * n];
        this.n = n;
        this.openSites = 0;

        this.percolates = false;
    }

    private int convertRowCol(int row, int col) {
        return (row - 1) * n + col - 1;
    }

    private boolean connect(int root1, int root2) {
        byte connected = (byte) (sites[root1] | sites[root2]);
        sites[root1] = connected;
        sites[root2] = connected;
        wqu.union(root1, root2);

        return (connected & SITE_TOP) != 0 && (connected & SITE_BOTTOM) != 0;
    }

    public void open(int row, int col) {
        validate(row, col);
        if (isOpen(row, col)) return;

        // if this is the first time we
        // open this site, flag it as open and increase the counter by one.
        sites[convertRowCol(row, col)] = SITE_OPEN;
        openSites++;

        if (row == 1) sites[convertRowCol(row, col)] |= SITE_TOP;
        if (row == n) sites[convertRowCol(row, col)] |= SITE_BOTTOM;
        if (n == 1) {
            percolates = true;
            return;
        }

        // connect to South
        if (row < n && isOpen(row + 1, col) &&
                connect(wqu.find(convertRowCol(row, col)),
                        wqu.find(convertRowCol(row + 1, col))))
            percolates = true;

        // connect to North
        if (row > 1 && isOpen(row - 1, col) &&
                connect(wqu.find(convertRowCol(row, col)),
                        wqu.find(convertRowCol(row - 1, col))))
            percolates = true;

        // connect to East
        if (col < n && isOpen(row, col + 1) &&
                connect(wqu.find(convertRowCol(row, col)),
                        wqu.find(convertRowCol(row, col + 1))))
            percolates = true;

        // connect to West
        if (col > 1 && isOpen(row, col - 1) &&
                connect(wqu.find(convertRowCol(row, col)),
                        wqu.find(convertRowCol(row, col - 1))))
            percolates = true;
    }

    public boolean isOpen(int row, int col) {
        validate(row, col);
        return (sites[convertRowCol(row, col)] & SITE_OPEN) != 0;
    }

    public boolean isFull(int row, int col) {
        validate(row, col);
        return (sites[wqu.find(convertRowCol(row, col))] & SITE_TOP) != 0;
    }

    public int numberOfOpenSites() {
        return openSites;
    }

    public boolean percolates() {
        return this.percolates;
    }

    private void validate(int n) {
        if (n < 1)
            throw new IllegalArgumentException();
    }

    private void validate(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n)
            throw new IllegalArgumentException();
    }

    public static void main(String[] args) {
        int n = 5000;
        Percolation p = new Percolation(n);
        while (!p.percolates()) {
            p.open(n--, 1);
        }

        System.out.println(n);
    }
}