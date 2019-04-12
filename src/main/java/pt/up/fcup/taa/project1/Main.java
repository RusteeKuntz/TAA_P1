package pt.up.fcup.taa.project1;

/**
 * Main class for polygon generation
 *
 * @author Dusan Hetlerovic, Vincent Lopes
 */
public class Main {

    private static final int VERTICES = 40;

    public static void main(String[] args) {

        Grid grid = new Grid();
        grid.printGrid();
        for (int i = 0; i < VERTICES - 3; ++i) {
            grid.addVertex();
            grid.printGrid();
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
