package com.github.bernardflo.stc;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.parser.OpenAPIV3Parser;

import java.util.List;
import java.util.stream.Stream;

public class SwaggerReader {

    public List<ApiInfo> read() {
//        var openAPI = new OpenAPIV3Parser().read("https://pri-mcp-mds.dev.music-flo.com/v3/api-docs");
        var openAPI = new OpenAPIV3Parser().read("file:/Users/bernard/Downloads/mcp-mds-api-docs.json");
        return readOpenAPI(openAPI);
    }

    private List<ApiInfo> readOpenAPI(OpenAPI openAPI) {
        return openAPI.getPaths().entrySet().stream().flatMap(entry -> readPath(entry.getKey(), entry.getValue())).toList();
    }

    private Stream<ApiInfo> readPath(String pathName, PathItem pathItem) {
        return pathItem.readOperationsMap().entrySet().stream()
                .map(entry -> readOperation(pathName, entry.getKey(), entry.getValue()));
    }

    private ApiInfo readOperation(String pathName, PathItem.HttpMethod httpMethod, Operation operation) {
        return new ApiInfo(
                httpMethod.name(),
                pathName,
                operation.getDescription(),
                operation.getParameters() != null ? operation.getParameters().stream().map(this::parameterToInfo).toList() : List.of(),
                apiResponsesToInfo(operation.getResponses())
        );
    }

    private ParameterInfo parameterToInfo(Parameter parameter) {
        return new ParameterInfo(
                parameter.getName(),
                parameter.getDescription(),
                schemaToInfo(parameter.getSchema())
        );
    }

    private ResponseInfo apiResponsesToInfo(ApiResponses apiResponses) {
        var responseKeyIterator = apiResponses.keySet().iterator();
        if (!responseKeyIterator.hasNext()) {
            return new ResponseInfo(new SchemaInfo());
        }
        var firstContentKey = responseKeyIterator.next();
        var apiResponse = apiResponses.get(firstContentKey);
        var content = apiResponse.getContent();
        if (content == null) {
            return new ResponseInfo(new SchemaInfo());
        }
        var contentIterator = content.keySet().iterator();
        if (!contentIterator.hasNext()) {
            return new ResponseInfo(new SchemaInfo());
        }
        var firstKey = contentIterator.next();
        var mediaType = content.get(firstKey);
        return new ResponseInfo(
                schemaToInfo(mediaType.getSchema())
        );
    }

    private SchemaInfo schemaToInfo(Schema<?> schema) {
        return new SchemaInfo();
    }

}
