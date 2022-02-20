import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import net.sf.jxls.transformer.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipFile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ZipTest {

    private ClassLoader cl = ZipTest.class.getClassLoader();

    private String ZIP_FILE_NAME = "gods_bundle.zip";

    @Test
    public void zipTest() throws Exception {
        final ZipFile zf = new ZipFile(new File(cl.getResource(ZIP_FILE_NAME).toURI()));

        InputStream stream = zf.getInputStream(zf.getEntry("book.pdf"));
        assertTrue(new PDF(stream).text.contains("Of Gods and Mythologies"));

        stream = zf.getInputStream(zf.getEntry("gods.csv"));
        final CSVReader reader = new CSVReader(new InputStreamReader(stream));
        final List<String[]> list = reader.readAll();

        assertThat(list)
                .contains(
                        new String[] {"God", "Origin"},
                        new String[] {"Odin", "Scandinavian"},
                        new String[] {"Thor", "Scandinavian"},
                        new String[] {"Zeus", "Greek"}
                );

        stream = zf.getInputStream(zf.getEntry("TableOfContents.xlsx"));

        final XLS xls = new XLS(stream);

        final Sheet sheet = xls.excel.getSheetAt(0);


        assertEquals(sheet.getRow(0).getCell(1).getStringCellValue(), "Book Chapter");
        assertEquals(sheet.getRow(0).getCell(2).getStringCellValue(), "Number of Gods present");

        assertEquals(sheet.getRow(3).getCell(1).getNumericCellValue(), 3.0);
        assertEquals(sheet.getRow(3).getCell(2).getNumericCellValue(), 7.0);
        assertEquals(sheet.getRow(4).getCell(1).getNumericCellValue(), 4.0);
        assertEquals(sheet.getRow(4).getCell(2).getNumericCellValue(), 9.0);


    }



}
