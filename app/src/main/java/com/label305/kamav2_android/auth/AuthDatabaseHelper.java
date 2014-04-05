package com.label305.kamav2_android.auth;

import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.label305.kamav2_android.auth.objects.AuthData;

public class AuthDatabaseHelper extends OrmLiteSqliteOpenHelper {

	// name of the database file for your application -- change to something
	// appropriate for your app
	private static final String DATABASE_NAME = "auth.db";
	// any time you make changes to your database objects, you may have to
	// increase the database version
	private static final int DATABASE_VERSION = 1;

	// the DAO object we use to access the SimpleData table
	private Dao<AuthData, Integer> kamaDao = null;

	// we do this so there is only one helper
	private static AuthDatabaseHelper helper = null;
	private static final AtomicInteger usageCounter = new AtomicInteger(0);

	public AuthDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public AuthDatabaseHelper(Context context, String dbName, CursorFactory cf, int dbVersion) {
		super(context, dbName, cf, dbVersion);
	}

	/**
	 * Get the helper, possibly constructing it if necessary. For each call to
	 * this method, there should be 1 and only 1 call to {@link #close()}.
	 */
	public static synchronized AuthDatabaseHelper getHelper(Context context) {
		if (helper == null) {
			helper = new AuthDatabaseHelper(context);
		}
		usageCounter.incrementAndGet();
		return helper;
	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			TableUtils.createTable(connectionSource, AuthData.class);

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
		try {
			TableUtils.dropTable(connectionSource, AuthData.class, true);
			// after we drop the old databases, we create the new ones
			onCreate(db, connectionSource);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * Returns the Database Access Object (DAO) for our SimpleData class. It
	 * will create it or just give the cached value.
	 */
	public Dao<AuthData, Integer> getAuthDataDao() throws SQLException {
		if (kamaDao == null) {
			kamaDao = getDao(AuthData.class);
		}
		return kamaDao;
	}

}
