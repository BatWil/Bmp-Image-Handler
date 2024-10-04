import java.io.*;

public class BmpHandlerCore {
    private static final int RED = 0;
    private static final int GREEN = 1;
    private static final int BLUE = 2;

    private int width = 640; // Ancho de la imagen
    private int height = 480; // Alto de la imagen
    private int[][][] pixels;

    public BmpHandlerCore(String filename) {
        pixels = new int[height][width][3];
        readBMP(filename);
        generateRGBImages();
        generateSepiaImage();
    }

    private void readBMP(String filename) {
        try (FileInputStream fis = new FileInputStream(filename)) {
            fis.skip(54); // Saltar encabezado BMP

            for (int i = height - 1; i >= 0; i--) {
                for (int j = 0; j < width; j++) {
                    int b = fis.read(); // Azul
                    int g = fis.read(); // Verde
                    int r = fis.read(); // Rojo
                    pixels[i][j][RED] = r;
                    pixels[i][j][GREEN] = g;
                    pixels[i][j][BLUE] = b;
                }
                fis.skip(2); // Saltar bytes de alineación
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo BMP: " + e.getMessage());
        }
    }

    private void generateRGBImages() {
        saveImage("Image-red.bmp", RED);
        saveImage("Image-green.bmp", GREEN);
        saveImage("Image-blue.bmp", BLUE);
    }

    private void saveImage(String filename, int colorIndex) {
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            writeBMPHeader(fos);
            for (int i = height - 1; i >= 0; i--) {
                for (int j = 0; j < width; j++) {
                    int colorValue = pixels[i][j][colorIndex];
                    fos.write(colorValue); // Escribir el valor del color
                    fos.write(0); // Otros colores como 0
                    fos.write(0); // Otro color como 0
                }
                fos.write(new byte[2]); // Bytes de alineación
            }
        } catch (IOException e) {
            System.err.println("Error al guardar la imagen: " + e.getMessage());
        }
    }

    private void generateSepiaImage() {
        int[][][] sepiaPixels = new int[height][width][3];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int r = pixels[i][j][RED];
                int g = pixels[i][j][GREEN];
                int b = pixels[i][j][BLUE];

                int tr = (int) (0.393 * r + 0.769 * g + 0.189 * b);
                int tg = (int) (0.349 * r + 0.686 * g + 0.168 * b);
                int tb = (int) (0.272 * r + 0.534 * g + 0.131 * b);

                sepiaPixels[i][j][RED] = Math.min(255, tr);
                sepiaPixels[i][j][GREEN] = Math.min(255, tg);
                sepiaPixels[i][j][BLUE] = Math.min(255, tb);
            }
        }
        saveSepiaImage("Image-sepia.bmp", sepiaPixels);
    }

    private void saveSepiaImage(String filename, int[][][] sepiaPixels) {
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            writeBMPHeader(fos);
            for (int i = height - 1; i >= 0; i--) {
                for (int j = 0; j < width; j++) {
                    fos.write(sepiaPixels[i][j][BLUE]);
                    fos.write(sepiaPixels[i][j][GREEN]);
                    fos.write(sepiaPixels[i][j][RED]);
                }
                fos.write(new byte[2]); // Bytes de alineación
            }
        } catch (IOException e) {
            System.err.println("Error al guardar la imagen sepia: " + e.getMessage());
        }
    }

    private void writeBMPHeader(FileOutputStream fos) throws IOException {
        byte[] header = new byte[54];
        header[0] = 'B';
        header[1] = 'M';
        int fileSize = 54 + (width * height * 3) + (height * 2);
        header[2] = (byte) (fileSize);
        header[3] = (byte) (fileSize >> 8);
        header[4] = (byte) (fileSize >> 16);
        header[5] = (byte) (fileSize >> 24);
        header[10] = 54;
        header[14] = 40;
        header[18] = (byte) (width);
        header[19] = (byte) (width >> 8);
        header[20] = (byte) (width >> 16);
        header[21] = (byte) (width >> 24);
        header[22] = (byte) (height);
        header[23] = (byte) (height >> 8);
        header[24] = (byte) (height >> 16);
        header[25] = (byte) (height >> 24);
        header[26] = 1;
        header[28] = 24;

        fos.write(header);
    }
}
