package cloud.fogbow.as.api.http;

import cloud.fogbow.common.constants.Messages;
import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.as.core.ApplicationFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.log4j.Logger;
import cloud.fogbow.as.core.constants.ApiDocumentation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@CrossOrigin
@RestController
@RequestMapping(value = Token.TOKEN_ENDPOINT)
@Api(description = ApiDocumentation.Token.API)
public class Token {
    public static final String TOKEN_ENDPOINT = "tokens";
    public static final String PUBLIC_KEY_KEY = "publicKey";

    private final Logger LOGGER = Logger.getLogger(Token.class);

    @ApiOperation(value = ApiDocumentation.Token.CREATE_OPERATION)
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> createTokenValue(
            @ApiParam(value = ApiDocumentation.Token.CREATE_REQUEST_BODY)
            @RequestBody HashMap<String, String> userCredentials,
            @ApiParam(value = ApiDocumentation.Token.PUBLIC_KEY)
            @RequestHeader(required = false, value = PUBLIC_KEY_KEY) String publicKey)
            throws FogbowException {

        try {
            LOGGER.info(String.format(cloud.fogbow.as.core.constants.Messages.Info.RECEIVING_CREATE_TOKEN_REQUEST, userCredentials.size()));
            String tokenValue = ApplicationFacade.getInstance().createTokenValue(userCredentials, publicKey);
            return new ResponseEntity<String>(tokenValue, HttpStatus.CREATED);
        } catch (Exception e) {
            LOGGER.info(String.format(
                    Messages.Exception.GENERIC_EXCEPTION, e.getMessage()), e);
            throw e;
        }
    }
}
