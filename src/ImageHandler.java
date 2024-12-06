import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageHandler extends JPanel {
    BufferedImage image;
    int peaks = 0;

    public ImageHandler() throws IOException {
        setLayout(null);
        image = ImageIO.read(new File("C:\\Users\\Uto4ka\\Downloads\\img3_low_contrast.jpg"));

        JSlider slider = new JSlider(1, 100, 1);
        slider.setMinorTickSpacing(1);
        slider.setBounds(300, 475, 200, 50);
        slider.setMaximum(10);
        JButton button = new JButton();
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "Image Files", "jpg", "jpeg", "png", "gif");
                // Создание диалога для выбора файла
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(filter);
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    try {
                        image = ImageIO.read(selectedFile);
                        repaint();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    // label.setText("Выбран файл: " + selectedFile.getAbsolutePath());
                    // Здесь можно добавить код для работы с файлом, например, его чтение
                } else {
                 //  label.setText("Файл не выбран");
                }
            }
        });
        button.setText("Открыть");
        button.setBounds(300,550,100,50);
        JTextPane textPane = new JTextPane();
        textPane.setBounds(300,400,200,50);
        add(textPane);
        add(slider);
        add(button);

        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                peaks = slider.getValue();
                textPane.setText("Колличество пил "+peaks);
                repaint();
            }
        });
    }
    public static BufferedImage getPreparingImage(BufferedImage inputImage,int threshold) {
        // Пороговое значение
        BufferedImage outputImage = new BufferedImage(
                inputImage.getWidth(),
                inputImage.getHeight(),
                BufferedImage.TYPE_INT_RGB
        );

        for (int y = 0; y < inputImage.getHeight(); y++) {
            for (int x = 0; x < inputImage.getWidth(); x++) {
                // Получаем текущий пиксель
                Color color = new Color(inputImage.getRGB(x, y));
                // Рассчитываем яркость (грэйскейл)
                int grey = (int) (color.getRed() * 0.299 +
                        color.getGreen() * 0.587 +
                        color.getBlue() * 0.114);

                // Применяем пороговое преобразование
                int binaryValue = (grey*threshold)%255;

                // Устанавливаем новый цвет пикселя
                Color newColor = new Color(binaryValue, binaryValue, binaryValue);
                outputImage.setRGB(x, y, newColor.getRGB());
            }
        }

        return outputImage;
    }

    public static BufferedImage applyContrastEnhancementFilter(BufferedImage img, double a, double b) {
        int width = img.getWidth();
        int height = img.getHeight();

        int[][] kernel = {
                {0, -1, 0},
                {-1, 4, -1},
                {0, -1, 0}
        };

        BufferedImage newImage = new BufferedImage(width, height, img.getType());

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int color = 0;
                for (int di = -1; di <= 1; di++) {
                    for (int dj = -1; dj <= 1; dj++) {
                        int x = Math.min(Math.max(i + di, 0), width - 1);
                        int y = Math.min(Math.max(j + dj, 0), height - 1);
                        color += img.getRGB(x, y) * kernel[di + 1][dj + 1];
                    }
                }
                int finalColor = (int) (a + b * color);
                finalColor = Math.min(255, Math.max(0, finalColor));
                newImage.setRGB(i, j, new Color(finalColor, finalColor, finalColor).getRGB());
            }
        }
        return newImage;
    }
    public BufferedImage getGrayImage(BufferedImage b) {
        BufferedImage grayImage = new BufferedImage(b.getWidth(), b.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < grayImage.getWidth(); x++) {
            for (int y = 0; y < grayImage.getHeight(); y++) {
                Color c = new Color(b.getRGB(x, y));
                int gray = calculateBrightness(c);
                Color grayColor = new Color(gray, gray, gray);
                grayImage.setRGB(x, y, grayColor.getRGB());
            }
        }
        return grayImage;
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
        Image tempImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resizedBufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = resizedBufferedImage.createGraphics();
        g2d.drawImage(tempImage, 0, 0, null);
        g2d.dispose();
        return resizedBufferedImage;
    }

    public static int calculateBrightness(Color color) {
        return (int) (0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        BufferedImage img = resizeImage(image,200,200);
        BufferedImage grayImage = getGrayImage(img);
        g.drawImage(grayImage, 300, 0, this);
        g.drawImage(img, 0, 0, this);
        g.drawImage(getPreparingImage(img,peaks), 600, 0, this);
        g.drawImage(applyContrastEnhancementFilter(grayImage,0,1),0,400,this);
    }

    public static void main(String[] args) throws IOException {
        JFrame jFrame = new JFrame("Image Processing");
        ImageHandler imageHandler = new ImageHandler();
        jFrame.add(imageHandler);
        jFrame.setSize(1000, 1000);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setVisible(true);
    }
}
