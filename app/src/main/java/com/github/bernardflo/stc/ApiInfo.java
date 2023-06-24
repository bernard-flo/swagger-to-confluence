package com.github.bernardflo.stc;

import java.util.List;

public record ApiInfo(
        String method,
        String path,
        String description,
        List<ParameterInfo> parameters,
        ResponseInfo response
) {
}
