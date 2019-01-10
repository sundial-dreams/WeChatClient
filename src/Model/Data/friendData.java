package Model.Data;

public class friendData {
    private String head;
    private String account;

    public friendData(String head, String account) {
        this.head = head;
        this.account = account;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getHead() {
        return head;
    }

    public String getAccount() {
        return account;
    }
}
