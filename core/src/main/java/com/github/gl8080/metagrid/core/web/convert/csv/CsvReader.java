package com.github.gl8080.metagrid.core.web.convert.csv;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;

public class CsvReader implements MessageBodyReader<CsvUploadFile> {

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return CsvUploadFile.class.isAssignableFrom(type)
                && mediaType.isCompatible(new MediaType("text", "csv"));
    }

    @Override
    public CsvUploadFile readFrom(Class<CsvUploadFile> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
        return new CsvUploadFile(entityStream);
    }
}
