package com.boushib.eventsismagi.pdfgenerator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.util.Log;
import androidx.core.content.ContextCompat;
import com.boushib.eventsismagi.R;
import com.boushib.eventsismagi.model.MyEventsReservation;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import java.io.ByteArrayOutputStream;
import java.io.File;
import android.content.ContentValues;

import android.os.Environment;
import android.provider.MediaStore;
import android.net.Uri;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.IOException;


import java.io.FileOutputStream;

public class PDFGenerator {

    public static File generateTicket(Context context, MyEventsReservation reservation) throws Exception {
        File dir = new File(context.getExternalFilesDir(null), "tickets");
        if (!dir.exists() && !dir.mkdirs()) {
            throw new Exception("Failed to create tickets directory");
        }

        File file = new File(dir, "ticket_" + System.currentTimeMillis() + ".pdf");

        try (PdfWriter writer = new PdfWriter(file);
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {



            // Charger l'image depuis drawable
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.home1);
            if (drawable == null) {
                throw new Exception("Impossible de charger l'image home1 !");
            }

            // Convertir Drawable en Bitmap


            Bitmap bitmap = getBitmapFromDrawable(drawable);
            // Vérifier que le bitmap est bien chargé
            if (bitmap == null) {
                throw new Exception("Le bitmap de home1 est null !");
            }


            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image logo = new Image(ImageDataFactory.create(stream.toByteArray()))
                    .setWidth(150)
                    .setHorizontalAlignment(HorizontalAlignment.CENTER);
            document.add(logo);

            // Add content
            DeviceRgb primaryColor = new DeviceRgb(0, 102, 204);

            document.add(new Paragraph("RECU DE RESERVATION")
                    .setFontSize(20)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(primaryColor));

            document.add(new Paragraph("\n"));

            addField(document, "Événement:", reservation.getTitre(), 18f, primaryColor);
            addField(document, "Date:", reservation.getDateEvent(), 12f);
            addField(document, "Lieu:", reservation.getLieu(), 12f);
            addField(document, "Prix:", String.format("%.2f €", reservation.getPrix()), 12f);
            addField(document, "Statut:", reservation.isPaymentValid() ? "Payé" : "Non payé", 12f);

            document.add(new Paragraph("\nMerci pour votre réservation !\n")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setItalic());
            savePdfToDownloads(context, file);
            return file;
        } catch (Exception e) {
            if (file.exists()) {
                file.delete();
            }
            throw e;
        }
    }

    private static Bitmap getBitmapFromDrawable(Drawable drawable) {

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof VectorDrawable) {
            VectorDrawable vectorDrawable = (VectorDrawable) drawable;
            Bitmap bitmap = Bitmap.createBitmap(
                    vectorDrawable.getIntrinsicWidth(),
                    vectorDrawable.getIntrinsicHeight(),
                    Bitmap.Config.ARGB_8888
            );
            Canvas canvas = new Canvas(bitmap);
            vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            vectorDrawable.draw(canvas);
            return bitmap;
        } else {
            return null;
        }
    }

    private static void addField(Document doc, String label, String value, float fontSize) {
        doc.add(new Paragraph(label + " " + value)
                .setFontSize(fontSize)
                .setMarginBottom(5f));
    }

    private static void addField(Document doc, String label, String value, float fontSize, DeviceRgb color) {
        doc.add(new Paragraph(label)
                .setFontSize(fontSize)
                .setFontColor(color)
                .setBold()
                .add(new Paragraph(value)
                        .setFontColor(new DeviceRgb(0, 0, 0))
                        .setBold()));
    }

    public static void savePdfToDownloads(Context context, File pdfFile) {
        if (!pdfFile.exists()) {
            Log.e("PDF Save", "Le fichier PDF n'existe pas.");
            return;
        }

        String fileName = pdfFile.getName(); // Nom du fichier PDF
        ContentValues values = new ContentValues();
        values.put(MediaStore.Downloads.DISPLAY_NAME, fileName);
        values.put(MediaStore.Downloads.MIME_TYPE, "application/pdf");
        values.put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

        Uri uri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            uri = context.getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values);
        }

        try (OutputStream outputStream = context.getContentResolver().openOutputStream(uri);
             FileInputStream inputStream = new FileInputStream(pdfFile)) {

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            Log.d("PDF Save", "PDF enregistré dans le dossier Téléchargements !");
        } catch (IOException e) {
            Log.e("PDF Save", "Erreur lors de l'enregistrement du PDF", e);
        }
    }

}