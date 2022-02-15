package payload.request;

import java.util.HashSet;
import java.util.Set;

public class TextRequest {


    private Set<MyText> texts;

    public TextRequest(MyText text) {
        texts=new HashSet<>(){{add(text);}};
    }

    public TextRequest() {
    }

    public Set<MyText> getTexts() {
        return texts;
    }

    public void setTexts(Set<MyText> texts) {
        this.texts = texts;
    }
}
