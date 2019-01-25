package cloud.fogbow.as.core.tokengenerator.plugins.ldap;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import cloud.fogbow.as.common.constants.Messages;
import cloud.fogbow.as.common.exceptions.InvalidParameterException;
import cloud.fogbow.as.common.exceptions.InvalidUserCredentialsException;
import cloud.fogbow.as.common.util.RSAUtil;
import cloud.fogbow.as.core.PropertiesHolder;
import cloud.fogbow.as.common.constants.FogbowConstants;
import cloud.fogbow.as.common.exceptions.UnauthenticatedUserException;
import cloud.fogbow.as.common.exceptions.UnexpectedException;

import cloud.fogbow.as.core.constants.ConfigurationConstants;
import cloud.fogbow.as.core.tokengenerator.TokenGeneratorPlugin;
import cloud.fogbow.as.core.tokengenerator.plugins.AttributeJoiner;

public class LdapTokenGeneratorPlugin implements TokenGeneratorPlugin {
    public static final String CRED_USERNAME = "username";
    public static final String CRED_PASSWORD = "password";
    private static final String ENCRYPT_TYPE = ":TYPE:";
    private static final String ENCRYPT_PASS = ":PASS:";
    private static final String PASSWORD_ENCRYPTED = "{" + ENCRYPT_TYPE + "}" + ENCRYPT_PASS;
    private String tokenProviderId;
    private String ldapBase;
    private String ldapUrl;
    private String encryptType;

    public LdapTokenGeneratorPlugin() {
        this.tokenProviderId = PropertiesHolder.getInstance().getProperty(ConfigurationConstants.LOCAL_MEMBER_ID);
        this.ldapBase = PropertiesHolder.getInstance().getProperty(ConfigurationConstants.LDAP_BASE);
        this.ldapUrl = PropertiesHolder.getInstance().getProperty(ConfigurationConstants.LDAP_ENDPOINT);
        this.encryptType = PropertiesHolder.getInstance().getProperty(ConfigurationConstants.LDAP_ENCRYPT_TYPE);
    }

    @Override
    public String createTokenValue(Map<String, String> userCredentials) throws InvalidUserCredentialsException,
            UnexpectedException, InvalidParameterException, UnauthenticatedUserException {

        String userId = userCredentials.get(CRED_USERNAME);
        String password = userCredentials.get(CRED_PASSWORD);

        String name = null;
        name = ldapAuthenticate(userId, password);

        Map<String, String> attributes = new HashMap<>();
        attributes.put(FogbowConstants.PROVIDER_ID_KEY, this.tokenProviderId);
        attributes.put(FogbowConstants.USER_ID_KEY, userId);
        attributes.put(FogbowConstants.USER_NAME_KEY, name);
        return AttributeJoiner.join(attributes);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public String ldapAuthenticate(String uid, String password) throws UnexpectedException, InvalidParameterException,
            InvalidUserCredentialsException, UnauthenticatedUserException {

        String contextFactory = "com.sun.jndi.ldap.LdapCtxFactory";
        String securityAuthentication = "simple";

        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, contextFactory);
        env.put(Context.PROVIDER_URL, this.ldapUrl);
        env.put(Context.SECURITY_AUTHENTICATION, securityAuthentication);

        DirContext ctx = null;
        String name = null;
        try {
            password = encryptPassword(password);

            ctx = new InitialDirContext(env);

            // Search the directory to get User Name and Domain from UID
            String filter = "(&(objectClass=inetOrgPerson)(uid={0}))";
            SearchControls ctls = new SearchControls();
            ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            ctls.setReturningAttributes(new String[0]);
            ctls.setReturningObjFlag(true);
            NamingEnumeration enm = ctx.search(this.ldapBase, filter, new String[]{uid}, ctls);

            String dn = null;

            if (enm.hasMore()) {
                SearchResult result = (SearchResult) enm.next();
                dn = result.getNameInNamespace();
                name = extractUserName(result);
            }

            if (dn == null || enm.hasMore()) {
                // uid not found or not unique
                throw new InvalidUserCredentialsException(cloud.fogbow.as.core.constants.Messages.Exception.UNABLE_TO_LOAD_LDAP_ACCOUNT);
            }

            // Bind with found DN and given password
            ctx.addToEnvironment(Context.SECURITY_PRINCIPAL, dn);
            ctx.addToEnvironment(Context.SECURITY_CREDENTIALS, password);
            // Perform a lookup in order to force a bind operation with JNDI
            ctx.lookup(dn);

            enm.close();

            return name;

        } catch (AuthenticationException e0) {
            throw new UnauthenticatedUserException(Messages.Exception.AUTHENTICATION_ERROR);
        } catch (NamingException e1) {
            throw new InvalidParameterException(cloud.fogbow.as.core.constants.Messages.Exception.LDAP_URL_MISSING, e1);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e2) {
            throw new UnexpectedException(e2.getMessage(), e2);
        }
    }

    private String extractUserName(SearchResult result) {
        String nameGroup[] = result.getName().split(",");
        if (nameGroup != null && nameGroup.length > 0) {
            String cnName[] = nameGroup[0].split("=");
            if (cnName != null && cnName.length > 1) {
                return cnName[1];
            }
        }
        return null;
    }

    private String encryptPassword(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        if (this.encryptType == null || this.encryptType.isEmpty()) {
            return password;
        }

        MessageDigest algorithm = MessageDigest.getInstance(this.encryptType);
        byte messageDigest[] = algorithm.digest(password.getBytes(RSAUtil.UTF_8));

        StringBuilder hexString = new StringBuilder();
        for (byte b : messageDigest) {
            hexString.append(String.format("%02X", 0xFF & b));
        }

        return PASSWORD_ENCRYPTED
                .replaceAll(ENCRYPT_TYPE, this.encryptType)
                .replaceAll(ENCRYPT_PASS, hexString.toString());
    }
}
