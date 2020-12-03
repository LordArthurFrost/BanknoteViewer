package edu.zntu.ukrainianbanknoteviewer.managers;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

import com.google.android.vending.expansion.zipfile.ZipResourceFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ImageManager
{
    private final String IMAGE_PATH;
    private final String ARCHIVE_NAME = "BanknotesImages.zip";
    private static ZipResourceFile zipResourceFile;
    private Context context;

    public ImageManager(Context context)
    {
        this.context = context;
        IMAGE_PATH = context.getFilesDir().getPath() + ARCHIVE_NAME;
        createLocalArchive();
        try
        {
            zipResourceFile = new ZipResourceFile(IMAGE_PATH);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public static BitmapDrawable getImage(String imageName)
    {
        imageName = imageName + ".jpg";
        InputStream inputStream = null;

        try
        {
            inputStream = zipResourceFile.getInputStream(imageName);
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return (BitmapDrawable) BitmapDrawable.createFromStream(inputStream, null);
    }


    void createLocalArchive()
    {
        InputStream inputStream;
        OutputStream outputStream;
        Log.d("ImageManager", "Checking creation");
        try
        {
            File file = new File(IMAGE_PATH);
            if (!file.exists())
            {
                Log.d("ImageManager", "CreatingLocalArchive");
                //получаем локальную бд как поток
                inputStream = context.getAssets().open(ARCHIVE_NAME);
                // Путь к новой бд
                String outFileName = IMAGE_PATH;

                // Открываем пустую бд
                outputStream = new FileOutputStream(outFileName);

                // побайтово копируем данные
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0)
                {
                    outputStream.write(buffer, 0, length);
                }

                outputStream.flush();
                outputStream.close();
                inputStream.close();
            }
        } catch (IOException ex)
        {
            Log.d("ImageManager", ex.getMessage());
        }
    }
}
