package pt.up.fcup.taa.project1;

/**
 *
 * @author Ayy lmao
 */
public class Main {

    private static final int VERTICES = 50;

    public static void main(String[] args) {

        Grid grid = new Grid();
        grid.printGrid();
        for (int i = 0; i < VERTICES - 3; ++i) {
            grid.addVertex();
            grid.printGrid();
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

}
