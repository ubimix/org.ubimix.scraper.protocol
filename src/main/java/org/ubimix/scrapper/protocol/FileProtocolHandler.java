/**
 * 
 */
package org.ubimix.scrapper.protocol;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.ubimix.commons.uri.Path;
import org.ubimix.commons.uri.Uri;
import org.ubimix.resources.IContentAdapter;
import org.ubimix.resources.IWrfResource;
import org.ubimix.resources.adapters.cache.CachedResourceAdapter;

/**
 * @author kotelnikov
 */
public class FileProtocolHandler implements IProtocolHandler {

    /**
     * 
     */
    public FileProtocolHandler() {
    }

    private HttpStatusCode copyFileResource(File file, IWrfResource resource) {
        try {
            IContentAdapter contentAdapter = resource
                .getAdapter(IContentAdapter.class);
            FileInputStream input = new FileInputStream(file);
            try {
                contentAdapter.writeContent(input);
            } finally {
                input.close();
            }
            CachedResourceAdapter cacheAdapter = resource
                .getAdapter(CachedResourceAdapter.class);
            cacheAdapter.setLastModified(System.currentTimeMillis());
            return HttpStatusCode.STATUS_200;
        } catch (IOException e) {
            return HttpStatusCode.STATUS_505;
        }
    }

    /**
     * @see org.ubimix.scrapper.protocol.IProtocolHandler#handleRequest(org.ubimix.commons.uri.Uri,
     *      java.lang.String, java.lang.String,
     *      org.ubimix.resources.IWrfResource)
     */
    public HttpStatusCode handleRequest(
        Uri uri,
        String login,
        String password,
        IWrfResource resource) {
        Path path = uri.getPath();
        File file = new File(path.toString());
        HttpStatusCode result = HttpStatusCode.STATUS_404;
        if (file.exists()) {
            if (!file.isFile()) {
                result = HttpStatusCode.STATUS_404;
            } else {
                result = copyFileResource(file, resource);
            }
        }
        return result;
    }
}
