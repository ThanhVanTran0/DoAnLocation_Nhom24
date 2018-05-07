package Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import Models.DiaDiem;

import com.example.thanh.doanlocation_nhom24.R;

import java.util.List;

/**
 * Created by Thanh on 4/30/2018.
 */

public class CustomAdapter extends ArrayAdapter<DiaDiem> {

    private Context context;
    private int Resource;
    private List<DiaDiem> objects;

    public CustomAdapter(@NonNull Context context, int resource, @NonNull List<DiaDiem> objects) {
        super(context, resource, objects);
        this.context = context;
        this.Resource = resource;
        this.objects = objects;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder viewHolder;
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(Resource,null);
            viewHolder = new ViewHolder();
            //Anh xa;
            viewHolder.imgDiaDiem = convertView.findViewById(R.id.imgDiaDiemUaThich);
            viewHolder.cbXoa = convertView.findViewById(R.id.cbXoa);
            viewHolder.txtTenCuaHang = convertView.findViewById(R.id.txtTenCuaHang);
            viewHolder.txtViTri = convertView.findViewById(R.id.txtViTri);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        DiaDiem diaDiem = objects.get(position);
        Bitmap b = diaDiem.getImgDiaDiem();
        if(b == null)
        {
            viewHolder.imgDiaDiem.setImageResource(R.drawable.img_noimage);
        }
        else {
            viewHolder.imgDiaDiem.setImageBitmap(b);
        }
        viewHolder.txtTenCuaHang.setText(diaDiem.getTenDiaDiem());
        viewHolder.txtViTri.setText(diaDiem.getDiaChi());
        return convertView;
    }

    private class ViewHolder {
        ImageView imgDiaDiem;
        TextView txtTenCuaHang;
        TextView txtViTri;
        CheckBox cbXoa;
    }
}
