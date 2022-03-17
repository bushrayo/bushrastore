package mailsender;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

//my email: "bushraclothingstore@gmail.com", 
@ManagedBean(name = "sendMail")
@SessionScoped
public class MailController implements Serializable {

	private static final long serialVersionUID = 1L;
	private String fromMail;
    private String password;
    private String toMail="bushra.younis89@gmail.com";
    private String subject;
    private String message;

    public String getFromMail() {
        return fromMail;
    }

    public void setFromMail(String fromMail) {
        this.fromMail = fromMail;
    }

    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToMail() {
        return toMail;
    }

    public void setToMail(String toMail) {
        this.toMail = toMail;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void send() {
        try {
            MailSender mailSender=new MailSender();
           mailSender.sendMail(fromMail, password, toMail, subject, message);
        } catch (Exception e) {
        }
    }
}

