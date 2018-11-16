package banco_dados;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class resgatarImagemBanco {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static Uri getBitMapFromURL(String nomeImagemNoticia,Context context) {
        try {
            ContextWrapper cw = new ContextWrapper(context);


            File directory = cw.getDir("imagens", Context.MODE_PRIVATE);
            File mypath=new File(directory,nomeImagemNoticia);
            Uri uri = Uri.fromFile(mypath);
            if (!mypath.exists()) {
                RequisicaoHTTP banco = new RequisicaoHTTP();
                URL url = new URL(banco.getIpBanco() + "imagens/" + nomeImagemNoticia);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.connect();
                InputStream input = conn.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(input);
                saveToInternalStorage(bitmap, nomeImagemNoticia, context);
                directory = cw.getDir("imagens", Context.MODE_PRIVATE);
                mypath=new File(directory,nomeImagemNoticia);
                uri = Uri.fromFile(mypath);
            }
            return uri;
        } catch (IOException e) {
            Log.i("queijinho", "fonfon");
            e.printStackTrace();
        }
        return null;
    }

    private static void saveToInternalStorage(Bitmap bitmapImage, String nomeImagemNoticia,Context applicationContext){
        ContextWrapper cw = new ContextWrapper(applicationContext);
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imagens", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,nomeImagemNoticia);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        directory.getAbsolutePath();
    }

    /*public static void saveBitmap(Bitmap pBitmap, String nomeImagemNoticia){
        try{
            File file = new File(Environment.getDataDirectory() + "/imagens");
            file.mkdir();
            File ifile= new File(Environment.getExternalStorageDirectory() + "/imagens/", nomeImagemNoticia);
            FileOutputStream outStream = new FileOutputStream(ifile);
            pBitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.close();

        }
        catch(Exception e){
            Log.e("Could not save", e.toString());
        }
    }*/
}
