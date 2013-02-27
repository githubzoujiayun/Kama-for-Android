package com.label305.kamav2_android.auth;

import java.sql.SQLException;

import net.smartam.leeloo.common.exception.OAuthProblemException;
import net.smartam.leeloo.common.exception.OAuthSystemException;

import com.j256.ormlite.dao.Dao;
import com.label305.kamav2_android.KamaParam;
import com.label305.kamav2_android.auth.objects.AuthData;
import com.label305.kamav2_android.exceptions.KamaException_Database;
import com.label305.kamav2_android.exceptions.KamaException_Not_Authorized;

public class AuthHelper {
	
	private AuthData authToken;
	
	private AuthDatabaseHelper databaseHelper;
	
	AuthHelper(AuthDatabaseHelper databaseHelper) {
		this.databaseHelper = databaseHelper;
	}
	
	
	public boolean authenticate(String authUrl, String login, String password) throws KamaException_Not_Authorized, KamaException_Database {
		MyOAuthClient oAuthClient = new MyOAuthClient(new MyURLConnectionClient());

		try {
			authToken = new AuthData(oAuthClient.authenticate(authUrl, KamaParam.APPKEY, login, password));
			if (authToken.getToken() != null && authToken.getToken().length() > 0) {
				// store in database
				// get our dao
				Dao<AuthData, Integer> kamaDao = databaseHelper.getAuthDataDao();

				kamaDao.create(authToken);

				return true;
			}
		} catch(OAuthProblemException e) {
			throw new KamaException_Not_Authorized(e);
		} catch (OAuthSystemException e) {
			throw new KamaException_Not_Authorized(e);
		} catch (SQLException e) {
			throw new KamaException_Database(e);
		}

		return false;
	}

	public boolean authenticateFacebook(String authUrl, String accessToken) throws KamaException_Not_Authorized, KamaException_Database {
		MyOAuthClient oAuthClient = new MyOAuthClient(new MyURLConnectionClient());

		try {
			authToken = new AuthData(oAuthClient.authenticateFacebook(authUrl, KamaParam.APPKEY, accessToken));
			if (authToken.getToken() != null && authToken.getToken().length() > 0) {
				// store in database
				// get our dao
				Dao<AuthData, Integer> kamaDao = databaseHelper.getAuthDataDao();

				kamaDao.create(authToken);
				
				return true;
			}
		} catch(OAuthProblemException e) {
			throw new KamaException_Not_Authorized(e);
		} catch (OAuthSystemException e) {
			throw new KamaException_Not_Authorized(e);
		} catch (SQLException e) {
			throw new KamaException_Database(e);
		}

		return false;
	}
}
