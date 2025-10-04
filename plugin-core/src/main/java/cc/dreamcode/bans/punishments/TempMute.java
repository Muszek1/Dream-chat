package cc.dreamcode.bans.punishments;

import eu.okaeri.configs.OkaeriConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class TempMute extends OkaeriConfig {
    private String reason;
    private final String mutedBy;
    private long date;
    private final long until;

}
