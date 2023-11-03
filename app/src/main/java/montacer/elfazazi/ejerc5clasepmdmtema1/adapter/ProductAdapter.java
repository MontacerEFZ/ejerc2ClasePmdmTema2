package montacer.elfazazi.ejerc5clasepmdmtema1.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductVH> {
    @NonNull
    @Override
    public ProductVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductVH holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ProductVH extends RecyclerView.ViewHolder{
        public ProductVH(@NonNull View itemView) {
            super(itemView);
        }
    }
}
