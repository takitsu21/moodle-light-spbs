package payload.request;

public class QCMRequest {

    private Integer number;

    private String name;

    private String description;

    public QCMRequest(Integer number, String name, String description) {
        this.number = number;
        this.name = name;
        this.description = description;
    }

    public QCMRequest() {
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
}
