package edu.zntu.ukrainianbanknoteviewer.managers;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import edu.zntu.ukrainianbanknoteviewer.ConstantsBanknote;
import edu.zntu.ukrainianbanknoteviewer.ShortBanknoteInfo;

public class DataBaseManager extends SQLiteOpenHelper
{
    private final static int DATABASE_VERSION = 1;
    private final static String DATABASE_NAME = "banknotes.db";
    private static String DATABASE_PATH;
    private static SQLiteDatabase sqLiteDatabase;
    private final Context context;


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

            if (!Objects.equals(map.getOrDefault(ConstantsBanknote.MEMORABLE, ""), ""))
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
                        case ConstantsBanknote.DENOMINATION:
                            basicQuery.append("main.denomination = ? ");
                            result[counter] = map.get(i);
                            ++counter;
                            break;
                        case ConstantsBanknote.PRINTYEAR:
                            basicQuery.append("main.printYear = ? ");
                            result[counter] = map.get(i);
                            ++counter;
                            break;
                        case ConstantsBanknote.DATE:
                            basicQuery.append("main.date = ? ");
                            result[counter] = "`" + map.get(i) + "`";
                            ++counter;
                            break;
                        case ConstantsBanknote.MEMORABLE:

                            basicQuery.append("main.memorable is not null ");
                            break;
                        case ConstantsBanknote.TURNOVER:

                            basicQuery.append("main.turnover = ? ");
                            result[counter] = map.get(i);
                            ++counter;
                            break;
                    }
                }
            }
            Cursor cursor;
            cursor = sqLiteDatabase.rawQuery(basicQuery.toString(), result);
            cursor.moveToFirst();
            counter = 0;
            String memorable, turnover;

            while (cursor.moveToNext())
            {

                if (cursor.getString(cursor.getColumnIndex("memorable")) == null)
                {
                    memorable = "Не пам'ятна";
                } else
                {
                    memorable = "Пам'ятна";
                }
                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex("turnover"))) == 0)
                {
                    turnover = "Вийшла з обігу";
                } else
                {
                    turnover = "Дійсна";
                }
                shortBanknoteInfoList.add(counter, new ShortBanknoteInfo(cursor.getString(cursor.getColumnIndex("_id")), cursor.getString(cursor.getColumnIndex("denomination")), cursor.getString(cursor.getColumnIndex("printYear")), cursor.getString(cursor.getColumnIndex("date")), memorable, turnover));
                ++counter;
            }
            runnable.run();
            cursor.close();
        }).start();
    }

    public static void searchToShowTransfer(final Map<Integer, String> transferMap, Runnable runnable)
    {
        new Thread(() -> {
            Cursor cursor;
            String basicQuery = "select main.size, description.front, description.back, description.protection, description.extra " +
                    "from main, description " +
                    "where main._id = description._id and main._id = ?";
            cursor = sqLiteDatabase.rawQuery(basicQuery, new String[]{String.valueOf(transferMap.get(ConstantsBanknote.IDINFO))});
            cursor.moveToFirst();
            transferMap.put(ConstantsBanknote.DESCRIPTIONFRONT, cursor.getString(cursor.getColumnIndex("front")));
            transferMap.put(ConstantsBanknote.DESCRIPTIONBACK, cursor.getString(cursor.getColumnIndex("back")));
            transferMap.put(ConstantsBanknote.PROTECTIONINFO, cursor.getString(cursor.getColumnIndex("protection")));
            transferMap.put(ConstantsBanknote.EXTRAINFOINFO, cursor.getString(cursor.getColumnIndex("extra")));
            transferMap.put(ConstantsBanknote.SIZEINFO, cursor.getString(cursor.getColumnIndex("size")));

            runnable.run();
            cursor.close();
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


