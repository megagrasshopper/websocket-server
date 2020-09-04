package ru.grasshopper.ws.common.domain;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ClientRequest {
    private String ip;
    private String userAgent;
    private String url;
}
