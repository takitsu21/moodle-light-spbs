package payload.request;

import javax.persistence.OrderColumn;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class MyText {

    @NotNull
    @OrderColumn
    private Integer num;

    @NotBlank
    private String content;

    public MyText() {
    }

    public MyText(int num, String content) {
        this.num = num;
        this.content = content;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
