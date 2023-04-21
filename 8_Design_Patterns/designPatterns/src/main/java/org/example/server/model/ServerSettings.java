package org.example.server.model;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.example.server.AccessLevel;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ServerSettings extends Settings{

    private String port;
    private String protocol;
    private String connectionTimeout;
    private String redirectPort;
    private String hostName;
    private Map<String, AccessLevel> userAccessSettings;

}
