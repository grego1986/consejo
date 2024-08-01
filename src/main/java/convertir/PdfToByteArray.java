package convertir;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class PdfToByteArray {

	 public static byte[] convertPdfToByteArray(File pdfFile) throws IOException {
	        FileInputStream fis = new FileInputStream(pdfFile);
	        byte[] byteArray = new byte[(int) pdfFile.length()];
	        fis.read(byteArray);
	        fis.close();
	        return byteArray;
	    }
}
