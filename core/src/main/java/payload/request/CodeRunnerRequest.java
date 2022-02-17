package payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CodeRunnerRequest {

    private Integer number;

    private String name;

    private String description;

    private String code;

    private String answer;

    private String test;

    public CodeRunnerRequest(Integer number, String name, String description, String answer, String test) {
        this.number = number;
        this.name = name;
        this.description = description;
        this.answer = answer;
        this.test = test;
    }

    public CodeRunnerRequest(String code, String test, String answer) {
        this.code = code;
        this.test = test;
        this.answer = answer;
    }

    public CodeRunnerRequest(String code) {
        this.code = code;
    }

    public CodeRunnerRequest() {
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }
}
