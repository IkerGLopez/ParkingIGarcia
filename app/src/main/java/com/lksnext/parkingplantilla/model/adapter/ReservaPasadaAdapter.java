package com.lksnext.parkingplantilla.model.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lksnext.parkingplantilla.model.domain.Reserva;
import com.lksnext.parkingplantilla.R;

import java.util.List;

public class ReservaPasadaAdapter extends RecyclerView.Adapter<ReservaPasadaAdapter.ReservaViewHolder> {

    private List<Reserva> reservas;

    public ReservaPasadaAdapter(List<Reserva> reservas) {
        this.reservas = reservas;
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReservaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reserva_pasada, parent, false);
        return new ReservaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservaViewHolder holder, int position) {
        Reserva reserva = reservas.get(position);

        holder.tvFecha.setText("Fecha: " + safe(reserva.getFecha()));
        holder.tvHoraInicio.setText("Hora inicio: " + safe(reserva.getHoraInicio()));
        holder.tvHoraFin.setText("Hora fin: " + safe(reserva.getHoraFin()));
        holder.tvPlaza.setText("Plaza: " + safe(reserva.getPlazaId()));
    }

    @Override
    public int getItemCount() {
        return reservas.size();
    }

    public static class ReservaViewHolder extends RecyclerView.ViewHolder {
        TextView tvFecha, tvHoraInicio, tvHoraFin, tvPlaza;

        public ReservaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvHoraInicio = itemView.findViewById(R.id.tvHoraInicio);
            tvHoraFin = itemView.findViewById(R.id.tvHoraFin);
            tvPlaza = itemView.findViewById(R.id.tvPlaza);
        }
    }

    private String safe(String value) {
        return value != null ? value : "N/A";
    }

}
