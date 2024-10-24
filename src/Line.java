import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Line extends JPanel {
    List<Point> pointList = new ArrayList<>();
    int mode = 0;
    int fillMode =0;
    int sx,sy;
    Color[][] buffer = new Color[][]{};
    public Line(){
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
               if(fillMode==0) {
                   pointList.add(new Point(e.getX(),e.getY()));
                   drawPoints(Color.RED,new Point(e.getX(),e.getY()));
                   if(pointList.size()>=2 &&mode==0) {
                       for (int i=0;i<pointList.size()-1;i++) {
                           paintLine(Color.BLACK, pointList.get(i), pointList.get(i+1));
                       }
                   }
                   //        if(pointList.size()>= 4&&mode==1){
                   if(pointList.size()== 4&&mode==1) {
                       for (int i = 0; i < pointList.size(); i++) {
                           paintLine(Color.GREEN, pointList.get(i), pointList.get(i + 1));
                           paintLine(Color.GREEN, pointList.get(i + 1), pointList.get(i + 2));
                           paintLine(Color.GREEN, pointList.get(i + 2), pointList.get(i + 3));
                           Point p1 = pointList.get(i);
                           Point p2 = pointList.get(++i);
                           Point p3 = pointList.get(++i);
                           Point p4 = pointList.get(++i);
                           paintBezie(Color.BLUE, p1, p2, p3, p4);
                           pointList.clear();

                       }
                   }
                   if (pointList.size()>=2&&mode==2){
            paintCircle(Color.RED,pointList.get(0),pointList.get(1));
            pointList.clear();
        }

               }
               else {
                   sx = e.getX();
                   sy = e.getY();
                   if(fillMode==1){
                       ReqFill(sx,sy,Color.BLUE,buffer[sx][sy]);
                   }
               }
                if(fillMode==2){
                    //  CorFill(new Point(sx,sy), getPatternC(),buffer[sx][sy]);
             CorFill(new Point(sx,sy), getPatternR(),buffer[sx][sy]);
            }
               repaint();
            }
        });
        setLayout(null );
        JButton button = new JButton("Очистить");
        button.setSize(100,50);
        button.setLocation(0,0);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pointList.clear();
                clearArr();
                fillMode = 0;
                repaint();
            }
        });

        JButton buttonBez = new JButton("Безье");
        buttonBez.setSize(100,50);
        buttonBez.setLocation(0,0);
        buttonBez.addActionListener(e -> {
            mode = 1;
           pointList.clear();
            fillMode = 0;
           // clearArr();
            //repaint();
        });

        JButton buttonLine = new JButton("Прямая");
        buttonLine.setSize(100,50);
        buttonLine.setLocation(0,0);
        buttonLine.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mode = 0;
                pointList.clear();
                fillMode = 0;
               // clearArr();
               // repaint();
            }
        });
        JButton buttonCr = new JButton("Круг");
        buttonCr.setSize(100,50);
        buttonCr.setLocation(0,0);
        buttonCr.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mode = 2;
               pointList.clear();
                fillMode = 0;
               // clearArr();
               // repaint();
            }
        });
        JButton buttonFl = new JButton("Заливка \n (рекурсия)");
        buttonFl.setSize(210,50);
        buttonFl.setLocation(0,0);
        buttonFl.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fillMode = 1;
               pointList.clear();
                //repaint();
            }
        });
        JButton buttonFlC = new JButton("Заливка \n (короед)");
        buttonFlC.setSize(210,50);
        buttonFlC.setLocation(0,0);
        buttonFlC.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fillMode = 2;
                // pointList.clear();
                //repaint();
            }
        });
        addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                int x = getWidth() - button.getWidth() - 10;
                int y = getHeight() - button.getHeight() - 10;
                button.setLocation(x, y);
                buttonBez.setLocation(button.getX()-buttonBez.getWidth()-10,button.getY());
                buttonLine.setLocation(buttonBez.getX()-buttonLine.getWidth()-10,buttonBez.getY());
                buttonCr.setLocation(buttonLine.getX()-buttonCr.getWidth()-10,buttonLine.getY());
                buttonFl.setLocation(getWidth()-buttonFl.getWidth()-10,buttonCr.getY()-buttonFl.getHeight()-10);
                buttonFlC.setLocation(buttonFl.getX()-buttonFlC.getWidth()-10,buttonFl.getY());
                ResizeArr();
            }

            @Override
            public void componentMoved(ComponentEvent e) {

            }

            @Override
            public void componentShown(ComponentEvent e) {

            }

            @Override
            public void componentHidden(ComponentEvent e) {

            }
        });
        add(button);
        add(buttonBez);
        add(buttonLine);
        add(buttonCr);
        add(buttonFl);
        add(buttonFlC);

    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for(int i=0;i<buffer.length;i++){
            for (int k=0;k<buffer[0].length;k++){
                if(buffer[i][k]!=null){
                    g.setColor(buffer[i][k]);
                    g.fillRect(i,k,2,2);
                }
            }
        }
        g.drawImage(getPatternC(),10,getHeight()-getPatternC().getHeight(),null);
        g.drawImage(getPatternR(),getPatternC().getWidth()+getPatternR().getHeight()+10,getHeight()-getPatternC().getHeight(),null);
        g.setFont(new Font("Arial", Font.BOLD, 12));
        g.setColor(Color.BLACK);
        g.drawString("Паттерны:",10,getHeight()-getPatternC().getHeight()-10);
    }


    void ResizeArr() {
        Color[][] temp = buffer;
        buffer = new Color[getWidth()][getHeight()];
        for (int x = 0; x < Math.min(temp.length, buffer.length); x++) {
            System.arraycopy(temp[x], 0, buffer[x], 0, Math.min(temp[x].length, buffer[x].length));
        }
    }
    void clearArr(){
        buffer = new Color[getWidth()][getHeight()];
    }
    @Override
    public void addNotify() {
        super.addNotify();
        JButton button = (JButton) getComponent(0);
     //   button.setSize(10,10);
    }


    void paintCircle(Color c,Point p, Point p2){
        int r = (int) Math.sqrt(Math.pow((p2.x-p.x),2)+Math.pow((p2.y-p.y),2));
        for (double t =0; t<2*Math.PI;t+=1./r){
            int x = (int) (p.getX()+r*Math.cos(t));
            int y = (int) (p.getY()+r*Math.sin(t));
            buffer[x][y]=c;
            if(x<buffer.length&&y<buffer[0].length&&x>1&&y>1) buffer[x][y] = c;
        }
        System.out.println("circle");
    }
    void ReqFill(int x, int y, Color c, Color target) {
        if (x < 0 || x >= getWidth() || y < 0 || y >= getHeight()||y>buffer[0].length||x>buffer.length) return;
        Color currentColor = buffer[x][y];

        if (currentColor != target) return;
        buffer[x][y] = c;
        ReqFill(x + 1, y, c, target);
        ReqFill( x - 1, y, c, target);
        ReqFill( x, y + 1, c, target);
        ReqFill( x, y - 1, c, target);
//        ReqFill( x-1, y - 1, c, target);
//        ReqFill( x+1, y + 1, c, target);
//        ReqFill( x+1, y - 1, c, target);
//        ReqFill( x-1, y + 1, c, target);
    }
    void CorFill(Point p,BufferedImage bufferedImage,Color target){
        Stack<Point> stack = new Stack<>();
        stack.push(p);
        while (!stack.isEmpty()){
            Point point = stack.pop();
            if(point.x<=0||point.x>=getWidth()||point.y<=0||point.y>=getHeight()) continue;
            if(buffer[point.x][point.y]!=target)continue;

            int patternX = point.x % bufferedImage.getWidth();
            int patternY = point.y%bufferedImage.getHeight();
            Color patternC = new Color(bufferedImage.getRGB(patternX,patternY));

          //  g.setColor(patternC);
            int x = point.x;
            int y = point.y;
           // g.drawRect(x,y,1,1);
            buffer[x][y] = patternC;


            stack.push(new Point(x+1,y));
            stack.push(new Point(x-1,y));
            stack.push(new Point(x,y+1));
            stack.push(new Point(x,y-1));
        }
    }
    void paintLine(Color c,Point p1,Point p2){

        int dx = p2.x - p1.x;
        int dy = p2.y - p1.y;
        int steps = Math.max(Math.abs(dx), Math.abs(dy));
        //g.setColor(c);
        double xIncrement = (double) dx / steps;
        double yIncrement = (double) dy / steps;

        double x = p1.x;
        double y = p1.y;

        for (int i = 0; i <= steps; i++) {
            if(x<buffer.length&&y<buffer[0].length&&x>1&&y>1) buffer[(int) Math.round(x)][(int) Math.round(y)] = c;
            x += xIncrement;
            y += yIncrement;
        }
    }
    void paintBezie(Color c,Point p1,Point p2,Point p3,Point p4){
       int steps = 1000;
        for (int i =0;i<steps;i++){
            double t = i/(double)steps;
            Point p = getBezie(t,p1,p2,p3,p4);
            buffer[p.x][p.y] = c;
        }
    }
    void drawPoints(Color color,Point ...points){
        for (Point p:points){
            for (int x =0;x<5;x++){
                for (int y =0;y<5;y++){
                    if(x+p.x<buffer.length&&y+p.y<buffer[0].length) buffer[p.x+x][p.y+y] = color;
                }
            }

        }
    }
    Point getBezie(double t,Point p1,Point p2, Point p3, Point p4){
        double x = Math.pow((1-t),3)*p1.x +3*Math.pow((1-t),2)*t*p2.x+3*(1-t)*Math.pow(t,2)*p3.x+Math.pow(t,3)* p4.x;
        double y = Math.pow((1-t),3)*p1.y +3*Math.pow((1-t),2)*t*p2.y+3*(1-t)*Math.pow(t,2)*p3.y+Math.pow(t,3)* p4.y;
        return new Point((int) x, (int) y);
    }
    BufferedImage getPatternC(){
        BufferedImage pattern = new BufferedImage(20,20,BufferedImage.TYPE_INT_RGB);
        int centerX = 10;
        int centerY = 10;
        int radius = 8;
        for (int x = 0; x < 20; x++) {
            for (int y = 0; y < 20; y++) {
                if (Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2)) < radius) {
                    pattern.setRGB(x, y, Color.RED.getRGB());
                } else {
                    pattern.setRGB(x, y, Color.YELLOW.getRGB());
                }
            }
        }
        return pattern;
    }
    BufferedImage getPatternR(){
        BufferedImage pattern = new BufferedImage(20,20,BufferedImage.TYPE_INT_RGB);
        int centerX = 10;
        int centerY = 10;
        int radius = 8;
        for (int x = 0; x < 20; x++) {
            for (int y = 0; y < 20; y++) {
                if (x<10) {
                    pattern.setRGB(x, y, Color.RED.getRGB());
                } else {
                    pattern.setRGB(x, y, Color.YELLOW.getRGB());
                }
            }
        }
        return pattern;
    }
    public static void main(String[] args) {
       // System.out.println("Hello world!");
        JFrame frame = new JFrame("Test");
        Line main = new Line();
        frame.add(main);
        frame.setSize(800,800);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}