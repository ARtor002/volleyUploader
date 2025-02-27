package ir.ARtor.volley.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import ir.ARtor.volley.R;
import ir.ARtor.volley.app.app;
import ir.ARtor.volley.interfaces.MultiAction_interface;
import ir.ARtor.volley.models.Image_Model;
import ir.ARtor.volley.views.ShowImage;

public class Images_adapter extends RecyclerView.Adapter<Images_adapter.MyViewHolder>{

    List<Image_Model> list;
    Context context;
    Activity activity;
    MultiAction_interface multiActionInterface;

    public static boolean multiAction = false;
    public static int count = 0;

    public Images_adapter(List<Image_Model> list, Context context, Activity activity, MultiAction_interface multiActionInterface){
        this.list = list;
        this.context = context;
        this.activity = activity;
        this.multiActionInterface = multiActionInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(app.LOCAL2 + list.get(position).getImage()).into(holder.imageView);
        holder.textView.setText(list.get(position).getUploaded_At());

        holder.checkBox.setChecked(false);
        holder.checkBox.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout parent;
        ImageView imageView;
        TextView textView;
        CheckBox checkBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            parent = itemView.findViewById(R.id.itemParent);
            imageView = itemView.findViewById(R.id.imageViewItem);
            textView = itemView.findViewById(R.id.textView);
            checkBox = itemView.findViewById(R.id.checkbox);

            parent.setOnClickListener(v -> {
                if (multiAction){
                    if (count > 0){
                        if (!list.get(getAdapterPosition()).isSelected()){
                            list.get(getAdapterPosition()).setSelected(true);
                            multiActionInterface.selected(++count, getAdapterPosition());
                            checkBox.setVisibility(View.VISIBLE);
                            checkBox.setChecked(true);
                        }else {
                            list.get(getAdapterPosition()).setSelected(false);
                            multiActionInterface.deSelected(--count, getAdapterPosition());
                            checkBox.setVisibility(View.GONE);
                            checkBox.setChecked(false);
                        }
                    }else {
                        multiAction = false;
                    }
                }
                if (!multiAction) {
                    ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity,imageView,"image");
                    context.startActivity(new Intent(context, ShowImage.class).putExtra("image", list.get(getAdapterPosition()).getImage())
                            .putExtra("date", list.get(getAdapterPosition()).getUploaded_At()).putExtra("id", list.get(getAdapterPosition()).getId()), activityOptionsCompat.toBundle());
                }
            });

            parent.setOnLongClickListener(v -> {
                if (count == 0 || !multiAction){
                    multiActionInterface.started();
                    multiActionInterface.selected(++count, getAdapterPosition());
                    multiAction = true;
                    list.get(getAdapterPosition()).setSelected(true);

                    checkBox.setVisibility(View.VISIBLE);
                    checkBox.setChecked(true);
                }
                return true;
            });

        }
    }
}


//show date of uploaded for pictures inside a textview in showimage.java
//visibility of checkboxes for all item is visible when longClicked