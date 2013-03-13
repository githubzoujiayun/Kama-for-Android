package com.label305.kamav2_android.auth;

import java.sql.SQLException;

import net.smartam.leeloo.common.exception.OAuthProblemException;
import net.smartam.leeloo.common.exception.OAuthSystemException;
import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.label305.kamav2_android.KamaParam;
import com.label305.kamav2_android.auth.objects.AuthData;
import com.label305.kamav2_android.exceptions.DatabaseKamaException;
import com.label305.kamav2_android.exceptions.NotAuthorizedKamaException;

public class AuthHelper {

	public static boolean authenticate(Context context, String authUrl, String login, String password) throws NotAuthorizedKamaException, DatabaseKamaException {
		AuthDatabaseHelper databaseHelper = AuthDatabaseHelper.getHelper(context);
		MyOAuthClient oAuthClient = new MyOAuthClient(new MyURLConnectionClient());

		try {
			AuthData authToken = new AuthData(oAuthClient.authenticate(authUrl, KamaParam.APIKEY, login, password));
			if (authToken.getToken() != null && authToken.getToken().length() > 0) {
				// store in database
				// get our dao
				Dao<AuthData, Integer> kamaDao = databaseHelper.getAuthDataDao();

				kamaDao.create(authToken);

				return true;
			}
		} catch (OAuthProblemException e) {
			throw new NotAuthorizedKamaException(e);
		} catch (OAuthSystemException e) {
			throw new NotAuthorizedKamaException(e);
		} catch (SQLException e) {
			throw new DatabaseKamaException(e);
		}

		return false;
	}

	public static boolean authenticateFacebook(Context context, String authUrl, String accessToken) throws NotAuthorizedKamaException, DatabaseKamaException {
		AuthDatabaseHelper databaseHelper = AuthDatabaseHelper.getHelper(context);

		MyOAuthClient oAuthClient = new MyOAuthClient(new MyURLConnectionClient());

		try {
			AuthData authToken = new AuthData(oAuthClient.authenticateFacebook(authUrl, KamaParam.APIKEY, accessToken));
			if (authToken.getToken() != null && authToken.getToken().length() > 0) {
				// store in database
				// get our dao
				Dao<AuthData, Integer> kamaDao = databaseHelper.getAuthDataDao();

				kamaDao.create(authToken);

				return true;
			}
		} catch (OAuthProblemException e) {
			throw new NotAuthorizedKamaException(e);
		} catch (OAuthSystemException e) {
			throw new NotAuthorizedKamaException(e);
		} catch (SQLException e) {
			throw new DatabaseKamaException(e);
		}

		return false;
	}
}
