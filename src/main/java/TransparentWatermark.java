import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;

public class TransparentWatermark {
    public static final String DEST = "./transparent_watermark.pdf";
    public static final String SRC = "src/main/resources/pdfs/Test.pdf";
    public static final String IMG = "src/main/resources/border.png";


    public static void main(String[] args) throws Exception {
        PdfReader reader = new PdfReader(SRC);
        PdfWriter writer = new PdfWriter(DEST);
        // we can't edit existing pdf
        // we create tmp pdf, copy existing content there, add new content and save
        // In order to do that we have to create PdfDocument with both reader and writer
        try (PdfDocument pdf = new PdfDocument(reader, writer, new StampingProperties().useAppendMode())) {

            // Document is a high level abstraction for manipulating data,
            // but it has poor abilities
            Document doc = new Document(pdf);
            ImageData img = ImageDataFactory.create(IMG);

            float w = img.getWidth();
            float h = img.getHeight();


            PdfPage page = pdf.getLastPage();
            Rectangle pageSize = page.getPageSize();

            float x = (pageSize.getLeft() + pageSize.getRight()) / 2;
            float y = (pageSize.getTop() + pageSize.getBottom()) / 2;

            // as I understood from official guideline
            // the image can be added to specified position only using canvas
            PdfCanvas over = new PdfCanvas(page);
            PdfExtGState gs1 = new PdfExtGState().setFillOpacity(0.5f);
            over.saveState();
            over.setExtGState(gs1);
            over.addImageAt(img, x - w / 2, y - h / 2, false);
            over.restoreState();

            // paragraph can be added using high level Document api
            Paragraph paragraph = new Paragraph()
                    .add("Test text")
                    .add("Test text")
                    .add("Test text")
                    .add("Test text")
                    .setOpacity(0.5f)
                    .setWidth((float) (w - 0.4 * w))
                    .setBorder(new SolidBorder(1));

            doc.showTextAligned(paragraph, x, y, pdf.getNumberOfPages(), TextAlignment.CENTER, VerticalAlignment.BOTTOM, 0);
        }
    }
}
