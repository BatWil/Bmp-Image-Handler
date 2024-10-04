import java.io.*;

public class BmpHandlerRotator {
    private int[][][] pixels;
    private int width = 640;
    private int height = 480;

    public BmpHandlerRotator(String filename) {
        BmpHandlerCore core = new BmpHandlerCore(filename);
        this.pixels = core.getPixels();
        rotate180Horizontal();
        rotate180Vertical();
        saveImage("Image-rotated.bmp");
    }

    private void rotate180Horizontal() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width / 2; j++) {
                int[] temp = pixels[i][j];
                pixels[i][j] = pixels[i][width - 1 - j];
                pixels[i][width - 1 - j] = temp;
            }
        }
    }

    private void rotate180Vertical() {
        for (int i = 0; i < height / 2; i++) {
            for (int j = 0; j < width; j++) {
                int[] temp = pixels[i][j];
                pixels[i][j] = pixels[height - 1 - i][j];
                pixels[height - 1 - i][j] = temp;
            }
        }
    }

    private void saveImage(String filename) {
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            // Escribir encabezado BMP aquí
            writeBMPHeader(fos);

            // Escribir píxeles de la imagen rotada
            for (int i = height - 1; i >= 0; i--) {
                for (int j = 0; j < width; j++) {
                    fos.write(pixels[i][j][2]); // Azul
                    fos.write(pixels[i][j][1]); // Verde
                    fos.write(pixels[i][j][0]); // Rojo
                }
                fos.write(new byte[2]); // Bytes de alineación
            }
        } catch (IOException e) {
            e.printStackTrace();
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