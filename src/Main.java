import javax.swing.*;
import java.awt.*;

public class Main extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintLine(g,new Point(200,200),new Point(400,400));
    }
void paintLine(Graphics g,Point p1,Point p2){
        int dx = p1.x - p2.x;
        int dy = p1.y - p2.y;
        int steps = Math.max(dx,dy);
        double x = (double) dx /steps;
        double y = (double) dy/steps;
        double x0 = p1.x;
        double y0 = p1.y;
        for (int i = 0;i<steps;i++) {
            g.drawRect((int) Math.round(x0), (int) Math.round(y0), 1, 1);
            x0+=x;
            y0+=y;

        }
}
    public static void main(String[] args) {
        System.out.println("Hello world!");
        JFrame frame = new JFrame("Test");
        Main main = new Main();
        frame.add(main);
        frame.setSize(800,800);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    class Point{
        private int x,y;
        public Point(int x,int y){
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }
    }
}