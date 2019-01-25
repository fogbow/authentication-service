package cloud.fogbow.as.api.http;

import cloud.fogbow.common.exceptions.UnexpectedException;
import cloud.fogbow.as.core.ApplicationFacade;
import cloud.fogbow.as.core.constants.ApiDocumentation;
import cloud.fogbow.as.core.constants.Messages;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(value = PublicKey.PUBLIC_KEY_ENDPOINT)
@Api(description = ApiDocumentation.PublicKey.API)
public class PublicKey {
    public static final String PUBLIC_KEY_ENDPOINT = "publicKey";

    private final Logger LOGGER = Logger.getLogger(PublicKey.class);

    @ApiOperation(value = ApiDocumentation.PublicKey.GET_OPERATION)
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> getPublicKey() throws UnexpectedException {
        try {
            LOGGER.info(Messages.Info.RECEIVING_GET_PUBLIC_KEY_REQUEST);
            String publicKey = ApplicationFacade.getInstance().getPublicKey();
            return new ResponseEntity<>(publicKey, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.info(String.format(
                    cloud.fogbow.common.constants.Messages.Exception.GENERIC_EXCEPTION, e.getMessage()), e);
            throw e;
        }
    }
}