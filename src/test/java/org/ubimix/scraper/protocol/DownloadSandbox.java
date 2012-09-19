/**
 * 
 */
package org.ubimix.scraper.protocol;

import java.io.File;
import java.io.IOException;

import org.ubimix.commons.adapters.CompositeAdapterFactory;
import org.ubimix.commons.uri.Path;
import org.ubimix.commons.uri.Uri;
import org.ubimix.commons.uri.UriToPath;
import org.ubimix.resources.IWrfResource;
import org.ubimix.resources.IWrfResourceProvider;
import org.ubimix.resources.adapters.string.StringAdapter;
import org.ubimix.resources.impl.WrfRepositoryUtils;
import org.ubimix.resources.impl.WrfResourceRepository;

/**
 * @author kotelnikov
 */
public class DownloadSandbox {

    public static void main(String[] args) throws IOException {
        new DownloadSandbox().run();
    }

    /**
     * 
     */
    public DownloadSandbox() {
    }

    protected WrfResourceRepository newResourceRepository() {
        File rootDir = new File("./tmp");
        CompositeAdapterFactory adapters = new CompositeAdapterFactory();
        WrfResourceRepository resourceRepository = new WrfResourceRepository(
            adapters,
            rootDir);
        WrfRepositoryUtils.registerDefaultResourceAdapters(adapters);
        return resourceRepository;
    }

    private void run() throws IOException {
        String str = "https://wiki.webreformatter.com/xwiki/bin/view/wiki/WebHome?srid=CvLRdwKi";
        // str = "https://bitbucket.org/smartproject/oauth-2.0/wiki/Home";
        Uri uri = new Uri(str);
        String login = null;
        String password = null;

        CompositeProtocolHandler handler = new CompositeProtocolHandler();
        ProtocolHandlerUtils.registerDefaultProtocols(handler);
        WrfResourceRepository resourceRepository = newResourceRepository();
        IWrfResourceProvider resourceProvider = resourceRepository
            .getResourceProvider("test", true);
        Path path = UriToPath.getPath(uri);
        IWrfResource resource = resourceProvider.getResource(path, true);
        HttpStatusCode status = handler.handleRequest(
            uri,
            login,
            password,
            resource);
        System.out.println(status);
        String content = resource
            .getAdapter(StringAdapter.class)
            .getContentAsString();
        System.out.println(content);
    }
}
