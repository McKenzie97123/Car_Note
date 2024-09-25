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
import java.util.Iterator;
import java.util.Map;
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

        AtomicInteger pageCounter = new AtomicInteger(0);
        Iterator<Map.Entry<Event, ArrayList<Bitmap>>> iterator = eventsWithBitmaps.entrySet().iterator();
        while (iterator.hasNext()) {
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pdfPageWidth, pdfPageHeight, pageCounter.get()).create();
            PdfDocument.Page page = pdfDocument.startPage(pageInfo);
            Canvas canvas = page.getCanvas();

            Map.Entry<Event, ArrayList<Bitmap>> firstEntry = iterator.next();
            int yPosition = 50;
            int xPosition = 60;

            pageCounter.getAndIncrement();
            canvas.drawText("Event number: " + pageCounter.get(), xPosition, yPosition, title);
            yPosition += 28;
            canvas.drawText("Event title: " + firstEntry.getKey().getTitle(), xPosition, yPosition, title);
            yPosition += 28;
            canvas.drawText("Event description: " + firstEntry.getKey().getDescription(), xPosition, yPosition, title);
            yPosition += 28;
            canvas.drawText("Event type: " + firstEntry.getKey().getType(), xPosition, yPosition, title);
            yPosition += 28;
            canvas.drawText("Event pictures: ", xPosition, yPosition, title);

            drawEventImages(canvas, firstEntry.getValue(), yPosition + 28, xPosition, paint);


            if (iterator.hasNext()) {
                Map.Entry<Event, ArrayList<Bitmap>> secondEntry = iterator.next();
                yPosition = pdfPageHeight / 2;

                pageCounter.getAndIncrement();
                canvas.drawText("Event number: " + pageCounter.get(), xPosition, yPosition, title);
                yPosition += 28;
                canvas.drawText("Event title: " + secondEntry.getKey().getTitle(), xPosition, yPosition, title);
                yPosition += 28;
                canvas.drawText("Event description: " + secondEntry.getKey().getDescription(), xPosition, yPosition, title);
                yPosition += 28;
                canvas.drawText("Event type: " + secondEntry.getKey().getType(), xPosition, yPosition, title);
                yPosition += 28;
                canvas.drawText("Event pictures: ", xPosition, yPosition, title);

                drawEventImages(canvas, secondEntry.getValue(), yPosition + 28, xPosition, paint);
            }

            pdfDocument.finishPage(page);
        }

        File picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File fileDirectory = new File(picturesDir, userId + "/" + carId + "/Pdfs/");
        if (!fileDirectory.exists()) {
            boolean mkdirResult = fileDirectory.mkdirs();
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
        pdfDocument.close();
    }

    private void drawEventImages(Canvas canvas, ArrayList<Bitmap> bitmaps, int yPosition, int xPosition, Paint paint) {
        int xImagePosition = xPosition;
        int yImagePosition = yPosition;
        int columnCount = 0;
        int imagePosition = 1;

        for (Bitmap bitmap : bitmaps) {
            canvas.drawBitmap(bitmap, xImagePosition, yImagePosition, paint);

            columnCount++;
            xImagePosition += 130;

            if (columnCount == 5) {
                columnCount = 0;
                xImagePosition = xPosition;
                yImagePosition += 150;
            }

            imagePosition++;
            if (imagePosition > 10) {
                break;
            }
        }
    }
}
