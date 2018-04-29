package un.light.mafhh.utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.apache.http.HttpResponse;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author ahajri
 *
 */
public final class UrlUtils {

	/**
	 * 
	 * @param root
	 * @param params
	 * @return
	 * @throws URISyntaxException
	 * @throws MalformedURLException
	 */
	public static final String buildParamUrl(@NotNull String root, Map<String, Object> params)
			throws URISyntaxException, MalformedURLException {
		URIBuilder b = new URIBuilder(root);
		params.forEach((k, v) -> b.addParameter(k, String.valueOf(v)));
		return b.build().toURL().toString();
	}

	/**
	 * 
	 * @param response
	 * @param clazz
	 * @return  <T>
	 * @throws IOException
	 */
	public static <T> T retrieveResourceFromResponse(HttpResponse response, Class<T> clazz) throws IOException {

		String jsonFromResponse = EntityUtils.toString(response.getEntity());
		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return mapper.readValue(jsonFromResponse, clazz);
	}
}
