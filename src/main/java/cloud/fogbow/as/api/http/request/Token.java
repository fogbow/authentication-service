package cloud.fogbow.as.api.http.request;

import cloud.fogbow.as.api.parameters.TokenParameters;
import cloud.fogbow.as.constants.Messages;
import cloud.fogbow.as.constants.SystemConstants;
import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.as.core.ApplicationFacade;
import cloud.fogbow.common.exceptions.InternalServerErrorException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.log4j.Logger;
import cloud.fogbow.as.constants.ApiDocumentation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(value = Token.TOKEN_ENDPOINT)
@Api(description = ApiDocumentation.Token.API)
public class Token {
    public static final String TOKEN_ENDPOINT = SystemConstants.SERVICE_BASE_ENDPOINT + "tokens";

    private final Logger LOGGER = Logger.getLogger(Token.class);

    @ApiOperation(value = ApiDocumentation.Token.CREATE_OPERATION)
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<cloud.fogbow.as.api.http.response.Token> createTokenValue(
            @ApiParam(value = ApiDocumentation.Token.CREATE_REQUEST_BODY)
            @RequestBody TokenParameters request)
            throws FogbowException {

        try {
            LOGGER.info(String.format(Messages.Log.CREATE_TOKEN_REQUEST_RECEIVED_S, request.getCredentials().size()));
            String tokenValue = ApplicationFacade.getInstance().createToken(
                    request.getCredentials(), request.getPublicKey());
            cloud.fogbow.as.api.http.response.Token token = new cloud.fogbow.as.api.http.response.Token(tokenValue);
            return new ResponseEntity<>(token, HttpStatus.CREATED);
        } catch (Exception e) {
            LOGGER.info(String.format(Messages.Log.OPERATION_RETURNED_ERROR_S, e.getMessage()), e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}
