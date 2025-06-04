package com.boushib.eventsismagi.adapter;

import android.Manifest;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;
import com.boushib.eventsismagi.R;
import com.boushib.eventsismagi.model.MyEventsReservation;
import com.boushib.eventsismagi.pdfgenerator.PDFGenerator;
import com.bumptech.glide.Glide;
import java.io.File;
import java.util.List;



public class EventAdapterMyEventsClient extends RecyclerView.Adapter<EventAdapterMyEventsClient.EventViewHolder> {
    private static final int REQUEST_STORAGE_PERMISSION = 101;
    private final List<MyEventsReservation> reservationList;
    private final OnItemClickListener listener;
    private final Activity activity;

    public interface OnItemClickListener {
        void onItemClick(MyEventsReservation reservation);

        void onRequestPermission();
    }

    public EventAdapterMyEventsClient(List<MyEventsReservation> reservationList,
                                      OnItemClickListener listener,
                                      Activity activity) {
        this.reservationList = reservationList;
        this.listener = listener;
        this.activity = activity;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_item_myevents_client, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        holder.bind(reservationList.get(position));
    }

    @Override
    public int getItemCount() {
        return reservationList.size();
    }

    class EventViewHolder extends RecyclerView.ViewHolder {
        private final TextView titre, lieu, dateEvent, prix, confirmation;
        private final ImageView imageEvent;
        private final Button btnGetTicket;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            titre = itemView.findViewById(R.id.titreMyEventsClient);
            lieu = itemView.findViewById(R.id.lieuEventsClients);
            dateEvent = itemView.findViewById(R.id.dateDeEvenmentClietns);
            prix = itemView.findViewById(R.id.prixEventsCleint);
            confirmation = itemView.findViewById(R.id.confirmationCleint);
            imageEvent = itemView.findViewById(R.id.imageEventMyeventsClients);
            btnGetTicket = itemView.findViewById(R.id.btngetticketClients);
        }

