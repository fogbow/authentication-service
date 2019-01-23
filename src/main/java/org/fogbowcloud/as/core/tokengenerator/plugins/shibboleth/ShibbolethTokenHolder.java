package org.fogbowcloud.as.core.tokengenerator.plugins.shibboleth;

import org.apache.commons.lang.StringUtils;

public class ShibbolethTokenHolder {

	protected static final String SHIBBOLETH_SEPARETOR = ShibbolethTokenGenerator.SHIBBOLETH_SEPARATOR;

	public static final int PARAMETERS_SIZE_TOKEN_VALUE = 7;

	public static final int STR_TOKEN_VALUE_INDEX = 0;
	public static final int TOKEN_PROVIDER_TOKEN_VALUE_INDEX = 1;
	public static final int USER_ID_TOKEN_VALUE_INDEX = 2;
	public static final int USER_NAME_TOKEN_VALUE_INDEX = 3;
	public static final int SAML_ATTRIBUTES_TOKEN_VALUE_INDEX = 4;
	public static final int EXPIRATION_TIME_TOKEN_VALUE_INDEX = 5;

	public static String createRawToken(String tokenValue, String tokenProvider, String userId, String userName,
			String samlAttributes, String expirationTime) {

		int parametersWithoutSignature = PARAMETERS_SIZE_TOKEN_VALUE - 1;
		String[] parameters = new String[parametersWithoutSignature];
		parameters[STR_TOKEN_VALUE_INDEX] = tokenValue;
		parameters[TOKEN_PROVIDER_TOKEN_VALUE_INDEX] = tokenProvider;
		parameters[USER_ID_TOKEN_VALUE_INDEX] = userId;
		parameters[USER_NAME_TOKEN_VALUE_INDEX] = userName;
		parameters[SAML_ATTRIBUTES_TOKEN_VALUE_INDEX] = samlAttributes;
		parameters[EXPIRATION_TIME_TOKEN_VALUE_INDEX] = expirationTime;

		return StringUtils.join(parameters, SHIBBOLETH_SEPARETOR);
	}

	public static String generateTokenValue(String rawToken, String rawTokenSignature) {
		String[] parameters = new String[] {
				rawToken,
				rawTokenSignature
		};
		return StringUtils.join(parameters, SHIBBOLETH_SEPARETOR);
	}
}