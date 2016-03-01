package com.setup.solarpanel.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.setup.solarpanel.data.ReportsModel;
import com.setup.solarpanel.utils.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by Mayank on 9/5/2015.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
	private static DatabaseHelper sInstance;
	private static final String DATABASE_NAME = "SolarPanel_DB";
	private static final int DATABASE_VERSION = 1;
	private static final String LOG_TAG = "DatabaseHelper";
	private static String DB_PATH = "data/data/com.setup.solarpanel/databases/";
	private static String DB_NAME = "solar_panel.db";
	private Context context;
	private SQLiteDatabase applicationDatabase;

	public static synchronized DatabaseHelper getInstance(Context context) {

		// Use the application context, which will ensure that you
		// don't accidentally leak an Activity's context.
		// See this article for more information: http://bit.ly/6LRzfx
		if (sInstance == null) {
			sInstance = new DatabaseHelper(context.getApplicationContext());
		}
		return sInstance;
	}

	/**
	 * Constructor should be private to prevent direct instantiation.
	 * make call to static method "getInstance()" instead.
	 */
	private DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	public boolean checkDataBase() {
		File dbFile = new File(DB_PATH + DB_NAME);
		return dbFile.exists();
	}

	public void copyDataBase() throws IOException {
		try {

			InputStream input = context.getAssets().open(DB_NAME);
			String outPutFileName = DB_PATH + DB_NAME;
			OutputStream output = new FileOutputStream(outPutFileName);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = input.read(buffer)) > 0) {
				output.write(buffer, 0, length);
			}
			output.flush();
			output.close();
			input.close();
		} catch (IOException e) {
			Log.v("error", e.toString());
		}
	}

	public void openDataBase() throws SQLException {
		String fullDbPath = DB_PATH + DB_NAME;
		applicationDatabase = SQLiteDatabase.openDatabase(fullDbPath, null,
				SQLiteDatabase.OPEN_READWRITE);

	}

	/**
	 * Creates a empty database on the system and rewrites it with your own
	 * database.
	 * */
	public void createDataBase() throws IOException {

		boolean dbExist = checkDataBase();
		Log.i("database already exists", "" + dbExist);
		if (dbExist) {
			// do nothing - database already exist
		} else {

			// By calling this method and empty database will be created into
			// the default system path
			// of your application so we are gonna be able to overwrite that
			// database with our database.
			this.getReadableDatabase();

			try {

				copyDataBase();
				showToast("Database is successfully created");

			} catch (IOException e) {

				throw new Error("Error copying database");

			}
		}

	}

	private void showToast(String message){

		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}

	@Override
	public synchronized void close() {
		if (applicationDatabase != null)
			applicationDatabase.close();
		super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	public long insertAverageLux() {
		openDataBase();
		String selection = DatabaseConstants.CompassData.Date + " = ? AND " + DatabaseConstants.CompassData.StartTime + " = ? AND "
				+ DatabaseConstants.CompassData.EndTime + " = ? ";
		String[] selectionArgs = {Constants.date, Constants.startTime, Constants.endTime};

		ContentValues initialValues = new ContentValues();
		initialValues.put(DatabaseConstants.CompassData.AverageLux, Constants.averageLux);
		long noOfRowInserted = applicationDatabase.update(DatabaseConstants.CompassData.TABLE, initialValues, selection, selectionArgs);
		close();
		return noOfRowInserted;
	}


	public long insertCompassData() {
		openDataBase();
		ContentValues initialValues = new ContentValues();
		initialValues.put(DatabaseConstants.CompassData.Date, Constants.date);
		initialValues.put(DatabaseConstants.CompassData.StartTime, Constants.startTime);
		initialValues.put(DatabaseConstants.CompassData.EndTime, Constants.endTime);
		initialValues.put(DatabaseConstants.CompassData.Interval, String.valueOf(Constants.interval));
		initialValues.put(DatabaseConstants.CompassData.Longitude, Constants.longitude);
		initialValues.put(DatabaseConstants.CompassData.Latitude, Constants.latitude);
		initialValues.put(DatabaseConstants.CompassData.CurrentDirection, Constants.currentDirection);
		initialValues.put(DatabaseConstants.CompassData.CurrentAngle, Constants.currentAngle);
		initialValues.put(DatabaseConstants.CompassData.OptimalDirection, Constants.optimalDirection);
		initialValues.put(DatabaseConstants.CompassData.OptimalAngle, Constants.optimalAngle);
		long noOfRowInserted = applicationDatabase.insert(DatabaseConstants.CompassData.TABLE, null, initialValues);
		close();
		return noOfRowInserted;
	}

	public long insertCapturedLux() {
		openDataBase();
		ContentValues initialValues = new ContentValues();
		initialValues.put(DatabaseConstants.CapturedLux.Date, Constants.date);
		initialValues.put(DatabaseConstants.CapturedLux.StartTime, Constants.startTime);
		initialValues.put(DatabaseConstants.CapturedLux.EndTime, Constants.endTime);
		initialValues.put(DatabaseConstants.CapturedLux.LuxValue, Constants.lux);
		initialValues.put(DatabaseConstants.CapturedLux.CapturedAt, Constants.capturedAt);
		long noOfRowInserted = applicationDatabase.insert(DatabaseConstants.CapturedLux.TABLE, null, initialValues);
		close();
		return noOfRowInserted;
	}

	public ArrayList<ReportsModel> getDailyReport(String date) {
		openDataBase();
		ArrayList<ReportsModel> reportsModelList = new ArrayList<ReportsModel>();

		String SELECT_QUERY = "SELECT * FROM "
				+ DatabaseConstants.CompassData.TABLE //+ " inner join " + DatabaseConstants.CapturedLux.TABLE + " on ( "
//				+ DatabaseConstants.CompassData.TABLE + "." + DatabaseConstants.CompassData.Date + "=" + DatabaseConstants.CapturedLux.TABLE + "." + DatabaseConstants.CapturedLux.Date
//				+ " AND "
//				+ DatabaseConstants.CompassData.TABLE + "." + DatabaseConstants.CompassData.StartTime + "=" + DatabaseConstants.CapturedLux.TABLE + "." + DatabaseConstants.CapturedLux.StartTime
//				+ " AND "
//				+ DatabaseConstants.CompassData.TABLE + "." + DatabaseConstants.CompassData.EndTime + "=" + DatabaseConstants.CapturedLux.TABLE + "." + DatabaseConstants.CapturedLux.EndTime
//				+ ") where "+
				+ " where "+
				DatabaseConstants.CompassData.TABLE + "." +DatabaseConstants.CompassData.Date + "='"+ date  +"';";

		Cursor cursor = applicationDatabase.rawQuery(SELECT_QUERY, null);

		if (cursor != null && cursor.getCount() > 0) {
			if (cursor.moveToFirst()) {
				do {

					ReportsModel reportsModel = new ReportsModel();
					reportsModel.setDate(cursor.getString(cursor.getColumnIndex(DatabaseConstants.CompassData.Date)));
					reportsModel.setStartTime(cursor.getString(cursor.getColumnIndex(DatabaseConstants.CompassData.StartTime)));
					reportsModel.setEndTime(cursor.getString(cursor.getColumnIndex(DatabaseConstants.CompassData.EndTime)));
					reportsModel.setLongitude(cursor.getString(cursor.getColumnIndex(DatabaseConstants.CompassData.Longitude)));
					reportsModel.setLatitude(cursor.getString(cursor.getColumnIndex(DatabaseConstants.CompassData.Latitude)));
					reportsModel.setDirectionCaptured(cursor.getString(cursor.getColumnIndex(DatabaseConstants.CompassData.CurrentDirection)));
					reportsModel.setAngleCaptured(cursor.getString(cursor.getColumnIndex(DatabaseConstants.CompassData.CurrentAngle)));
					reportsModel.setOptimalDirection(cursor.getString(cursor.getColumnIndex(DatabaseConstants.CompassData.OptimalDirection)));
					reportsModel.setOptimalAngle(cursor.getString(cursor.getColumnIndex(DatabaseConstants.CompassData.OptimalAngle)));
					reportsModel.setAverageLux(cursor.getString(cursor.getColumnIndex(DatabaseConstants.CompassData.AverageLux)));
//					reportsModel.setCapturedAt(cursor.getString(cursor.getColumnIndex(DatabaseConstants.CapturedLux.CapturedAt)));
//					reportsModel.setLuxValue(cursor.getString(cursor.getColumnIndex(DatabaseConstants.CapturedLux.LuxValue)));

					reportsModelList.add(reportsModel);
				} while (cursor.moveToNext());
			}
		}

		close();
		return reportsModelList;
	}


	public ArrayList<ReportsModel> getDetailReport(String date, String startTime, String endTime) {
		openDataBase();
		ArrayList<ReportsModel> reportsModelList = new ArrayList<ReportsModel>();

		String SELECT_QUERY = "SELECT * FROM "
				+ DatabaseConstants.CapturedLux.TABLE
				+ " where "
				+ DatabaseConstants.CapturedLux.TABLE + "." +DatabaseConstants.CompassData.Date + "='"+ date  +"' AND "
				+ DatabaseConstants.CapturedLux.TABLE + "." +DatabaseConstants.CompassData.StartTime + "='"+ startTime  +"' AND "
				+ DatabaseConstants.CapturedLux.TABLE + "." +DatabaseConstants.CompassData.EndTime + "='"+ endTime  +"';";

		Cursor cursor = applicationDatabase.rawQuery(SELECT_QUERY, null);

		if (cursor != null && cursor.getCount() > 0) {
			if (cursor.moveToFirst()) {
				do {

					ReportsModel reportsModel = new ReportsModel();
					reportsModel.setCapturedAt(cursor.getString(cursor.getColumnIndex(DatabaseConstants.CapturedLux.CapturedAt)));
					reportsModel.setLuxValue(cursor.getString(cursor.getColumnIndex(DatabaseConstants.CapturedLux.LuxValue)));

					reportsModelList.add(reportsModel);
				} while (cursor.moveToNext());
			}
		}

		close();
		return reportsModelList;
	}

}
