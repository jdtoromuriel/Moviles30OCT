package com.example.library;

import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    // Instanciar los objetos que tienen ID en el archivo XML
    EditText idBook, name, author;
    Spinner editorial;
    Switch savailable;
    TextView message;
    ImageButton bSave, bSearch, bEdit, bList;
    // Generar array con las opciones del spinner
    String[] arrayEditorial = {"Oveja negra", "Prentice Hall"};
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        /* Referenciar los objetos con cada id respecto del archivo xml*/
        message = findViewById(R.id.tvMessage);
        idBook = findViewById(R.id.etIdBook);
        name = findViewById(R.id.etName);
        author = findViewById(R.id.etAuthor);
        editorial = findViewById(R.id.spEditorial);
        savailable = findViewById(R.id.swAvailable);
        bSave = findViewById(R.id.ibSave);
        bSearch = findViewById(R.id.ibSearch);
        bEdit = findViewById(R.id.ibEdit);
        bList = findViewById(R.id.ibList);
        // Poblar el spinner con Array y luego con el array adapter
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked, arrayEditorial);
        editorial.setAdapter(arrayAdapter);
        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mIdBook = idBook.getText().toString();
                String mName = name.getText().toString();
                String mAuthor = author.getText().toString();
                int mAvailable = savailable.isChecked() ? 1 : 0;
                checkDataBook(mIdBook, mName, mAuthor);
                if (checkDataBook(mIdBook, mName, mAuthor)){
                    Map<String, Object> mapBook = new HashMap<>();

                    mapBook.put("idbook", mIdBook);
                    mapBook.put("name", mName);
                    mapBook.put("author", mAuthor);
                    mapBook.put("available", mAvailable);
                    db.collection("book")
                            .add(mapBook)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    message.setTextColor(Color.parseColor("#31S11E"));
                                    message.setText("Libro agregado correctamente");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    message.setTextColor(Color.parseColor("#31S11E"));
                                    message.setText("No se agregó el libro...");
                                }
                            });
                } else {
                    message.setTextColor(Color.parseColor("#FF4545"));
                    message.setText("Debe ingresar todos los datos del libro");
                }
            }
        });
    }

    private Boolean checkDataBook(String mIdBook, String mName, String mAuthor){
        return !mIdBook.isEmpty() && !mName.isEmpty() && !mAuthor.isEmpty();
    }
}