package com.smartcity.databasehelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.smartcity.model.ComplaintItem;
import com.smartcity.model.NotificationsItem;
import com.smartcity.util.Constants;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String LOG = "DatabaseHelper";
	private static final int DATABASE_VERSION = 4;
	private static final String DATABASE_NAME = "SmartCity";
	private static final String TABLE_NOTIFICATION= "notification";
	private static final String TABLE_COMPLAINTS= "complaints";
	private static final String CREATE_TABLE_Notification = "CREATE TABLE "
			+ TABLE_NOTIFICATION + "("
			+ Constants.id  + " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ Constants.notification_message+ " TEXT,"
			+ Constants.notification_date+ " TEXT, "
			+ Constants.notification_subject+ " TEXT,"
			+ Constants.notification_sortMessage+ " TEXT,"
			+ Constants.notification_complaintId+ " TEXT,"
			+ Constants.notification_address+ " TEXT,"
			+ Constants.notification_contactNumber+ " TEXT"+
			")";
	private static final String CREATE_TABLE_COMPLAINTS = "CREATE TABLE "
			+ TABLE_COMPLAINTS + "("
			+ Constants.userComplaintId+ " TEXT,"
			+ Constants.address+ " TEXT,"
			+ Constants.link+ " TEXT,"
			+ Constants.contactNumber+ " TEXT,"
			+ Constants.date+ " TEXT"+
			")";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL(CREATE_TABLE_Notification);
		db.execSQL(CREATE_TABLE_COMPLAINTS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATION);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPLAINTS);
		onCreate(db);
	}

	public long addInToNotification(NotificationsItem cartItemSqlite)
	{
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(Constants.notification_message, cartItemSqlite.getNotification_message());
		values.put(Constants.notification_date, cartItemSqlite.getNotification_date());
		values.put(Constants.notification_subject, cartItemSqlite.getNotification_subject());
		values.put(Constants.notification_sortMessage, cartItemSqlite.getNotification_sortMessage());
		values.put(Constants.notification_complaintId, cartItemSqlite.getNotification_complaintId());
		values.put(Constants.notification_address, cartItemSqlite.getNotification_address());
		values.put(Constants.notification_contactNumber, cartItemSqlite.getNotification_contactNumber());

		// insert row
		long tag_id = db.insert(TABLE_NOTIFICATION, null, values);

		return tag_id;
	}

	public long addInToUSerComplaints(ComplaintItem complaintItem)
	{
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(Constants.userComplaintId, complaintItem.getUserComplaintId());
		values.put(Constants.link, complaintItem.getLink());
		values.put(Constants.address, complaintItem.getAddress());
		values.put(Constants.contactNumber, complaintItem.getContactNumber());
		values.put(Constants.date, complaintItem.getDate());

		// insert row
		long tag_id = db.insert(TABLE_COMPLAINTS, null, values);

		return tag_id;
	}

	public ArrayList<NotificationsItem> getAllNotification()
	{
		ArrayList<NotificationsItem> tags = new ArrayList<NotificationsItem>();
		String selectQuery = "SELECT  * FROM " + TABLE_NOTIFICATION +" asc";

		Log.e(LOG, selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				NotificationsItem t = new NotificationsItem();
				t.setId(c.getString((c.getColumnIndex(Constants.id))));
				t.setNotification_message(c.getString((c.getColumnIndex(Constants.notification_message))));
				t.setNotification_date(c.getString((c.getColumnIndex(Constants.notification_date))));
				t.setNotification_subject(c.getString((c.getColumnIndex(Constants.notification_subject))));
				t.setNotification_sortMessage(c.getString((c.getColumnIndex(Constants.notification_sortMessage))));
				t.setNotification_complaintId(c.getString((c.getColumnIndex(Constants.notification_complaintId))));
				t.setNotification_address(c.getString((c.getColumnIndex(Constants.notification_address))));
				t.setNotification_contactNumber(c.getString((c.getColumnIndex(Constants.notification_contactNumber))));

				// adding to tags list
				tags.add(t);
			} while (c.moveToNext());
		}
		return tags;
	}

	public ArrayList<ComplaintItem> getAllComplaints()
	{
		ArrayList<ComplaintItem> tags = new ArrayList<ComplaintItem>();
		String selectQuery = "SELECT  * FROM " + TABLE_COMPLAINTS +" asc";

		Log.e(LOG, selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				ComplaintItem t = new ComplaintItem();
				t.setUserComplaintId(c.getString((c.getColumnIndex(Constants.userComplaintId))));
				t.setLink(c.getString((c.getColumnIndex(Constants.link))));
				t.setAddress(c.getString((c.getColumnIndex(Constants.address))));
				t.setContactNumber(c.getString((c.getColumnIndex(Constants.contactNumber))));
				t.setDate(c.getString((c.getColumnIndex(Constants.date))));

				// adding to tags list
				tags.add(t);
			} while (c.moveToNext());
		}
		return tags;
	}

	public void deleteAllNotification()
	{
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_NOTIFICATION, null, null);
	}

	public void deleteAllComplaints()
	{
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_COMPLAINTS, null, null);
	}

//	public void updateNotification(){
//		long tag_id;
//
//		ContentValues values = new ContentValues();
//
//		values.put(Constants.status, cartDBItem.getProductQuantity());
//		SQLiteDatabase db = this.getWritableDatabase();
//		tag_id = db.update(TABLE_USERCART,values, Constants.storeId+"=\'"+cartDBItem.getStoreId()+"\' and "
//				+Constants.productId+"=\'"+cartDBItem.getProductId()+"\'", null);
//	}
}
