package org.fogbowcloud.as.core.tokengenerator.plugins.shibboleth;

import org.apache.commons.lang.StringUtils;
import org.fogbowcloud.as.common.constants.FogbowConstants;

public class ShibbolethTokenHolder {
	private static final int PARAMETERS_SIZE_TOKEN_VALUE = 7;
	private static final int STR_TOKEN_VALUE_INDEX = 0;
	private static final int TOKEN_PROVIDER_TOKEN_VALUE_INDEX = 1;
	private static final int USER_ID_TOKEN_VALUE_INDEX = 2;
	private static final int USER_NAME_TOKEN_VALUE_INDEX = 3;
	private static final int SAML_ATTRIBUTES_TOKEN_VALUE_INDEX = 4;

	public static String createRawToken(String tokenValue, String tokenProvider, String userId, String userName,
			String samlAttributes) {

		int parametersWithoutSignature = PARAMETERS_SIZE_TOKEN_VALUE - 1;
		String[] parameters = new String[parametersWithoutSignature];
		parameters[STR_TOKEN_VALUE_INDEX] = tokenValue;
		parameters[TOKEN_PROVIDER_TOKEN_VALUE_INDEX] = tokenProvider;
		parameters[USER_ID_TOKEN_VALUE_INDEX] = userId;
		parameters[USER_NAME_TOKEN_VALUE_INDEX] = userName;
		parameters[SAML_ATTRIBUTES_TOKEN_VALUE_INDEX] = samlAttributes;

		return StringUtils.join(parameters, FogbowConstants.ATTRIBUTE_SEPARATOR);
	}

	public static String generateTokenValue(String rawToken, String rawTokenSignature) {
		String[] parameters = new String[] {
				rawToken,
				rawTokenSignature
		};
		return StringUtils.join(parameters, FogbowConstants.ATTRIBUTE_SEPARATOR);
	}
}