package coderunner.controllers;

import executor.CodeRunnerExec;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import payload.request.CodeRunnerRequest;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/coderunner")
public class CodeRunnerController {

    @PostMapping("/")
    public ResponseEntity testExecCodeRunner(@Valid @RequestBody CodeRunnerRequest codeRunnerRequest) {

        CodeRunnerExec codeRunnerExec = new CodeRunnerExec();

        return ResponseEntity.ok().body(codeRunnerExec.execPy(
                codeRunnerRequest.getCode(),
                codeRunnerRequest.getTest(),
                codeRunnerRequest.getAnswer()));
    }
}
