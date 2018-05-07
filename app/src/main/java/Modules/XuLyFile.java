package Modules;

import android.content.Context;
import android.widget.Toast;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import Models.DiaDiem;

/**
 * Created by Thanh on 5/7/2018.
 */

public class XuLyFile{
    private final String TEN_FILE = "list.txt";
    private Context mContext;

    public XuLyFile(Context context) {
        this.mContext = context;
    }

    public void LuuDanhSachDiaDiem(ArrayList<DiaDiem> diaDiems) {
        try {
            FileOutputStream outputStream = mContext.openFileOutput(TEN_FILE,Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(diaDiems);
            objectOutputStream.close();
            outputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(mContext, "Không tìm thấy file", Toast.LENGTH_SHORT).show();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Toast.makeText(mContext, "Đã lưu danh sách", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<DiaDiem> DocDanhSachDiaDiem() {
        ArrayList<DiaDiem> diaDiems = new ArrayList<>();
        try {
            FileInputStream fis = mContext.openFileInput(TEN_FILE);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object data = ois.readObject();
            diaDiems = (ArrayList<DiaDiem>) data;
            ois.close();
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        return diaDiems;
    }
}
