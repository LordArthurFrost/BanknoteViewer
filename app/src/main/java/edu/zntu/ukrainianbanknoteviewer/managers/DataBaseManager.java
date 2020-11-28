package edu.zntu.ukrainianbanknoteviewer.managers;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import edu.zntu.ukrainianbanknoteviewer.ShortBanknoteInfo;

public class DataBaseManager extends SQLiteOpenHelper
{
    private final static int DATABASE_VERSION = 1;
    private final static String DATABASE_NAME = "banknotes.db";
    private static String DATABASE_PATH;
    private static SQLiteDatabase sqLiteDatabase;
    private final Context context;
    private static final int DENOMINATION = 0;
    private static final int PRINTYEAR = 1;
    private static final int DATE = 2;
    private static final int MEMORABLE = 3;
    private static final int TURNOVER = 4;


    public DataBaseManager(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        DATABASE_PATH = context.getFilesDir().getPath() + DATABASE_NAME;
        create_db();
        open();
    }

    public static void getAutocompleteEditText(final ArrayList<String> autocompleteEditText, Runnable runnable)
    {
        new Thread(() -> {
            Cursor cursor = sqLiteDatabase.rawQuery("select main.denomination, main.printYear " +
                    "from main", null);

            cursor.moveToFirst();
            int i = 0, denomination;
            while (cursor.moveToNext())
            {
                denomination = Integer.parseInt(cursor.getString(cursor.getColumnIndex("denomination")));
                if (denomination == 1)
                {
                    autocompleteEditText.add(i, denomination + " гривня " + cursor.getString(cursor.getColumnIndex("printYear")) + " року");
                }
                if (denomination == 2)
                {
                    autocompleteEditText.add(i, denomination + " гривні " + cursor.getString(cursor.getColumnIndex("printYear")) + " року");

                }
                if (denomination > 2)
                {
                    autocompleteEditText.add(i, denomination + " гривень " + cursor.getString(cursor.getColumnIndex("printYear")) + " року");

                }
                ++i;
            }
            runnable.run();
        }).start();

    }

    public static void fillShortBanknoteInfoList(final List<ShortBanknoteInfo> shortBanknoteInfoList, Map<Integer, String> map, Runnable runnable)
    {
        new Thread(() -> {
            StringBuilder basicQuery = new StringBuilder("select main._id, main.denomination, main.printYear, main.date, main.memorable, main.turnover from main ");
            int counter = 0;
            for (String value : map.values())
            {
                if (!value.equals(""))
                {
                    ++counter;
                }
            }

            if (!Objects.equals(map.getOrDefault(MEMORABLE, ""), ""))
            {
                --counter;
            }

            String[] result = new String[counter];
            counter = 0;

            for (Integer i : map.keySet())
            {
                if (!Objects.equals(map.getOrDefault(i, ""), ""))
                {
                    if (counter != 0)
                    {
                        basicQuery.append("and ");
                    } else
                    {
                        basicQuery.append("where ");
                    }
                    switch (i)
                    {
                        case DENOMINATION:
                            basicQuery.append("main.denomination = ? ");
                            result[counter] = map.get(i);
                            ++counter;
                            break;
                        case PRINTYEAR:
                            basicQuery.append("main.printYear = ? ");
                            result[counter] = map.get(i);
                            ++counter;
                            break;
                        case DATE:
                            basicQuery.append("main.date = ? ");
                            result[counter] = "`" + map.get(i) + "`";
                            ++counter;
                            break;
                        case MEMORABLE:

                            basicQuery.append("main.memorable is not null ");
                            break;
                        case TURNOVER:

                            basicQuery.append("main.turnover = ? ");
                            result[counter] = map.get(i);
                            ++counter;
                            break;
                    }
                }
            }
            Cursor cursor2;
            cursor2 = sqLiteDatabase.rawQuery(basicQuery.toString(), result);
            cursor2.moveToFirst();
            counter = 0;
            String memorable, turnover;

            while (cursor2.moveToNext())
            {

                if (cursor2.getString(cursor2.getColumnIndex("memorable")) == null)
                {
                    memorable = "Не пам'ятна";
                } else
                {
                    memorable = "Пам'ятна";
                }
                if (Integer.parseInt(cursor2.getString(cursor2.getColumnIndex("turnover"))) == 0)
                {
                    turnover = "Вийшла з обігу";
                } else
                {
                    turnover = "Дійсна";
                }
                shortBanknoteInfoList.add(counter, new ShortBanknoteInfo(cursor2.getString(cursor2.getColumnIndex("_id")), cursor2.getString(cursor2.getColumnIndex("denomination")), cursor2.getString(cursor2.getColumnIndex("printYear")), cursor2.getString(cursor2.getColumnIndex("date")), memorable, turnover));
                ++counter;
            }
            runnable.run();
        }).start();
    }


    void create_db()
    {
        InputStream inputStream;
        OutputStream outputStream;
        Log.d("Database", "Checking creation");
        try
        {
            File file = new File(DATABASE_PATH);
            if (!file.exists())
            {
                Log.d("Database", "Creating Database");
                this.getReadableDatabase();
                //получаем локальную бд как поток
                inputStream = context.getAssets().open(DATABASE_NAME);
                // Путь к новой бд
                String outFileName = DATABASE_PATH;

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
            Log.d("Database", ex.getMessage());
        }
    }

    public void open() throws SQLException
    {

        sqLiteDatabase = SQLiteDatabase.openDatabase(DATABASE_PATH, null, SQLiteDatabase.OPEN_READONLY);
        Log.d("Database", "Opened Successfully");
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        //NULL
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        //NULL
    }
}


