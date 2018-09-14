package in.co.chicmic.canvas.activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

import in.co.chicmic.canvas.R;
import in.co.chicmic.canvas.adapters.ImageAdapter;
import in.co.chicmic.canvas.listeners.RecyclerClickListener;
import in.co.chicmic.canvas.utilities.Constants;

public class GalleryActivity extends AppCompatActivity implements RecyclerClickListener{

    private ArrayList<String> mAllImageList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        mRecyclerView = findViewById(R.id.gallery_rv);
        setUpRecycler();
    }

    private void setUpRecycler() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mAdapter = new ImageAdapter(mAllImageList, this);
        mRecyclerView.setAdapter(mAdapter);
        getAllImageData();
    }

    public void getAllImageData(){
        Uri u = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.ImageColumns.DATA};
        Cursor c = null;
        SortedSet<String> dirList = new TreeSet<>();
        ArrayList<String> resultIAV = new ArrayList<>();

        String[] directories = null;
        if (u != null)
        {
            c = managedQuery(u, projection, null, null, null);
        }

        if ((c != null) && (c.moveToFirst()))
        {
            do
            {
                String tempDir = c.getString(0);
                tempDir = tempDir.substring(0, tempDir.lastIndexOf("/"));
                try{
                    dirList.add(tempDir);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
            while (c.moveToNext());
            directories = new String[dirList.size()];
            dirList.toArray(directories);

        }

        for(int i=0;i<dirList.size();i++)
        {
            File imageDir = null;
            if (directories != null) {
                imageDir = new File(directories[i]);
            }
            File[] imageList = new File[0];
            if (imageDir != null) {
                imageList = imageDir.listFiles();
            }
            if(imageList == null)
                continue;
            for (File imagePath : imageList) {
                try {

                    if(imagePath.isDirectory())
                    {
                        File[] files = imagePath.listFiles();

                    }
                    if (imagePath.getName().contains(".png") || imagePath.getName().contains(".PNG"))
                    {
                        String path= imagePath.getAbsolutePath();
                        resultIAV.add(path);
                    }
                }
                //  }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        mAllImageList.addAll(resultIAV);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRecyclerItemClick(int adapterPosition) {
        Intent intent = new Intent(this, CanvasActivity.class);
        intent.putExtra(Constants.sIMAGE_PATH, mAllImageList.get(adapterPosition));
        startActivity(intent);
        finish();
    }
}
