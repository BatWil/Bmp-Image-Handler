public class BMPImageHandler {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Uso: java BMPImageHandler <flag> <filename>");
            return;
        }

        String flag = args[0];
        String filename = args[1];

        switch (flag) {
            case "-core":
                new BmpHandlerCore(filename);
                break;
            case "-rotate":
                new BmpHandlerRotator(filename);
                break;
            case "-resize":
                new BmpHandlerResizer(filename);
                break;
            case "-all":
                new BmpHandlerCore(filename);
                new BmpHandlerRotator(filename);
                new BmpHandlerResizer(filename);
                break;
            case "-help":
                printHelp();
                break;
            default:
                System.out.println("Flag no reconocido.");
                break;
        }
    }

    private static void printHelp() {
        System.out.println("Uso:");
        System.out.println("java BMPImageHandler -core <filename>");
        System.out.println("java BMPImageHandler -rotate <filename>");
        System.out.println("java BMPImageHandler -resize <filename>");
        System.out.println("java BMPImageHandler -all <filename>");
        System.out.println("java BMPImageHandler -help");
    }
}