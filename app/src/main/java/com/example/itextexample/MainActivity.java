package com.example.itextexample;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.itextexample.databinding.ActivityMainBinding;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        context = this;

        Dexter.withContext(context)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        binding.btnOk.setOnClickListener(view -> createPDFFile(Common.getAppPath(MainActivity.this) + "test_pdf.pdf"));
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                    }
                })
                .check();


    }

    private void createPDFFile(String path) {
        if (new File(path).exists())
            new File(path).delete();

        try {
            Document document =  new Document();
            //Save
            PdfWriter.getInstance(document, new FileOutputStream(path));
            //Open to write
            document.open();

            //Setting
            document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.addAuthor("Ashik");
            document.addCreator("Entraze");

            //Font Setting
            BaseColor colorAccent = new BaseColor(0,153,204,255);
            float fontSize = 20.0f;
            float valueFontSize = 26.0f;

            //Custom Font
            BaseFont fontName = BaseFont.createFont("assets/fonts/brandon_medium.otf", "UTF-8", BaseFont.EMBEDDED);

            //Create Title of document
            Font titleFont = new Font(fontName, 36.0f, Font.NORMAL, BaseColor.BLACK);
            addNewItem(document, "Order Details", Element.ALIGN_CENTER, titleFont);

            //Add more
            Font orderNumberFont = new Font(fontName, fontSize, Font.NORMAL, colorAccent);
            addNewItem(document, "Order No: ", Element.ALIGN_LEFT, orderNumberFont);

            Font orderNumberValueFont = new Font(fontName, valueFontSize, Font.NORMAL, BaseColor.BLACK);
            addNewItem(document, "#717171", Element.ALIGN_LEFT, orderNumberValueFont);

            addLineSeparator(document);

            addNewItem(document, "Order Date", Element.ALIGN_LEFT, orderNumberFont);
            addNewItem(document, "06 Aug 2020", Element.ALIGN_LEFT, orderNumberValueFont);

            addLineSeparator(document);

            addNewItem(document, "Account Name", Element.ALIGN_LEFT, orderNumberFont);
            addNewItem(document, "Eddy Lee", Element.ALIGN_LEFT, orderNumberValueFont);

            addLineSeparator(document);

            //Add Product order detail
            addLineSpace(document);
            addNewItem(document, "Product Details", Element.ALIGN_CENTER, titleFont);
            addLineSeparator(document);

            //Item 1
            addNewItemWithLeftAndRight(document, "Pizza 25", "(0.0%)", titleFont, orderNumberValueFont);
            addNewItemWithLeftAndRight(document, "12.0*1000", "12000.0", titleFont, orderNumberValueFont);

            addLineSeparator(document);

            //Item 1
            addNewItemWithLeftAndRight(document, "Pizza 26", "(0.0%)", titleFont, orderNumberValueFont);
            addNewItemWithLeftAndRight(document, "12.0*1000", "12000.0", titleFont, orderNumberValueFont);

            addLineSeparator(document);

            //Total
            addLineSpace(document);
            addLineSpace(document);

            addNewItemWithLeftAndRight(document, "Total", "24000.0", titleFont, orderNumberValueFont);

            document.close();

            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();

            printPDF();

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }

    private void printPDF() {
        PrintManager printManager =  (PrintManager) getSystemService(Context.PRINT_SERVICE);

        PrintDocumentAdapter printDocumentAdapter = new PdfDocumentAdapter(MainActivity.this,
                Common.getAppPath(MainActivity.this) + "test_pdf.pdf");
        printManager.print("Document", printDocumentAdapter, new PrintAttributes.Builder().build());
    }

    private void addNewItemWithLeftAndRight(Document document, String textLeft, String textRight,
                                            Font textLeftFont, Font textRightFont) throws DocumentException {
        Chunk chunkTextLeft = new Chunk(textLeft, textLeftFont);
        Chunk chunkTextRight = new Chunk(textRight, textRightFont);
        Paragraph p = new Paragraph(chunkTextLeft);
        p.add(new Chunk(new VerticalPositionMark()));
        p.add(chunkTextRight);
        document.add(p);
    }

    private void addLineSeparator(Document document) throws DocumentException {
        LineSeparator lineSeparator = new LineSeparator();
        lineSeparator.setLineColor(new BaseColor(0, 0, 0, 68));
        addLineSpace(document);
        document.add(new Chunk(lineSeparator));
        addLineSpace(document);
    }

    private void addLineSpace(Document document) throws DocumentException {
        document.add(new Paragraph(""));
    }

    private void addNewItem(Document document, String text, int align, Font font) throws DocumentException {
        Chunk chunk = new Chunk(text, font);
        Paragraph paragraph = new Paragraph(chunk);
        paragraph.setAlignment(align);
        document.add(paragraph);
    }

}