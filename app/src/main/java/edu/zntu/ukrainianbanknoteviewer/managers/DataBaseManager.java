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
import java.util.Random;

import edu.zntu.ukrainianbanknoteviewer.ConstantsBanknote;
import edu.zntu.ukrainianbanknoteviewer.ShortBanknoteInfo;

public class DataBaseManager extends SQLiteOpenHelper
{
    private final static int DATABASE_VERSION = 1;
    private final static String DATABASE_NAME = "banknotes.db";
    private static String DATABASE_PATH;
    private static SQLiteDatabase sqLiteDatabase;
    private final Context context;
    private static Thread currentThread;


    private static void cancelThread()
    {
        if (currentThread != null)
        {
            currentThread.interrupt();
            try
            {
                currentThread.join();
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }


    public static String fixDenomination(int denomination, int type)
    {
        if (denomination >= 100 && type == 0)
        {
            denomination /= 100;
            type = 1;
        }
        if (type == 1)
        {
            switch (denomination)
            {
                case 1:
                    return denomination + " Гривня";

                case 2:
                    return denomination + " Гривні";

                default:
                    return denomination + " Гривень";
            }
        } else
        {
            switch (denomination)
            {
                case 1:
                    return denomination + " Копійка";

                case 2:
                    return denomination + " Копійки";

                default:
                    return denomination + " Копійок";
            }
        }
    }


    public DataBaseManager(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        DATABASE_PATH = context.getFilesDir().getPath() + DATABASE_NAME;
        create_db();
        open();
    }


    public static void getAllInformation(final Map<Integer, String> map, Runnable runnable)
    {
        cancelThread();
        currentThread = new Thread(() -> {
            int randomRow;
            Random random = new Random();
            Cursor cursor = sqLiteDatabase.rawQuery("select main._id, main.denomination, main.printYear, main.date, description.front, description.back,description.protection, description.extra, main.size, main.turnover, main.Memorable, main.isBanknote, main.isBanknote " +
                    "from main, description " +
                    "where main.codeDescription = description._id", null);

            String memorable, turnover;

            cursor.moveToFirst();
            randomRow = random.nextInt(cursor.getCount());
            cursor.move(randomRow);

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

            map.put(ConstantsBanknote.IDINFO, cursor.getString(cursor.getColumnIndex("_id")));
            map.put(ConstantsBanknote.MEMORABLE, memorable);
            map.put(ConstantsBanknote.DENOMINATION, fixDenomination(Integer.parseInt(cursor.getString(cursor.getColumnIndex("denomination"))), Integer.parseInt(cursor.getString(cursor.getColumnIndex("isBanknote")))));
            map.put(ConstantsBanknote.TURNOVER, turnover);
            map.put(ConstantsBanknote.DATE, cursor.getString(cursor.getColumnIndex("date")));
            map.put(ConstantsBanknote.PRINTYEAR, cursor.getString(cursor.getColumnIndex("printYear")));
            map.put(ConstantsBanknote.DESCRIPTIONFRONT, cursor.getString(cursor.getColumnIndex("front")));
            map.put(ConstantsBanknote.DESCRIPTIONBACK, cursor.getString(cursor.getColumnIndex("back")));
            map.put(ConstantsBanknote.PROTECTIONINFO, cursor.getString(cursor.getColumnIndex("protection")));
            map.put(ConstantsBanknote.EXTRAINFOINFO, cursor.getString(cursor.getColumnIndex("extra")));
            map.put(ConstantsBanknote.SIZEINFO, cursor.getString(cursor.getColumnIndex("size")));
            map.put(ConstantsBanknote.ISBANKNOTE, cursor.getString(cursor.getColumnIndex("isBanknote")));
            cursor.close();

            if (!currentThread.isInterrupted())
            {
                runnable.run();
            }
        });
        currentThread.start();
    }


    public static void getAutocompleteEditText(final ArrayList<String> autocompleteEditText, Runnable runnable)
    {
        cancelThread();
        currentThread = new Thread(() -> {
            Cursor cursor = sqLiteDatabase.rawQuery("select main.denomination, main.printYear, main.isBanknote " +
                    "from main", null);

            cursor.moveToFirst();
            int i = 0, denomination, isBanknote;
            while (cursor.moveToNext() && !currentThread.isInterrupted())
            {
                denomination = Integer.parseInt(cursor.getString(cursor.getColumnIndex("denomination")));
                isBanknote = Integer.parseInt(cursor.getString(cursor.getColumnIndex("isBanknote")));
                autocompleteEditText.add(i, fixDenomination(denomination, isBanknote) + " " + cursor.getString(cursor.getColumnIndex("printYear")) + " року");
                ++i;
            }
            cursor.close();
            if (!currentThread.isInterrupted())
            {
                runnable.run();
            }
        });
        currentThread.start();
    }


    public static void getSize(final ArrayList<String> stringArrayList, String type, Runnable runnable)
    {
        cancelThread();
        currentThread = new Thread(() -> {
            Thread currentThread = Thread.currentThread();

            Cursor cursor;

            stringArrayList.clear();

            if (type.equals(""))
            {
                cursor = sqLiteDatabase.rawQuery("select distinct main.size, main.isBanknote from main", null);
            } else
            {
                cursor = sqLiteDatabase.rawQuery("select distinct main.size, main.isBanknote from main where main.isBanknote = ?", new String[]{type});
            }


            while (cursor.moveToNext() && !currentThread.isInterrupted())
            {

                stringArrayList.add(cursor.getString(cursor.getColumnIndex("size")) + " мм");
            }

            cursor.close();

            if (!currentThread.isInterrupted())
            {
                runnable.run();
            }
        });
        currentThread.start();
    }


    public static void getDenominationOrPrintYear(final ArrayList<String> arrayList, Boolean isDenomination, int isBanknote, Runnable runnable)
    {
        cancelThread();
        currentThread = new Thread(() -> {
            Cursor cursor;
            String selectedColumn;



            if (isDenomination)
            {
                cursor = sqLiteDatabase.rawQuery("select distinct main.denomination, main.isBanknote from main where main.isBanknote = ? order by main.denomination", new String[]{String.valueOf(isBanknote)});
                selectedColumn = "denomination";

                while (cursor.moveToNext() && !currentThread.isInterrupted())
                {
                    arrayList.add(fixDenomination(Integer.parseInt(cursor.getString(cursor.getColumnIndex(selectedColumn))),
                            Integer.parseInt(cursor.getString(cursor.getColumnIndex("isBanknote")))));
                }
            } else
            {
                cursor = sqLiteDatabase.rawQuery("select distinct main.printYear, main.isBanknote from main where main.isBanknote = ? order by main.printYear", new String[]{String.valueOf(isBanknote)});
                selectedColumn = "printYear";

                while (cursor.moveToNext() && !currentThread.isInterrupted())
                {
                    arrayList.add(cursor.getString(cursor.getColumnIndex(selectedColumn)) + " рік");
                }
            }
            cursor.close();
            if (!currentThread.isInterrupted())
            {
                runnable.run();
            }
        });
        currentThread.start();
    }


    public static void fillShortBanknoteInfoList(final List<ShortBanknoteInfo> shortBanknoteInfoList, Map<Integer, String> map, Runnable runnable)
    {
        cancelThread();
        currentThread = new Thread(() -> {
            StringBuilder basicQuery = new StringBuilder("select main._id, main.denomination, main.printYear, main.date, main.memorable, main.size, main.turnover, main.isBanknote from main ");
            int counter = 0;
            Cursor cursor = null;
            if (map != null)
            {
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
                    if (currentThread.isInterrupted())
                    {
                        cursor.close();
                        return;
                    }

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
                                if (map.get(i).equals(""))
                                {
                                    basicQuery.append("main.memorable is null ");
                                } else
                                {
                                    basicQuery.append("main.memorable is not null ");
                                }
                                break;
                            case ConstantsBanknote.TURNOVER:

                                basicQuery.append("main.turnover = ? ");
                                result[counter] = map.get(i);
                                ++counter;
                                break;
                            case ConstantsBanknote.SIZEINFO:

                                basicQuery.append("main.size = ? ");
                                result[counter] = map.get(i);
                                ++counter;
                                break;
                            case ConstantsBanknote.ISBANKNOTE:

                                basicQuery.append("main.isBanknote = ? ");
                                result[counter] = map.get(i);
                                ++counter;
                                break;
                            case ConstantsBanknote.IDINFO:

                                basicQuery.append("main._id = ? ");
                                result[counter] = map.get(i);
                                ++counter;
                                break;
                            case ConstantsBanknote.ISHRYVNYA:
                                basicQuery.append("main.isHryvnya = ? ");
                                result[counter] = map.get(i);
                                ++counter;
                                break;
                        }
                    }
                }

                cursor = sqLiteDatabase.rawQuery(basicQuery.toString() + " order by main.isBanknote, main.denomination", result);
                map.clear();
            } else
            {
                cursor = sqLiteDatabase.rawQuery(basicQuery.toString() + " order by main.isBanknote, main.denomination ", null);
            }


            if (!(shortBanknoteInfoList == null))
            {
                shortBanknoteInfoList.clear();
            }


            counter = 0;
            String memorable, turnover;

            while (cursor.moveToNext() && !currentThread.isInterrupted())
            {
                try
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
                    shortBanknoteInfoList.add(counter,
                            new ShortBanknoteInfo(cursor.getString(cursor.getColumnIndex("_id")),
                                    fixDenomination(Integer.parseInt(cursor.getString(cursor.getColumnIndex("denomination"))),
                                            (Integer.parseInt(cursor.getString(cursor.getColumnIndex("isBanknote"))))),
                                    cursor.getString(cursor.getColumnIndex("printYear")),
                                    cursor.getString(cursor.getColumnIndex("date")),
                                    memorable,
                                    turnover,
                                    cursor.getString(cursor.getColumnIndex("isBanknote"))));
                    ++counter;
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            cursor.close();

            if (!currentThread.isInterrupted())
            {
                runnable.run();
            }

        });
        currentThread.start();
    }


