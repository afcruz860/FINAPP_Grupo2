package com.example.firebase_finapp.Adaptadores;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.firebase_finapp.Models.Bancos;
import com.example.firebase_finapp.R;

import java.util.ArrayList;
public class ListViewBancosAdapter extends BaseAdapter {
    Context context;
    ArrayList<Bancos> BancoData;
    LayoutInflater layoutInflater;
    Bancos bancoModel;

    public ListViewBancosAdapter(Context context, ArrayList<Bancos> BancoData) {
        this.context = context;
        this.BancoData = BancoData;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return BancoData.size();
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null){
            rowView = layoutInflater.inflate(R.layout.lista_bancos,null, true);
        }
        // enlazamos las vistas
        TextView nombres = rowView.findViewById(R.id.nombres);
        TextView telefono = rowView.findViewById(R.id.telefono);
        TextView fecharegistro = rowView.findViewById(R.id.fechaRegistro);
        TextView direccion = rowView.findViewById(R.id.direccion);
        TextView horario = rowView.findViewById(R.id.horario);

        bancoModel = BancoData.get(position);
        nombres.setText(bancoModel.getNombres());
        telefono.setText(bancoModel.getTelefono());
        fecharegistro.setText(bancoModel.getFecharegistro());
        direccion.setText(bancoModel.getDireccion());
        horario.setText(bancoModel.getHorario());

        return rowView;
    }

    @Override
    public Object getItem(int position) {
        return BancoData.get(position);
    }
}
