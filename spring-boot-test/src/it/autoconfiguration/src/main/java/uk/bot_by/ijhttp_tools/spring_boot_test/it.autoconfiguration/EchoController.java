package uk.bot_by.ijhttp_tools.spring_boot_test.it.autoconfiguration;

import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EchoController {

  @GetMapping("/echo")
  public ResponseEntity<RequestEntity<Void>> echo(RequestEntity<Void> request) {
    return ResponseEntity.ok(request);
  }

}