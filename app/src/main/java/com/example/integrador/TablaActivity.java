package com.example.integrador;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import android.util.Log;

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
        //borrar todos los datos al inicio
        restaurarValoresPredeterminados(tabla);

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

        guardarButton.setOnClickListener(v -> {
            List<Map<String, Object>> datos = new ArrayList<>(); // Cambiar tipo a Object para manejar String e int

            // Recorremos todas las filas de la tabla
            for (int i = 0; i < tabla.getChildCount(); i++) {
                View row = tabla.getChildAt(i);

                // Verificamos si la fila es una instancia de TableRow
                if (row instanceof TableRow) {
                    TableRow tableRow = (TableRow) row;

                    // Asegúrate de que hay suficientes celdas (en este caso 3: Nombre, Registro, Calificación)
                    if (tableRow.getChildCount() >= 3) {
                        String nombre = ((TextView) tableRow.getChildAt(0)).getText().toString().trim();
                        int registro = 0;
                        int calificacion = 0;

                        // Validar que los valores de registro y calificación sean enteros válidos
                        try {
                            registro = Integer.parseInt(((TextView) tableRow.getChildAt(1)).getText().toString().trim());
                            calificacion = Integer.parseInt(((TextView) tableRow.getChildAt(2)).getText().toString().trim());
                        } catch (NumberFormatException e) {
                            // En caso de que no sean números válidos, se puede manejar el error
                            Log.e("Guardar", "Registro o Calificación no es un número válido", e);
                            continue; // Saltar esta fila si los valores no son válidos
                        }

                        // Validamos que los campos no estén vacíos
                        if (!nombre.isEmpty()) {
                            // Crear un mapa con los datos de la fila
                            Map<String, Object> filaDatos = new HashMap<>();
                            filaDatos.put("Nombre", nombre);
                            filaDatos.put("Registro", registro);  // Guardar como int
                            filaDatos.put("Calificacion", calificacion);  // Guardar como int

                            // Agregar la fila al listado de datos
                            datos.add(filaDatos);
                        }
                    }
                }
            }

            // Enviar los datos al servidor
            enviarDatosComoJson(datos);
        });
    }
    // Método para enviar los datos como un JSON al servidor
    private void enviarDatosComoJson(List<Map<String, Object>> datos) {
        String url = "http://10.0.2.2/tabla.php";

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Toast.makeText(TablaActivity.this, response, Toast.LENGTH_SHORT).show();
                },
                error -> {
                    Toast.makeText(TablaActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                try {
                    // Crear un JSONArray que contendrá los objetos JSON
                    JSONArray jsonArray = new JSONArray();

                    // Recorrer los datos y crear un JSONObject por cada fila
                    for (Map<String, Object> fila : datos) {
                        JSONObject jsonObject = new JSONObject();

                        // Obtener calificación y registro como enteros, ya que se almacenan como int
                        int calificacion = (int) fila.get("Calificacion");
                        int registro = (int) fila.get("Registro");

                        // Insertar los datos en el objeto JSON
                        jsonObject.put("Nombre", fila.get("Nombre"));
                        jsonObject.put("Calificacion", calificacion);  // Se agrega como int
                        jsonObject.put("Registro", registro);  // Se agrega como int

                        // Agregar el objeto JSON al JSONArray
                        jsonArray.put(jsonObject);
                    }

                    // Convertir el JSONArray a bytes con codificación UTF-8
                    return jsonArray.toString().getBytes("utf-8");
                } catch (Exception e) {
                    e.printStackTrace(); // Agregar manejo de errores en caso de problemas con la conversión
                    return null;
                }
            }
        };

        // Agregar la solicitud a la cola de solicitudes
        queue.add(stringRequest);
    }



    // Método para obtener texto desde un EditText por ID
        private String obtenerTextoDesdeId(String id) {
            int resID = getResources().getIdentifier(id, "id", getPackageName());
            EditText editText = findViewById(resID);
            return editText != null ? editText.getText().toString() : "1";
        }



    /**
         * Método para crear la tabla con los valores predeterminados usando EditText.
         * @param tabla El GridLayout donde se añadirá el contenido.
         */
    private void crearTablaConValoresPredeterminados(GridLayout tabla) {
        // Encabezados de la tabla (EditText también)
        agregarEditTextATabla(tabla, "Nombre", 0, 0, true);
        agregarEditTextATabla(tabla, "Registro", 0, 1, true);
        agregarEditTextATabla(tabla, "calificación", 0, 2, true);

        // Datos predeterminados para cada fila (con EditText)
        for (int i = 1; i <= 10; i++) {
            agregarEditTextATabla(tabla, "Alumno" + i, i, 0, true);
            agregarEditTextATabla(tabla, "" + i, i, 1, true);
            agregarEditTextATabla(tabla, "" + i, i, 2, true);
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

        // Generar un ID único basado en fila y columna
        String id = (columna == 0 ? "Nombre_" : columna == 1 ? "Registro_" : "calificacion_") + fila;
        int resID = getResources().getIdentifier(id, "id", getPackageName());
        editText.setId(resID);

        if (bloqueado) {
            bloquearEditText(editText);
        } else {
            desbloquearEditText(editText);
        }

        GridLayout.LayoutParams params = new GridLayout.LayoutParams(
                GridLayout.spec(fila),
                GridLayout.spec(columna)
        );
        params.setMargins(8, 8, 8, 8);
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
