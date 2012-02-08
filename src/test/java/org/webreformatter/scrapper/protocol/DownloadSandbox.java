/**
 * 
 */
package org.webreformatter.scrapper.protocol;

import java.io.File;
import java.io.IOException;

import org.webreformatter.commons.adapters.CompositeAdapterFactory;
import org.webreformatter.commons.uri.Path;
import org.webreformatter.commons.uri.Uri;
import org.webreformatter.commons.uri.UriToPath;
import org.webreformatter.resources.IWrfResource;
import org.webreformatter.resources.IWrfResourceProvider;
import org.webreformatter.resources.adapters.string.StringAdapter;
import org.webreformatter.resources.impl.WrfRepositoryUtils;
import org.webreformatter.resources.impl.WrfResourceRepository;

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
