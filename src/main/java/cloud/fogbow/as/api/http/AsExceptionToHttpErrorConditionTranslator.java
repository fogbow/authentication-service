package cloud.fogbow.as.api.http;

import cloud.fogbow.common.http.FogbowExceptionToHttpErrorConditionTranslator;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class AsExceptionToHttpErrorConditionTranslator extends FogbowExceptionToHttpErrorConditionTranslator {
}
