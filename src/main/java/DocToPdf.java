import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.*;

public class DocToPdf {

    public static final String WORD = "src/main/resources/word-doc.docx";
    public static final String PDF = "./pdf-doc.pdf";

    public static void main(String[] args) throws Exception {
        InputStream in = new FileInputStream(WORD);
        XWPFDocument document = new XWPFDocument(in);
        PdfOptions options = PdfOptions.create();
        OutputStream out = new FileOutputStream(PDF);
        PdfConverter.getInstance().convert(document, out, options);

        document.close();
        out.close();
    }
}