    public static void searchToShowTransfer(final Map<Integer, String> transferMap, Runnable runnable)
    {
        cancelThread();
        currentThread = new Thread(() -> {
            Cursor cursor;
            String basicQuery = "select main.size, description.front, description.back, description.protection, description.extra " +
                    "from main, description " +
                    "where main.codeDescription = description._id and main._id = ?";
            cursor = sqLiteDatabase.rawQuery(basicQuery, new String[]{String.valueOf(transferMap.get(ConstantsBanknote.IDINFO))});
            cursor.moveToFirst();
            transferMap.put(ConstantsBanknote.DESCRIPTIONFRONT, cursor.getString(cursor.getColumnIndex("front")));
            transferMap.put(ConstantsBanknote.DESCRIPTIONBACK, cursor.getString(cursor.getColumnIndex("back")));
            transferMap.put(ConstantsBanknote.PROTECTIONINFO, cursor.getString(cursor.getColumnIndex("protection")));
            transferMap.put(ConstantsBanknote.EXTRAINFOINFO, cursor.getString(cursor.getColumnIndex("extra")));
            transferMap.put(ConstantsBanknote.SIZEINFO, cursor.getString(cursor.getColumnIndex("size")));

            cursor.close();
            if (!currentThread.isInterrupted())
            {
                runnable.run();
            }
        });

        currentThread.start();
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