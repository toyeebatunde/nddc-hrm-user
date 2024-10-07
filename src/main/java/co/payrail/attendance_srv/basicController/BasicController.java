package co.payrail.attendance_srv.basicController;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/public")
@Slf4j
@RequiredArgsConstructor
public class BasicController extends Controller {

    @GetMapping
    public String getAccountDetailsRequest() {
        return "Hello";
    }


}
