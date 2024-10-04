import java.io.*;

public class BmpHandlerResizer {
    private int[][][] pixels;
    private int width = 640;
    private int height = 480;

    public BmpHandlerResizer(String filename) {
        BmpHandlerCore core = new BmpHandlerCore(filename);
        this.pixels = core.getPixels();
        resizeThin();
        resizeFlat();
    }

    private void resizeThin() {
        int newWidth = width / 2;
        int[][][] resizedPixels = new int[height][newWidth][3];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < newWidth; j++) {
                resizedPixels[i][j] = pixels[i][j * 2];
            }
        }

        saveImage("Image-thin.bmp", resizedPixels, newWidth, height);
    }

    private void resizeFlat() {
        int newHeight = height / 2;
        int[][][] resizedPixels = new int[newHeight][width][3];

        for (int i = 0; i < newHeight; i++) {
            for (int j = 0; j < width; j++) {
                resizedPixels[i][j] = pixels[i * 2][j];
            }
        }

        saveImage("Image-flat.bmp", resizedPixels, width, newHeight);
    }

    private void saveImage(String filename, int[][][] resizedPixels, int newWidth, int newHeight) {
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            // Escribir encabezado BMP aquí
            writeBMPHeader(fos, newWidth, newHeight);

            // Escribir píxeles de la imagen redimensionada
            for (int i = newHeight - 1; i >= 0; i--) {
                for (int j = 0; j < newWidth; j++) {
                    fos.write(resizedPixels[i][j][2]); // Azul
                    fos.write(resizedPixels[i][j][1]); // Verde
                    fos.write(resizedPixels[i][j][0]); // Rojo
                }
                fos.write(new byte[2]); // Bytes de alineación
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeBMPHeader(FileOutputStream fos, int newWidth, int newHeight) throws IOException {
        byte[] header = new byte[54];
        header[0] = 'B';
        header[1] = 'M';
        int fileSize = 54 + (newWidth * newHeight * 3) + (newHeight * 2);
        header[2] = (byte) (fileSize);
        header[3] = (byte) (fileSize >> 8);
        header[4] = (byte) (fileSize >> 16);
        header[5] = (byte) (fileSize >> 24);
        header[10] = 54;
        header[14] = 40;
        header[18] = (byte) (newWidth);
        header[19] = (byte) (newWidth >> 8);
        header[20] = (byte) (newWidth >> 16);
        header[21] = (byte) (newWidth >> 24);
        header[22] = (byte) (newHeight);
        header[23] = (byte) (newHeight >> 8);
        header[24] = (byte) (newHeight >> 16);
        header[25] = (byte) (newHeight >> 24);
        header[26] = 1;
        header[28] = 24;

        fos.write(header);
    }
}