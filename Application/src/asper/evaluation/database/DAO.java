package asper.evaluation.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;
import asper.evaluation.Settings;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Database Access Object.
 * Abstracts database command invocation
 */

public class DAO
{
    private static Database refrence;

    /**
     * Initialization procedure for
     * this DAO instance.
     *
     * @param context
     */
    static
    {
        refrence = new Database();
    }

    /**
     * Database population procedure.
     * Preloads the database with a set
     * of random rows.
     */
    public static void populate()
    {
        try
        {
            Random random = new Random();
            refrence.getInstance().beginTransaction();

            for (int i = 0; i < Settings.DATABASE_POPULATION; i++)
            {
                ContentValues values = new ContentValues();
                values.put(refrence.KEY_T1, random.nextDouble());
                values.put(refrence.KEY_T2, random.nextDouble());
                values.put(refrence.KEY_T3, random.nextDouble());
                values.put(refrence.KEY_T4, random.nextDouble());
                values.put(refrence.KEY_T5, random.nextDouble());

                refrence.getInstance().insert
                        (
                                refrence.TABLE_NAME,
                                null,
                                values
                        );
            }

            refrence.getInstance().setTransactionSuccessful();

        } catch (Exception e)
        {
            Log.e(Settings.TAG, "Database population failed. " + e.getCause());
        } finally
        {
            refrence.getInstance().endTransaction();
        }

        Log.i(Settings.TAG, "Added database records");
    }


    /**
     * Clears the contents of the database.
     * Initiates a teardown procedure.
     */
    public static void clear()
    {
        refrence.onUpgrade(refrence.getInstance(), 0, 0);
    }

    /**
     * Locates a database row by
     * a primary identficator.
     * <p/>
     * Utilized for testing purposes related to
     * measurements between Esper and native query performance.
     *
     * @param id the primary that you want to find.
     * @return
     */
    public static Map<String, Object> findById(int id)
    {
        String keys = refrence.KEY_ID + ", "
                + refrence.KEY_T1 + ", "
                + refrence.KEY_T2 + ", "
                + refrence.KEY_T3 + ", "
                + refrence.KEY_T4 + ", "
                + refrence.KEY_T5;

        Cursor cursor = refrence.getInstance().rawQuery(
                "SELECT " + keys
                        + " FROM " + refrence.TABLE_NAME
                        + " WHERE " + refrence.KEY_ID + " = " + id,
                null
        );

        Map map = new HashMap<String, Object>();

        cursor.moveToFirst();
        map.put(refrence.KEY_ID, cursor.getInt(0));
        map.put(refrence.KEY_T1, cursor.getDouble(1));
        map.put(refrence.KEY_T2, cursor.getDouble(2));
        map.put(refrence.KEY_T3, cursor.getDouble(3));
        map.put(refrence.KEY_T4, cursor.getDouble(4));
        map.put(refrence.KEY_T5, cursor.getDouble(5));
        cursor.close();

        return map;
    }
}
