package com.label305.kamav2_android.auth;

import java.sql.SQLException;

import net.smartam.leeloo.common.exception.OAuthProblemException;
import net.smartam.leeloo.common.exception.OAuthSystemException;
import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.table.TableUtils;
import com.label305.kamav2_android.auth.objects.AuthData;
import com.label305.kamav2_android.exceptions.DatabaseKamaException;
import com.label305.kamav2_android.exceptions.NotAuthorizedKamaException;

public class AuthHelper {

	public static void authenticate(Context context, String authUrl, String apiKey, String login, String password) throws NotAuthorizedKamaException, DatabaseKamaException {
		AuthDatabaseHelper databaseHelper = AuthDatabaseHelper.getHelper(context);
		MyOAuthClient oAuthClient = new MyOAuthClient(new MyURLConnectionClient());

		try {
			AuthData authToken = new AuthData(oAuthClient.authenticate(authUrl, apiKey, login, password));
			if (authToken.getToken() != null && authToken.getToken().length() > 0) {
				Dao<AuthData, Integer> kamaDao = databaseHelper.getAuthDataDao();
				kamaDao.create(authToken);
			}
		} catch (OAuthProblemException e) {
			throw new NotAuthorizedKamaException(e);
		} catch (OAuthSystemException e) {
			throw new NotAuthorizedKamaException(e);
		} catch (SQLException e) {
			throw new DatabaseKamaException(e);
		}

	}

	public static void authenticateFacebook(Context context, String authUrl, String apiKey, String accessToken) throws NotAuthorizedKamaException, DatabaseKamaException {
		AuthDatabaseHelper databaseHelper = AuthDatabaseHelper.getHelper(context);

		MyOAuthClient oAuthClient = new MyOAuthClient(new MyURLConnectionClient());

		try {
			AuthData authToken = new AuthData(oAuthClient.authenticateFacebook(authUrl, apiKey, accessToken));
			if (authToken.getToken() != null && authToken.getToken().length() > 0) {
				Dao<AuthData, Integer> kamaDao = databaseHelper.getAuthDataDao();
				kamaDao.create(authToken);
			}
		} catch (OAuthProblemException e) {
			throw new NotAuthorizedKamaException(e);
		} catch (OAuthSystemException e) {
			throw new NotAuthorizedKamaException(e);
		} catch (SQLException e) {
			throw new DatabaseKamaException(e);
		}
	}

	public static void logOut(Context context) throws DatabaseKamaException {
		AuthDatabaseHelper databaseHelper = AuthDatabaseHelper.getHelper(context);

		try {
			TableUtils.clearTable(databaseHelper.getConnectionSource(), AuthData.class);
		} catch (SQLException e) {
			throw new DatabaseKamaException(e);
		}
	}
}
