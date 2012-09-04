package org.ubimix.scrapper.protocol;

import org.ubimix.commons.uri.Uri;
import org.ubimix.resources.IWrfResource;

public interface IProtocolHandler {

    HttpStatusCode handleRequest(
        Uri uri,
        String login,
        String password,
        IWrfResource resource);

}