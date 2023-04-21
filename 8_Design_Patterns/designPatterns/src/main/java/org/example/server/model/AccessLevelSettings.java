package org.example.server.model;

import java.util.List;
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
public class AccessLevelSettings extends Settings{

    private Map<AccessLevel, List<String>> accessForUrls;

}
