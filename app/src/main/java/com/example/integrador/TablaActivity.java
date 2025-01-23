package com.example.integrador;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class TablaActivity extends AppCompatActivity {

    private boolean estanBloqueados = true; // Estado inicial: bloqueados

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabla); // Asegúrate de tener un layout llamado activity_tabla.xml

        // Referencias a los botones
        Button borrarButton = findViewById(R.id.Borrar);
        Button modificarButton = findViewById(R.id.Modificar);
        Button guardarButton = findViewById(R.id.GuardarButton);

        // Referencia al GridLayout
        GridLayout tabla = findViewById(R.id.Tabla);


        // Configuración de la tabla: filas y columnas
        int rowCount = 18; // Incluye encabezado
        int columnCount = 3;
        tabla.setRowCount(rowCount);
        tabla.setColumnCount(columnCount);

        // Crear la tabla con los valores predeterminados
        crearTablaConValoresPredeterminados(tabla);

        // Configuración de los botones
        borrarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TablaActivity.this, "Borrar presionado", Toast.LENGTH_SHORT).show();
                // Llamar al método para restaurar los valores predeterminados
                restaurarValoresPredeterminados(tabla);
            }
        });

        // Dentro del onClick de tu botón de guardar o modificar
        modificarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TablaActivity.this, "Modificar presionado", Toast.LENGTH_SHORT).show();
                modificarTabla(tabla);
            }
        });

        guardarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String[]> datos = new ArrayList<>();
                boolean hayErrores = false;

                // Recolectar datos de la tabla
                for (int i = 1; i <= 10; i++) {
                    String nombre = obtenerTextoDesdeId("nombre_" + i);
                    String registro = obtenerTextoDesdeId("registro_" + i);
                    String calificacionTexto = obtenerTextoDesdeId("calificacion_" + i);

                    if (nombre.isEmpty() || registro.isEmpty() || calificacionTexto.isEmpty()) {
                        Toast.makeText(TablaActivity.this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                        hayErrores = true;
                        break;
                    }

                    try {
                        int calificacion = Integer.parseInt(calificacionTexto);
                        if (calificacion > 100) {
                            Toast.makeText(TablaActivity.this, "La calificación no puede ser mayor a 100", Toast.LENGTH_SHORT).show();
                            hayErrores = true;
                            break;
                        }
                        datos.add(new String[]{nombre, registro, calificacionTexto});
                    } catch (NumberFormatException e) {
                        Toast.makeText(TablaActivity.this, "Calificación inválida", Toast.LENGTH_SHORT).show();
                        hayErrores = true;
                        break;
                    }
                }

                if (!hayErrores) {
                    for (String[] fila : datos) {
                        enviarDatosAlServidor(fila[0], fila[1], fila[2]);
                    }
                    Toast.makeText(TablaActivity.this, "Datos enviados correctamente", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
// Método para obtener texto desde un EditText por ID
        private String obtenerTextoDesdeId(String id) {
            int resID = getResources().getIdentifier(id, "id", getPackageName());
            EditText editText = findViewById(resID);
            return editText != null ? editText.getText().toString() : "";
        }

// Método para enviar datos al servidor
        private void enviarDatosAlServidor(String nombre, String registro, String calificacion) {
            String url = "http://tu_servidor/guardar_datos.php"; // Cambia por tu URL

            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    response -> {
                        // Respuesta del servidor
                        Toast.makeText(TablaActivity.this, response, Toast.LENGTH_SHORT).show();
                    },
                    error -> {
                        // Error al conectar
                        Toast.makeText(TablaActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("Nombre", nombre);
                    params.put("Registro", registro);
                    params.put("Calificacion", calificacion);
                    return params;
                }
            };

            queue.add(stringRequest);
        }

        /**
         * Método para crear la tabla con los valores predeterminados usando EditText.
         * @param tabla El GridLayout donde se añadirá el contenido.
         */
    private void crearTablaConValoresPredeterminados(GridLayout tabla) {
        // Encabezados de la tabla (EditText también)
        agregarEditTextATabla(tabla, "Nombre", 0, 0, true);
        agregarEditTextATabla(tabla, "Registro", 0, 1, true);
        agregarEditTextATabla(tabla, "Calificación", 0, 2, true);

        // Datos predeterminados para cada fila (con EditText)
        for (int i = 1; i <= 10; i++) {
            agregarEditTextATabla(tabla, "Alumno " + i, i, 0, true);
            agregarEditTextATabla(tabla, "Registro " + i, i, 1, true);
            agregarEditTextATabla(tabla, "Calificación " + i, i, 2, true);
        }
    }

    /**
     * Método para restaurar la tabla con los valores predeterminados (después de presionar el botón "Borrar").
     * @param tabla El GridLayout donde se añadirá el contenido.
     */
    private void restaurarValoresPredeterminados(GridLayout tabla) {
        // Limpiar todos los elementos en la tabla (GridLayout)
        tabla.removeAllViews();

        // Volver a crear la tabla con los valores predeterminados
        crearTablaConValoresPredeterminados(tabla);
    }

    /**
     * Método para agregar un EditText al GridLayout en una posición específica.
     * @param tabla El GridLayout donde se añadirá el EditText.
     * @param texto El texto inicial del EditText.
     * @param fila La fila donde se colocará el EditText.
     * @param columna La columna donde se colocará el EditText.
     * @param bloqueado Estado de bloqueo del EditText (true para bloqueado).
     */
    private void agregarEditTextATabla(GridLayout tabla, String texto, int fila, int columna, boolean bloqueado) {

        EditText editText = new EditText(this);
        editText.setText(texto);

        // Si bloqueado es true, el EditText estará bloqueado.
        if (bloqueado) {
            bloquearEditText(editText);
        } else {
            desbloquearEditText(editText);
        }

        GridLayout.LayoutParams params = new GridLayout.LayoutParams(
                GridLayout.spec(fila),
                GridLayout.spec(columna)
        );
        params.setMargins(8, 8, 8, 8); // Márgenes opcionales
        editText.setLayoutParams(params);
        tabla.addView(editText);
    }

    /**
     * Método para bloquear un EditText.
     * @param editText El EditText que se quiere bloquear.
     */
    private void bloquearEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setFocusableInTouchMode(false);
        editText.setClickable(false);
    }

    /**
     * Método para desbloquear un EditText.
     * @param editText El EditText que se quiere desbloquear.
     */
    private void desbloquearEditText(EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.setClickable(true);
    }

    /**
     * Método para modificar la tabla, bloqueando o desbloqueando los EditText.
     * @param tabla El GridLayout donde están los EditText.
     */
    private void modificarTabla(GridLayout tabla) {
        for (int i = 0; i < tabla.getChildCount(); i++) {
            View child = tabla.getChildAt(i);

            if (child instanceof EditText) {
                if (estanBloqueados) {
                    desbloquearEditText((EditText) child); // Desbloquear EditText
                } else {
                    bloquearEditText((EditText) child); // Bloquear EditText
                }
            }
        }

        // Cambiar el estado de bloqueo
        estanBloqueados = !estanBloqueados;
    }
}
