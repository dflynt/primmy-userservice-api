package org.dflynt.primmy.userservice.models;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class User {

    @Id
    long id;

    @Column(name="userid")
    String userid;

    @Column(name="firstname")
    String firstName;

    @Column(name="lastname")
    String lastName;

    @Column(name="email")
    String email;

    @Column(name="password")
    String password;

    @Column(name="institution")
    String institution;


    @Column(name="field")
    String field;

    @Column(name="focus")
    String focus;

    @Column(name="signupdate")
    Date signupdate;

    @Column(name="leader")
    boolean leader;

    @Column(name="avatar")
    String avatar;

    @Column(name="enabled")
    boolean enabled;

    @Column(name="verificationcode")
    String verificationCode;

    public User() {}

    public User(String firstName) {
        this.firstName = firstName;
    }

    public User(String uuid, String firstName, String lastName, String email, String password,
                String institution, Date signupdate, String field, String focus, boolean leader, String verificationCode) {
        this.userid = uuid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.institution = institution;
        this.signupdate = signupdate;
        this.leader = leader;
        this.field = field;
        this.focus = focus;
        this.leader = leader;
        this.avatar = null;
        this.verificationCode = verificationCode;
        this.enabled = false;
    }
}
