package cloud.fogbow.as.core.systemidp.plugins.ldap;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

import cloud.fogbow.as.constants.Messages;
import cloud.fogbow.as.core.systemidp.SystemIdentityProviderPlugin;
import cloud.fogbow.common.exceptions.ConfigurationErrorException;
import cloud.fogbow.common.models.SystemUser;
import cloud.fogbow.common.util.CryptoUtil;
import cloud.fogbow.as.core.PropertiesHolder;
import cloud.fogbow.common.exceptions.UnauthenticatedUserException;

import cloud.fogbow.as.constants.ConfigurationPropertyKeys;
import org.apache.log4j.Logger;

public class LdapSystemIdentityProviderPlugin implements SystemIdentityProviderPlugin<SystemUser> {
    private static final Logger LOGGER = Logger.getLogger(LdapSystemIdentityProviderPlugin.class.getName());

    public static final String CRED_USERNAME = "username";
    public static final String CRED_PASSWORD = "password";
    private static final String ENCRYPT_TYPE = ":TYPE:";
    private static final String ENCRYPT_PASS = ":PASS:";
    private static final String PASSWORD_ENCRYPTED = "{" + ENCRYPT_TYPE + "}" + ENCRYPT_PASS;
    private String identityProviderId;
    private String ldapBase;
    private String ldapUrl;
    private String encryptType;

    public LdapSystemIdentityProviderPlugin(String identityProviderId, String ldapBase, 
            String ldapUrl, String encryptType) {
        this.identityProviderId = identityProviderId;
        this.ldapBase = ldapBase;
        this.ldapUrl = ldapUrl;
        this.encryptType = encryptType;
    }
    
    public LdapSystemIdentityProviderPlugin() {
        this.identityProviderId = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.PROVIDER_ID_KEY);
        this.ldapBase = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.LDAP_BASE_KEY);
        this.ldapUrl = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.LDAP_URL_KEY);
        this.encryptType = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.LDAP_ENCRYPT_TYPE_KEY);
        LOGGER.debug(String.format("p_id=[%s], base=[%s], url=[%s], encry=[%s]", identityProviderId, ldapBase, ldapUrl, encryptType));
    }

    @Override
    public SystemUser getSystemUser(Map<String, String> userCredentials) throws UnauthenticatedUserException,
            ConfigurationErrorException {

        String userId = userCredentials.get(CRED_USERNAME);
        String password = userCredentials.get(CRED_PASSWORD);

        String name = null;
        name = ldapAuthenticate(userId, password);

        return new SystemUser(userId, name, this.identityProviderId);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public String ldapAuthenticate(String uid, String password) throws UnauthenticatedUserException,
            ConfigurationErrorException {

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
                throw new UnauthenticatedUserException(Messages.Exception.UNABLE_TO_LOAD_LDAP_INFO);
            }

            // Bind with found DN and given password
            ctx.addToEnvironment(Context.SECURITY_PRINCIPAL, dn);
            ctx.addToEnvironment(Context.SECURITY_CREDENTIALS, password);
            // Perform a lookup in order to force a bind operation with JNDI
            ctx.lookup(dn);

            enm.close();

            return name;

        } catch (AuthenticationException e0) {
            throw new UnauthenticatedUserException();
        } catch (NamingException e1) {
            throw new ConfigurationErrorException(Messages.Exception.MISSING_LDAP_ENDPOINT);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e2) {
            throw new ConfigurationErrorException(Messages.Exception.INVALID_ALGORITHM_OR_ENCODING);
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
        byte messageDigest[] = algorithm.digest(password.getBytes(CryptoUtil.UTF_8));

        StringBuilder hexString = new StringBuilder();
        for (byte b : messageDigest) {
            hexString.append(String.format("%02X", 0xFF & b));
        }

        return PASSWORD_ENCRYPTED
                .replaceAll(ENCRYPT_TYPE, this.encryptType)
                .replaceAll(ENCRYPT_PASS, hexString.toString());
    }
}
