/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.AcyclicSP;
import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture picture;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException("Illegal Argument");
        }

        this.picture = new Picture(picture);
    }

    private int[] rowAndColFromVertex(DirectedEdge edge) {
        int col = (edge.from()) % this.width();
        int row = ((edge.from()) - col) / this.width();
        int[] rowColList = { row, col };
        return rowColList;
    }

    private EdgeWeightedDigraph createVerticalDigraphFromPicture() {
        EdgeWeightedDigraph graph = new EdgeWeightedDigraph(this.height() * this.width() + 2);
        for (int col = 0; col < this.width(); col++) {
            for (int row = 0; row < this.height(); row++) {
                double weight = energy(col, row);
                int vertex = row * this.width() + col;
                int upVertex = vertex - this.width();
                int upLeftVertex = upVertex - 1;
                int upRightVertex = upVertex + 1;
                DirectedEdge edge1;
                DirectedEdge edge2;
                DirectedEdge edge3;
                DirectedEdge edge4;
                if (row == 0) {
                    edge1 = new DirectedEdge(this.height() * this.width(), vertex, weight);
                    graph.addEdge(edge1);
                }
                else {
                    edge2 = new DirectedEdge(upVertex, vertex, weight);
                    graph.addEdge(edge2);
                    if (col != 0) {
                        edge3 = new DirectedEdge(upLeftVertex, vertex, weight);
                        graph.addEdge(edge3);
                    }
                    if (col != this.width() - 1) {
                        edge4 = new DirectedEdge(upRightVertex, vertex, weight);
                        graph.addEdge(edge4);
                    }
                }
                if (row == this.height() - 1) {
                    edge1 = new DirectedEdge(vertex, this.height() * this.width() + 1, weight);
                    graph.addEdge(edge1);
                }
            }
        }
        return graph;
    }

    private EdgeWeightedDigraph createHorizontalDigraphFromPicture() {
        EdgeWeightedDigraph graph = new EdgeWeightedDigraph(this.height() * this.width() + 2);
        for (int row = 0; row < this.height(); row++) {
            for (int col = 0; col < this.width(); col++) {
                double weight = energy(col, row);
                int vertex = row * this.width() + col;
                int leftVertex = vertex - 1;
                int leftUpVertex = leftVertex - this.width();
                int leftDownVertex = leftVertex + this.width();
                DirectedEdge edge1;
                DirectedEdge edge2;
                DirectedEdge edge3;
                DirectedEdge edge4;
                if (col == 0) {
                    edge1 = new DirectedEdge(this.height() * this.width(), vertex, weight);
                    graph.addEdge(edge1);
                }
                else {
                    edge2 = new DirectedEdge(leftVertex, vertex, weight);
                    graph.addEdge(edge2);
                    if (row != 0) {
                        edge3 = new DirectedEdge(leftUpVertex, vertex, weight);
                        graph.addEdge(edge3);
                    }
                    if (row != this.height() - 1) {
                        edge4 = new DirectedEdge(leftDownVertex, vertex, weight);
                        graph.addEdge(edge4);
                    }
                }
                if (col == this.width() - 1) {
                    edge1 = new DirectedEdge(vertex, this.height() * this.width() + 1, weight);
                    graph.addEdge(edge1);
                }
            }
        }
        return graph;
    }

    // current picture
    public Picture picture() {
        Picture picture = new Picture(this.picture);
        return this.picture;
    }

    // width of current picture
    public int width() {
        return picture().width();
    }

    // height of current picture
    public int height() {
        return picture().height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= this.width() || y < 0 || y >= this.height()) {
            throw new IllegalArgumentException("Illegal Argument");
        }

        double energy = 0.0;
        if (x == width() - 1 || x == 0 || y == height() - 1 || y == 0) {
            return 1000;
        }
        else {
            energy += Math.pow((picture.get(x + 1, y).getRed() - picture.get(x - 1, y).getRed()),
                               2);
            energy += Math.pow(
                    (picture.get(x + 1, y).getGreen() - picture.get(x - 1, y).getGreen()),
                    2);
            energy += Math.pow((picture.get(x + 1, y).getBlue() - picture.get(x - 1, y).getBlue()),
                               2);

            energy += Math.pow((picture.get(x, y + 1).getRed() - picture.get(x, y - 1).getRed()),
                               2);
            energy += Math.pow(
                    (picture.get(x, y + 1).getGreen() - picture.get(x, y - 1).getGreen()),
                    2);
            energy += Math.pow((picture.get(x, y + 1).getBlue() - picture.get(x, y - 1).getBlue()),
                               2);

            return Math.sqrt(energy);
        }
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        EdgeWeightedDigraph graph = createHorizontalDigraphFromPicture();
        AcyclicSP path = new AcyclicSP(graph, this.width() * this.height());
        int[] indexPath = new int[this.width()];
        int count = 0;
        for (DirectedEdge edge : path.pathTo(this.width() * this.height() + 1)) {
            if (count != 0) {
                indexPath[count - 1] = rowAndColFromVertex(edge)[0];
            }
            count++;
        }
        return indexPath;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        EdgeWeightedDigraph graph = createVerticalDigraphFromPicture();
        AcyclicSP path = new AcyclicSP(graph, this.width() * this.height());
        int[] indexPath = new int[this.height()];
        int count = 0;
        for (DirectedEdge edge : path.pathTo(this.width() * this.height() + 1)) {
            if (count != 0) {
                indexPath[count - 1] = rowAndColFromVertex(edge)[1];
            }
            count++;
        }
        return indexPath;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException("Illegal Argument");
        }

        if (seam.length != this.width()) {
            throw new IllegalArgumentException("Illegal Argument");
        }

        if (this.height() <= 1) {
            throw new IllegalArgumentException("Illegal Argument");
        }

        for (int index = 0; index < seam.length; index++) {
            if (seam[index] > this.height() || seam[index] > this.height()) {
                throw new IllegalArgumentException("Illegal Argument");
            }
            if (index > 0) {
                if (seam[index] > seam[index - 1] + 1 || seam[index] < seam[index - 1] - 1) {
                    throw new IllegalArgumentException("Illegal Argument");
                }
            }
        } /**/

        Picture newPic = new Picture(this.width(), this.height() - 1);

        for (int col = 0; col < this.width(); col++) {
            for (int row = 0; row < this.height(); row++) {
                if (row < seam[col]) {
                    newPic.set(col, row, this.picture.get(col, row));
                }
                if (row > seam[col]) {
                    newPic.set(col, row - 1, this.picture.get(col, row));
                }
            }
        }

        this.picture = newPic;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException("Illegal Argument");
        }

        if (seam.length != this.height()) {
            throw new IllegalArgumentException("Illegal Argument");
        }

        if (this.width() <= 1) {
            throw new IllegalArgumentException("Illegal Argument");
        }

        for (int index = 0; index < seam.length; index++) {
            if (seam[index] > this.width() || seam[index] > this.width()) {
                throw new IllegalArgumentException("Illegal Argument");
            }
            if (index > 0) {
                if (seam[index] > seam[index - 1] + 1 || seam[index] < seam[index - 1] - 1) {
                    throw new IllegalArgumentException("Illegal Argument");
                }
            }
        } /**/

        Picture newPic = new Picture(this.width() - 1, this.height());

        for (int row = 0; row < this.height(); row++) {
            for (int col = 0; col < this.width(); col++) {
                if (col < seam[row]) {
                    newPic.set(col, row, this.picture.get(col, row));
                }
                if (col > seam[row]) {
                    newPic.set(col - 1, row, this.picture.get(col, row));
                }
            }
        }

        this.picture = newPic;
    }

    //  unit testing (optional)
    public static void main(String[] args) {

    }

}
