package com.lksnext.parkingplantilla.model.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lksnext.parkingplantilla.R;
import com.lksnext.parkingplantilla.model.domain.Reserva;
import com.lksnext.parkingplantilla.model.listener.ReservaListener;

import java.util.List;

public class ReservaActivaAdapter extends RecyclerView.Adapter<ReservaActivaAdapter.ReservaViewHolder> {

    private List<ReservaConId> reservas;
    private final ReservaListener listener;

    public ReservaActivaAdapter(List<ReservaConId> reservas, ReservaListener listener) {
        this.reservas = reservas;
        this.listener = listener;
    }

    public void setReservas(List<ReservaConId> reservas) {
        this.reservas = reservas;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReservaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reserva_activa, parent, false);
        return new ReservaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservaViewHolder holder, int position) {
        ReservaConId item = reservas.get(position);
        Reserva reserva = item.getReserva();

        holder.tvFecha.setText("Fecha: " + reserva.getFecha().toString());
        holder.tvHoraInicio.setText("Hora inicio: " + reserva.getHoraInicio().toString());
        holder.tvHoraFin.setText("Hora fin: " + reserva.getHoraFin().toString());
        holder.tvPlaza.setText("Plaza: " + reserva.getPlazaId());

        holder.btnCancelar.setOnClickListener(v -> listener.onCancelar(reserva, item.getDocId()));
    }

    @Override
    public int getItemCount() {
        return reservas.size();
    }

    public static class ReservaViewHolder extends RecyclerView.ViewHolder {
        TextView tvFecha, tvHoraInicio, tvHoraFin, tvPlaza;
        Button btnCancelar;

        public ReservaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvHoraInicio = itemView.findViewById(R.id.tvHoraInicio);
            tvHoraFin = itemView.findViewById(R.id.tvHoraFin);
            tvPlaza = itemView.findViewById(R.id.tvPlaza);
            btnCancelar = itemView.findViewById(R.id.btnCancelar);
        }
    }

    // Clase auxiliar para asociar Reserva con su documentId
    public static class ReservaConId {
        private final Reserva reserva;
        private final String docId;

        public ReservaConId(Reserva reserva, String docId) {
            this.reserva = reserva;
            this.docId = docId;
        }

        public Reserva getReserva() {
            return reserva;
        }

        public String getDocId() {
            return docId;
        }
    }
}
