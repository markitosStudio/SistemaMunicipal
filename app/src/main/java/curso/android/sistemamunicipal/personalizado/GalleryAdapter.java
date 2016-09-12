package curso.android.sistemamunicipal.personalizado;
import  curso.android.sistemamunicipal.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by markitos on 11/09/16.
 */
public class GalleryAdapter extends BaseAdapter
{
    Context context;
    ArrayList<Integer> imagenes;
    int background;
    //guardamos las imágenes reescaladas para mejorar el rendimiento ya que estas operaciones son costosas
    //se usa SparseArray siguiendo la recomendación de Android Lint
    SparseArray<Bitmap> imagenesEscaladas = new SparseArray<Bitmap>(7);

    public GalleryAdapter(Context context, ArrayList<Integer> imagenes)
    {
        super();
        this.imagenes = imagenes;
        this.context = context;

        //establecemos un marco para las imágenes (estilo por defecto proporcionado)
        //por android y definido en /values/attr.xml
        TypedArray typedArray = context.obtainStyledAttributes(R.styleable.Gallery1);
        background = typedArray.getResourceId(R.styleable.Gallery1_android_galleryItemBackground, 1);
        typedArray.recycle();
    }

    @Override
    public int getCount()
    {
        return imagenes.size();
    }

    @Override
    public Object getItem(int position)
    {
        return position;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ImageView imagen = new ImageView(context);

        //reescalamos la imagen para evitar "java.lang.OutOfMemory" en el caso de imágenes de gran resolución
        //como es este ejemplo
        if (imagenesEscaladas.get(position) == null)
        {
            Bitmap bitmap = BitmapUtils.decodeSampledBitmapFromResource(context.getResources(), imagenes.get(position), 120, 0);
            imagenesEscaladas.put(position, bitmap);
        }
        imagen.setImageBitmap(imagenesEscaladas.get(position));
        //se aplica el estilo
        imagen.setBackgroundResource(background);

        return imagen;
    }
}
