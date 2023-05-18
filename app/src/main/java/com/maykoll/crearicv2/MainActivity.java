package com.maykoll.crearicv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText email, password;
    Button btn_sigin;
    Button btn_sigup;
    DBInterface dbInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Base de datos
        dbInterface = new DBInterface(this);
        dbInterface.abre();

        validarUsuario();
        crearUsuario();

    }

    public void crearUsuario() {
        //listener
        btn_sigup = findViewById(R.id.btn_signup);
        email = findViewById(R.id.txt_emailloggin);
        password = findViewById(R.id.txt_passlogin);

        btn_sigup.setOnClickListener(view -> {
            String correo = email.getText().toString();
            String pass = password.getText().toString();

            if (correo.equals("") || pass.equals("")) {
                Toast.makeText(MainActivity.this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                boolean checkuser = dbInterface.checkusername(correo);
                if (!checkuser) {
                    boolean insert = dbInterface.insertarUsuario(correo, pass);
                    if (insert == true) {
                        Toast.makeText(MainActivity.this, "Resgistrado Correctamente", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, Formulario.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "Registro Fallido", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Ya existe el usuario!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void validarUsuario() {
        btn_sigin = findViewById(R.id.btn_signin);
        email = findViewById(R.id.txt_emailloggin);
        password = findViewById(R.id.txt_passlogin);

        btn_sigin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String correo = email.getText().toString();
                String pass = password.getText().toString();
                if (correo.equals("") || pass.equals("")) {
                    Toast.makeText(MainActivity.this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    boolean checkuserpass = dbInterface.checkusernamepassword(correo, pass);
                    if (checkuserpass == true) {

                        Toast.makeText(MainActivity.this, "Usuario válido", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), Formulario.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "Credenciales no Válidas", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    @Override
    public void onStop() {
        super.onStop();
        dbInterface.cierra();
    }

    @Override
    public void onResume() {
        super.onResume();
        dbInterface.abre(); // Volver a abrir la base de datos
    }
}
