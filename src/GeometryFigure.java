import javax.swing.*;
import java.awt.*;

public class GeometryFigure extends JPanel {
    private static final int BASE_RADIUS = 150;
    private double scaleFactor = 1.0;
    private double rotationX = 0, rotationY = 0, rotationZ = 0;
    private double moveX = 0, moveY = 0;


    private double[] applyAffineTransformation(double[] point, double[][] matrix) {
        double[] result = new double[4];
        for (int i = 0; i < 4; i++) {
            result[i] = 0;
            for (int j = 0; j < 4; j++) {
                result[i] += matrix[i][j] * point[j];
            }
        }
        return result;
    }


    private double[][] createTransformationMatrix(){
        double[][] scaleMatrix = {
                {scaleFactor, 0, 0, 0},
                {0, scaleFactor, 0, 0},
                {0, 0, scaleFactor, 0},
                {0, 0,           0, 1}
        };

        double[][] rotationXMatrix = {
                {1,                   0,                   0, 0},
                {0, Math.cos(rotationX), -Math.sin(rotationX),0},
                {0, Math.sin(rotationX), Math.cos(rotationX), 0},
                {0, 0,                0,                      1}
        };

        double[][] rotationYMatrix = {
                {Math.cos(rotationY), 0, Math.sin(rotationY), 0},
                {                   0, 1,                  0, 0},
                {-Math.sin(rotationY), 0, Math.cos(rotationY),0},
                {0                   , 0,                   0,1}
        };

        double[][] rotationZMatrix = {
                {Math.cos(rotationZ), -Math.sin(rotationZ), 0,0},
                {Math.sin(rotationZ), Math.cos(rotationZ), 0, 0},
                {                  0,                   0, 1, 0},
                {                  0,                   0, 0, 1}
        };

        double[][] translationMatrix = {
                {1, 0, 0, moveX},
                {0, 1, 0, moveY},
                {0, 0, 1,     0},
                {0, 0, 0,     1}
        };

        double[][] transformMatrix = multiplyMatrices(translationMatrix, rotationXMatrix);
        transformMatrix = multiplyMatrices(transformMatrix, rotationYMatrix);
        transformMatrix = multiplyMatrices(transformMatrix, rotationZMatrix);
        transformMatrix = multiplyMatrices(transformMatrix, scaleMatrix);

        return transformMatrix;
    }

    private double[][] multiplyMatrices(double[][] a, double[][] b) {
        double[][] result = new double[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result[i][j] = 0;
                for (int k = 0; k < 4; k++) {
                    result[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return result;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK);

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int numPoints = 100;

        double[][] transformMatrix = createTransformationMatrix();

        for (int i = 0; i < numPoints; i++) {
            double alpha = Math.acos(2.0 * i / numPoints - 1);
            for (int j = 0; j < numPoints; j++) {
                double beta = 2 * Math.PI * j / numPoints;
                double x = BASE_RADIUS * Math.sin(alpha) * Math.cos(beta);
                double y = BASE_RADIUS * Math.sin(alpha) * Math.sin(beta);
                double z = BASE_RADIUS * Math.cos(alpha);

                double[] point = {x, y, z, 1};
                double[] transformedPoint = applyAffineTransformation(point, transformMatrix);

                int screenX = centerX + (int) transformedPoint[0];
                int screenY = centerY - (int) transformedPoint[1];
                g2d.fillRect(screenX, screenY, 2, 2);
            }
        }
    }

    public void rotate(double deltaX, double deltaY, double deltaZ) {
        rotationX += deltaX;
        rotationY += deltaY;
        rotationZ += deltaZ;
        repaint();
    }

    public void moves(double x, double y) {
        moveX += x;
        moveY += y;
        repaint();
    }

    public void scale(double factor) {
        scaleFactor *= factor;
        repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("3D");
        GeometryFigure spherePanel = new GeometryFigure();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.add(spherePanel);
        frame.setVisible(true);

        Timer timer = new Timer(30, e -> {
            spherePanel.rotate(0, 0.01, 0);
        });
        timer.start();

        frame.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                switch (e.getKeyCode()) {
                    case java.awt.event.KeyEvent.VK_UP:
                        spherePanel.scale(1.1);
                        break;
                    case java.awt.event.KeyEvent.VK_DOWN:
                        spherePanel.scale(0.9);
                        break;
                    case java.awt.event.KeyEvent.VK_LEFT:
                        spherePanel.moves(-10, 0);
                        break;
                    case java.awt.event.KeyEvent.VK_RIGHT:
                        spherePanel.moves(10, 0);
                        break;
                }
            }
        });
    }
}
