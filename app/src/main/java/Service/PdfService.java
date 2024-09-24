package Service;

import Class.Event;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class PdfService {
    int pdfPageHeight = 1120;
    int pdfPageWidth = 792;

    public void generatePDF(
            Context context,
            HashMap<Event, ArrayList<Bitmap>> eventsWithBitmaps,
            int userId,
            int carId
    ) {
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();
        Paint title = new Paint();

        title.setTypeface(Typeface.create(Typeface.defaultFromStyle(Typeface.NORMAL), Typeface.NORMAL));
        title.setTextSize(15);
        title.setTextAlign(Paint.Align.LEFT);

        AtomicInteger pageCounter = new AtomicInteger(1);

        eventsWithBitmaps.forEach((event, bitmaps) -> {
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pdfPageWidth, pdfPageHeight, pageCounter.get()).create();
            PdfDocument.Page page = pdfDocument.startPage(pageInfo);
            Canvas canvas = page.getCanvas();

            int yPosition = 50;
            int xPosition = 60;

            String eventIndex = String.valueOf(pageCounter.get());
            canvas.drawText("Event number: " + eventIndex, xPosition, yPosition, title);
            yPosition += 30;
            canvas.drawText("Event title: " + event.getTitle(), xPosition, yPosition, title);
            yPosition += 30;
            canvas.drawText("Event description: " + event.getDescription(), xPosition, yPosition, title);
            yPosition += 30;
            canvas.drawText("Event type: " + event.getType(), xPosition, yPosition, title);
            yPosition += 30;
            canvas.drawText("Event pictures: ", xPosition, yPosition, title);

            int imagePosition = 1;
            int incrementalYPosition = 30;
            int xImagePosition = 50;
            int yImagePosition = yPosition + 30;
            int columnCount = 0;
            for (Bitmap bitmap : bitmaps) {
                canvas.drawBitmap(bitmap, xImagePosition, yImagePosition, paint);

                columnCount++;
                xImagePosition += 130;

                if (columnCount == 5) {
                    columnCount = 0;
                    xImagePosition = 50;
                    yImagePosition += incrementalYPosition;
                }

                imagePosition++;
                if (imagePosition > 10) {
                    break;
                }
            }

            try {
                pdfDocument.finishPage(page);
                pageCounter.getAndIncrement();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        pdfDocument.close();

        File picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File fileDirectory = new File(picturesDir, userId + "/" + carId + "/Pdfs/");

        if (!fileDirectory.exists()) {
            boolean mkdirResult = fileDirectory.mkdirs();
            fileDirectory.setReadable(true);
            fileDirectory.setWritable(true);
            fileDirectory.setExecutable(true);
            if (mkdirResult) {
                Toast.makeText(context, "Created directory: " + fileDirectory.getPath(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "Failed to create directory", Toast.LENGTH_LONG).show();
                return;
            }
        }

        File pdfFile = new File(fileDirectory, "Pdf_" + System.currentTimeMillis() + ".pdf");

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(pdfFile);
            pdfDocument.writeTo(fileOutputStream);
            fileOutputStream.close();
            Toast.makeText(context, "PDF saved successfully", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Failed to save PDF", Toast.LENGTH_LONG).show();
        }
    }
}
