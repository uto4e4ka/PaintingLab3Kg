import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SphereRotation extends JPanel {

    // Радиус шара
    private static final int RADIUS = 150;

    // Углы вращения вокруг осей X, Y и Z
    private double rotationX = 0;
    private double rotationY = 0;
    private double rotationZ = 0;

    // Обновление углов вращения
    public void rotate(double deltaX, double deltaY, double deltaZ) {
        rotationX += deltaX;
        rotationY += deltaY;
        rotationZ += deltaZ;
        System.out.println("rot"+rotationX);
        repaint();
    }

    // Вращение точек на сфере
    private void rotatePoint(double[] point, double angleX, double angleY, double angleZ) {
        // Вращение вокруг оси X
        double tempY = point[1] * Math.cos(angleX) - point[2] * Math.sin(angleX);
        double tempZ = point[1] * Math.sin(angleX) + point[2] * Math.cos(angleX);
        point[1] = tempY;
        point[2] = tempZ;

        // Вращение вокруг оси Y
        double tempX = point[0] * Math.cos(angleY) + point[2] * Math.sin(angleY);
        point[2] = -point[0] * Math.sin(angleY) + point[2] * Math.cos(angleY);
        point[0] = tempX;

        // Вращение вокруг оси Z
        tempX = point[0] * Math.cos(angleZ) - point[1] * Math.sin(angleZ);
        point[1] = point[0] * Math.sin(angleZ) + point[1] * Math.cos(angleZ);
        point[0] = tempX;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK);

        // Центр панели (экран)
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        // Количество точек (для сглаживания)
        int numPoints = 200;

        // Перебираем значения углов для создания точек на сфере
        for (int i = 0; i < numPoints; i++) {
            double alpha = Math.acos(2.0 * i / numPoints - 1); // угол от 0 до π
            for (int j = 0; j < numPoints; j++) {
                double beta = 2 * Math.PI * j / numPoints; // угол от 0 до 2π

                // Переводим сферические координаты в декартовы
                double x = RADIUS * Math.sin(alpha) * Math.cos(beta);
                double y = RADIUS * Math.sin(alpha) * Math.sin(beta);
                double z = RADIUS * Math.cos(alpha);

                // Вращаем точку
                rotatePoint(new double[] {x, y, z}, rotationX, rotationY, rotationZ);

                // Проецируем точку на 2D (игнорируем z-координату)
                int screenX = centerX + (int) x;
                int screenY = centerY - (int) y;  // инвертируем Y для правильного отображения

                // Рисуем точку
                g2d.fillRect(screenX, screenY, 2, 2); // рисуем точку как маленький прямоугольник
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Rotating 3D Sphere");
        SphereRotation spherePanel = new SphereRotation();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.add(spherePanel);
        frame.setVisible(true);

        // Таймер для обновления углов вращения и перерисовки
        Timer timer = new Timer(30, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Вращение на небольшое количество градусов по всем осям
                spherePanel.rotate(1, 1, 1);
            }
        });
        timer.start();
    }
}