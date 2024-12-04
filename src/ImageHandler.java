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
        image = ImageIO.read(new File("C:\\Users\\Alex\\Downloads\\img2_low_contrast.jpg"));

        JSlider slider = new JSlider(1, 100, 1);
        slider.setMinorTickSpacing(1);
        slider.setBounds(300, 475, 200, 50);
        slider.setMaximum(200);
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
                textPane.setText("Колличество пиков "+peaks);
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
                int binaryValue = (grey >= threshold) ? 255 : 0;

                // Устанавливаем новый цвет пикселя
                Color newColor = new Color(binaryValue, binaryValue, binaryValue);
                outputImage.setRGB(x, y, newColor.getRGB());
            }
        }

        return outputImage;
    }
//}
//        BufferedImage outputImage = new BufferedImage(inputImage.getWidth(),inputImage.getHeight(),BufferedImage.TYPE_INT_RGB);
//        int period = 64;
//        for (int y = 0; y < inputImage.getHeight(); y++) {
//            for (int x = 0; x < inputImage.getWidth(); x++) {
//                Color color = new Color(inputImage.getRGB(x, y));
//                int red = sawtoothTransform(color.getRed(), period);
//                int green = sawtoothTransform(color.getGreen(), period);
//                int blue = sawtoothTransform(color.getBlue(), period);
//
//                Color newColor = new Color(red, green, blue);
//                outputImage.setRGB(x, y, newColor.getRGB());
//            }
//        }
//        return outputImage;
   // }
private static int calculateBrightness(int rgb) {
    Color color = new Color(rgb);
    return (int) (color.getRed() * 0.299 + color.getGreen() * 0.587 + color.getBlue() * 0.114);
}
    public static BufferedImage applyContrastEnhancementFilter(BufferedImage img,double a,double b) {
        int width = img.getWidth();
        int height = img.getHeight();

        // Фильтр повышения контрастности
        int[][] kernel = {
                {0, -1, 0},
                {-1, 8, -1},
                {0, -1, 0}
        };

        BufferedImage newImage = new BufferedImage(width, height, img.getType());
        for (int i=1;i<img.getWidth()-1;i++) {
            for (int j = 1; j < img.getHeight() - 1;j++) {
                int color = (int) (a + b * (
                                img.getRGB(i - 1, j - 1) * kernel[0][0] +
                                img.getRGB(i - 1, j) * kernel[0][1] +
                                img.getRGB(i - 1, j + 1) * kernel[0][2] +
                                img.getRGB(i, j - 1) * kernel[1][0] +
                                img.getRGB(i, j) * kernel[1][1] +
                                img.getRGB(i, j + 1) * kernel[1][2] +
                                img.getRGB(i + 1, j - 1) * kernel[2][0] +
                                img.getRGB(i + 1, j) * kernel[2][1] +
                                img.getRGB(i + 1, j + 1) * kernel[2][2]
                ));
                int finalColor = (int) Math.min(255, Math.max(0, color));
                newImage.setRGB(i, j, new Color(finalColor,finalColor,finalColor).getRGB());
            }
        }
                //Color newColor = new Color(newValue, newValue, newValue)
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

    public static int applySawtoothContrast(int value, int numPeaks, double phase) {
        double period = 255.0 / numPeaks;
        double newValue = 0.5 * (1 + Math.sin((2 * Math.PI / period) * value + phase));
        return (int) (newValue * 255);
    }

    public static BufferedImage applySawtoothContrast(BufferedImage img, int numPeaks, double phase) {
        int width = img.getWidth();
        int height = img.getHeight();

        BufferedImage newImage = new BufferedImage(width, height, img.getType());

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color originalColor = new Color(img.getRGB(x, y));

                int newRed = applySawtoothContrast(originalColor.getRed(), numPeaks, phase);
                int newGreen = applySawtoothContrast(originalColor.getGreen(), numPeaks, phase);
                int newBlue = applySawtoothContrast(originalColor.getBlue(), numPeaks, phase);


                newRed = Math.min(255, Math.max(0, newRed));
                newGreen = Math.min(255, Math.max(0, newGreen));
                newBlue = Math.min(255, Math.max(0, newBlue));

                Color newColor = new Color(newRed, newGreen, newBlue);
                newImage.setRGB(x, y, newColor.getRGB());
            }
        }

        return newImage;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        BufferedImage img = resizeImage(image,200,200);
        BufferedImage grayImage = getGrayImage(img);
        g.drawImage(grayImage, 300, 0, this);
        g.drawImage(img, 0, 0, this);
        g.drawImage(getPreparingImage(grayImage,peaks), 600, 0, this);
        g.drawImage(applyContrastEnhancementFilter(grayImage,0,1/4.),0,400,this);
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
