package org.ubimix.scraper.protocol;

import java.io.IOException;
import java.io.InputStream;

import org.ubimix.commons.uri.Path;
import org.ubimix.commons.uri.Uri;
import org.ubimix.commons.uri.UriToPath;
import org.ubimix.resources.IContentAdapter;
import org.ubimix.resources.IWrfRepository;
import org.ubimix.resources.IWrfResource;
import org.ubimix.resources.IWrfResourceProvider;
import org.ubimix.resources.adapters.cache.CachedResourceAdapter;

/**
 * This implementation of the {@link IProtocolHandler} loads the content from a
 * local repository (see {@link IWrfRepository}).
 * 
 * @author kotelnikov
 */
public class WrfRepositoryProtocolHandler implements IProtocolHandler {

    private IWrfResourceProvider fResourceProvider;

    public WrfRepositoryProtocolHandler(IWrfResourceProvider resourceProvider) {
        fResourceProvider = resourceProvider;
    }

    public HttpStatusCode handleRequest(
        Uri uri,
        String login,
        String password,
        IWrfResource target) {
        try {
            Path path = UriToPath.getPath(uri);
            HttpStatusCode status = HttpStatusCode.STATUS_404;

            IWrfResource source = fResourceProvider.getResource(path, true);
            IContentAdapter sourceContentAdapter = source
                .getAdapter(IContentAdapter.class);
            if (sourceContentAdapter.exists()) {
                InputStream input = sourceContentAdapter.getContentInput();
                try {
                    IContentAdapter outputContentAdapter = target
                        .getAdapter(IContentAdapter.class);
                    outputContentAdapter.writeContent(input);
                    status = HttpStatusCode.STATUS_200;
                } finally {
                    input.close();
                }
            }

            CachedResourceAdapter targetPropertyAdapter = target
                .getAdapter(CachedResourceAdapter.class);
            targetPropertyAdapter.copyPropertiesFrom(source);
            targetPropertyAdapter.setStatusCode(status.getStatusCode());
            targetPropertyAdapter.setLastModified(System.currentTimeMillis());
            return status;
        } catch (IOException e) {
            return HttpStatusCode.STATUS_505;
        }
    }
}