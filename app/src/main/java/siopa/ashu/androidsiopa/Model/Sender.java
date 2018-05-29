package siopa.ashu.androidsiopa.Model;

/**
 * Created by DELL on 5/28/2018.
 */

public class Sender {
    public String to;
    public Notification notification;

    public Sender(String to, Notification notification) {
        this.to = to;
        this.notification = notification;
    }
}
