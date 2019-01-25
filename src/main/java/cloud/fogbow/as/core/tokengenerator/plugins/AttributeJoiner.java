package cloud.fogbow.as.core.tokengenerator.plugins;

import org.apache.commons.lang.StringUtils;
import cloud.fogbow.as.common.constants.FogbowConstants;

import java.util.ArrayList;
import java.util.Map;

public class AttributeJoiner {
    public static String join(Map<String, String> attributesMap) {
        ArrayList<String> attributes = new ArrayList<>();
        for (Map.Entry<String, String> entry : attributesMap.entrySet())
        {
            attributes.add(entry.getKey() + FogbowConstants.KEY_VALUE_SEPARATOR + entry.getValue());
        }
        return StringUtils.join(attributes, FogbowConstants.ATTRIBUTE_SEPARATOR);
    }
}
