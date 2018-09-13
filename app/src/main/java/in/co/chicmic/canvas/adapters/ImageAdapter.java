package in.co.chicmic.canvas.adapters;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import in.co.chicmic.canvas.R;
import in.co.chicmic.canvas.listeners.RecyclerClickListener;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder>{

    private ArrayList<String> mAllImageList;
    private RecyclerClickListener mListener;
    public ImageAdapter(ArrayList<String> pAllImageList, RecyclerClickListener pListener) {
        mAllImageList = pAllImageList;
        mListener = pListener;
    }

    @NonNull
    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.image_recycler_item
                        , parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ImageAdapter.ViewHolder holder, int position) {
        holder.mGalleryImage.setImageURI(Uri.parse(mAllImageList.get(position)));
        holder.mParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onRecyclerItemClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAllImageList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mParent;
        ImageView mGalleryImage;
        ViewHolder(View itemView) {
            super(itemView);
            mParent = itemView.findViewById(R.id.ll_parent);
            mGalleryImage = itemView.findViewById(R.id.img_gallery);
        }
    }
}