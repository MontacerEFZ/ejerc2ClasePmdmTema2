package montacer.elfazazi.ejerc5clasepmdmtema1;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import montacer.elfazazi.ejerc5clasepmdmtema1.adapter.ProductAdapter;
import montacer.elfazazi.ejerc5clasepmdmtema1.configuracion.Constantes;
import montacer.elfazazi.ejerc5clasepmdmtema1.databinding.ActivityMainBinding;
import montacer.elfazazi.ejerc5clasepmdmtema1.modelos.Product;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ArrayList<Product> productList;
    private ProductAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private SharedPreferences sp;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sp = getSharedPreferences(Constantes.DATOS, MODE_PRIVATE);
        gson = new Gson();

        productList = new ArrayList<>(); //importante, no olvidar inicializar lista al crearla

        adapter = new ProductAdapter(productList, R.layout.product_view_holder, MainActivity.this); //adapter siempre despues de inicializar el arraylist
            //este es el contructor del ProductAdapter, le pasamos: la lista, que vista queremos mostrar q es el resource y donde queremos mostrarla que es en el main

        layoutManager = new GridLayoutManager(this, 2); //mostrara las cosas en columnas de 2

        binding.contentMain.container.setAdapter(adapter);
        binding.contentMain.container.setLayoutManager(layoutManager);

        leerInformacion();

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createProduct().show();
               }
        });
    }

    private void leerInformacion() {
        if(sp.contains(Constantes.LISTAPRODUCTOS)){
            String listaJson = sp.getString(Constantes.LISTAPRODUCTOS, "[]");
            Type tipo = new TypeToken<ArrayList<Product>>(){}.getType(); //est es una especie de cast
            ArrayList<Product> temp = gson.fromJson(listaJson, tipo);
            productList.clear();
            productList.addAll(temp);
            adapter.notifyItemRangeInserted(0, productList.size());
        }
    }

    private AlertDialog createProduct(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setTitle(R.string.titleCreate);
        builder.setCancelable(false);

        View productViewModel = LayoutInflater.from(this).inflate(R.layout.product_view_model, null);
        EditText txtName = productViewModel.findViewById(R.id.txtNameProductViewModel);
        EditText txtQuantity = productViewModel.findViewById(R.id.txtQuantityProductViewModel);
        EditText txtPrice = productViewModel.findViewById(R.id.txtPriceProductViewModel);
        TextView lbTotal = productViewModel.findViewById(R.id.lbTotalProductViewModel);

        builder.setView(productViewModel);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    int quantity = Integer.parseInt(txtQuantity.getText().toString());
                    float price = Float.parseFloat(txtPrice.getText().toString());

                    float total = quantity * price;

                    lbTotal.setText(String.valueOf(total)+"$");
                }catch (Exception e){

                }
            }
        };

        txtQuantity.addTextChangedListener(textWatcher);
        txtPrice.addTextChangedListener(textWatcher);

        builder.setNegativeButton(R.string.cancel, null);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (txtName.getText().toString().isEmpty() || txtQuantity.getText().toString().isEmpty() ||
                txtPrice.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, R.string.missing, Toast.LENGTH_SHORT).show();
                }else {
                    Product product = new Product(txtName.getText().toString(),
                            Integer.parseInt(txtQuantity.getText().toString()),
                            Float.parseFloat(txtPrice.getText().toString()));

                    productList.add(0, product);
                    adapter.notifyItemInserted(0);
                    guardarInformacion();
                   // Toast.makeText(MainActivity.this, product.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });


        return builder.create();
    }

    private void guardarInformacion() {
        SharedPreferences.Editor editor = sp.edit();
        String listaJson = gson.toJson(productList);
        editor.putString(Constantes.LISTAPRODUCTOS, listaJson);
        editor.apply();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) { //metodo para q funcione el movil cuando se gire
        super.onSaveInstanceState(outState);
        outState.putSerializable("LIST", productList);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) { //metodo para q funcione el movil cuando se gire
        super.onRestoreInstanceState(savedInstanceState);

        productList.addAll((ArrayList<Product>) savedInstanceState.getSerializable("LIST"));

        adapter.notifyItemRangeInserted(0, productList.size());
    }
}