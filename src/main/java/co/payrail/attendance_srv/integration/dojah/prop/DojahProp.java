package co.payrail.attendance_srv.integration.dojah.prop;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class DojahProp {
    @Value("${dojah.app.id}")
    private String appId;
    @Value("${dojah.private.key}")
    private String privateKey;
    @Value("${dojah.baseurl}")
    private String url;
}
