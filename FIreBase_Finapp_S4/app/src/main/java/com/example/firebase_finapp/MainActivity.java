package com.example.firebase_finapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.firebase_finapp.Adaptadores.ListViewBancosAdapter;
import com.example.firebase_finapp.Models.Bancos;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private Location punto;
    private ArrayList <Bancos> ListBancos = new ArrayList<Bancos>();
    ArrayAdapter<Bancos> arrayAdapterBanco;
    ListViewBancosAdapter listViewBancosAdapter;
    LinearLayout linearLayoutEditar;
    ListView listViewBancos;
    EditText inputNombre, inputTelefono, inputDireccion, inputHorario;
    Button btnCancelar;

    Bancos bancoSeleccionado;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputNombre = findViewById(R.id.inputNombre);
        inputTelefono = findViewById(R.id.inputTelefono);
        inputDireccion = findViewById(R.id.inputDireccion);
        inputHorario = findViewById(R.id.inputHorario);
        btnCancelar = findViewById(R.id.btnCancelar);
        listViewBancos = findViewById(R.id.listViewBancos);
        linearLayoutEditar = findViewById(R.id.linearLayoutEditar);

        listViewBancos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                bancoSeleccionado = (Bancos) parent.getItemAtPosition(position);
                inputNombre.setText(bancoSeleccionado.getNombres());
                inputTelefono.setText(bancoSeleccionado.getTelefono());
                inputDireccion.setText(bancoSeleccionado.getDireccion());
                inputHorario.setText(bancoSeleccionado.getHorario());
                //hacemos visible el linearlayout

                linearLayoutEditar.setVisibility(view.VISIBLE);
            }
        });
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayoutEditar.setVisibility(view.GONE);
                bancoSeleccionado = null;
            }
        });

        incializarFirebase();
        listarBancos();




    }

    private void listarBancos() {
        databaseReference.child("Bancos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ListBancos.clear();
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                    Bancos p = objSnapshot.getValue(Bancos.class);
                    ListBancos.add(p);
                }

                //Inicializar nuestro adaptador
                listViewBancosAdapter = new ListViewBancosAdapter(MainActivity.this, ListBancos);
                listViewBancos.setAdapter(listViewBancosAdapter);
                arrayAdapterBanco = new ArrayAdapter<Bancos>(MainActivity.this, android.R.layout.simple_list_item_1, ListBancos
                );
                listViewBancos.setAdapter(arrayAdapterBanco);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

    private void incializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = firebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }
    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.crud_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        String nombres = inputNombre.getText().toString();
        String telefono = inputTelefono.getText().toString();
        String direccion = inputDireccion.getText().toString();
        String horario = inputHorario.getText().toString();


        switch (item.getItemId()) {
            case R.id.menu_agregar:
                insertar();
                break;
            case R.id.menu_guardar:
                if(bancoSeleccionado !=null) {
                    if (validarInputs() == false) {
                        Bancos p = new Bancos();
                        p.setIdbanco(bancoSeleccionado.getIdbanco());
                        p.setNombres(nombres);
                        p.setTelefono(telefono);
                        p.setDireccion(direccion);
                        p.setHorario(horario);
                        p.setFecharegistro(bancoSeleccionado.getFecharegistro());
                        p.setTimestamp(bancoSeleccionado.getTimestamp());
                        databaseReference.child("Bancos").child(p.getIdbanco()).setValue(p);
                        Toast.makeText(this, "Actualizado correctamente", Toast.LENGTH_SHORT).show();
                        linearLayoutEditar.setVisibility(View.GONE);
                        bancoSeleccionado = null;
                    }
                } else
                {
                    Toast.makeText(this,"Seleccione un banco",Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.menu_eliminar:
                if(bancoSeleccionado != null) {
                    Bancos p2= new Bancos();
                    p2.setIdbanco(bancoSeleccionado.getIdbanco());
                    databaseReference.child("Bancos").child(p2.getIdbanco()).removeValue();
                    linearLayoutEditar.setVisibility(View.GONE);
                    bancoSeleccionado = null;
                    Toast.makeText(this, "Eliminado correctamente", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(this, "Seleccione un banco para eliminar", Toast.LENGTH_SHORT).show();
                }
        }
        return super.onOptionsItemSelected(item);

    }

    private boolean validarInputs() {
        String nombre = inputNombre.getText().toString();
        String telefono = inputTelefono.getText().toString();
        String direccion = inputDireccion.getText().toString();
        String horario = inputHorario.getText().toString();
        if (nombre.isEmpty() || nombre.length() < 3) {
            showError(inputNombre, "Nombre invalido, (Mínimo 3 letras)");
            return true;
        } else if (telefono.isEmpty() || telefono.length() < 9) {
            showError(inputTelefono, "Telefono inválido, (Mínimo 9 números)");
            return true;
        } else if (direccion.isEmpty() || direccion.length() < 9) {
            showError(inputTelefono, "direccion inválida, (Mínimo 9 caracteres)");
            return true;
        } else if (horario.isEmpty() || horario.length() < 9) {
            showError(inputTelefono, "horario inválido, (Mínimo 9 caracteres)");
            return true;
        } else {
            return false;
        }
    }

    private void insertar() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(
                MainActivity.this
        );

        View mView = getLayoutInflater().inflate(R.layout.insertar, null);
        Button btnInsertar = (Button) mView.findViewById(R.id.btnInsertar);
        final EditText mInputNombres = (EditText) mView.findViewById(R.id.inputNombre);
        final EditText mInputTelefono = (EditText) mView.findViewById(R.id.inputTelefono);
        final EditText mInputDireccion = (EditText) mView.findViewById(R.id.inputDireccion);
        final EditText mInputHorario = (EditText) mView.findViewById(R.id.inputHorario);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();
        btnInsertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombres = mInputNombres.getText().toString();
                String telefono = mInputTelefono.getText().toString();
                String direccion = mInputDireccion.getText().toString();
                String horario = mInputHorario.getText().toString();
                if(nombres.isEmpty() || nombres.length()<3)
                {
                    showError(mInputNombres, "Nombre Invalido (Mínimo 3 letras)");
                } else if (telefono.isEmpty() || telefono.length()<9)
                {
                    showError(mInputTelefono, "Telefono inválido (Mínimo 9 números)");
                } else if (direccion.isEmpty() || direccion.length()<9)
                {
                    showError(mInputDireccion, "Direccion inválida (Mínimo 9 caracteres)");
                } else if (horario.isEmpty() || horario.length()<9)
                {
                    showError(mInputHorario, "Horario inválido (Mínimo 9 caracteres)");
                } else {
                    Bancos p = new Bancos();
                    p.setIdbanco(UUID.randomUUID().toString());
                    p.setNombres(nombres);
                    p.setTelefono(telefono);
                    p.setDireccion(direccion);
                    p.setHorario(horario);
                    p.setFecharegistro(getFechaNormal(getFechaMilisegundos()));
                    p.setTimestamp(getFechaMilisegundos()* -1);
                    databaseReference.child("Bancos").child(p.getIdbanco()).setValue(p);
                    Toast.makeText(MainActivity.this, "Registrado Correctamente", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }

            private String getFechaNormal(long fechamilisegundos) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                sdf.setTimeZone(TimeZone.getTimeZone("GMT-5"));
                String fecha = sdf.format(fechamilisegundos);
                return fecha;
            }
        });
    }

    private long getFechaMilisegundos() {
        Calendar calendar = Calendar.getInstance();
        long tiempounix = calendar.getTimeInMillis();

        return tiempounix;
    }
    public void verMapa (View view){
        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
        intent.putExtra("punto", punto);
        startActivity(intent);

    }

    private void showError(EditText input, String s) {
        input.requestFocus();
        input.setError(s);

    }



}