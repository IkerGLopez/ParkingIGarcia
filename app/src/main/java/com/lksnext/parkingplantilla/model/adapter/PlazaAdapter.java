package com.lksnext.parkingplantilla.model.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lksnext.parkingplantilla.R;
import com.lksnext.parkingplantilla.model.domain.Plaza;

import java.util.List;

public class PlazaAdapter extends RecyclerView.Adapter<PlazaAdapter.PlazaViewHolder> {

    public interface OnPlazaSelectedListener {
        void onPlazaSelected(Plaza plaza);
    }

    private final List<Plaza> plazaList;
    private final Context context;
    private final OnPlazaSelectedListener listener;
    private int selectedPosition = -1;

    public PlazaAdapter(Context context, List<Plaza> plazaList, OnPlazaSelectedListener listener) {
        this.context = context;
        this.plazaList = plazaList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PlazaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_plaza, parent, false);
        return new PlazaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlazaViewHolder holder, int position) {
        Plaza plaza = plazaList.get(position);

        // Determinar la imagen a usar
        int imageResId = getImageResource(plaza);
        holder.imgPlaza.setImageResource(imageResId);

        // Gestión del click (solo si la plaza está libre)
        holder.itemView.setOnClickListener(v -> {
            if (plaza.getEstado() == Plaza.Estado.LIBRE) {
                // Desmarcar anterior
                if (selectedPosition != -1) {
                    plazaList.get(selectedPosition).setEstado(Plaza.Estado.LIBRE);
                    notifyItemChanged(selectedPosition);
                }

                // Marcar nueva plaza
                plaza.setEstado(Plaza.Estado.SELECCIONADA);
                selectedPosition = holder.getAdapterPosition();
                notifyItemChanged(selectedPosition);

                listener.onPlazaSelected(plaza);
            }
        });
    }

    @Override
    public int getItemCount() {
        return plazaList.size();
    }

    public static class PlazaViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPlaza;

        public PlazaViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPlaza = itemView.findViewById(R.id.imgPlaza);
        }
    }

    private int getImageResource(Plaza plaza) {
        switch (plaza.getTipo()) {
            case NORMAL:
                switch (plaza.getEstado()) {
                    case LIBRE: return R.drawable.sitio_libre;
                    case OCUPADA: return R.drawable.sitio_ocupado;
                    case SELECCIONADA: return R.drawable.sitio_seleccionado;
                }
                break;
            case MOTO:
                switch (plaza.getEstado()) {
                    case LIBRE: return R.drawable.moto_sitio_libre;
                    case OCUPADA: return R.drawable.moto_sitio_ocupado;
                    case SELECCIONADA: return R.drawable.moto_sitio_seleccionado;
                }
                break;
            case ELECTRICO:
                switch (plaza.getEstado()) {
                    case LIBRE: return R.drawable.electrico_sitio_libre;
                    case OCUPADA: return R.drawable.electrico_sitio_ocupado;
                    case SELECCIONADA: return R.drawable.electrico_sitio_seleccionado;
                }
                break;
            case MINUSVALIDO:
                switch (plaza.getEstado()) {
                    case LIBRE: return R.drawable.minusvalido_sitio_libre;
                    case OCUPADA: return R.drawable.minusvalido_sitio_ocupado;
                    case SELECCIONADA: return R.drawable.minusvalido_sitio_seleccionado;
                }
                break;
        }
        return R.drawable.sitio_ocupado; // fallback
    }
}