        public void bind(MyEventsReservation reservation) {
            titre.setText(reservation.getTitre());
            lieu.setText("Lieu: " + reservation.getLieu());
            dateEvent.setText("Date: " + reservation.getDateEvent());
            prix.setText(String.format("Prix: %.2f €", reservation.getPrix()));
            confirmation.setText("Statut: " + (reservation.isPaymentValid() ? "Confirmé" : "En attente"));

            // Load image
            if (reservation.getImageUrl() != null && !reservation.getImageUrl().isEmpty()) {
                String imageUrl = reservation.getImageUrl().replace("http://", "https://");
                Glide.with(itemView.getContext())
                        .load(imageUrl)
                        .error(R.drawable.placeholder)
                        .into(imageEvent);
            } else {
                imageEvent.setImageResource(R.drawable.placeholder);
            }

            btnGetTicket.setOnClickListener(v -> handlePdfGeneration(reservation));

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(reservation);
                }
            });
        }

        private void handlePdfGeneration(MyEventsReservation reservation) {
            Context context = itemView.getContext();

            // For Android 10+, we don't need storage permission for app-specific storage
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                if (ContextCompat.checkSelfPermission(context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_STORAGE_PERMISSION);
                    return;
                }
            }

            new PdfGenerationTask(context, reservation).execute();
        }
    }

    private static class PdfGenerationTask extends AsyncTask<Void, Void, File> {
        private final Context context;
        private final MyEventsReservation reservation;

        public PdfGenerationTask(Context context, MyEventsReservation reservation) {
            this.context = context.getApplicationContext();
            this.reservation = reservation;
        }

        @Override
        protected File doInBackground(Void... voids) {
            try {
                Log.d("PDF_GEN", "Starting PDF generation");
                File pdfFile = PDFGenerator.generateTicket(context, reservation);
                Log.d("PDF_GEN", "PDF generated at: " + pdfFile.getAbsolutePath());
                return pdfFile;
            } catch (Exception e) {
                Log.e("PDF_GEN", "PDF generation failed", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(File pdfFile) {
            if (pdfFile != null && pdfFile.exists()) {
                sharePdfFile(pdfFile);
            } else {
                Toast.makeText(context,
                        "Échec de la génération du PDF",
                        Toast.LENGTH_LONG).show();
            }
        }

        private void sharePdfFile(File pdfFile) {
            try {
                if (pdfFile.exists()) {
                    Uri pdfUri = FileProvider.getUriForFile(context, "com.boushib.eventsismagi.provider", pdfFile);

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(pdfUri, "application/pdf");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    context.startActivity(intent);
                } else {
                    Log.e("PDF Share", "Le fichier PDF n'existe pas.");
                }



            } catch (Exception e) {
                Toast.makeText(context,
                        "Erreur de partage: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
                Log.e("PDF_SHARE", "Sharing failed", e);
            }
        }
    }
}
/*public class EventAdapterMyEventsClient extends RecyclerView.Adapter<EventAdapterMyEventsClient.EventViewHolder> {
    private List<MyEventsReservation> reservationList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(MyEventsReservation reservation);
    }

    public EventAdapterMyEventsClient(List<MyEventsReservation> reservationList, OnItemClickListener listener) {
        this.reservationList = reservationList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item_myevents_client, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        MyEventsReservation reservation = reservationList.get(position);
        holder.bind(reservation);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(reservation);
            }
        });
    }

    @Override
    public int getItemCount() {
        return reservationList.size();
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        private final TextView titre, lieu, dateEvent, prix, confirmation;
        private final ImageView imageEvent;
        private final Button btnGetTicket;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            titre = itemView.findViewById(R.id.titreMyEventsClient);
            lieu = itemView.findViewById(R.id.lieuEventsClients);
            dateEvent = itemView.findViewById(R.id.dateDeEvenmentClietns);
            prix = itemView.findViewById(R.id.prixEventsCleint);
            confirmation = itemView.findViewById(R.id.confirmationCleint);
            imageEvent = itemView.findViewById(R.id.imageEventMyeventsClients);
            btnGetTicket = itemView.findViewById(R.id.btngetticketClients);
        }

        @SuppressLint("StaticFieldLeak")
        public void bind(MyEventsReservation reservation) {
            titre.setText(reservation.getTitre());
            lieu.setText("Lieu: " + reservation.getLieu());
            dateEvent.setText("Date: " + reservation.getDateEvent());
            prix.setText(String.format("Prix: %.2f €", reservation.getPrix()));

            // Afficher le statut de confirmation
            String status = reservation.isPaymentValid() ? "Confirmé" : "En attente";
            confirmation.setText("Statut: " + status);

            btnGetTicket.setOnClickListener(v -> {
                try {


                    // Generate PDF in background thread
                    new AsyncTask<Void, Void, File>() {
                        @Override
                        protected File doInBackground(Void... voids) {
                            try {
                                return PDFGenerator.generateTicket(v.getContext(), reservation);
                            } catch (Exception e) {
                                Log.e("PDF", "Generation error", e);
                                return null;
                            }
                        }

                        @SuppressLint("StaticFieldLeak")
                        @Override
                        protected void onPostExecute(File pdfFile) {
                            if (pdfFile != null) {
                                sharePdfFile(v.getContext(), pdfFile);
                            } else {
                                Toast.makeText(v.getContext(),
                                        "Failed to generate PDF", Toast.LENGTH_SHORT).show();
                            }
                        }

                        private void sharePdfFile(Context context, File pdfFile) {
                            try {
                                Uri uri = FileProvider.getUriForFile(context,
                                        context.getPackageName() + ".provider",
                                        pdfFile);

                                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                shareIntent.setType("application/pdf");
                                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                                context.startActivity(Intent.createChooser(
                                        shareIntent,
                                        "Share Ticket"));
                            } catch (Exception e) {
                                Toast.makeText(context,
                                        "Sharing failed: " + e.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }.execute();

                } catch (Exception e) {
                    Toast.makeText(v.getContext(),
                            "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("PDF", "Error", e);
                }
            });


            String imageUrld = reservation.getImageUrl();
            if (reservation.getImageUrl() != null && !reservation.getImageUrl().isEmpty()) {
                String imageUrl = reservation.getImageUrl();

                if (imageUrl.startsWith("http://")) {
                    imageUrl = imageUrl.replace("http://", "https://");
                }

                Glide.with(itemView.getContext())
                        .load(imageUrl)
                        .into(imageEvent);
            } else {
                imageEvent.setImageResource(R.drawable.placeholder);
            }
        }
    }
}*/